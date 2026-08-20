/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMThrower;
import invmod.common.entity.ai.EntityAIKillEntity;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAIThrowerKillEntity<T extends EntityLivingBase>
extends EntityAIKillEntity<T> {
    private boolean melee;
    private float attackRangeSq;
    private float launchSpeed;
    private final EntityIMThrower theEntity;
    private Random rand = new Random();
    private int maxBoulderAmount;

    public EntityAIThrowerKillEntity(EntityIMThrower entity, Class<? extends T> targetClass, int attackDelay, float throwRange, float launchSpeed) {
        super(entity, targetClass, attackDelay);
        this.attackRangeSq = throwRange * throwRange;
        this.launchSpeed = launchSpeed;
        this.theEntity = entity;
        this.maxBoulderAmount = 3;
    }

    @Override
    protected void attackEntity(Entity target) {
        if (this.melee) {
            this.setAttackTime(this.getAttackDelay());
            super.attackEntity(target);
        } else {
            this.setAttackTime(this.getAttackDelay() * 2);
            int distance = Math.round((float)this.theEntity.func_70011_f(target.field_70165_t, target.field_70163_u, target.field_70161_v));
            int missDistance = Math.round((float)Math.ceil(distance / 10));
            for (int i = 1; i <= this.rand.nextInt(this.maxBoulderAmount); ++i) {
                double x = target.field_70165_t - (double)missDistance + (double)this.rand.nextInt((missDistance + 1) * 2);
                double y = target.field_70163_u - (double)missDistance + 1.0 + (double)this.rand.nextInt((missDistance + 1) * 2);
                double z = target.field_70161_v - (double)missDistance + (double)this.rand.nextInt((missDistance + 1) * 2);
                if (this.theEntity.getTier() == 1) {
                    this.theEntity.throwBoulder(x, y, z);
                    continue;
                }
                this.theEntity.throwTNT(x, y, z);
            }
        }
    }

    @Override
    protected boolean canAttackEntity(Entity target) {
        this.melee = super.canAttackEntity(target);
        if (this.melee) {
            return true;
        }
        if (!this.theEntity.canThrow()) {
            return false;
        }
        double dX = this.theEntity.field_70165_t - target.field_70165_t;
        double dZ = this.theEntity.field_70161_v - target.field_70161_v;
        double dXY = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ));
        return this.getAttackTime() <= 0 && this.theEntity.func_70635_at().func_75522_a(target) && 0.025 * dXY / (double)(this.launchSpeed * this.launchSpeed) <= 1.0;
    }
}

