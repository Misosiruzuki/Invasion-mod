/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.projectile.EntityArrow
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.EntityAIKillEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;

public class EntityAIKillWithArrow<T extends EntityLivingBase>
extends EntityAIKillEntity<T> {
    private float attackRangeSq;

    public EntityAIKillWithArrow(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay, float attackRange) {
        super(entity, targetClass, attackDelay);
        this.attackRangeSq = attackRange * attackRange;
    }

    @Override
    public void func_75246_d() {
        super.func_75246_d();
        Object target = this.getTarget();
        if (this.getEntity().func_70092_e(((EntityLivingBase)target).field_70165_t, ((EntityLivingBase)target).field_70121_D.field_72338_b, ((EntityLivingBase)target).field_70161_v) < 36.0 && this.getEntity().func_70635_at().func_75522_a(target)) {
            this.getEntity().getNavigatorNew().haltForTick();
        }
    }

    @Override
    protected void attackEntity(Entity target) {
        this.setAttackTime(this.getAttackDelay());
        EntityIMLiving entity = this.getEntity();
        EntityArrow entityarrow = new EntityArrow(((EntityLivingBase)entity).field_70170_p, (EntityLivingBase)entity, this.getTarget(), 1.1f, 12.0f);
        ((EntityLivingBase)entity).field_70170_p.func_72956_a((Entity)entity, "random.bow", 1.0f, 1.0f / (entity.func_70681_au().nextFloat() * 0.4f + 0.8f));
        ((EntityLivingBase)entity).field_70170_p.func_72838_d((Entity)entityarrow);
    }

    @Override
    protected boolean canAttackEntity(Entity target) {
        return this.getAttackTime() <= 0 && this.getEntity().func_70092_e(target.field_70165_t, target.field_70121_D.field_72338_b, target.field_70161_v) < (double)this.attackRangeSq && this.getEntity().func_70635_at().func_75522_a(target);
    }
}

