package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class TerrainDigger implements ITerrainDig {
    private final Level level;
    public TerrainDigger(Level level) { this.level = level; }

    @Override
    public boolean askRemoveBlock(BlockPos pos, float strength) {
        if (level.isClientSide) return false;
        float destroy = level.getBlockState(pos).getDestroySpeed(level, pos);
        if (destroy < 0 || destroy > strength) return false;
        return level.destroyBlock(pos, true);
    }
}
