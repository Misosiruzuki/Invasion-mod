package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ModifyBlockEntry {
    private final BlockPos pos;
    private final BlockState newState;
    private final float cost;

    public ModifyBlockEntry(BlockPos pos, BlockState newState, float cost) {
        this.pos = pos;
        this.newState = newState;
        this.cost = cost;
    }

    public BlockPos getPos() { return pos; }
    public BlockState getNewState() { return newState; }
    public float getCost() { return cost; }
}
