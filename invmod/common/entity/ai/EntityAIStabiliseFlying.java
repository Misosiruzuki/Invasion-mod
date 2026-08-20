/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStabiliseFlying
extends EntityAIBase {
    private static int INITIAL_STABILISE_TIME = 50;
    private EntityIMFlying theEntity;
    private int time;
    private int stabiliseTime;

    public EntityAIStabiliseFlying(EntityIMFlying entity, int stabiliseTime) {
        this.theEntity = entity;
        this.time = 0;
        this.stabiliseTime = stabiliseTime;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.STABILISE;
    }

    public boolean func_75253_b() {
        if (this.time >= this.stabiliseTime) {
            this.theEntity.transitionAIGoal(Goal.NONE);
            this.theEntity.getNavigatorNew().setPitchBias(0.0f, 0.0f);
            return false;
        }
        return true;
    }

    public void func_75249_e() {
        this.time = 0;
        INavigationFlying nav = this.theEntity.getNavigatorNew();
        nav.clearPath();
        nav.setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
        nav.setPitchBias(20.0f, 0.5f);
    }

    public void func_75246_d() {
        ++this.time;
        if (this.time == INITIAL_STABILISE_TIME) {
            this.theEntity.getNavigatorNew().setPitchBias(0.0f, 0.0f);
        }
    }
}

