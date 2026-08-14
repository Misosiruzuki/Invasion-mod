package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityIMCreeper extends EntityIMMob {

    public EntityIMCreeper(EntityType<EntityIMCreeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAICreeperIMSwell(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.0D, 20));
        goalSelector.addGoal(5, new com.whammich.invasion.entity.ai.EntityAIWanderIM(this, 0.8D));
        targetSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAITargetRetaliate(this));
        targetSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAISimpleTarget(this, 10));
    }
}
