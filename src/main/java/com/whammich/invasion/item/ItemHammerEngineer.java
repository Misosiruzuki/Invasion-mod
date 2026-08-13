package com.whammich.invasion.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Engineer hammer — tool used by Pigman Engineer (entity AI later). */
public class ItemHammerEngineer extends Item {

    public ItemHammerEngineer(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.engy_hammer.tooltip"));
    }
}
