/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 */
package invmod.common.nexus;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput
extends Slot {
    public SlotOutput(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return false;
    }
}

