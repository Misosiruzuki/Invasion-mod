package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;

public class PathNode {
    private final BlockPos pos;
    private final PathAction action;
    private float cost;
    private float heuristic;
    private PathNode parent;

    public PathNode(BlockPos pos, PathAction action) {
        this.pos = pos;
        this.action = action;
    }

    public BlockPos getPos() { return pos; }
    public PathAction getAction() { return action; }
    public float getCost() { return cost; }
    public void setCost(float cost) { this.cost = cost; }
    public float getHeuristic() { return heuristic; }
    public void setHeuristic(float heuristic) { this.heuristic = heuristic; }
    public PathNode getParent() { return parent; }
    public void setParent(PathNode parent) { this.parent = parent; }
    public float total() { return cost + heuristic; }
}
