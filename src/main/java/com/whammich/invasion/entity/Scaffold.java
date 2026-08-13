package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;

public class Scaffold {
    private final BlockPos pos;
    public Scaffold(BlockPos pos) { this.pos = pos; }
    public BlockPos getPos() { return pos; }
}
