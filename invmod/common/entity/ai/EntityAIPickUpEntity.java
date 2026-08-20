/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.Goal;
import invmod.common.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIPickUpEntity
extends EntityAIBase {
    private EntityIMBird theEntity;
    private int time;
    private int holdTime;
    private int abortTime;
    private float pickupPointY;
    private float pickupRangeY;
    private float pickupPointX;
    private float pickupPointZ;
    private float pickupRangeXZ;
    private float abortAngleYaw;
    private float abortAnglePitch;
    private boolean isHoldingEntity;

    public EntityAIPickUpEntity(EntityIMBird entity, float pickupPointX, float pickupPointY, float pickupPointZ, float pickupRangeY, float pickupRangeXZ, int abortTime, float abortAngleYaw, float abortAnglePitch) {
        this.theEntity = entity;
        this.time = 0;
        this.holdTime = 70;
        this.pickupPointX = pickupPointX;
        this.pickupPointY = pickupPointY;
        this.pickupPointZ = pickupPointZ;
        this.pickupRangeY = pickupRangeY;
        this.pickupRangeXZ = pickupRangeXZ;
        this.abortTime = abortTime;
        this.abortAngleYaw = abortAngleYaw;
        this.abortAnglePitch = abortAnglePitch;
        this.isHoldingEntity = false;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.PICK_UP_TARGET || this.theEntity.field_70153_n != null;
    }

    public void func_75249_e() {
        this.isHoldingEntity = this.theEntity.field_70153_n != null;
        this.time = 0;
    }

    public boolean func_75253_b() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target != null && !target.field_70128_L && (!this.isHoldingEntity ? this.time > this.abortTime && this.isLinedUp((Entity)target) : this.theEntity.field_70153_n == target)) {
            return true;
        }
        this.theEntity.transitionAIGoal(Goal.NONE);
        this.theEntity.setClawsForward(false);
        return false;
    }

    public void func_75246_d() {
        ++this.time;
        if (!this.isHoldingEntity) {
            EntityLivingBase target = this.theEntity.func_70638_az();
            double dY = target.field_70167_r - this.theEntity.field_70167_r;
            System.out.println(dY);
            if (Math.abs(dY - (double)this.pickupPointY) < (double)this.pickupRangeY) {
                double dAngle = (double)(this.theEntity.field_70126_B / 180.0f) * Math.PI;
                double sinF = Math.sin(dAngle);
                double cosF = Math.cos(dAngle);
                double x = (double)this.pickupPointX * cosF - (double)this.pickupPointZ * sinF;
                double z = (double)this.pickupPointZ * cosF + (double)this.pickupPointX * sinF;
                double dX = target.field_70169_q - (x + this.theEntity.field_70169_q);
                double dZ = target.field_70166_s - (z + this.theEntity.field_70166_s);
                double dXZ = Math.sqrt(dX * dX + dZ * dZ);
                System.out.println(dXZ);
                if (dXZ < (double)this.pickupRangeXZ) {
                    target.func_70078_a((Entity)this.theEntity);
                    this.isHoldingEntity = true;
                    this.time = 0;
                    this.theEntity.getNavigatorNew().clearPath();
                    this.theEntity.getNavigatorNew().setPitchBias(20.0f, 1.5f);
                }
            }
        } else if (this.time == 45) {
            this.theEntity.getNavigatorNew().setPitchBias(0.0f, 0.0f);
        } else if (this.time > this.holdTime) {
            this.theEntity.func_70638_az().func_70078_a(null);
        }
    }

    private boolean isLinedUp(Entity target) {
        double dX = target.field_70165_t - this.theEntity.field_70165_t;
        double dY = target.field_70163_u - this.theEntity.field_70163_u;
        double dZ = target.field_70161_v - this.theEntity.field_70161_v;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        double yawToTarget = Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0;
        double dYaw = MathUtil.boundAngle180Deg(yawToTarget - (double)this.theEntity.field_70177_z);
        if (dYaw < (double)(-this.abortAngleYaw) || dYaw > (double)this.abortAngleYaw) {
            return false;
        }
        double dPitch = Math.atan(dY / dXZ) * 180.0 / Math.PI - (double)this.theEntity.field_70125_A;
        return !(dPitch < (double)(-this.abortAnglePitch)) && !(dPitch > (double)this.abortAnglePitch);
    }
}

