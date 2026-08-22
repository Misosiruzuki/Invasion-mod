package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * Invasion zombie — looks similar to vanilla but uses fixed Invasion stats (E-03 / E-10+).
 * Attack strengths from 1.7 {@code EntityIMZombie.setAttributes}; HP from Invasion defaults
 * (not vanilla 20 HP / 3 dmg).
 */
public class EntityIMZombie extends EntityIMMob implements ICanDig {

    public EntityIMZombie(EntityType<EntityIMZombie> type, Level level) {
        super(type, level);
        setCanDig(true);
        applyTierAttributes(1, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Base registration values; instance applies tier on construct / setTier
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.19D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void setTier(int tier) {
        super.setTier(tier);
        applyTierAttributes(tier, 0);
    }

    /**
     * 1.7 setAttributes(tier, flavour) — flavour 0 basic (E-03: not vanilla stats).
     * HP: Wiki/Invasion table T1=10 T2=15 T3=32.5 (tar handled later).
     */
    private void applyTierAttributes(int tier, int flavour) {
        double health;
        double damage;
        double speed = 0.19D;
        if (tier <= 1) {
            health = 10.0D;
            damage = flavour == 1 ? 6.0D : 4.0D; // 1.7: bare 4 / sword 6
        } else if (tier == 2) {
            health = 15.0D;
            damage = flavour == 1 ? 10.0D : 7.0D;
        } else {
            health = 32.5D;
            damage = 9.0D;
            speed = 0.21D;
        }
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
            setHealth((float) health);
        }
        if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage);
        }
        if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        }
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
