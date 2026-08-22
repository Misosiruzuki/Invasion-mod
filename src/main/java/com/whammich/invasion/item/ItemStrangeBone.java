package com.whammich.invasion.item;

import com.whammich.invasion.entity.EntityIMWolf;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.registry.BlockRegistry;
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
 */
public class ItemStrangeBone extends Item {

    private static final int SEARCH_XZ = 7;
    private static final int SEARCH_Y = 4;

    public ItemStrangeBone(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BindResult result = tryBindWolf(player.level(), player, target, !player.getAbilities().instabuild ? stack : null);
        switch (result) {
            case BOUND -> {
                player.displayClientMessage(Component.translatable("item.invasion.strange_bone.bound"), true);
                return InteractionResult.CONSUME;
            }
            case NOT_TAMED -> {
                player.displayClientMessage(Component.translatable("item.invasion.strange_bone.not_tamed"), true);
                return InteractionResult.FAIL;
            }
            case NO_NEXUS -> {
                player.displayClientMessage(Component.translatable("item.invasion.strange_bone.no_nexus"), true);
                return InteractionResult.FAIL;
            }
            default -> {
                return InteractionResult.PASS;
            }
        }
    }

    public enum BindResult { BOUND, NOT_WOLF, NOT_TAMED, NO_NEXUS, FAILED }

    /**
     * Shared bind path for item use and {@code /invasion bindwolf}.
     * @param consumeFrom if non-null and bind succeeds, shrink by 1
     */
    public static BindResult tryBindWolf(Level level, Player player, LivingEntity target, ItemStack consumeFrom) {
        if (!(target instanceof Wolf wolf) || target instanceof EntityIMWolf) {
            return BindResult.NOT_WOLF;
        }
        if (!wolf.isTame()) {
            return BindResult.NOT_TAMED;
        }
        NexusBlockEntity nexus = findNearbyNexus(level, wolf.blockPosition());
        if (nexus == null) {
            return BindResult.NO_NEXUS;
        }
        if (!(level instanceof ServerLevel server)) {
            return BindResult.FAILED;
        }
        EntityIMWolf imWolf = EntityRegistry.WOLF.get().create(server);
        if (imWolf == null) {
            return BindResult.FAILED;
        }
        imWolf.moveTo(wolf.getX(), wolf.getY(), wolf.getZ(), wolf.getYRot(), wolf.getXRot());
        imWolf.bindToNexus(nexus.getBlockPos());
        if (wolf.getCustomName() != null) {
            imWolf.setCustomName(wolf.getCustomName());
            imWolf.setCustomNameVisible(true);
        }
        level.addFreshEntity(imWolf);
        wolf.discard();
        if (consumeFrom != null && !consumeFrom.isEmpty()) {
            consumeFrom.shrink(1);
        }
        LogHelper.info("StrangeBone: bound IM wolf at {} to nexus {}", wolf.blockPosition(), nexus.getBlockPos());
        return BindResult.BOUND;
    }

    public static NexusBlockEntity findNearbyNexus(Level level, BlockPos origin) {
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
