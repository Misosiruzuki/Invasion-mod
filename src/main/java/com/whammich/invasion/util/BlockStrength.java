package com.whammich.invasion.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

/**
 * Invasion dig-resistance values shown by Material Probe (1.7 EntityIMLiving table subset).
 */
public final class BlockStrength {

    private static final Map<Block, Float> TABLE = new HashMap<>();
    private static final float DEFAULT = 2.5f;

    static {
        put(Blocks.AIR, 0.0f);
        put(Blocks.GRASS_BLOCK, 0.3f);
        put(Blocks.DIRT, 3.13f);
        put(Blocks.COARSE_DIRT, 3.13f);
        put(Blocks.SAND, 2.5f);
        put(Blocks.RED_SAND, 2.5f);
        put(Blocks.GRAVEL, 2.5f);
        put(Blocks.OAK_LEAVES, 1.25f);
        put(Blocks.BIRCH_LEAVES, 1.25f);
        put(Blocks.SPRUCE_LEAVES, 1.25f);
        put(Blocks.JUNGLE_LEAVES, 1.25f);
        put(Blocks.ACACIA_LEAVES, 1.25f);
        put(Blocks.DARK_OAK_LEAVES, 1.25f);
        put(Blocks.STONE, 5.0f);
        put(Blocks.COBBLESTONE, 5.0f);
        put(Blocks.MOSSY_COBBLESTONE, 5.0f);
        put(Blocks.STONE_BRICKS, 5.0f);
        put(Blocks.OAK_LOG, 3.2f);
        put(Blocks.OAK_PLANKS, 3.2f);
        put(Blocks.GLASS, 2.0f);
        put(Blocks.OBSIDIAN, 50.0f);
        put(Blocks.BEDROCK, 9999.0f);
    }

    private BlockStrength() {}

    private static void put(Block block, float v) {
        TABLE.put(block, v);
    }

    public static float get(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        Float base = TABLE.get(block);
        if (base == null) {
            // hardness-based fallback
            float h = state.getDestroySpeed(level, pos);
            if (h < 0) {
                return 9999.0f;
            }
            if (h == 0) {
                return 0.3f;
            }
            return Math.max(DEFAULT, h * 1.5f);
        }
        // stone-like adjacency bonus (1.7 CONSTRUCTION_STONE)
        if (block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.STONE_BRICKS
                || block == Blocks.MOSSY_COBBLESTONE) {
            int bonus = 0;
            for (BlockPos n : new BlockPos[]{
                    pos.below(), pos.above(), pos.north(), pos.south(), pos.east(), pos.west()}) {
                Block nb = level.getBlockState(n).getBlock();
                if (nb == Blocks.STONE || nb == Blocks.COBBLESTONE || nb == Blocks.STONE_BRICKS
                        || nb == Blocks.MOSSY_COBBLESTONE) {
                    bonus++;
                }
            }
            return base * (1.0f + bonus * 0.1f);
        }
        return base;
    }
}
