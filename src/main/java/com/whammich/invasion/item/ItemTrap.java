package com.whammich.invasion.item;

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
 * Trap item — places EntityIMTrap when that entity is ported.
 */
public class ItemTrap extends Item {

    public ItemTrap(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos place = context.getClickedPos().relative(context.getClickedFace());
        if (context.getPlayer() != null) {
            context.getPlayer().displayClientMessage(
                    Component.translatable("item.invasion.trap.pending", place.getX(), place.getY(), place.getZ()),
                    true);
            if (!context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.trap.tooltip"));
    }
}
