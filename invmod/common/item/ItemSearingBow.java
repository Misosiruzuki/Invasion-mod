/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.eventhandler.Event
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IIcon
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.player.ArrowLooseEvent
 *  net.minecraftforge.event.entity.player.ArrowNockEvent
 */
package invmod.common.item;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemSearingBow
extends ItemBow {
    @SideOnly(value=Side.CLIENT)
    private IIcon iconCharge1;
    @SideOnly(value=Side.CLIENT)
    private IIcon iconCharge2;
    @SideOnly(value=Side.CLIENT)
    private IIcon iconCharge3;
    public static final String[] bowPullIconNameArray = new String[]{"sbowc1", "sbowc2", "sbowc3"};
    @SideOnly(value=Side.CLIENT)
    private IIcon[] iconArray;

    public ItemSearingBow() {
        this.func_77655_b("searingBow");
        this.func_77637_a(mod_Invasion.tabInvmod);
    }

    public void func_77615_a(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
        boolean var5;
        int var6 = this.func_77626_a(par1ItemStack) - par4;
        ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            return;
        }
        var6 = event.charge;
        boolean bl = var5 = par3EntityPlayer.field_71075_bZ.field_75098_d || EnchantmentHelper.func_77506_a((int)Enchantment.field_77342_w.field_77352_x, (ItemStack)par1ItemStack) > 0;
        if (var5 || par3EntityPlayer.field_71071_by.func_146028_b(Items.field_151032_g)) {
            float f = (float)var6 / 20.0f;
            f = (f * f + f * 2.0f) / 3.0f;
            boolean special = false;
            if ((double)f < 0.1) {
                return;
            }
            if (f >= 3.8f) {
                special = true;
                f = 1.0f;
            } else if (f > 1.0f) {
                f = 1.0f;
            }
            if (!special) {
                int var10;
                int var9;
                EntityArrow var8 = new EntityArrow(par2World, (EntityLivingBase)par3EntityPlayer, f * 2.0f);
                if (f == 1.0f) {
                    var8.func_70243_d(true);
                }
                if ((var9 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77345_t.field_77352_x, (ItemStack)par1ItemStack)) > 0) {
                    var8.func_70239_b(var8.func_70242_d() + (double)var9 * 0.5 + 0.5);
                }
                if ((var10 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77344_u.field_77352_x, (ItemStack)par1ItemStack)) > 0) {
                    var8.func_70240_a(var10);
                }
                if (EnchantmentHelper.func_77506_a((int)Enchantment.field_77343_v.field_77352_x, (ItemStack)par1ItemStack) > 0) {
                    var8.func_70015_d(100);
                }
                if (var5) {
                    var8.field_70251_a = 2;
                } else {
                    par3EntityPlayer.field_71071_by.func_146026_a(Items.field_151032_g);
                }
                if (!par2World.field_72995_K) {
                    par2World.func_72838_d((Entity)var8);
                }
            } else {
                EntityArrow var8 = new EntityArrow(par2World, (EntityLivingBase)par3EntityPlayer, f * 2.0f);
                var8.func_70015_d(100);
                var8.func_70239_b((var8.func_70242_d() + 0.5 + 0.5) * 3.0 / 2.0 + 1.0);
                if (!par2World.field_72995_K) {
                    par2World.func_72838_d((Entity)var8);
                }
            }
            par1ItemStack.func_77972_a(1, (EntityLivingBase)par3EntityPlayer);
            par2World.func_72956_a((Entity)par3EntityPlayer, "random.bow", 1.0f, 1.0f / (Item.field_77697_d.nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        }
    }

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    public int func_77626_a(ItemStack itemstack) {
        return 72000;
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.bow;
    }

    public ItemStack func_77659_a(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ArrowNockEvent event = new ArrowNockEvent(entityPlayer, itemStack);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            return event.result;
        }
        if (entityPlayer.field_71075_bZ.field_75098_d || entityPlayer.field_71071_by.func_146028_b(Items.field_151032_g)) {
            entityPlayer.func_71008_a(itemStack, this.func_77626_a(itemStack));
        }
        return itemStack;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_94581_a(IIconRegister iconRegister) {
        this.field_77791_bV = iconRegister.func_94245_a("invmod:" + this.func_77658_a().substring(5));
        this.iconArray = new IIcon[bowPullIconNameArray.length + 1];
        this.iconArray[0] = iconRegister.func_94245_a("invmod:" + this.func_77658_a().substring(5));
        for (int i = 1; i < this.iconArray.length; ++i) {
            this.iconArray[i] = iconRegister.func_94245_a("invmod:" + bowPullIconNameArray[i - 1]);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (player.func_71011_bu() == null) {
            return this.field_77791_bV;
        }
        int Pulling = stack.func_77988_m() - useRemaining;
        float f = (float)Pulling / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) >= 3.8f) {
            return this.iconArray[3];
        }
        if (Pulling > 17) {
            return this.iconArray[2];
        }
        if (Pulling > 0) {
            return this.iconArray[1];
        }
        return this.iconArray[0];
    }
}

