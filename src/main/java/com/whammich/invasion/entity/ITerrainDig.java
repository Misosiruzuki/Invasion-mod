package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;

public interface ITerrainDig {
    boolean askRemoveBlock(BlockPos pos, float strength);
}
