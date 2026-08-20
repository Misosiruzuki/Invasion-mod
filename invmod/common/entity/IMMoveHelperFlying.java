/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.FlyState;
import invmod.common.entity.IMMoveHelper;
import invmod.common.entity.MoveState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class IMMoveHelperFlying
extends IMMoveHelper {
    private EntityIMFlying a;
    private double targetFlySpeed;
    private boolean wantsToBeFlying;

    public IMMoveHelperFlying(EntityIMFlying entity) {
        super(entity);
        this.a = entity;
        this.wantsToBeFlying = false;
    }

    public void setHeading(float yaw, float pitch, float idealSpeed, int time) {
        double x = this.a.field_70165_t + Math.sin((double)(yaw / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        double y = this.a.field_70163_u + Math.sin((double)(pitch / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        double z = this.a.field_70161_v + Math.cos((double)(yaw / 180.0f) * Math.PI) * (double)idealSpeed * (double)time;
        this.func_75642_a(x, y, z, idealSpeed);
    }

    public void setWantsToBeFlying(boolean flag) {
        this.wantsToBeFlying = flag;
    }

    @Override
    public void func_75641_c() {
        this.a.func_70657_f(0.0f);
        this.a.setFlightAccelerationVector(0.0f, 0.0f, 0.0f);
        if (!this.needsUpdate && this.a.getMoveState() != MoveState.FLYING) {
            this.a.setMoveState(MoveState.STANDING);
            this.a.setFlyState(FlyState.GROUNDED);
            this.a.field_70125_A = this.correctRotation(this.a.field_70125_A, 50.0f, 4.0f);
            return;
        }
        this.needsUpdate = false;
        if (this.wantsToBeFlying) {
            if (this.a.getFlyState() == FlyState.GROUNDED) {
                this.a.setMoveState(MoveState.RUNNING);
                this.a.setFlyState(FlyState.TAKEOFF);
            } else if (this.a.getFlyState() == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }
        } else if (this.a.getFlyState() == FlyState.FLYING) {
            this.a.setFlyState(FlyState.LANDING);
        }
        if (this.a.getFlyState() == FlyState.FLYING) {
            FlyState result = this.doFlying();
            if (result == FlyState.GROUNDED) {
                this.a.setMoveState(MoveState.STANDING);
            } else if (result == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }
            this.a.setFlyState(result);
        } else if (this.a.getFlyState() == FlyState.TAKEOFF) {
            FlyState result = this.doTakeOff();
            if (result == FlyState.GROUNDED) {
                this.a.setMoveState(MoveState.STANDING);
            } else if (result == FlyState.TAKEOFF) {
                this.a.setMoveState(MoveState.RUNNING);
            } else if (result == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }
            this.a.setFlyState(result);
        } else if (this.a.getFlyState() == FlyState.LANDING || this.a.getFlyState() == FlyState.TOUCHDOWN) {
            FlyState result = this.doLanding();
            if (result == FlyState.GROUNDED || result == FlyState.TOUCHDOWN) {
                this.a.setMoveState(MoveState.RUNNING);
            }
            this.a.setFlyState(result);
        } else {
            MoveState result = this.doGroundMovement();
            this.a.setMoveState(result);
        }
    }

    @Override
    protected MoveState doGroundMovement() {
        this.a.setGroundFriction(0.6f);
        this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), 0.0f, 6.0f));
        this.targetSpeed = this.a.getMoveSpeedStat();
        this.a.field_70125_A = this.correctRotation(this.a.field_70125_A, 50.0f, 4.0f);
        return super.doGroundMovement();
    }

    protected FlyState doFlying() {
        this.targetFlySpeed = this.setSpeed;
        return this.fly();
    }

    protected FlyState fly() {
        this.a.setGroundFriction(1.0f);
        boolean isInLiquid = this.a.func_70090_H() || this.a.func_70058_J();
        double dX = this.b - this.a.field_70165_t;
        double dZ = this.d - this.a.field_70161_v;
        double dY = this.c - this.a.field_70163_u;
        double dXZSq = dX * dX + dZ * dZ;
        double dXZ = Math.sqrt(dXZSq);
        double distanceSquared = dXZSq + dY * dY;
        if (distanceSquared > 0.04) {
            float gravity;
            int timeToTurn = 10;
            float liftConstant = gravity = this.a.getGravity();
            double xAccel = 0.0;
            double yAccel = 0.0;
            double zAccel = 0.0;
            double velX = this.a.field_70159_w;
            double velY = this.a.field_70181_x;
            double velZ = this.a.field_70179_y;
            double hSpeedSq = velX * velX + velZ * velZ;
            if (hSpeedSq == 0.0) {
                hSpeedSq = 1.0E-8;
            }
            double horizontalSpeed = Math.sqrt(hSpeedSq);
            double flySpeed = Math.sqrt(hSpeedSq + velY * velY);
            double desiredYVelocity = dY / (double)timeToTurn;
            double dVelY = desiredYVelocity - (velY - (double)gravity);
            float minFlightSpeed = 0.05f;
            if (flySpeed < (double)minFlightSpeed) {
                float newYaw = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0);
                this.a.field_70177_z = newYaw = this.correctRotation(this.a.field_70177_z, newYaw, this.a.getTurnRate());
                if (this.a.field_70122_E) {
                    return FlyState.GROUNDED;
                }
            } else {
                double newPitch;
                double dXZHeading;
                double climbAccel;
                double liftForce = flySpeed / (double)(this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor()) * (double)liftConstant;
                double climbForce = liftForce * horizontalSpeed / (Math.abs(velY) + horizontalSpeed);
                double forwardForce = liftForce * Math.abs(velY) / (Math.abs(velY) + horizontalSpeed);
                double turnForce = liftForce;
                if (dVelY < 0.0) {
                    double maxDiveForce = this.a.getMaxTurnForce() - gravity;
                    climbAccel = -Math.min(Math.min(climbForce, maxDiveForce), -dVelY);
                } else {
                    double maxClimbForce = this.a.getMaxTurnForce() + gravity;
                    climbAccel = Math.min(Math.min(climbForce, maxClimbForce), dVelY);
                }
                float minBankForce = 0.01f;
                if (turnForce < (double)minBankForce) {
                    turnForce = minBankForce;
                }
                double desiredXZHeading = Math.atan2(dZ, dX) - 1.570796326794897;
                double currXZHeading = Math.atan2(velZ, velX) - 1.570796326794897;
                for (dXZHeading = desiredXZHeading - currXZHeading; dXZHeading >= Math.PI; dXZHeading -= Math.PI * 2) {
                }
                while (dXZHeading < -Math.PI) {
                    dXZHeading += Math.PI * 2;
                }
                double bankForce = horizontalSpeed * dXZHeading / (double)timeToTurn;
                double maxBankForce = Math.min(turnForce, (double)this.a.getMaxTurnForce());
                if (bankForce > maxBankForce) {
                    bankForce = maxBankForce;
                } else if (bankForce < -maxBankForce) {
                    bankForce = -maxBankForce;
                }
                double bankXAccel = bankForce * -velZ / horizontalSpeed;
                double bankZAccel = bankForce * velX / horizontalSpeed;
                double totalForce = xAccel + yAccel + zAccel;
                double r = liftForce / totalForce;
                xAccel += bankXAccel;
                zAccel += bankZAccel;
                velX += bankXAccel;
                velY += climbAccel;
                velZ += bankZAccel;
                double dYAccelGravity = (yAccel += climbAccel) - (double)gravity;
                double middlePitch = 15.0;
                if (velY - (double)gravity < 0.0) {
                    double climbForceRatio = yAccel / climbForce;
                    if (climbForceRatio > 1.0) {
                        climbForceRatio = 1.0;
                    } else if (climbForceRatio < -1.0) {
                        climbForceRatio = -1.0;
                    }
                    double xzSpeed = Math.sqrt(velX * velX + velZ * velZ);
                    double velPitch = xzSpeed > 0.0 ? Math.atan(velY / xzSpeed) / Math.PI * 180.0 : -180.0;
                    double pitchInfluence = ((double)this.a.getMaxPoweredFlightSpeed() - Math.abs(velY)) / (double)this.a.getMaxPoweredFlightSpeed();
                    if (pitchInfluence < 0.0) {
                        pitchInfluence = 0.0;
                    }
                    newPitch = velPitch + 15.0 * climbForceRatio * pitchInfluence;
                } else {
                    double pitchLimit = this.a.getMaxPitch();
                    double climbForceRatio = Math.min(yAccel / climbForce, 1.0);
                    newPitch = middlePitch + (pitchLimit - middlePitch) * climbForceRatio;
                }
                newPitch = this.correctRotation(this.a.field_70125_A, (float)newPitch, 1.5f);
                double newYaw = Math.atan2(velZ, velX) * 180.0 / Math.PI - 90.0;
                newYaw = this.correctRotation(this.a.field_70177_z, (float)newYaw, this.a.getTurnRate());
                this.a.func_70080_a(this.a.field_70165_t, this.a.field_70163_u, this.a.field_70161_v, (float)newYaw, (float)newPitch);
                double newRoll = 60.0 * bankForce / turnForce;
                this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), (float)newRoll, 6.0f));
                double horizontalForce = velY > 0.0 ? -climbAccel : forwardForce;
                int xDirection = velX > 0.0 ? 1 : -1;
                int zDirection = velZ > 0.0 ? 1 : -1;
                double hComponentX = (double)xDirection * velX / ((double)xDirection * velX + (double)zDirection * velZ);
                double xLiftAccel = (double)xDirection * horizontalForce * hComponentX;
                double zLiftAccel = (double)zDirection * horizontalForce * (1.0 - hComponentX);
                double loss = 0.4;
                xAccel += (xLiftAccel += (double)xDirection * -Math.abs(bankForce * loss) * hComponentX);
                zAccel += (zLiftAccel += (double)zDirection * -Math.abs(bankForce * loss) * (1.0 - hComponentX));
            }
            if (flySpeed < this.targetFlySpeed) {
                this.a.setThrustEffort(0.6f);
                if (!this.a.isThrustOn()) {
                    this.a.setThrustOn(true);
                }
                double desiredVThrustRatio = (dVelY - yAccel) / (double)this.a.getThrust();
                Vec3 thrust = this.calcThrust(desiredVThrustRatio);
                xAccel += thrust.field_72450_a;
                yAccel += thrust.field_72448_b;
                zAccel += thrust.field_72449_c;
            } else if (flySpeed > this.targetFlySpeed * 1.8) {
                this.a.setThrustEffort(1.0f);
                if (!this.a.isThrustOn()) {
                    this.a.setThrustOn(true);
                }
                double desiredVThrustRatio = (dVelY - yAccel) / (double)(this.a.getThrust() * 10.0f);
                Vec3 thrust = this.calcThrust(desiredVThrustRatio);
                xAccel += -thrust.field_72450_a;
                yAccel += thrust.field_72448_b;
                zAccel += -thrust.field_72449_c;
            } else if (this.a.isThrustOn()) {
                this.a.setThrustOn(false);
            }
            this.a.setFlightAccelerationVector((float)xAccel, (float)yAccel, (float)zAccel);
        }
        return FlyState.FLYING;
    }

    protected FlyState doTakeOff() {
        float gravity;
        this.a.setGroundFriction(0.98f);
        this.a.setThrustOn(true);
        this.a.setThrustEffort(1.0f);
        this.targetSpeed = this.a.getMoveSpeedStat();
        MoveState result = this.doGroundMovement();
        if (result == MoveState.STANDING) {
            return FlyState.GROUNDED;
        }
        if (this.a.field_70123_F) {
            this.a.func_70683_ar().func_75660_a();
        }
        Vec3 thrust = this.calcThrust(0.0);
        this.a.setFlightAccelerationVector((float)thrust.field_72450_a, (float)thrust.field_72448_b, (float)thrust.field_72449_c);
        double speed = Math.sqrt(this.a.field_70159_w * this.a.field_70159_w + this.a.field_70181_x * this.a.field_70181_x + this.a.field_70179_y * this.a.field_70179_y);
        this.a.field_70125_A = this.correctRotation(this.a.field_70125_A, 40.0f, 4.0f);
        float liftConstant = gravity = this.a.getGravity();
        double liftForce = speed / (double)(this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor()) * (double)liftConstant;
        if (liftForce > (double)gravity) {
            return FlyState.FLYING;
        }
        return FlyState.TAKEOFF;
    }

    protected FlyState doLanding() {
        this.a.setGroundFriction(0.3f);
        int x = MathHelper.func_76128_c((double)this.a.field_70165_t);
        int y = MathHelper.func_76128_c((double)this.a.field_70163_u);
        int z = MathHelper.func_76128_c((double)this.a.field_70161_v);
        for (int i = 1; i < 5 && this.a.field_70170_p.func_147439_a(x, y - i, z) == Blocks.field_150350_a; ++i) {
            this.targetFlySpeed = this.setSpeed * (double)(0.66f - (0.4f - (float)(i - 1) * 0.133f));
        }
        FlyState result = this.fly();
        this.a.setThrustOn(true);
        if (result == FlyState.FLYING) {
            double speed = Math.sqrt(this.a.field_70159_w * this.a.field_70159_w + this.a.field_70181_x * this.a.field_70181_x + this.a.field_70179_y * this.a.field_70179_y);
            if (this.a.field_70122_E) {
                if (speed < (double)this.a.getLandingSpeedThreshold()) {
                    return FlyState.GROUNDED;
                }
                this.a.setRotationRoll(this.correctRotation(this.a.getRotationRoll(), 40.0f, 6.0f));
                return FlyState.TOUCHDOWN;
            }
        }
        return FlyState.LANDING;
    }

    protected Vec3 calcThrust(double desiredVThrustRatio) {
        float thrust = this.a.getThrust();
        float rMin = this.a.getThrustComponentRatioMin();
        double vThrustRatio = desiredVThrustRatio;
        float rMax = this.a.getThrustComponentRatioMax();
        if (vThrustRatio > (double)rMax) {
            vThrustRatio = rMax;
        } else if (vThrustRatio < (double)rMin) {
            vThrustRatio = rMin;
        }
        double hThrust = (1.0 - vThrustRatio) * (double)thrust;
        double vThrust = vThrustRatio * (double)thrust;
        double xAccel = hThrust * -Math.sin((double)(this.a.field_70177_z / 180.0f) * Math.PI);
        double yAccel = vThrust;
        double zAccel = hThrust * Math.cos((double)(this.a.field_70177_z / 180.0f) * Math.PI);
        Vec3 vec = Vec3.func_72443_a((double)xAccel, (double)yAccel, (double)zAccel);
        return vec;
    }
}

