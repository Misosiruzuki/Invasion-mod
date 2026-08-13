package com.whammich.invasion.item;

import com.whammich.invasion.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Debug wand — useful when enableLogging is on; full Nexus debug when Nexus exists.
 */
public class ItemWandDebug extends Item {

    public ItemWandDebug(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!ConfigHandler.COMMON.enableLogging.get()) {
            if (context.getPlayer() != null) {
                context.getPlayer().displayClientMessage(
                        Component.translatable("item.invasion.debug_wand.disabled"), true);
            }
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos();
        if (context.getPlayer() != null) {
            context.getPlayer().displayClientMessage(
                    Component.literal(String.format("Debug @ %d %d %d dim=%s",
                            pos.getX(), pos.getY(), pos.getZ(),
                            level.dimension().location())),
                    false);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.debug_wand.tooltip"));
    }
}
