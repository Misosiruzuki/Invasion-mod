package com.whammich.invasion.item;

import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.registry.BlockRegistry;
import com.whammich.invasion.util.BlockStrength;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Nexus Adjuster / Material Probe (1.7 ItemProbe meta 0/1).
 * Nexus GUI would swallow normal useOn — handle via {@link #handleRightClickBlock}.
 */
public class ItemProbe extends Item {

    public enum Kind {
        ADJUSTER,
        MATERIAL
    }

    private final Kind kind;

    public ItemProbe(Properties properties, Kind kind) {
        super(properties);
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }

    /**
     * Called from Forge RightClickBlock before the Nexus opens its menu (1.7 onItemUseFirst parity).
     * @return true if the click was handled (caller should cancel the event)
     */
    public static boolean handleRightClickBlock(Player player, Level level, BlockPos pos, ItemStack stack) {
        if (!(stack.getItem() instanceof ItemProbe probe)) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockRegistry.NEXUS.get())) {
            if (!level.isClientSide) {
                applyNexusRadius(level, pos, player);
            }
            return true;
        }
        if (probe.kind == Kind.MATERIAL) {
            if (!level.isClientSide) {
                float strength = BlockStrength.get(level, pos, state);
                double shown = Math.round((strength + 0.005) * 100.0) / 100.0;
                player.displayClientMessage(
                        Component.translatable("item.invasion.material_probe.strength", shown), false);
                LogHelper.info("MaterialProbe strength={} at {}", shown, pos);
            }
            return true;
        }
        return false;
    }

    private static InteractionResult applyNexusRadius(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof NexusBlockEntity nexus)) {
            return InteractionResult.FAIL;
        }
        int current = nexus.getSpawnRadius();
        int next;
        boolean sneak = player != null && player.isShiftKeyDown();
        if (sneak) {
            next = current - 8;
            if (next < 32) {
                next = 128;
            }
        } else {
            next = current + 8;
            if (next > 128) {
                next = 32;
            }
        }
        if (!nexus.setSpawnRadius(next)) {
            if (player != null) {
                player.displayClientMessage(Component.translatable("item.invasion.nexus_adjuster.busy"), true);
            }
            return InteractionResult.FAIL;
        }
        if (player != null) {
            player.displayClientMessage(
                    Component.translatable("item.invasion.nexus_adjuster.range", nexus.getSpawnRadius()), false);
        }
        LogHelper.info("NexusAdjuster radius {} -> {} at {}", current, nexus.getSpawnRadius(), pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        // Fallback if event did not fire; still try
        if (ItemProbe.handleRightClickBlock(ctx.getPlayer(), ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand())) {
            return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (kind == Kind.ADJUSTER) {
            tooltip.add(Component.translatable("item.invasion.nexus_adjuster.tooltip"));
        } else {
            tooltip.add(Component.translatable("item.invasion.material_probe.tooltip"));
        }
    }
}
