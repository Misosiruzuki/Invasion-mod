/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.ai.EntityAIMeleeAttack;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIWingAttack
extends EntityAIMeleeAttack {
    private EntityIMBird theEntity;

    public EntityAIWingAttack(EntityIMBird entity, Class<? extends EntityLivingBase> targetClass, int attackDelay) {
        super(entity, targetClass, attackDelay);
        this.theEntity = entity;
    }

    @Override
    public void func_75246_d() {
        if (this.getAttackTime() == 0) {
            this.theEntity.setAttackingWithWings(this.isInStartMeleeRange());
        }
        super.func_75246_d();
    }

    public void func_75251_c() {
        this.theEntity.setAttackingWithWings(false);
    }

    protected boolean isInStartMeleeRange() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target == null) {
            return false;
        }
        double d = (double)(this.theEntity.field_70130_N + this.theEntity.getAttackRange()) + 3.0;
        return this.theEntity.func_70092_e(target.field_70165_t, target.field_70121_D.field_72338_b, target.field_70161_v) < d * d;
    }
}

