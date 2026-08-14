package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class EntityAITargetRetaliate extends HurtByTargetGoal {
    private final EntityIMLiving imMob;

    public EntityAITargetRetaliate(EntityIMLiving mob) {
        super(mob);
        this.imMob = mob;
    }

    @Override
    public void start() {
        super.start();
        if (imMob.getTarget() != null) {
            imMob.setAIGoal(IMGoal.TARGET_ENTITY);
        }
    }
}
