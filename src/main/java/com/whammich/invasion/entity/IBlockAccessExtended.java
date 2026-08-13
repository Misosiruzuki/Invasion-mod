package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockAccessExtended extends BlockGetter {
    int getData(BlockPos pos);
    void setData(BlockPos pos, int value);
    BlockState getBlockState(BlockPos pos);
}
