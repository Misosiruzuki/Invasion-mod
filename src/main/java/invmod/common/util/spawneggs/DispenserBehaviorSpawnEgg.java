/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockDispenser
 *  net.minecraft.dispenser.BehaviorDefaultDispenseItem
 *  net.minecraft.dispenser.IBlockSource
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumFacing
 */
package invmod.common.util.spawneggs;

import invmod.common.util.spawneggs.ItemSpawnEgg;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class DispenserBehaviorSpawnEgg
extends BehaviorDefaultDispenseItem {
    public ItemStack func_82487_b(IBlockSource blockSource, ItemStack stack) {
        EnumFacing enumfacing = BlockDispenser.func_149937_b((int)blockSource.func_82620_h());
        double x = blockSource.func_82615_a() + (double)enumfacing.func_82601_c();
        double y = (float)blockSource.func_82622_e() + 0.2f;
        double z = blockSource.func_82616_c() + (double)enumfacing.func_82599_e();
        Entity entity = ItemSpawnEgg.spawnCreature(blockSource.func_82618_k(), stack, x, y, z);
        if (entity instanceof EntityLiving && stack.func_82837_s()) {
            ((EntityLiving)entity).func_94058_c(stack.func_82833_r());
        }
        stack.func_77979_a(1);
        return stack;
    }
}

