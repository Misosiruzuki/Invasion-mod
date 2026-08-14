package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityIMZombiePigman extends EntityIMMob implements ICanDig {

    public EntityIMZombiePigman(EntityType<EntityIMZombiePigman> type, Level level) {
        super(type, level);
        setCanDig(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAISprinpigman(this));
        goalSelector.addGoal(4, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.1D, 18));
        goalSelector.addGoal(5, new com.whammich.invasion.entity.ai.EntityAIWanderIM(this, 0.85D));
        goalSelector.addGoal(8, new com.whammich.invasion.entity.ai.EntityAIWatchTarget(this));
        targetSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAITargetRetaliate(this));
        targetSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAISimpleTarget(this, 10));
        targetSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAITargetOnNoNexusPath(this));
    }

    @Override
    public boolean canDigBlock(BlockPos pos) { return canDig(); }

    @Override
    public void onBlockDigged(BlockPos pos) {}
}
