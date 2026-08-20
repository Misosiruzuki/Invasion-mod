/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.EntityAIFollowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIRallyBehindEntity<T extends EntityLivingBase, ILeader>
extends EntityAIFollowEntity<T> {
    private static final float DEFAULT_FOLLOW_DISTANCE = 5.0f;

    public EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader) {
        this(entity, leader, 5.0f);
    }

    public EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader, float followDistance) {
        super(entity, leader, followDistance);
    }

    @Override
    public boolean func_75250_a() {
        return this.getEntity().readyToRally() && super.func_75250_a();
    }

    @Override
    public boolean func_75253_b() {
        return this.getEntity().readyToRally() && super.func_75253_b();
    }

    @Override
    public void func_75246_d() {
        super.func_75246_d();
        if (this.getEntity().readyToRally()) {
            Object t = this.getTarget();
        }
    }

    protected void rally(T leader) {
        this.getEntity().rally((Entity)leader);
    }
}

