package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityIMSpider extends EntityIMMob implements ISpawnsOffspring {

    public EntityIMSpider(EntityType<EntityIMSpider> type, Level level) {
        super(type, level);
        setCanClimb(true);
        this.moveControl = new IMMoveHelperSpider(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 18.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIPounce(this));
        goalSelector.addGoal(4, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.0D, 15));
        goalSelector.addGoal(6, new com.whammich.invasion.entity.ai.EntityAILayEgg(this));
        goalSelector.addGoal(7, new com.whammich.invasion.entity.ai.EntityAIWanderIM(this, 0.8D));
        targetSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAITargetRetaliate(this));
        targetSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAISimpleTarget(this, 10));
    }

    @Override
    public void spawnOffspring() {}
}
