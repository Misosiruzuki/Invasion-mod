/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IPathResult;
import invmod.common.entity.IPathSource;
import invmod.common.entity.Path;
import invmod.common.entity.PathfinderIM;
import invmod.common.util.CoordsInt;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class PathCreator
implements IPathSource {
    private int searchDepth;
    private int quickFailDepth;
    private int[] nanosUsed;
    private int index;

    public PathCreator() {
        this(200, 50);
    }

    public PathCreator(int searchDepth, int quickFailDepth) {
        this.searchDepth = searchDepth;
        this.quickFailDepth = quickFailDepth;
        this.nanosUsed = new int[6];
        this.index = 0;
    }

    @Override
    public int getSearchDepth() {
        return this.searchDepth;
    }

    @Override
    public int getQuickFailDepth() {
        return this.quickFailDepth;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.searchDepth = depth;
    }

    @Override
    public void setQuickFailDepth(int depth) {
        this.quickFailDepth = depth;
    }

    @Override
    public Path createPath(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
        int elapsed;
        long time = System.nanoTime();
        Path path = PathfinderIM.createPath(entity, x, y, z, x2, y2, z2, targetRadius, maxSearchRange, terrainMap, this.searchDepth, this.quickFailDepth);
        this.nanosUsed[this.index] = elapsed = (int)(System.nanoTime() - time);
        if (++this.index >= this.nanosUsed.length) {
            this.index = 0;
        }
        return path;
    }

    @Override
    public Path createPath(EntityIMLiving entity, Entity target, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
        return this.createPath(entity, MathHelper.func_76128_c((double)(target.field_70165_t + 0.5 - (double)(entity.field_70130_N / 2.0f))), MathHelper.func_76128_c((double)target.field_70163_u), MathHelper.func_76128_c((double)(target.field_70161_v + 0.5 - (double)(entity.field_70130_N / 2.0f))), targetRadius, maxSearchRange, terrainMap);
    }

    @Override
    public Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
        int startZ;
        int startY;
        int startX;
        CoordsInt size = entity.getCollideSize();
        if (size.getXCoord() <= 1 && size.getZCoord() <= 1) {
            startX = entity.getXCoord();
            startY = MathHelper.func_76128_c((double)entity.field_70121_D.field_72338_b);
            startZ = entity.getZCoord();
        } else {
            startX = MathHelper.func_76128_c((double)entity.field_70121_D.field_72340_a);
            startY = MathHelper.func_76128_c((double)entity.field_70121_D.field_72338_b);
            startZ = MathHelper.func_76128_c((double)entity.field_70121_D.field_72339_c);
        }
        return this.createPath(entity, startX, startY, startZ, MathHelper.func_76128_c((double)((float)x + 0.5f - entity.field_70130_N / 2.0f)), y, MathHelper.func_76128_c((double)((float)z + 0.5f - entity.field_70130_N / 2.0f)), targetRadius, maxSearchRange, terrainMap);
    }

    @Override
    public void createPath(IPathResult observer, IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float maxSearchRange, IBlockAccess terrainMap) {
    }

    @Override
    public void createPath(IPathResult observer, EntityIMLiving entity, Entity target, float maxSearchRange, IBlockAccess terrainMap) {
    }

    @Override
    public void createPath(IPathResult observer, EntityIMLiving entity, int x, int y, int z, float maxSearchRange, IBlockAccess terrainMap) {
    }

    @Override
    public boolean canPathfindNice(IPathSource.PathPriority priority, float maxSearchRange, int searchDepth, int quickFailDepth) {
        return true;
    }
}

