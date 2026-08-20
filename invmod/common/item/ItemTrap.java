/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IIcon
 *  net.minecraft.world.World
 */
package invmod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.entity.EntityIMTrap;
import invmod.common.item.ItemIM;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemTrap
extends ItemIM {
    @SideOnly(value=Side.CLIENT)
    private IIcon emptyIcon;
    @SideOnly(value=Side.CLIENT)
    private IIcon riftIcon;
    @SideOnly(value=Side.CLIENT)
    private IIcon flameIcon;
    public static final String[] trapNames = new String[]{"emptyTrap", "riftTrap", "flameTrap", "XYZ Trap"};

    public ItemTrap() {
        this.func_77625_d(64);
        this.func_77627_a(true);
        this.func_77656_e(0);
        this.func_77655_b("trap");
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void func_94581_a(IIconRegister par1IconRegister) {
        this.emptyIcon = par1IconRegister.func_94245_a("invmod:trapEmpty");
        this.riftIcon = par1IconRegister.func_94245_a("invmod:trapPurple");
        this.flameIcon = par1IconRegister.func_94245_a("invmod:trapRed");
    }

    public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.field_72995_K) {
            return false;
        }
        if (side == 1) {
            EntityIMTrap trap;
            if (itemstack.func_77960_j() == 1) {
                trap = new EntityIMTrap(world, (double)x + 0.5, (double)y + 1.0, (double)z + 0.5, 1);
            } else if (itemstack.func_77960_j() == 2) {
                trap = new EntityIMTrap(world, (double)x + 0.5, (double)y + 1.0, (double)z + 0.5, 2);
            } else {
                return false;
            }
            if (trap.isValidPlacement() && world.func_72872_a(EntityIMTrap.class, trap.field_70121_D).size() == 0) {
                world.func_72838_d((Entity)trap);
                if (!entityplayer.field_71075_bZ.field_75098_d) {
                    --itemstack.field_77994_a;
                }
            }
            return true;
        }
        return false;
    }

    public String func_77667_c(ItemStack itemstack) {
        if (itemstack.func_77960_j() < trapNames.length) {
            return trapNames[itemstack.func_77960_j()];
        }
        return "";
    }

    public IIcon func_77617_a(int i) {
        if (i == 1) {
            return this.riftIcon;
        }
        if (i == 2) {
            return this.flameIcon;
        }
        return this.emptyIcon;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_150895_a(Item item, CreativeTabs tab, List dest) {
        dest.add(new ItemStack(item, 1, 0));
        dest.add(new ItemStack(item, 1, 1));
        dest.add(new ItemStack(item, 1, 2));
    }
}

