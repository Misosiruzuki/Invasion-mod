/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingMoveToEntity
extends EntityAIBase {
    private EntityIMFlying theEntity;

    public EntityAIFlyingMoveToEntity(EntityIMFlying entity) {
        this.theEntity = entity;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.GOTO_ENTITY && this.theEntity.func_70638_az() != null;
    }

    public void func_75249_e() {
        INavigationFlying nav = this.theEntity.getNavigatorNew();
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target != nav.getTargetEntity()) {
            nav.clearPath();
            nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
            Path path = nav.getPathToEntity((Entity)target, 0.0f);
            if ((double)path.getCurrentPathLength() > 2.0 * (double)this.theEntity.func_70032_d((Entity)target)) {
                nav.setMovementType(INavigationFlying.MoveType.MIXED);
            }
            nav.autoPathToEntity((Entity)target);
        }
    }

    public void func_75246_d() {
    }
}

