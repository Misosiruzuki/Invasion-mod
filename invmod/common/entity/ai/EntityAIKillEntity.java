/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.EntityAIMoveToEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIKillEntity<T extends EntityLivingBase>
extends EntityAIMoveToEntity<T> {
    private static final float ATTACK_RANGE = 1.0f;
    private int attackDelay;
    private int nextAttack;

    public EntityAIKillEntity(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay) {
        super(entity, targetClass);
        this.attackDelay = attackDelay;
        this.nextAttack = 0;
    }

    @Override
    public void func_75246_d() {
        super.func_75246_d();
        this.setAttackTime(this.getAttackTime() - 1);
        Object target = this.getTarget();
        if (this.canAttackEntity((Entity)target)) {
            this.attackEntity((Entity)target);
        }
    }

    protected void attackEntity(Entity target) {
        this.getEntity().func_70652_k((Entity)this.getTarget());
        this.setAttackTime(this.getAttackDelay());
    }

    protected boolean canAttackEntity(Entity target) {
        if (this.getAttackTime() <= 0) {
            EntityIMLiving entity = this.getEntity();
            double d = (((Entity)entity).field_70130_N + 1.0f) * (((Entity)entity).field_70130_N + 1.0f);
            return entity.func_70092_e(target.field_70165_t, target.field_70121_D.field_72338_b, target.field_70161_v) < d;
        }
        return false;
    }

    protected int getAttackTime() {
        return this.nextAttack;
    }

    protected void setAttackTime(int time) {
        this.nextAttack = time;
    }

    protected int getAttackDelay() {
        return this.attackDelay;
    }

    protected void setAttackDelay(int time) {
        this.attackDelay = time;
    }
}

