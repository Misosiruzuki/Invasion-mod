package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;

public interface ICanDig {
    boolean canDigBlock(BlockPos pos);
    void onBlockDigged(BlockPos pos);
}
