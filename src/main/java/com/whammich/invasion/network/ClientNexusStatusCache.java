package com.whammich.invasion.network;

import net.minecraft.core.BlockPos;

/**
 * Client-only last-known nexus status from {@link NexusStatusPacket}.
 * Kept in network package so server never loads it beyond the packet handler path.
 */
public final class ClientNexusStatusCache {
    private static BlockPos pos;
    private static int mode;
    private static int wave;
    private static int hp;
    private static int maxHp;

    private ClientNexusStatusCache() {}

    public static void update(BlockPos p, int m, int w, int h, int mh) {
        pos = p;
        mode = m;
        wave = w;
        hp = h;
        maxHp = mh;
    }

    public static BlockPos getPos() {
        return pos;
    }

    public static int getMode() {
        return mode;
    }

    public static int getWave() {
        return wave;
    }

    public static int getHp() {
        return hp;
    }

    public static int getMaxHp() {
        return maxHp;
    }
}
