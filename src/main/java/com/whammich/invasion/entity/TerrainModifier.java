package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainModifier implements ITerrainModify {
    private final TerrainDigger digger;
    private final TerrainBuilder builder;

    public TerrainModifier(Level level) {
        this.digger = new TerrainDigger(level);
        this.builder = new TerrainBuilder(level);
    }

    @Override public boolean askRemoveBlock(BlockPos pos, float strength) {
        return digger.askRemoveBlock(pos, strength);
    }

    @Override public boolean askPlaceBlock(BlockPos pos, BlockState state) {
        return builder.askPlaceBlock(pos, state);
    }
}
