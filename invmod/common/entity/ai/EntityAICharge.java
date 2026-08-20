/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombiePigman;
import invmod.common.entity.ai.EntityAIMoveToEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityAICharge<T extends EntityLivingBase>
extends EntityAIMoveToEntity<T> {
    protected EntityCreature charger;
    protected EntityLivingBase chargeTarget;
    protected double chargeX;
    protected double chargeY;
    protected double chargeZ;
    protected float speed;
    protected int windup;
    protected boolean hasAttacked;
    protected int chargeDelay;
    protected int runTime;

    public EntityAICharge(EntityIMLiving entity, Class<? extends T> targetClass, float f) {
        super(entity, targetClass);
        this.charger = entity;
        this.speed = f;
        this.windup = 0;
        this.hasAttacked = false;
        this.chargeDelay = 100;
        this.runTime = 15;
    }

    @Override
    public boolean func_75250_a() {
        if (this.chargeDelay > 0) {
            --this.chargeDelay;
            return false;
        }
        this.chargeTarget = this.charger.func_70638_az();
        if (this.chargeTarget == null) {
            return false;
        }
        double distance = Math.sqrt(this.charger.func_70068_e((Entity)this.chargeTarget));
        if (distance < 5.0 || distance > 20.0) {
            return false;
        }
        if (!this.charger.field_70122_E) {
            return false;
        }
        Vec3 chargePos = this.findChargePoint((Entity)this.charger, (Entity)this.chargeTarget, 6.0);
        if (chargePos == null) {
            return false;
        }
        this.chargeX = chargePos.field_72450_a;
        this.chargeY = chargePos.field_72448_b;
        this.chargeZ = chargePos.field_72449_c;
        return this.charger.func_70681_au().nextInt(1) == 0;
    }

    @Override
    public void func_75249_e() {
        this.windup = 15 + this.charger.func_70681_au().nextInt(25);
    }

    @Override
    public boolean func_75253_b() {
        if (this.windup == 0 && this.runTime > 0) {
            --this.runTime;
        }
        return this.windup > 0 || this.runTime > 0;
    }

    @Override
    public void func_75246_d() {
        this.charger.func_70671_ap().func_75650_a(this.chargeX, this.chargeY - 1.0, this.chargeZ, 10.0f, (float)this.charger.func_70646_bf());
        if (this.windup > 0) {
            if (--this.windup == 0) {
                this.charger.func_70661_as().func_75492_a(this.chargeX, this.chargeY, this.chargeZ, (double)this.speed);
            } else {
                EntityCreature tmp90_87 = this.charger;
                tmp90_87.field_70721_aZ = (float)((double)tmp90_87.field_70721_aZ + 0.8);
                if (this.charger instanceof EntityIMZombiePigman) {
                    ((EntityIMZombiePigman)this.charger).setCharging(true);
                }
            }
        }
        double var1 = this.charger.field_70130_N * 2.1f * this.charger.field_70130_N * 2.1f;
        if (this.charger.func_70092_e(this.chargeTarget.field_70165_t, this.chargeTarget.field_70121_D.field_72338_b, this.chargeTarget.field_70161_v) <= var1 && !this.hasAttacked) {
            this.hasAttacked = true;
            this.charger.func_70652_k((Entity)this.chargeTarget);
        }
    }

    @Override
    public void func_75251_c() {
        this.windup = 0;
        this.chargeTarget = null;
        this.hasAttacked = false;
        this.chargeDelay = 100;
        this.runTime = 15;
        if (this.charger instanceof EntityIMZombiePigman) {
            ((EntityIMZombiePigman)this.charger).setCharging(false);
        }
    }

    protected Vec3 findChargePoint(Entity attacker, Entity target, double overshoot) {
        double vecx = target.field_70165_t - attacker.field_70165_t;
        double vecz = target.field_70161_v - attacker.field_70161_v;
        float rangle = (float)Math.atan2(vecz, vecx);
        double distance = MathHelper.func_76133_a((double)(vecx * vecx + vecz * vecz));
        double dx = (double)MathHelper.func_76134_b((float)rangle) * (distance + overshoot);
        double dz = (double)MathHelper.func_76126_a((float)rangle) * (distance + overshoot);
        return Vec3.func_72443_a((double)(attacker.field_70165_t + dx), (double)target.field_70163_u, (double)(attacker.field_70161_v + dz));
    }
}

