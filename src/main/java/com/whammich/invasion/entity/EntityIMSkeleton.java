package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityIMSkeleton extends EntityIMMob {

    public EntityIMSkeleton(EntityType<EntityIMSkeleton> type, Level level) {
        super(type, level);
        setCanDig(false); // E-04: no block break, still attacks Nexus
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIKillWithArrow(this, 1.0D, 25));
        goalSelector.addGoal(5, new com.whammich.invasion.entity.ai.EntityAIWanderIM(this, 0.8D));
        targetSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAITargetRetaliate(this));
        targetSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAISimpleTarget(this, 10));
        targetSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAITargetOnNoNexusPath(this));
    }
}
