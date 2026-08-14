package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

/** Bird-of-prey pattern: keep range then swoop opportunity. */
public class EntityAIBoP extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;

    public EntityAIBoP(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        double d = mob.distanceToSqr(t);
        if (d > 100) {
            mob.setAIGoal(IMGoal.FLYING_TARGET_ENTITY);
            mob.getNavigation().moveTo(t, 1.1);
        } else if (d > 25) {
            mob.setAIGoal(IMGoal.FIND_ATTACK_OPPORTUNITY);
        } else {
            mob.setAIGoal(IMGoal.SWOOP);
        }
    }
}
