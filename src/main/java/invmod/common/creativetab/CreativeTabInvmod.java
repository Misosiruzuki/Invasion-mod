/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.block.Block
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.Item
 */
package invmod.common.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabInvmod
extends CreativeTabs {
    public CreativeTabInvmod() {
        super("invasionTab");
    }

    @SideOnly(value=Side.CLIENT)
    public Item func_78016_d() {
        return Item.func_150898_a((Block)mod_Invasion.blockNexus);
    }
}

