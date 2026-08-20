/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common;

import net.minecraft.world.IBlockAccess;

public interface IBlockAccessExtended
extends IBlockAccess {
    public int getLayeredData(int var1, int var2, int var3);

    public void setData(int var1, int var2, int var3, Integer var4);
}

