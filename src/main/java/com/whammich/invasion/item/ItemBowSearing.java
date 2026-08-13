package com.whammich.invasion.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Searing bow — uses vanilla BowItem behaviour for now.
 * Special charged shot (old f >= 3.8 special path) will be restored with custom projectiles.
 */
public class ItemBowSearing extends BowItem {

    public ItemBowSearing(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.searing_bow.tooltip"));
    }
}
