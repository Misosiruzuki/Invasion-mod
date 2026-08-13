package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface ITerrainBuild {
    boolean askPlaceBlock(BlockPos pos, BlockState state);
}
