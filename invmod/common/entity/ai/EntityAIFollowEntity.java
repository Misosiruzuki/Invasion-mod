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

public class EntityAIFollowEntity<T extends EntityLivingBase>
extends EntityAIMoveToEntity<T> {
    private float followDistanceSq;

    public EntityAIFollowEntity(EntityIMLiving entity, float followDistance) {
        this(entity, EntityLivingBase.class, followDistance);
    }

    public EntityAIFollowEntity(EntityIMLiving entity, Class<? extends T> target, float followDistance) {
        super(entity, target);
        this.followDistanceSq = followDistance * followDistance;
    }

    @Override
    public void func_75249_e() {
        this.getEntity().onFollowingEntity((Entity)this.getTarget());
        super.func_75249_e();
    }

    @Override
    public void func_75251_c() {
        this.getEntity().onFollowingEntity(null);
        super.func_75251_c();
    }

    @Override
    public void func_75246_d() {
        super.func_75246_d();
        Object entity = this.getTarget();
        if (this.getEntity().func_70092_e(((Entity)entity).field_70165_t, ((Entity)entity).field_70121_D.field_72338_b, ((Entity)entity).field_70161_v) < (double)this.followDistanceSq) {
            this.getEntity().getNavigatorNew().haltForTick();
        }
    }
}

