/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.Item$ToolMaterial
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.world.World
 */
package invmod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemInfusedSword
extends ItemSword {
    public ItemInfusedSword() {
        super(Item.ToolMaterial.EMERALD);
        this.func_77656_e(40);
        this.func_77637_a(mod_Invasion.tabInvmod);
        this.func_77655_b("infusedSword");
        this.func_77625_d(1);
    }

    @SideOnly(value=Side.CLIENT)
    public void func_94581_a(IIconRegister par1IconRegister) {
        this.field_77791_bV = par1IconRegister.func_94245_a("invmod:" + this.func_77658_a().substring(5));
    }

    public boolean func_77645_m() {
        return false;
    }

    public boolean func_77644_a(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        if (this.isDamaged(itemstack)) {
            this.setDamage(itemstack, this.getDamage(itemstack) - 1);
        }
        return true;
    }

    public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
        if (par2Block == Blocks.field_150321_G) {
            return 15.0f;
        }
        Material material = par2Block.func_149688_o();
        return material != Material.field_151585_k && material != Material.field_151582_l && material != Material.field_151589_v && material != Material.field_151584_j && material != Material.field_151583_m && material != Material.field_151570_A ? 1.0f : 1.5f;
    }

    public EnumAction func_77661_b(ItemStack par1ItemStack) {
        return EnumAction.none;
    }

    public int func_77626_a(ItemStack par1ItemStack) {
        return 0;
    }

    public ItemStack func_77659_a(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (itemstack.func_77960_j() == 0) {
            if (entityplayer.func_70093_af()) {
                entityplayer.func_71024_bL().func_75122_a(6, 0.5f);
                world.func_72956_a((Entity)entityplayer, "random.burp", 0.5f, world.field_73012_v.nextFloat() * 0.1f + 0.9f);
            } else {
                entityplayer.func_70691_i(6.0f);
                world.func_72869_a("heart", entityplayer.field_70165_t + 1.5, entityplayer.field_70163_u, entityplayer.field_70161_v, 0.0, 0.0, 0.0);
                world.func_72869_a("heart", entityplayer.field_70165_t - 1.5, entityplayer.field_70163_u, entityplayer.field_70161_v, 0.0, 0.0, 0.0);
                world.func_72869_a("heart", entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v + 1.5, 0.0, 0.0, 0.0);
                world.func_72869_a("heart", entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v - 1.5, 0.0, 0.0, 0.0);
            }
            itemstack.func_77964_b(this.func_77612_l());
        }
        return itemstack;
    }

    public boolean func_150897_b(Block block) {
        return block == Blocks.field_150321_G;
    }

    public boolean func_150894_a(ItemStack par1ItemStack, World par2World, Block block, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
        return true;
    }
}

