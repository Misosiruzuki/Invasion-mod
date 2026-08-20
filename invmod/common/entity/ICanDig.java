/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public interface ICanDig {
    public IPosition[] getBlockRemovalOrder(int var1, int var2, int var3);

    public float getBlockRemovalCost(int var1, int var2, int var3);

    public boolean canClearBlock(int var1, int var2, int var3);

    public void onBlockRemoved(int var1, int var2, int var3, Block var4);

    public IBlockAccess getTerrain();
}

