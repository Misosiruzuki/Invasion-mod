package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAISprint extends Goal {
    private final EntityIMLiving mob;
    private final double sprintMultiplier;

    public EntityAISprint(EntityIMLiving mob, double sprintMultiplier) {
        this.mob = mob;
        this.sprintMultiplier = sprintMultiplier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && mob.distanceToSqr(t) > 9 && mob.distanceToSqr(t) < 100;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.getNavigation().moveTo(t, sprintMultiplier);
        }
    }
}
