package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;

public interface IPathSource {
    Path createPath(BlockPos from, BlockPos to, IPathfindable agent);
}
