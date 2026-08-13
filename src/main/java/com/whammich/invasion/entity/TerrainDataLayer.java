package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.Map;

public class TerrainDataLayer implements IBlockAccessExtended {
    private final BlockGetter base;
    private final Map<Long, Integer> data = new HashMap<>();

    public TerrainDataLayer(BlockGetter base) { this.base = base; }

    private static long key(BlockPos pos) {
        return BlockPos.asLong(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override public int getData(BlockPos pos) { return data.getOrDefault(key(pos), 0); }
    @Override public void setData(BlockPos pos, int value) { data.put(key(pos), value); }
    @Override public BlockState getBlockState(BlockPos pos) { return base.getBlockState(pos); }
    @Override public FluidState getFluidState(BlockPos pos) { return base.getFluidState(pos); }
    @Override public BlockEntity getBlockEntity(BlockPos pos) { return base.getBlockEntity(pos); }
    @Override public int getHeight() { return base.getHeight(); }
    @Override public int getMinBuildHeight() { return base.getMinBuildHeight(); }
}
