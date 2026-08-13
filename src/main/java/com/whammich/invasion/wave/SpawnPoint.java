package com.whammich.invasion.wave;

import net.minecraft.core.BlockPos;

public class SpawnPoint {
    private final BlockPos pos;
    private final int angle;
    private final SpawnType type;

    public SpawnPoint(BlockPos pos, int angle, SpawnType type) {
        this.pos = pos;
        this.angle = angle;
        this.type = type;
    }

    public BlockPos getPos() { return pos; }
    public int getAngle() { return angle; }
    public SpawnType getType() { return type; }
}
