package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class PathfinderIM {
    private final PathCreator creator = new PathCreator();
    public Path createPath(BlockGetter level, IPathfindable agent, BlockPos from, BlockPos to) {
        return creator.createPath(from, to, agent);
    }
}
