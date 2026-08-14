package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class EntityIMZombie extends EntityIMMob implements ICanDig {

    public EntityIMZombie(EntityType<EntityIMZombie> type, Level level) {
        super(type, level);
        setCanDig(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 24.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.23D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.0D, 20));
        goalSelector.addGoal(5, new com.whammich.invasion.entity.ai.EntityAIWanderIM(this, 0.8D));
        goalSelector.addGoal(8, new com.whammich.invasion.entity.ai.EntityAIWatchTarget(this));
        targetSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAITargetRetaliate(this));
        targetSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAISimpleTarget(this, 10));
        targetSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAITargetOnNoNexusPath(this));
    }

    @Override
    public boolean canDigBlock(net.minecraft.core.BlockPos pos) {
        return canDig();
    }

    @Override
    public void onBlockDigged(net.minecraft.core.BlockPos pos) {
    }
}
