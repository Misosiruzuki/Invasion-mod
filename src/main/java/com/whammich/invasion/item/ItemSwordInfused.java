package com.whammich.invasion.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Infused Sword — 1.7 ItemInfusedSword parity (D-23..D-25).
 * <p>
 * Durability bar is a charge gauge (max 40). Hits on living entities reduce damage
 * (charge up). At damage==0 (full), right-click heals 3 hearts; sneak restores food.
 * After use, gauge resets to empty (damage = max).
 */
public class ItemSwordInfused extends SwordItem {

    public static final int GAUGE_MAX = 40;

    public static final Tier INFUSED = new Tier() {
        @Override
        public int getUses() {
            return GAUGE_MAX;
        }

        @Override
        public float getSpeed() {
            return 8.0F; // diamond-like
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.0F; // diamond-ish total with base
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 10; // diamond sword
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    };

    public ItemSwordInfused(Properties properties) {
        super(INFUSED, 3, -2.4F, properties.defaultDurability(GAUGE_MAX));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Charge: each hit reduces damage value toward 0 (full gauge)
        if (stack.isDamaged()) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
        }
        return true; // do not call super (would increase damage / break)
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Full gauge = damage 0
        if (stack.getDamageValue() != 0) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                player.getFoodData().eat(6, 0.5F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                        level.random.nextFloat() * 0.1F + 0.9F);
            } else {
                player.heal(6.0F); // 3 hearts
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.2F);
            }
            stack.setDamageValue(GAUGE_MAX); // empty gauge after use
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        int charge = GAUGE_MAX - stack.getDamageValue();
        tooltip.add(Component.translatable("item.invasion.infused_sword.tooltip"));
        tooltip.add(Component.translatable("item.invasion.infused_sword.charge", charge, GAUGE_MAX));
    }
}
