package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class PathCreator implements IPathSource {
    private final List<PathNode> options = new ArrayList<>();
    public void addOption(BlockPos pos, PathAction action) { options.add(new PathNode(pos, action)); }
    public List<PathNode> getOptions() { return options; }
    public void clear() { options.clear(); }
    @Override
    public Path createPath(BlockPos from, BlockPos to, IPathfindable agent) {
        return new Path();
    }
}
