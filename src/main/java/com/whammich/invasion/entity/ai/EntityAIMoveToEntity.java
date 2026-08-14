package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAIMoveToEntity extends Goal {
    private final EntityIMLiving mob;
    private final double speed;

    public EntityAIMoveToEntity(EntityIMLiving mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive();
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.getNavigation().moveTo(t, speed);
        }
    }
}
