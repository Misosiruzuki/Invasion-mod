/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.MoveState;
import invmod.common.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class EntityAISwoop
extends EntityAIBase {
    private static final int INITIAL_LINEUP_TIME = 25;
    private EntityIMBird theEntity;
    private float minDiveClearanceY;
    private EntityLivingBase swoopTarget;
    private float diveAngle;
    private float diveHeight;
    private float strikeDistance;
    private float minHeight;
    private float minXZDistance;
    private float maxSteepness;
    private float finalRunLength;
    private float finalRunArcLimit;
    private int time;
    private boolean isCommittedToFinalRun;
    private boolean endSwoop;
    private boolean usingClaws;

    public EntityAISwoop(EntityIMBird entity) {
        this.theEntity = entity;
        this.minDiveClearanceY = 0.0f;
        this.swoopTarget = null;
        this.diveAngle = 0.0f;
        this.diveHeight = 0.0f;
        this.maxSteepness = 40.0f;
        this.strikeDistance = entity.field_70130_N + 1.5f;
        this.minHeight = 6.0f;
        this.minXZDistance = 10.0f;
        this.finalRunLength = 4.0f;
        this.finalRunArcLimit = 15.0f;
        this.time = 0;
        this.isCommittedToFinalRun = false;
        this.endSwoop = false;
        this.usingClaws = false;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        if (this.theEntity.getAIGoal() == Goal.FIND_ATTACK_OPPORTUNITY && this.theEntity.func_70638_az() != null) {
            this.swoopTarget = this.theEntity.func_70638_az();
            double dX = this.swoopTarget.field_70165_t - this.theEntity.field_70165_t;
            double dY = this.swoopTarget.field_70163_u - this.theEntity.field_70163_u;
            double dZ = this.swoopTarget.field_70161_v - this.theEntity.field_70161_v;
            double dXZ = Math.sqrt(dX * dX + dZ * dZ);
            if (-dY < (double)this.minHeight || dXZ < (double)this.minXZDistance) {
                return false;
            }
            double pitchToTarget = Math.atan(dY / dXZ) * 180.0 / Math.PI;
            if (pitchToTarget > (double)this.maxSteepness) {
                return false;
            }
            this.finalRunLength = (float)(dXZ * 0.42);
            if (this.finalRunLength > 18.0f) {
                this.finalRunLength = 18.0f;
            } else if (this.finalRunLength < 4.0f) {
                this.finalRunLength = 4.0f;
            }
            this.diveAngle = (float)(Math.atan((dXZ - (double)this.finalRunLength) / dY) * 180.0 / Math.PI);
            if (this.swoopTarget != null && this.isSwoopPathClear(this.swoopTarget, this.diveAngle)) {
                this.diveHeight = (float)(-dY);
                return true;
            }
        }
        return false;
    }

    public boolean func_75253_b() {
        return this.theEntity.func_70638_az() == this.swoopTarget && !this.endSwoop && this.theEntity.getMoveState() == MoveState.FLYING;
    }

    public void func_75249_e() {
        this.time = 0;
        this.theEntity.transitionAIGoal(Goal.SWOOP);
        this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
        this.theEntity.getNavigatorNew().tryMoveToEntity((Entity)this.swoopTarget, 0.0f, this.theEntity.getMaxPoweredFlightSpeed());
        this.theEntity.doScreech();
    }

    public void func_75251_c() {
        this.endSwoop = false;
        this.isCommittedToFinalRun = false;
        this.theEntity.getNavigatorNew().enableDirectTarget(false);
        if (this.theEntity.getAIGoal() == Goal.SWOOP) {
            this.theEntity.transitionAIGoal(Goal.NONE);
            this.theEntity.setClawsForward(false);
        }
    }

    public void func_75246_d() {
        ++this.time;
        if (!this.isCommittedToFinalRun) {
            if (this.theEntity.func_70032_d((Entity)this.swoopTarget) < this.finalRunLength) {
                this.theEntity.getNavigatorNew().setPitchBias(0.0f, 1.0f);
                if (this.isFinalRunLinedUp()) {
                    this.usingClaws = this.theEntity.field_70170_p.field_73012_v.nextFloat() > 0.6f;
                    this.theEntity.setClawsForward(true);
                    this.theEntity.getNavigatorNew().enableDirectTarget(true);
                    this.isCommittedToFinalRun = true;
                } else {
                    this.theEntity.transitionAIGoal(Goal.NONE);
                    this.endSwoop = true;
                }
            } else if (this.time > 25) {
                double dYp = -(this.swoopTarget.field_70163_u - this.theEntity.field_70163_u);
                if (dYp < 2.9) {
                    dYp = 0.0;
                }
                this.theEntity.getNavigatorNew().setPitchBias(this.diveAngle * (float)(dYp / (double)this.diveHeight), (float)(0.6 * (dYp / (double)this.diveHeight)));
            }
        } else if (this.theEntity.func_70032_d((Entity)this.swoopTarget) < this.strikeDistance) {
            this.theEntity.transitionAIGoal(Goal.FLYING_STRIKE);
            this.theEntity.getNavigatorNew().enableDirectTarget(false);
            this.endSwoop = true;
        } else {
            double dZ = this.swoopTarget.field_70161_v - this.theEntity.field_70161_v;
            double dX = this.swoopTarget.field_70165_t - this.theEntity.field_70165_t;
            double yawToTarget = Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0;
            if (Math.abs(MathUtil.boundAngle180Deg(yawToTarget - (double)this.theEntity.field_70177_z)) > 90.0) {
                this.theEntity.transitionAIGoal(Goal.NONE);
                this.theEntity.getNavigatorNew().enableDirectTarget(false);
                this.theEntity.setClawsForward(false);
                this.endSwoop = true;
            }
        }
    }

    private boolean isSwoopPathClear(EntityLivingBase target, float diveAngle) {
        double dX = target.field_70165_t - this.theEntity.field_70165_t;
        double dY = target.field_70163_u - this.theEntity.field_70163_u;
        double dZ = target.field_70161_v - this.theEntity.field_70161_v;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        double dRayY = 2.0;
        int hitCount = 0;
        double lowestCollide = this.theEntity.field_70163_u;
        for (double y = this.theEntity.field_70163_u - dRayY; y > target.field_70163_u; y -= dRayY) {
            double z;
            double dist = Math.tan(90.0f + diveAngle) * (this.theEntity.field_70163_u - y);
            double x = -Math.sin((double)(this.theEntity.field_70177_z / 180.0f) * Math.PI) * dist;
            Vec3 source = Vec3.func_72443_a((double)x, (double)y, (double)(z = Math.cos((double)(this.theEntity.field_70177_z / 180.0f) * Math.PI) * dist));
            MovingObjectPosition collide = this.theEntity.field_70170_p.func_72933_a(source, target.func_70666_h(1.0f));
            if (collide == null) continue;
            if (hitCount == 0) {
                lowestCollide = y;
            }
            ++hitCount;
        }
        return this.isAcceptableDiveSpace(this.theEntity.field_70163_u, lowestCollide, hitCount);
    }

    private boolean isFinalRunLinedUp() {
        double dX = this.swoopTarget.field_70165_t - this.theEntity.field_70165_t;
        double dY = this.swoopTarget.field_70163_u - this.theEntity.field_70163_u;
        double dZ = this.swoopTarget.field_70161_v - this.theEntity.field_70161_v;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        double yawToTarget = Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0;
        double dYaw = MathUtil.boundAngle180Deg(yawToTarget - (double)this.theEntity.field_70177_z);
        if (dYaw < (double)(-this.finalRunArcLimit) || dYaw > (double)this.finalRunArcLimit) {
            return false;
        }
        double dPitch = Math.atan(dY / dXZ) * 180.0 / Math.PI - (double)this.theEntity.field_70125_A;
        return !(dPitch < (double)(-this.finalRunArcLimit)) && !(dPitch > (double)this.finalRunArcLimit);
    }

    protected boolean isAcceptableDiveSpace(double entityPosY, double lowestCollideY, int hitCount) {
        double clearanceY = entityPosY - lowestCollideY;
        return !(clearanceY < (double)this.minDiveClearanceY);
    }
}

