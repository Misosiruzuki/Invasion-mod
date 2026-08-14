package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class EntityAIWanderIM extends WaterAvoidingRandomStrollGoal {
    public EntityAIWanderIM(EntityIMLiving mob, double speed) {
        super(mob, speed);
    }
}
