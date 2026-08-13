package com.whammich.invasion.nexus;

import com.whammich.invasion.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Nexus container — input / output + player inventory; syncs status via ContainerData.
 */
public class NexusMenu extends AbstractContainerMenu {

    private final NexusBlockEntity nexus;
    private final ContainerData data;

    public NexusMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, getNexus(playerInv, buf), new SimpleContainerData(NexusBlockEntity.DATA_COUNT));
    }

    public NexusMenu(int id, Inventory playerInv, NexusBlockEntity nexus, ContainerData data) {
        super(MenuRegistry.NEXUS.get(), id);
        this.nexus = nexus;
        this.data = data;
        checkContainerSize(nexus, NexusBlockEntity.SLOT_COUNT);

        addSlot(new Slot(nexus, NexusBlockEntity.SLOT_INPUT, 56, 35));
        addSlot(new SlotOutput(nexus, NexusBlockEntity.SLOT_OUTPUT, 116, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    private static NexusBlockEntity getNexus(Inventory inv, FriendlyByteBuf buf) {
        BlockEntity be = inv.player.level().getBlockEntity(buf.readBlockPos());
        if (be instanceof NexusBlockEntity nexus) {
            return nexus;
        }
        throw new IllegalStateException("Nexus block entity missing at menu open");
    }

    public NexusBlockEntity getNexus() {
        return nexus;
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < NexusBlockEntity.SLOT_COUNT) {
                if (!moveItemStackTo(stack, NexusBlockEntity.SLOT_COUNT, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return nexus.stillValid(player);
    }
}
