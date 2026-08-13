package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainBuilder implements ITerrainBuild {
    private final Level level;
    public TerrainBuilder(Level level) { this.level = level; }

    @Override
    public boolean askPlaceBlock(BlockPos pos, BlockState state) {
        if (level.isClientSide || !level.getBlockState(pos).isAir()) return false;
        return level.setBlock(pos, state, 3);
    }
}
