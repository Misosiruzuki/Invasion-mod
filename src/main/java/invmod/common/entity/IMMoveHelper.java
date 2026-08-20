/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityMoveHelper
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.MoveState;
import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class IMMoveHelper
extends EntityMoveHelper {
    protected EntityIMLiving a;
    protected double b;
    protected double c;
    protected double d;
    protected double setSpeed;
    protected double targetSpeed;
    protected boolean needsUpdate = false;
    protected boolean isRunning;

    public IMMoveHelper(EntityIMLiving par1EntityLiving) {
        super((EntityLiving)par1EntityLiving);
        this.a = par1EntityLiving;
        this.b = par1EntityLiving.field_70165_t;
        this.c = par1EntityLiving.field_70163_u;
        this.d = par1EntityLiving.field_70161_v;
        this.targetSpeed = 0.0;
        this.setSpeed = 0.0;
    }

    public boolean func_75640_a() {
        return this.needsUpdate;
    }

    public double func_75638_b() {
        return this.setSpeed;
    }

    public void setMoveSpeed(float speed) {
        this.setSpeed = speed;
    }

    public void setMoveTo(IPosition pos, float speed) {
        this.func_75642_a(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), speed);
    }

    public void func_75642_a(double x, double y, double z, double speed) {
        this.b = x;
        this.c = y;
        this.d = z;
        this.setSpeed = speed;
        this.needsUpdate = true;
    }

    public void func_75641_c() {
        if (!this.needsUpdate) {
            this.a.func_70657_f(0.0f);
            this.a.setMoveState(MoveState.STANDING);
            return;
        }
        MoveState result = this.doGroundMovement();
        this.a.setMoveState(result);
    }

    protected MoveState doGroundMovement() {
        double dXZSq;
        double distanceSquared;
        this.needsUpdate = false;
        this.targetSpeed = this.setSpeed;
        boolean isInLiquid = this.a.func_70090_H() || this.a.func_70058_J();
        double dX = this.b - this.a.field_70165_t;
        double dZ = this.d - this.a.field_70161_v;
        double dY = this.c - (!isInLiquid ? (double)MathHelper.func_76128_c((double)(this.a.field_70121_D.field_72338_b + 0.5)) : this.a.field_70163_u);
        float newYaw = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI) - 90.0f;
        int ladderPos = -1;
        if (Math.abs(dX) < 0.8 && Math.abs(dZ) < 0.8 && (dY > 0.0 || this.a.isHoldingOntoLadder())) {
            ladderPos = this.getClimbFace(this.a.field_70165_t, this.a.field_70163_u, this.a.field_70161_v);
            if (ladderPos == -1) {
                ladderPos = this.getClimbFace(this.a.field_70165_t, this.a.field_70163_u + 1.0, this.a.field_70161_v);
            }
            switch (ladderPos) {
                case 0: {
                    newYaw = (float)(Math.atan2(dZ, dX + 1.0) * 180.0 / Math.PI) - 90.0f;
                    break;
                }
                case 1: {
                    newYaw = (float)(Math.atan2(dZ, dX - 1.0) * 180.0 / Math.PI) - 90.0f;
                    break;
                }
                case 2: {
                    newYaw = (float)(Math.atan2(dZ + 1.0, dX) * 180.0 / Math.PI) - 90.0f;
                    break;
                }
                case 3: {
                    newYaw = (float)(Math.atan2(dZ - 1.0, dX) * 180.0 / Math.PI) - 90.0f;
                }
            }
        }
        if ((distanceSquared = (dXZSq = dX * dX + dZ * dZ) + dY * dY) < 0.01 && ladderPos == -1) {
            return MoveState.STANDING;
        }
        if (dXZSq > 0.04 || ladderPos != -1) {
            this.a.field_70177_z = this.correctRotation(this.a.field_70177_z, newYaw, this.a.getTurnRate());
            double moveSpeed = distanceSquared >= 0.064 || this.a.func_70051_ag() ? this.targetSpeed : this.targetSpeed * 0.5;
            if (this.a.func_70090_H() && moveSpeed < 0.6) {
                moveSpeed = 0.6f;
            }
            this.a.func_70659_e((float)moveSpeed);
        }
        double w = Math.max((double)(this.a.field_70130_N * 0.5f + 1.0f), 1.0);
        w = this.a.field_70130_N * 0.5f + 1.0f;
        if (dY > 0.0 && (dX * dX + dZ * dZ <= w * w || isInLiquid)) {
            this.a.func_70683_ar().func_75660_a();
            if (ladderPos != -1) {
                return MoveState.CLIMBING;
            }
        }
        return MoveState.RUNNING;
    }

    protected float correctRotation(float currentYaw, float newYaw, float turnSpeed) {
        float dYaw;
        for (dYaw = newYaw - currentYaw; dYaw < -180.0f; dYaw += 360.0f) {
        }
        while (dYaw >= 180.0f) {
            dYaw -= 360.0f;
        }
        if (dYaw > turnSpeed) {
            dYaw = turnSpeed;
        }
        if (dYaw < -turnSpeed) {
            dYaw = -turnSpeed;
        }
        return currentYaw + dYaw;
    }

    protected int getClimbFace(double x, double y, double z) {
        int mobZ;
        int mobY;
        int mobX = MathHelper.func_76128_c((double)x);
        Block block = this.a.field_70170_p.func_147439_a(mobX, mobY = MathHelper.func_76128_c((double)y), mobZ = MathHelper.func_76128_c((double)z));
        if (block == Blocks.field_150468_ap) {
            int meta = this.a.field_70170_p.func_72805_g(mobX, mobY, mobZ);
            if (meta == 2) {
                return 2;
            }
            if (meta == 3) {
                return 3;
            }
            if (meta == 4) {
                return 0;
            }
            if (meta == 5) {
                return 1;
            }
        } else if (block == Blocks.field_150395_bd) {
            int meta = this.a.field_70170_p.func_72805_g(mobX, mobY, mobZ);
            if (meta == 1) {
                return 2;
            }
            if (meta == 4) {
                return 3;
            }
            if (meta == 2) {
                return 1;
            }
            if (meta == 8) {
                return 0;
            }
        }
        return -1;
    }
}

