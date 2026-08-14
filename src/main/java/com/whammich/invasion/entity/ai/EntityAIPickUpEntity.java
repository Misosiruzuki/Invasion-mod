package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

/** Attempt to carry target (birds). Simplified: stick close and mark goal. */
public class EntityAIPickUpEntity extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMLiving mob;

    public EntityAIPickUpEntity(EntityIMLiving mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && t.getBbHeight() < 1.2F && mob.distanceToSqr(t) < 4;
    }

    @Override
    public void start() {
        mob.setAIGoal(IMGoal.PICK_UP_TARGET);
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t != null) {
            t.setPos(mob.getX(), mob.getY() - 0.5, mob.getZ());
        }
    }
}
