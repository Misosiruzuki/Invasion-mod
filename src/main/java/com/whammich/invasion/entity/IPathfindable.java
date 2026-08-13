package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface IPathfindable {
    float getBlockPathCost(BlockGetter level, BlockPos pos, PathAction action);
    void getPathOptionsFromNode(BlockGetter level, PathNode node, PathCreator creator);
}
