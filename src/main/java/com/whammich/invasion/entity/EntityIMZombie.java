package com.whammich.invasion.entity;

import com.whammich.invasion.util.LogHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Port of 1.7 {@code invmod.common.entity.EntityIMZombie}
 * (compared to ref/1.7.10-decompiled/.../EntityIMZombie.java).
 *
 * <p>E-10 flavour 0: atk 4, dig on. E-11 flavour 1: atk 6, wooden sword held+drop 0.2, no dig.
 */
public class EntityIMZombie extends EntityIMMob implements ICanDig {

    private int flavour;
    /** 1.7 itemDrop */
    private Item itemDrop;
    /** 1.7 dropChance */
    private float dropChance;

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
        setAttributes(getTier(), flavour);
    }

    @Override
    public void setTier(int tier) {
        super.setTier(tier);
        setAttributes(tier, this.flavour);
    }

    /**
     * 1.7 {@code setAttributes(int tier, int flavour)} — combat + held/drop fields for T1.
     */
    private void setAttributes(int tier, int flavour) {
        this.flavour = flavour;
        double health;
        double damage;
        double speed = 0.19D;
        int destructiveness;
        this.itemDrop = null;
        this.dropChance = 0.0f;

        if (tier <= 1) {
            health = 10.0D;
            if (flavour == 1) {
                // 1.7: attackStrength=6, maxDestructiveness=0, wooden sword hold+drop 0.2
                damage = 6.0D;
                destructiveness = 0;
                this.itemDrop = Items.WOODEN_SWORD;
                this.dropChance = 0.2f;
                if (!level().isClientSide) {
                    setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
                    setDropChance(EquipmentSlot.MAINHAND, 0.0f); // 1.7 uses custom drop, not equipment drop
                }
            } else {
                damage = 4.0D;
                destructiveness = 2;
                if (!level().isClientSide) {
                    setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
            }
        } else if (tier == 2) {
            health = 15.0D;
            if (flavour == 1) {
                damage = 10.0D;
                destructiveness = 0;
                this.itemDrop = Items.IRON_SWORD;
                this.dropChance = 0.25f;
                if (!level().isClientSide) {
                    setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                    setDropChance(EquipmentSlot.MAINHAND, 0.0f);
                }
            } else {
                damage = 7.0D;
                destructiveness = 2;
            }
        } else {
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

    /**
     * 1.7 {@code dropFewItems} / {@code func_70628_a}: custom itemDrop with dropChance.
     * VoxPilotTrack name forces 100% for stable E-11 test (checklist X-04).
     */
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (itemDrop == null || level().isClientSide) {
            return;
        }
        float chance = dropChance;
        if (hasCustomName() && getCustomName() != null
                && "VoxPilotTrack".equals(getCustomName().getString())) {
            chance = 1.0f;
        }
        float roll = random.nextFloat();
        boolean success = roll < chance;
        LogHelper.info("ZombieDrop flavour={} item={} roll={} chance={} success={}",
                flavour, itemDrop, roll, chance, success);
        if (success) {
            spawnAtLocation(new ItemStack(itemDrop));
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
        goalSelector.addGoal(1, new com.whammich.invasion.entity.ai.EntityAIAttackNexus(this));
        goalSelector.addGoal(2, new com.whammich.invasion.entity.ai.EntityAIGoToNexus(this));
        goalSelector.addGoal(3, new com.whammich.invasion.entity.ai.EntityAIMeleeAttack(this, 1.0D, 20));
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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Flavour", flavour);
        tag.putFloat("DropChance", dropChance);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Flavour")) {
            setAttributes(getTier(), tag.getInt("Flavour"));
        }
    }
}
