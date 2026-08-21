package com.whammich.invasion.item;

import com.whammich.invasion.registry.BlockRegistry;
import com.whammich.invasion.entity.EntityIMWolf;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

/**
 * Strange Bone — 1.7 ItemStrangeBone parity (D-31..D-34).
 * Use on a tamed wolf near a Nexus to bind it as an IM wolf.
 */
public class ItemStrangeBone extends Item {

    private static final int SEARCH_XZ = 7;
    private static final int SEARCH_Y = 4;

    public ItemStrangeBone(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!(target instanceof Wolf wolf) || target instanceof EntityIMWolf) {
            return InteractionResult.PASS;
        }
        if (!wolf.isTame()) {
            player.displayClientMessage(Component.translatable("item.invasion.strange_bone.not_tamed"), true);
            return InteractionResult.FAIL;
        }
        BlockPos wolfPos = wolf.blockPosition();
        NexusBlockEntity nexus = findNearbyNexus(level, wolfPos);
        if (nexus == null) {
            player.displayClientMessage(Component.translatable("item.invasion.strange_bone.no_nexus"), true);
            return InteractionResult.FAIL;
        }
        EntityIMWolf imWolf = EntityRegistry.WOLF.get().create((ServerLevel) level);
        if (imWolf == null) {
            return InteractionResult.FAIL;
        }
        imWolf.moveTo(wolf.getX(), wolf.getY(), wolf.getZ(), wolf.getYRot(), wolf.getXRot());
        imWolf.bindToNexus(nexus.getBlockPos());
        imWolf.setCustomName(wolf.getCustomName());
        if (wolf.hasCustomName()) {
            imWolf.setCustomNameVisible(true);
        }
        level.addFreshEntity(imWolf);
        wolf.discard();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        LogHelper.info("StrangeBone: bound IM wolf at {} to nexus {}", wolfPos, nexus.getBlockPos());
        player.displayClientMessage(Component.translatable("item.invasion.strange_bone.bound"), true);
        return InteractionResult.CONSUME;
    }

    private static NexusBlockEntity findNearbyNexus(Level level, BlockPos origin) {
        for (int dy = -SEARCH_Y; dy <= SEARCH_Y; dy++) {
            for (int dx = -SEARCH_XZ; dx <= SEARCH_XZ; dx++) {
                for (int dz = -SEARCH_XZ; dz <= SEARCH_XZ; dz++) {
                    BlockPos p = origin.offset(dx, dy, dz);
                    if (!level.getBlockState(p).is(BlockRegistry.NEXUS.get())) {
                        continue;
                    }
                    BlockEntity be = level.getBlockEntity(p);
                    if (be instanceof NexusBlockEntity nexus) {
                        return nexus;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.strange_bone.tooltip"));
    }
}
