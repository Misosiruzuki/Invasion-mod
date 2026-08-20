/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common;

import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import net.minecraft.world.IBlockAccess;

public interface IPathfindable {
    public float getBlockPathCost(PathNode var1, PathNode var2, IBlockAccess var3);

    public void getPathOptionsFromNode(IBlockAccess var1, PathNode var2, PathfinderIM var3);
}

