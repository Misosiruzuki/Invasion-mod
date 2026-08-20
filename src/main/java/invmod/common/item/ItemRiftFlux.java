/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package invmod.common.item;

import invmod.common.item.ItemIM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRiftFlux
extends ItemIM {
    public ItemRiftFlux() {
        this.func_77656_e(0);
        this.func_77655_b("riftFlux");
        this.func_77625_d(64);
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }
}

