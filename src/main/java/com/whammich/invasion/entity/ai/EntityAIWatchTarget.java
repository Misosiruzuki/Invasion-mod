package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAIWatchTarget extends Goal {
    private final EntityIMLiving mob;

    public EntityAIWatchTarget(EntityIMLiving mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.getLookControl().setLookAt(t, 30.0F, 30.0F);
        }
    }
}
