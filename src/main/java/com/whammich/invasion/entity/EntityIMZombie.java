package com.whammich.invasion.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * Port of 1.7 {@code invmod.common.entity.EntityIMZombie}.
 * Compared file-for-file against ref/1.7.10-decompiled/.../EntityIMZombie.java
 * ({@code setAttributes(tier, flavour)}).
 *
 * <p>E-10 Tier1 flavour 0: move 0.19, attackStrength 4, destructiveness 2 → dig enabled.
 * HP: Wiki/checklist T1=10 until {@code getMobHealth} config map is ported from 1.7.
 *
 * <p>Note: 1.7 has no {@code EntityAIDig*} goal — digging is PathAction via AttackerAI.
 * {@link com.whammich.invasion.entity.ai.EntityAIDigTowardNexus} is an interim stand-in
 * until pathfinder DIG is ported (F-03).
 */
public class EntityIMZombie extends EntityIMMob implements ICanDig {

    /** 1.7 flavour: 0 basic, 1 sword-bearer (E-11). */
    private int flavour;

    public EntityIMZombie(EntityType<EntityIMZombie> type, Level level) {
        super(type, level);
        this.flavour = 0;
        setAttributes(1, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.19D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public int getFlavour() {
        return flavour;
    }

    public void setFlavour(int flavour) {
        this.flavour = flavour;
        setAttributes(getTier(), flavour);
    }

    @Override
    public void setTier(int tier) {
        super.setTier(tier);
        setAttributes(tier, this.flavour);
    }

    /**
     * 1.7 {@code private void setAttributes(int tier, int flavour)}.
     * Only numeric/combat fields ported here; name/gender/drop item left for later IDs.
     */
    private void setAttributes(int tier, int flavour) {
        this.flavour = flavour;
        double health;
        double damage;
        double speed = 0.19D;
        int destructiveness;
        if (tier <= 1) {
            // 1.7 tier==1
            if (flavour == 1) {
                // sword: attackStrength=6, maxDestructiveness=0
                health = 10.0D;
                damage = 6.0D;
                destructiveness = 0;
            } else {
                // bare: attackStrength=4, destructiveness=2
                health = 10.0D;
                damage = 4.0D;
                destructiveness = 2;
            }
        } else if (tier == 2) {
            if (flavour == 1) {
                health = 15.0D;
                damage = 10.0D;
                destructiveness = 0;
            } else if (flavour == 2) {
                // tar path partially: 1.7 attackStrength=5
                health = 15.0D;
                damage = 5.0D;
                destructiveness = 2;
            } else {
                health = 15.0D;
                damage = 7.0D;
                destructiveness = 2;
            }
        } else {
            // tier 3 brute: 1.7 attackStrength=18 in one branch; wiki 9 bare — use 1.7 18 for charged later
            health = 32.5D;
            damage = 9.0D;
            speed = 0.21D;
            destructiveness = 2;
        }
        setCanDig(destructiveness > 0);
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
        // 1.7 order (simplified): swim, kill player, AttackNexus, GoToNexus, Wander, watch
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.0D, 20));
        // Interim dig (not a 1.7 Goal class — see class javadoc)
        goalSelector.addGoal(4, new com.whammich.invasion.entity.ai.EntityAIDigTowardNexus(this));
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
        // 1.7 applies self-damage on dig via selfDamage fields — deferred
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Flavour", flavour);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Flavour")) {
            this.flavour = tag.getInt("Flavour");
            setAttributes(getTier(), flavour);
        }
    }
}
