/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMeleeAttack<T extends EntityLivingBase>
extends EntityAIBase {
    private EntityIMLiving theEntity;
    private Class<? extends T> targetClass;
    private float attackRange;
    private int attackDelay;
    private int nextAttack;

    public EntityAIMeleeAttack(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay) {
        this.theEntity = entity;
        this.targetClass = targetClass;
        this.attackDelay = attackDelay;
        this.attackRange = 0.6f;
        this.nextAttack = 0;
    }

    public boolean func_75250_a() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        return target != null && this.theEntity.getAIGoal() == Goal.MELEE_TARGET && this.theEntity.func_70032_d((Entity)target) < (this.attackRange + this.theEntity.field_70130_N + target.field_70130_N) * 4.0f && target.getClass().isAssignableFrom(this.targetClass);
    }

    public void func_75246_d() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (this.canAttackEntity(target)) {
            this.attackEntity(target);
        }
        this.setAttackTime(this.getAttackTime() - 1);
    }

    public Class<? extends T> getTargetClass() {
        return this.targetClass;
    }

    protected void attackEntity(EntityLivingBase target) {
        this.theEntity.func_70652_k((Entity)target);
        this.setAttackTime(this.getAttackDelay());
    }

    protected boolean canAttackEntity(EntityLivingBase target) {
        if (this.getAttackTime() <= 0) {
            double d = this.theEntity.field_70130_N + this.attackRange;
            return this.theEntity.func_70092_e(target.field_70165_t, target.field_70121_D.field_72338_b, target.field_70161_v) < d * d;
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

