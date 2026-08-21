package com.whammich.invasion.item;

import com.whammich.invasion.util.LogHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stat.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.function.Predicate;

/**
 * Searing Bow — 1.7 ItemSearingBow parity (D-26..D-30).
 * <p>
 * Normal draw behaves like a vanilla bow. When the uncapped power factor
 * reaches {@code >= 3.8} (~52+ ticks charge), the shot becomes a searing
 * arrow: set on fire, boosted damage (single target; no custom pierce entity).
 */
public class ItemBowSearing extends BowItem {

    /** 1.7 threshold after the standard bow power curve (uncapped). */
    public static final float SEARING_POWER_THRESHOLD = 3.8F;
    public static final int SEARING_FIRE_TICKS = 100;

    public ItemBowSearing(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) {
            return;
        }
        boolean creative = player.getAbilities().instabuild;
        ItemStack ammo = player.getProjectile(stack);
        int charge = this.getUseDuration(stack) - timeLeft;
        charge = ForgeEventFactory.onArrowLoose(stack, level, player, charge, !ammo.isEmpty() || creative);
        if (charge < 0) {
            return;
        }
        if (ammo.isEmpty() && !creative) {
            return;
        }
        if (ammo.isEmpty()) {
            ammo = new ItemStack(Items.ARROW);
        }

        float uncapped = uncappedPower(charge);
        boolean searing = uncapped >= SEARING_POWER_THRESHOLD;
        float power = Math.min(1.0F, uncapped);
        if (power < 0.1F) {
            return;
        }

        if (!level.isClientSide) {
            ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem a ? a : Items.ARROW);
            AbstractArrow arrow = arrowItem.createArrow(level, ammo, player);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);
            if (power == 1.0F) {
                arrow.setCritArrow(true);
            }
            int powerEnch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
            if (powerEnch > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerEnch * 0.5 + 0.5);
            }
            int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
            if (punch > 0) {
                arrow.setKnockback(punch);
            }
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                arrow.setSecondsOnFire(100);
            }
            if (searing) {
                // 1.7 special: always on fire, damage scaled ~1.5x + 1
                arrow.setSecondsOnFire(SEARING_FIRE_TICKS);
                arrow.setBaseDamage((arrow.getBaseDamage() + 0.5 + 0.5) * 3.0 / 2.0 + 1.0);
                arrow.setCritArrow(true);
                LogHelper.info("SearingBow special shot by {} charge={} uncapped={}",
                        player.getName().getString(), charge, uncapped);
            }
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
            if (creative || (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0
                    && ammo.is(Items.ARROW))) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            level.addFreshEntity(arrow);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);

        boolean infinite = creative || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        if (!infinite && !ammo.isEmpty()) {
            ammo.shrink(1);
            if (ammo.isEmpty() && ammo != player.getProjectile(stack)) {
                player.getInventory().removeItem(ammo);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    /** Uncapped bow power curve used by 1.7 to decide searing mode. */
    public static float uncappedPower(int chargeTicks) {
        float f = (float) chargeTicks / 20.0F;
        return (f * f + f * 2.0F) / 3.0F;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getEnchantmentValue() {
        return 1; // vanilla bow
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.searing_bow.tooltip"));
    }
}
