package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.EntityIMPigEngy;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class EntityAIWaitForEngy extends Goal {
    private final EntityIMLiving mob;

    public EntityAIWaitForEngy(EntityIMLiving mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<EntityIMPigEngy> engies = mob.level().getEntitiesOfClass(EntityIMPigEngy.class,
                mob.getBoundingBox().inflate(10), e -> e.isAlive());
        return !engies.isEmpty() && mob.getTarget() == null;
    }

    @Override
    public void tick() {
        mob.getNavigation().stop();
    }
}
