/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.FlyState;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.IPathSource;
import invmod.common.entity.MoveState;
import invmod.common.entity.NavigatorIM;
import invmod.common.entity.Path;
import invmod.common.util.Distance;
import invmod.common.util.MathUtil;
import invmod.common.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class NavigatorFlying
extends NavigatorIM
implements INavigationFlying {
    private static final int VISION_RESOLUTION_H = 30;
    private static final int VISION_RESOLUTION_V = 20;
    private static final float FOV_H = 300.0f;
    private static final float FOV_V = 220.0f;
    private final EntityIMFlying theEntity;
    private INavigationFlying.MoveType moveType;
    private boolean wantsToBeFlying;
    private float targetYaw;
    private float targetPitch;
    private float targetSpeed;
    private float visionDistance;
    private int visionUpdateRate;
    private int timeSinceVision;
    private float[][] retina;
    private float[][] headingAppeal;
    private Vec3 intermediateTarget;
    private Vec3 finalTarget;
    private boolean isCircling;
    private float circlingHeight;
    private float circlingRadius;
    private float pitchBias;
    private float pitchBiasAmount;
    private int timeLookingForEntity;
    private boolean precisionTarget;
    private float closestDistToTarget;
    private int timeSinceGotCloser;

    public NavigatorFlying(EntityIMFlying entityFlying, IPathSource pathSource) {
        super(entityFlying, pathSource);
        this.theEntity = entityFlying;
        this.moveType = INavigationFlying.MoveType.MIXED;
        this.visionDistance = 14.0f;
        this.timeSinceVision = 3;
        this.visionUpdateRate = 3;
        this.targetYaw = entityFlying.field_70177_z;
        this.targetPitch = 0.0f;
        this.targetSpeed = entityFlying.getMaxPoweredFlightSpeed();
        this.retina = new float[30][20];
        this.headingAppeal = new float[28][18];
        this.intermediateTarget = Vec3.func_72443_a((double)0.0, (double)0.0, (double)0.0);
        this.isCircling = false;
        this.pitchBias = 0.0f;
        this.pitchBiasAmount = 0.0f;
        this.timeLookingForEntity = 0;
        this.precisionTarget = false;
        this.closestDistToTarget = 0.0f;
        this.timeSinceGotCloser = 0;
    }

    @Override
    public void setMovementType(INavigationFlying.MoveType moveType) {
        this.moveType = moveType;
    }

    @Override
    public void enableDirectTarget(boolean enabled) {
        this.precisionTarget = enabled;
    }

    @Override
    public void setLandingPath() {
        this.clearPath();
        this.moveType = INavigationFlying.MoveType.PREFER_WALKING;
        this.setWantsToBeFlying(false);
    }

    @Override
    public void setCirclingPath(Entity target, float preferredHeight, float preferredRadius) {
        this.setCirclingPath(target.field_70165_t, target.field_70163_u, target.field_70161_v, preferredHeight, preferredRadius);
    }

    @Override
    public void setCirclingPath(double x, double y, double z, float preferredHeight, float preferredRadius) {
        this.clearPath();
        this.finalTarget = Vec3.func_72443_a((double)x, (double)y, (double)z);
        this.circlingHeight = preferredHeight;
        this.circlingRadius = preferredRadius;
        this.isCircling = true;
    }

    @Override
    public float getDistanceToCirclingRadius() {
        double dX = this.finalTarget.field_72450_a - this.theEntity.field_70165_t;
        double dY = this.finalTarget.field_72448_b - this.theEntity.field_70163_u;
        double dZ = this.finalTarget.field_72449_c - this.theEntity.field_70161_v;
        return (float)(Math.sqrt(dX * dX + dZ * dZ) - (double)this.circlingRadius);
    }

    @Override
    public void setFlySpeed(float speed) {
        this.targetSpeed = speed;
    }

    @Override
    public void setPitchBias(float pitch, float biasAmount) {
        this.pitchBias = pitch;
        this.pitchBiasAmount = biasAmount;
    }

    @Override
    protected void updateAutoPathToEntity() {
        double dSq;
        double dist = this.theEntity.func_70032_d(this.pathEndEntity);
        if (dist < (double)(this.closestDistToTarget - 1.0f)) {
            this.closestDistToTarget = (float)dist;
            this.timeSinceGotCloser = 0;
        } else {
            ++this.timeSinceGotCloser;
        }
        boolean pathUpdate = false;
        boolean needsPathfinder = false;
        if (this.path != null) {
            dSq = this.theEntity.func_70068_e(this.pathEndEntity);
            if ((this.moveType == INavigationFlying.MoveType.PREFER_FLYING || this.moveType == INavigationFlying.MoveType.MIXED && dSq > 100.0) && this.theEntity.func_70685_l(this.pathEndEntity)) {
                this.timeLookingForEntity = 0;
                pathUpdate = true;
            } else {
                double d2;
                double d1 = Distance.distanceBetween(this.pathEndEntity, this.pathEndEntityLastPos);
                if (d1 / (d2 = Distance.distanceBetween((Entity)this.theEntity, this.pathEndEntityLastPos)) > 0.1) {
                    pathUpdate = true;
                }
            }
        } else if (this.moveType == INavigationFlying.MoveType.PREFER_WALKING || this.timeSinceGotCloser > 160 || this.timeLookingForEntity > 600) {
            pathUpdate = true;
            needsPathfinder = true;
            this.timeSinceGotCloser = 0;
            this.timeLookingForEntity = 500;
        } else if (this.moveType == INavigationFlying.MoveType.MIXED && (dSq = this.theEntity.func_70068_e(this.pathEndEntity)) < 100.0) {
            pathUpdate = true;
        }
        if (pathUpdate) {
            if (this.moveType == INavigationFlying.MoveType.PREFER_FLYING) {
                if (needsPathfinder) {
                    this.theEntity.setPathfindFlying(true);
                    this.path = this.createPath(this.theEntity, this.pathEndEntity, 0.0f);
                    if (this.path != null) {
                        this.setWantsToBeFlying(true);
                        this.setPath(this.path, this.moveSpeed);
                    }
                } else {
                    this.setWantsToBeFlying(true);
                    this.resetStatus();
                }
            } else if (this.moveType == INavigationFlying.MoveType.MIXED) {
                this.theEntity.setPathfindFlying(false);
                Path path = this.createPath(this.theEntity, this.pathEndEntity, 0.0f);
                if (path != null && (double)path.getCurrentPathLength() < dist * 1.8) {
                    this.setWantsToBeFlying(false);
                    this.setPath(path, this.moveSpeed);
                } else if (needsPathfinder) {
                    this.theEntity.setPathfindFlying(true);
                    path = this.createPath(this.theEntity, this.pathEndEntity, 0.0f);
                    this.setWantsToBeFlying(true);
                    if (path != null) {
                        this.setPath(path, this.moveSpeed);
                    } else {
                        this.resetStatus();
                    }
                } else {
                    this.setWantsToBeFlying(true);
                    this.resetStatus();
                }
            } else {
                this.setWantsToBeFlying(false);
                this.theEntity.setPathfindFlying(false);
                Path path = this.createPath(this.theEntity, this.pathEndEntity, 0.0f);
                if (path != null) {
                    this.setPath(path, this.moveSpeed);
                }
            }
            this.pathEndEntityLastPos = Vec3.func_72443_a((double)this.pathEndEntity.field_70165_t, (double)this.pathEndEntity.field_70163_u, (double)this.pathEndEntity.field_70161_v);
        }
    }

    @Override
    public void autoPathToEntity(Entity target) {
        super.autoPathToEntity(target);
        this.isCircling = false;
    }

    @Override
    public boolean tryMoveToEntity(Entity targetEntity, float targetRadius, float speed) {
        if (this.moveType != INavigationFlying.MoveType.PREFER_WALKING) {
            this.clearPath();
            this.pathEndEntity = targetEntity;
            this.finalTarget = Vec3.func_72443_a((double)this.pathEndEntity.field_70165_t, (double)this.pathEndEntity.field_70163_u, (double)this.pathEndEntity.field_70161_v);
            this.isCircling = false;
            return true;
        }
        this.theEntity.setPathfindFlying(false);
        return super.tryMoveToEntity(targetEntity, targetRadius, speed);
    }

    @Override
    public boolean tryMoveToXYZ(double x, double y, double z, float targetRadius, float speed) {
        Vec3 target = Vec3.func_72443_a((double)x, (double)y, (double)z);
        if (this.moveType != INavigationFlying.MoveType.PREFER_WALKING) {
            this.clearPath();
            this.finalTarget = Vec3.func_72443_a((double)x, (double)y, (double)z);
            this.isCircling = false;
            return true;
        }
        this.theEntity.setPathfindFlying(false);
        return super.tryMoveToXYZ(x, y, z, targetRadius, speed);
    }

    @Override
    public boolean tryMoveTowardsXZ(double x, double z, int min, int max, int verticalRange, float speed) {
        Vec3 target = this.findValidPointNear(x, z, min, max, verticalRange);
        if (target != null) {
            return this.tryMoveToXYZ(target.field_72450_a, target.field_72448_b, target.field_72449_c, 0.0f, speed);
        }
        return false;
    }

    @Override
    public void clearPath() {
        super.clearPath();
        this.pathEndEntity = null;
        this.isCircling = false;
    }

    @Override
    public boolean isCircling() {
        return this.isCircling;
    }

    @Override
    public String getStatus() {
        if (!this.noPath()) {
            return super.getStatus();
        }
        String s = "";
        if (this.isAutoPathingToEntity()) {
            s = s + "Auto:";
        }
        s = s + "Flyer:";
        s = this.isCircling ? s + "Circling:" : (this.wantsToBeFlying ? (this.theEntity.getFlyState() == FlyState.TAKEOFF ? s + "TakeOff:" : s + "Flying:") : (this.theEntity.getFlyState() == FlyState.LANDING || this.theEntity.getFlyState() == FlyState.TOUCHDOWN ? s + "Landing:" : s + "Ground"));
        return s;
    }

    @Override
    protected void pathFollow() {
        Vec3 vec3d = this.getEntityPosition();
        int maxNextLeg = this.path.getCurrentPathLength();
        float fa = this.theEntity.field_70130_N * 0.5f;
        for (int j = this.path.getCurrentPathIndex(); j < maxNextLeg; ++j) {
            if (!(vec3d.func_72436_e(this.path.getPositionAtIndex((Entity)this.theEntity, j)) < (double)(fa * fa))) continue;
            this.path.setCurrentPathIndex(j + 1);
        }
    }

    @Override
    protected void noPathFollow() {
        if (this.theEntity.getMoveState() != MoveState.FLYING && this.theEntity.getAIGoal() == Goal.CHILL) {
            this.setWantsToBeFlying(false);
            return;
        }
        if (this.moveType == INavigationFlying.MoveType.PREFER_FLYING) {
            this.setWantsToBeFlying(true);
        } else if (this.moveType == INavigationFlying.MoveType.PREFER_WALKING) {
            this.setWantsToBeFlying(false);
        }
        if (++this.timeSinceVision >= this.visionUpdateRate) {
            this.timeSinceVision = 0;
            if (!this.precisionTarget || this.pathEndEntity == null) {
                this.updateHeading();
            } else {
                this.updateHeadingDirectTarget(this.pathEndEntity);
            }
            this.intermediateTarget = this.convertToVector(this.targetYaw, this.targetPitch, this.targetSpeed);
        }
        this.theEntity.getMoveHelper().func_75642_a(this.intermediateTarget.field_72450_a, this.intermediateTarget.field_72448_b, this.intermediateTarget.field_72449_c, this.targetSpeed);
    }

    protected Vec3 convertToVector(float yaw, float pitch, float idealSpeed) {
        int time = this.visionUpdateRate + 20;
        double x = this.theEntity.field_70165_t + -Math.sin((double)(yaw / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        double y = this.theEntity.field_70163_u + Math.sin((double)(pitch / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        double z = this.theEntity.field_70161_v + Math.cos((double)(yaw / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        return Vec3.func_72443_a((double)x, (double)y, (double)z);
    }

    protected void updateHeading() {
        double dXZ;
        double dZ;
        int i;
        float pixelDegreeH = 10.0f;
        float pixelDegreeV = 11.0f;
        for (i = 0; i < 30; ++i) {
            double nextAngleH = (double)((float)i * pixelDegreeH) + 0.5 * (double)pixelDegreeH - 150.0 + (double)this.theEntity.field_70177_z;
            for (int j = 0; j < 20; ++j) {
                double nextAngleV = (double)((float)j * pixelDegreeV) + 0.5 * (double)pixelDegreeV - 110.0;
                double y = this.theEntity.field_70163_u + Math.sin(nextAngleV / 180.0 * Math.PI) * (double)this.visionDistance;
                double distanceXZ = Math.cos(nextAngleV / 180.0 * Math.PI) * (double)this.visionDistance;
                double x = this.theEntity.field_70165_t + -Math.sin(nextAngleH / 180.0 * Math.PI) * distanceXZ;
                double z = this.theEntity.field_70161_v + Math.cos(nextAngleH / 180.0 * Math.PI) * distanceXZ;
                Vec3 target = Vec3.func_72443_a((double)x, (double)y, (double)z);
                Vec3 origin = this.theEntity.func_70666_h(1.0f);
                origin.field_72448_b += 1.0;
                MovingObjectPosition object = this.theEntity.field_70170_p.func_72933_a(origin, target);
            }
        }
        for (i = 1; i < 29; ++i) {
            for (int j = 1; j < 19; ++j) {
                float appeal = this.retina[i][j];
                appeal += this.retina[i - 1][j - 1];
                appeal += this.retina[i - 1][j];
                appeal += this.retina[i - 1][j + 1];
                appeal += this.retina[i][j - 1];
                appeal += this.retina[i][j + 1];
                appeal += this.retina[i + 1][j - 1];
                appeal += this.retina[i + 1][j];
                appeal += this.retina[i + 1][j + 1];
                this.headingAppeal[i - 1][j - 1] = appeal /= 9.0f;
            }
        }
        if (this.isCircling) {
            double dX = this.finalTarget.field_72450_a - this.theEntity.field_70165_t;
            double dY = this.finalTarget.field_72448_b - this.theEntity.field_70163_u;
            dZ = this.finalTarget.field_72449_c - this.theEntity.field_70161_v;
            dXZ = Math.sqrt(dX * dX + dZ * dZ);
            if (dXZ > 0.0 && dXZ > (double)this.circlingRadius * 0.6) {
                double intersectRadius = Math.abs(((double)this.circlingRadius - dXZ) * 2.0) + 8.0;
                if (intersectRadius > (double)this.circlingRadius * 1.8) {
                    intersectRadius = dXZ + 5.0;
                }
                float preferredYaw1 = (float)(Math.acos((dXZ * dXZ - (double)(this.circlingRadius * this.circlingRadius) + intersectRadius * intersectRadius) / (2.0 * dXZ) / intersectRadius) * 180.0 / Math.PI);
                float preferredYaw2 = -preferredYaw1;
                double dYaw = Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0;
                preferredYaw1 = (float)((double)preferredYaw1 + dYaw);
                preferredYaw2 = (float)((double)preferredYaw2 + dYaw);
                float preferredPitch = (float)(Math.atan((dY + (double)this.circlingHeight) / intersectRadius) * 180.0 / Math.PI);
                float yawBias = (float)(1.5 * Math.abs(dXZ - (double)this.circlingRadius) / (double)this.circlingRadius);
                float pitchBias = (float)(1.9 * Math.abs((dY + (double)this.circlingHeight) / (double)this.circlingHeight));
                this.doHeadingBiasPass(this.headingAppeal, preferredYaw1, preferredYaw2, preferredPitch, yawBias, pitchBias);
            } else {
                float yawToTarget = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0);
                float preferredPitch = (float)(Math.atan((dY + (double)this.circlingHeight) / Math.abs((double)this.circlingRadius - dXZ)) * 180.0 / Math.PI);
                float yawBias = (float)(0.5 * Math.abs(dXZ - (double)this.circlingRadius) / (double)this.circlingRadius);
                float pitchBias = (float)(0.9 * Math.abs((dY + (double)this.circlingHeight) / (double)this.circlingHeight));
                this.doHeadingBiasPass(this.headingAppeal, yawToTarget += 180.0f, yawToTarget, preferredPitch, yawBias, pitchBias);
            }
        } else if (this.pathEndEntity != null) {
            double dX = this.pathEndEntity.field_70165_t - this.theEntity.field_70165_t;
            double dY = this.pathEndEntity.field_70163_u - this.theEntity.field_70163_u;
            dZ = this.pathEndEntity.field_70161_v - this.theEntity.field_70161_v;
            dXZ = Math.sqrt(dX * dX + dZ * dZ);
            float yawToTarget = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0);
            float pitchToTarget = (float)(Math.atan(dY / dXZ) * 180.0 / Math.PI);
            this.doHeadingBiasPass(this.headingAppeal, yawToTarget, yawToTarget, pitchToTarget, 20.6f, 20.6f);
        }
        if (this.pathEndEntity == null) {
            float dOldYaw = this.targetYaw - this.theEntity.field_70177_z;
            MathUtil.boundAngle180Deg(dOldYaw);
            float dOldPitch = this.targetPitch;
            float approxLastTargetX = dOldYaw / pixelDegreeH + 14.0f;
            float approxLastTargetY = dOldPitch / pixelDegreeV + 9.0f;
            if (approxLastTargetX > 28.0f) {
                approxLastTargetX = 28.0f;
            } else if (approxLastTargetX < 0.0f) {
                approxLastTargetX = 0.0f;
            }
            if (approxLastTargetY > 18.0f) {
                approxLastTargetY = 18.0f;
            } else if (approxLastTargetY < 0.0f) {
                approxLastTargetY = 0.0f;
            }
            float statusQuoBias = 0.4f;
            float falloffDist = 30.0f;
            for (int i2 = 0; i2 < 28; ++i2) {
                float dXSq = (approxLastTargetX - (float)i2) * (approxLastTargetX - (float)i2);
                int j = 0;
                while (j < 18) {
                    float dY = approxLastTargetY - (float)j;
                    int tmp1306_1304 = j++;
                    float[] tmp1306_1303 = this.headingAppeal[i2];
                    tmp1306_1303[tmp1306_1304] = (float)((double)tmp1306_1303[tmp1306_1304] * ((double)(1.0f + statusQuoBias) - (double)statusQuoBias * Math.sqrt(dXSq + dY * dY) / (double)falloffDist));
                }
            }
        }
        if (this.pitchBias != 0.0f) {
            this.doHeadingBiasPass(this.headingAppeal, 0.0f, 0.0f, this.pitchBias, 0.0f, this.pitchBiasAmount);
        }
        if (!this.wantsToBeFlying) {
            Pair<Float, Float> landingInfo = this.appraiseLanding();
            if (landingInfo.getVal2().floatValue() < 4.0f) {
                if (landingInfo.getVal1().floatValue() >= 0.9f) {
                    this.doHeadingBiasPass(this.headingAppeal, 0.0f, 0.0f, -45.0f, 0.0f, 3.5f);
                } else if (landingInfo.getVal1().floatValue() >= 0.65f) {
                    this.doHeadingBiasPass(this.headingAppeal, 0.0f, 0.0f, -15.0f, 0.0f, 0.4f);
                }
            } else if (landingInfo.getVal1().floatValue() >= 0.52f) {
                this.doHeadingBiasPass(this.headingAppeal, 0.0f, 0.0f, -15.0f, 0.0f, 0.8f);
            }
        }
        Pair<Integer, Integer> bestPixel = this.chooseCoordinate();
        this.targetYaw = this.theEntity.field_70177_z - 150.0f + (float)(bestPixel.getVal1() + 1) * pixelDegreeH + 0.5f * pixelDegreeH;
        this.targetPitch = -110.0f + (float)(bestPixel.getVal2() + 1) * pixelDegreeV + 0.5f * pixelDegreeV;
    }

    protected void updateHeadingDirectTarget(Entity target) {
        double dX = target.field_70165_t - this.theEntity.field_70165_t;
        double dY = target.field_70163_u - this.theEntity.field_70163_u;
        double dZ = target.field_70161_v - this.theEntity.field_70161_v;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        this.targetYaw = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0);
        this.targetPitch = (float)(Math.atan(dY / dXZ) * 180.0 / Math.PI);
    }

    protected Pair<Integer, Integer> chooseCoordinate() {
        int bestPixelX = 0;
        int bestPixelY = 0;
        for (int i = 0; i < 28; ++i) {
            for (int j = 0; j < 18; ++j) {
                if (!(this.headingAppeal[bestPixelX][bestPixelY] < this.headingAppeal[i][j])) continue;
                bestPixelX = i;
                bestPixelY = j;
            }
        }
        return new Pair<Integer, Integer>(bestPixelX, bestPixelY);
    }

    protected void setTarget(double x, double y, double z) {
        this.intermediateTarget = Vec3.func_72443_a((double)x, (double)y, (double)z);
    }

    protected Vec3 getTarget() {
        return this.intermediateTarget;
    }

    protected void doHeadingBiasPass(float[][] array, float preferredYaw1, float preferredYaw2, float preferredPitch, float yawBias, float pitchBias) {
        float pixelDegreeH = 10.0f;
        float pixelDegreeV = 11.0f;
        for (int i = 0; i < array.length; ++i) {
            double nextAngleH = (double)((float)(i + 1) * pixelDegreeH) + 0.5 * (double)pixelDegreeH - 150.0 + (double)this.theEntity.field_70177_z;
            double dYaw1 = MathUtil.boundAngle180Deg((double)preferredYaw1 - nextAngleH);
            double dYaw2 = MathUtil.boundAngle180Deg((double)preferredYaw2 - nextAngleH);
            double yawBiasAmount = 1.0 + Math.min(Math.abs(dYaw1), Math.abs(dYaw2)) * (double)yawBias / 180.0;
            int j = 0;
            while (j < array[0].length) {
                double nextAngleV = (double)((float)(j + 1) * pixelDegreeV) + 0.5 * (double)pixelDegreeV - 110.0;
                double pitchBiasAmount = 1.0 + Math.abs(MathUtil.boundAngle180Deg((double)preferredPitch - nextAngleV)) * (double)pitchBias / 180.0;
                int tmp162_160 = j++;
                float[] tmp162_159 = array[i];
                tmp162_159[tmp162_160] = (float)((double)tmp162_159[tmp162_160] / (yawBiasAmount * pitchBiasAmount));
            }
        }
    }

    private void setWantsToBeFlying(boolean flag) {
        this.wantsToBeFlying = flag;
        this.theEntity.getMoveHelper().setWantsToBeFlying(flag);
    }

    private Pair<Float, Float> appraiseLanding() {
        float safety = 0.0f;
        float distance = 0.0f;
        int landingResolution = 3;
        double nextAngleH = this.theEntity.field_70177_z;
        for (int i = 0; i < landingResolution; ++i) {
            double nextAngleV = -90 + i * 30 / landingResolution;
            double y = this.theEntity.field_70163_u + Math.sin(nextAngleV / 180.0 * Math.PI) * 64.0;
            double distanceXZ = Math.cos(nextAngleV / 180.0 * Math.PI) * 64.0;
            double x = this.theEntity.field_70165_t + -Math.sin(nextAngleH / 180.0 * Math.PI) * distanceXZ;
            double z = this.theEntity.field_70161_v + Math.cos(nextAngleH / 180.0 * Math.PI) * distanceXZ;
            Vec3 target = Vec3.func_72443_a((double)x, (double)y, (double)z);
            Vec3 origin = this.theEntity.func_70666_h(1.0f);
            MovingObjectPosition object = this.theEntity.field_70170_p.func_72933_a(origin, target);
            if (object != null) {
                Block Block2 = this.theEntity.field_70170_p.func_147439_a(object.field_72311_b, object.field_72312_c, object.field_72309_d);
                if (!this.theEntity.avoidsBlock(Block2)) {
                    safety += 0.7f;
                }
                if (object.field_72310_e == 1) {
                    safety += 0.3f;
                }
                double dX = (double)object.field_72311_b - this.theEntity.field_70165_t;
                double dY = (double)object.field_72312_c - this.theEntity.field_70163_u;
                double dZ = (double)object.field_72309_d - this.theEntity.field_70161_v;
                distance = (float)((double)distance + Math.sqrt(dX * dX + dY * dY + dZ * dZ));
                continue;
            }
            distance += 64.0f;
        }
        return new Pair<Float, Float>(Float.valueOf(safety /= (float)landingResolution), Float.valueOf(distance /= (float)landingResolution));
    }
}

