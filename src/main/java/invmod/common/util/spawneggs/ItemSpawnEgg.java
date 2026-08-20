/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityList
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.Facing
 *  net.minecraft.util.IIcon
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.StatCollector
 *  net.minecraft.world.World
 */
package invmod.common.util.spawneggs;

import invmod.common.mod_Invasion;
import invmod.common.util.spawneggs.SpawnEggInfo;
import invmod.common.util.spawneggs.SpawnEggRegistry;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemSpawnEgg
extends Item {
    private IIcon overlay;

    public ItemSpawnEgg() {
        this.func_77627_a(true);
        this.func_77637_a(mod_Invasion.tabInvmod);
        this.func_77655_b("monsterPlacer");
    }

    public String func_77653_i(ItemStack stack) {
        String name = ("" + StatCollector.func_74838_a((String)(this.func_77658_a() + ".name"))).trim();
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short)stack.func_77960_j());
        if (info == null) {
            return name;
        }
        String mobID = info.mobID;
        String displayName = info.displayName;
        if (stack.func_77942_o()) {
            NBTTagCompound compound = stack.func_77978_p();
            if (compound.func_74764_b("mobID")) {
                mobID = compound.func_74779_i("mobID");
            }
            if (compound.func_74764_b("displayName")) {
                displayName = compound.func_74779_i("displayName");
            }
        }
        name = displayName == null ? name + ' ' + ItemSpawnEgg.attemptToTranslate("entity." + mobID + ".name", mobID) : name + ' ' + ItemSpawnEgg.attemptToTranslate("eggdisplay." + displayName, displayName);
        return name;
    }

    public int func_82790_a(ItemStack stack, int par2) {
        int color;
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short)stack.func_77960_j());
        if (info == null) {
            return 0xFFFFFF;
        }
        int n = color = par2 == 0 ? info.primaryColor : info.secondaryColor;
        if (stack.func_77942_o()) {
            NBTTagCompound compound = stack.func_77978_p();
            if (par2 == 0 && compound.func_74764_b("primaryColor")) {
                color = compound.func_74762_e("primaryColor");
            }
            if (par2 != 0 && compound.func_74764_b("secondaryColor")) {
                color = compound.func_74762_e("secondaryColor");
            }
        }
        return color;
    }

    public boolean func_77648_a(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        Entity entity;
        if (world.field_72995_K) {
            return true;
        }
        Block block = world.func_147439_a(x, y, z);
        x += Facing.field_71586_b[par7];
        y += Facing.field_71587_c[par7];
        z += Facing.field_71585_d[par7];
        double d0 = 0.0;
        if (par7 == 1 && block != null && block.func_149645_b() == 11) {
            d0 = 0.5;
        }
        if ((entity = ItemSpawnEgg.spawnCreature(world, stack, (double)x + 0.5, (double)y + d0, (double)z + 0.5)) != null) {
            if (entity instanceof EntityLiving && stack.func_82837_s()) {
                ((EntityLiving)entity).func_94058_c(stack.func_82833_r());
            }
            if (!player.field_71075_bZ.field_75098_d) {
                --stack.field_77994_a;
            }
        }
        return true;
    }

    public ItemStack func_77659_a(ItemStack stack, World world, EntityPlayer player) {
        if (world.field_72995_K) {
            return stack;
        }
        MovingObjectPosition trace = this.func_77621_a(world, player, true);
        if (trace == null) {
            return stack;
        }
        if (trace.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
            Entity entity;
            int x = trace.field_72311_b;
            int y = trace.field_72312_c;
            int z = trace.field_72309_d;
            if (!world.func_72962_a(player, x, y, z) || !player.func_82247_a(x, y, z, trace.field_72310_e, stack)) {
                return stack;
            }
            if (world.func_147439_a(x, y, z) instanceof BlockLiquid && (entity = ItemSpawnEgg.spawnCreature(world, stack, x, y, z)) != null) {
                if (entity instanceof EntityLiving && stack.func_82837_s()) {
                    ((EntityLiving)entity).func_94058_c(stack.func_82833_r());
                }
                if (!player.field_71075_bZ.field_75098_d) {
                    --stack.field_77994_a;
                }
            }
        }
        return stack;
    }

    public static Entity spawnCreature(World world, ItemStack stack, double x, double y, double z) {
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short)stack.func_77960_j());
        if (info == null) {
            return null;
        }
        String mobID = info.mobID;
        NBTTagCompound spawnData = info.spawnData;
        if (stack.func_77942_o()) {
            NBTTagCompound compound = stack.func_77978_p();
            if (compound.func_74764_b("mobID")) {
                mobID = compound.func_74779_i("mobID");
            }
            if (compound.func_74764_b("spawnData")) {
                spawnData = compound.func_74775_l("spawnData");
            }
        }
        Entity entity = null;
        entity = EntityList.func_75620_a((String)mobID, (World)world);
        if (entity != null && entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)entity;
            entity.func_70012_b(x, y, z, MathHelper.func_76142_g((float)(world.field_73012_v.nextFloat() * 360.0f)), 0.0f);
            entityliving.field_70759_as = entityliving.field_70177_z;
            entityliving.field_70761_aq = entityliving.field_70177_z;
            entityliving.func_110161_a(null);
            if (!spawnData.func_82582_d()) {
                ItemSpawnEgg.addNBTData(entity, spawnData);
            }
            world.func_72838_d(entity);
            entityliving.func_70642_aH();
            ItemSpawnEgg.spawnRiddenCreatures(entity, world, spawnData);
        }
        return entity;
    }

    private static void spawnRiddenCreatures(Entity entity, World world, NBTTagCompound cur) {
        while (cur.func_74764_b("Riding")) {
            Entity newEntity = EntityList.func_75620_a((String)(cur = cur.func_74775_l("Riding")).func_74779_i("id"), (World)world);
            if (newEntity != null) {
                ItemSpawnEgg.addNBTData(newEntity, cur);
                newEntity.func_70012_b(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, entity.field_70177_z, entity.field_70125_A);
                world.func_72838_d(newEntity);
                entity.func_70078_a(newEntity);
            }
            entity = newEntity;
        }
    }

    private static void addNBTData(Entity entity, NBTTagCompound spawnData) {
        NBTTagCompound newTag = new NBTTagCompound();
        entity.func_70039_c(newTag);
        for (String name : spawnData.func_150296_c()) {
            newTag.func_74782_a(name, spawnData.func_74781_a(name).func_74737_b());
        }
        entity.func_70020_e(newTag);
    }

    public boolean func_77623_v() {
        return true;
    }

    public IIcon func_77618_c(int par1, int par2) {
        return par2 > 0 ? this.overlay : super.func_77618_c(par1, par2);
    }

    public void func_150895_a(Item item, CreativeTabs par2CreativeTabs, List list) {
        for (SpawnEggInfo info : SpawnEggRegistry.getEggInfoList()) {
            list.add(new ItemStack(item, 1, (int)info.eggID));
        }
    }

    public void func_94581_a(IIconRegister iconRegister) {
        this.field_77791_bV = iconRegister.func_94245_a("spawn_egg");
        this.overlay = iconRegister.func_94245_a("spawn_egg_overlay");
    }

    public static String attemptToTranslate(String key, String _default) {
        String result = StatCollector.func_74838_a((String)key);
        return result.equals(key) ? _default : result;
    }
}

