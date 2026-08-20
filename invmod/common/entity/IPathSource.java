/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IPathResult;
import invmod.common.entity.Path;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;

public interface IPathSource {
    public Path createPath(IPathfindable var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8, float var9, IBlockAccess var10);

    public Path createPath(EntityIMLiving var1, Entity var2, float var3, float var4, IBlockAccess var5);

    public Path createPath(EntityIMLiving var1, int var2, int var3, int var4, float var5, float var6, IBlockAccess var7);

    public void createPath(IPathResult var1, IPathfindable var2, int var3, int var4, int var5, int var6, int var7, int var8, float var9, IBlockAccess var10);

    public void createPath(IPathResult var1, EntityIMLiving var2, Entity var3, float var4, IBlockAccess var5);

    public void createPath(IPathResult var1, EntityIMLiving var2, int var3, int var4, int var5, float var6, IBlockAccess var7);

    public int getSearchDepth();

    public int getQuickFailDepth();

    public void setSearchDepth(int var1);

    public void setQuickFailDepth(int var1);

    public boolean canPathfindNice(PathPriority var1, float var2, int var3, int var4);

    public static enum PathPriority {
        LOW,
        MEDIUM,
        HIGH;

    }
}

