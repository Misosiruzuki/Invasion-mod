/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAIPounce
extends EntityAIBase {
    private EntityIMSpider theEntity;
    private boolean isPouncing;
    private int pounceTimer;
    private int cooldown;
    private float minPower;
    private float maxPower;

    public EntityAIPounce(EntityIMSpider entity, float minPower, float maxPower, int cooldown) {
        this.theEntity = entity;
        this.isPouncing = false;
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.cooldown = cooldown;
    }

    public boolean func_75250_a() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        return --this.pounceTimer <= 0 && target != null && this.theEntity.func_70685_l((Entity)target) && this.theEntity.field_70122_E;
    }

    public boolean func_75253_b() {
        return this.isPouncing;
    }

    public void func_75249_e() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (this.pounce(target.field_70165_t, target.field_70163_u, target.field_70161_v)) {
            this.theEntity.setAirborneTime(0);
            this.isPouncing = true;
            this.theEntity.getNavigatorNew().haltForTick();
        } else {
            this.isPouncing = false;
        }
    }

    public void func_75246_d() {
        this.theEntity.getNavigatorNew().haltForTick();
        int airborneTime = this.theEntity.getAirborneTime();
        if (airborneTime > 20 && this.theEntity.field_70122_E) {
            this.isPouncing = false;
            this.pounceTimer = this.cooldown;
            this.theEntity.setAirborneTime(0);
            this.theEntity.getNavigatorNew().clearPath();
        } else {
            this.theEntity.setAirborneTime(airborneTime + 1);
        }
    }

    protected boolean pounce(double x, double y, double z) {
        double dY = y - this.theEntity.field_70163_u;
        double dX = x - this.theEntity.field_70165_t;
        double dZ = z - this.theEntity.field_70161_v;
        double dXZ = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ));
        double a = Math.atan(dY / dXZ);
        if (a > -0.7853981633974483 && a < 0.7853981633974483) {
            double rratio = (1.0 - Math.tan(a)) * (1.0 / Math.cos(a));
            double r = dXZ / rratio;
            double v = 1.0 / Math.sqrt((double)(1.0f / this.theEntity.getGravity()) / r);
            if (v > (double)this.minPower && v < (double)this.maxPower) {
                double distance = MathHelper.func_76133_a((double)(2.0 * (dXZ * dXZ)));
                this.theEntity.field_70159_w = v * dX / distance;
                this.theEntity.field_70181_x = v * dXZ / distance;
                this.theEntity.field_70179_y = v * dZ / distance;
                return true;
            }
        }
        return false;
    }
}

