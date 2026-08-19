package com.whammich.invasion.item;

import com.whammich.invasion.entity.EntityIMTrap;
import com.whammich.invasion.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Trap item — EMPTY charges in Nexus to RIFT; places EntityIMTrap (D-17..D-22).
 */
public class ItemTrap extends Item {

    public enum TrapKind {
        EMPTY,
        RIFT,
        FLAME
    }

    private final TrapKind kind;

    public ItemTrap(Properties properties, TrapKind kind) {
        super(properties);
        this.kind = kind;
    }

    public TrapKind getKind() {
        return kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (kind == TrapKind.EMPTY) {
            if (context.getPlayer() != null) {
                context.getPlayer().displayClientMessage(
                        Component.translatable("item.invasion.trap.empty_cannot_place"), true);
            }
            return InteractionResult.FAIL;
        }
        BlockPos place = context.getClickedPos().relative(context.getClickedFace());
        EntityIMTrap trap = EntityRegistry.TRAP.get().create(level);
        if (trap == null) {
            return InteractionResult.FAIL;
        }
        trap.setPos(place.getX() + 0.5, place.getY() + 0.05, place.getZ() + 0.5);
        trap.setTrapType(kind == TrapKind.FLAME ? EntityIMTrap.TYPE_FLAME : EntityIMTrap.TYPE_RIFT);
        level.addFreshEntity(trap);
        Player player = context.getPlayer();
        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.invasion.trap.tooltip." + kind.name().toLowerCase()));
    }
}
