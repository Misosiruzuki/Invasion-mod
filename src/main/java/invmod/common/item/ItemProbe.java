/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IIcon
 *  net.minecraft.world.World
 */
package invmod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.entity.EntityIMLiving;
import invmod.common.item.ItemIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemProbe
extends ItemIM {
    @SideOnly(value=Side.CLIENT)
    private IIcon iconAdjuster;
    @SideOnly(value=Side.CLIENT)
    private IIcon iconProbe;
    public static final String[] probeNames = new String[]{"nexusAdjuster", "materialProbe"};

    public ItemProbe() {
        this.func_77627_a(true);
        this.func_77656_e(0);
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void func_94581_a(IIconRegister par1IconRegister) {
        this.iconAdjuster = par1IconRegister.func_94245_a("invmod:adjuster");
        this.iconProbe = par1IconRegister.func_94245_a("invmod:probe");
    }

    public boolean func_77662_d() {
        return true;
    }

    public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.field_72995_K) {
            return false;
        }
        Block block = world.func_147439_a(x, y, z);
        EntityPlayerMP player = (EntityPlayerMP)entityplayer;
        if (block == mod_Invasion.blockNexus) {
            TileEntityNexus nexus = (TileEntityNexus)world.func_147438_o(x, y, z);
            int newRange = nexus.getSpawnRadius();
            if (entityplayer.func_70093_af()) {
                if ((newRange -= 8) < 32) {
                    newRange = 128;
                }
            } else if ((newRange += 8) > 128) {
                newRange = 32;
            }
            nexus.setSpawnRadius(newRange);
            mod_Invasion.sendMessageToPlayer(player, "Nexus range changed to: " + nexus.getSpawnRadius());
            return true;
        }
        if (itemstack.func_77960_j() == 1) {
            float blockStrength = EntityIMLiving.getBlockStrength(x, y, z, block, world);
            mod_Invasion.sendMessageToPlayer(player, "Block strength: " + (double)((int)(((double)blockStrength + 0.005) * 100.0)) / 100.0);
            return true;
        }
        return false;
    }

    public String func_77667_c(ItemStack itemstack) {
        if (itemstack.func_77960_j() < probeNames.length) {
            return probeNames[itemstack.func_77960_j()];
        }
        return "";
    }

    public IIcon func_77617_a(int i) {
        if (i == 1) {
            return this.iconProbe;
        }
        return this.iconAdjuster;
    }

    public int func_77619_b() {
        return 14;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_150895_a(Item item, CreativeTabs tab, List dest) {
        dest.add(new ItemStack(item, 1, 0));
        dest.add(new ItemStack(item, 1, 1));
    }
}

