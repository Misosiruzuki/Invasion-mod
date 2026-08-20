/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.MoveState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingTackle
extends EntityAIBase {
    private EntityIMFlying theEntity;
    private int time;

    public EntityAIFlyingTackle(EntityIMFlying entity) {
        this.theEntity = entity;
        this.time = 0;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.TACKLE_TARGET;
    }

    public boolean func_75253_b() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target == null || target.field_70128_L) {
            this.theEntity.transitionAIGoal(Goal.NONE);
            return false;
        }
        return this.theEntity.getAIGoal() == Goal.TACKLE_TARGET;
    }

    public void func_75249_e() {
        this.time = 0;
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target != null) {
            this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
        }
    }

    public void func_75246_d() {
        if (this.theEntity.getMoveState() != MoveState.FLYING) {
            this.theEntity.transitionAIGoal(Goal.MELEE_TARGET);
        }
    }
}

