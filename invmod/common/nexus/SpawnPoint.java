/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.nexus;

import invmod.common.nexus.SpawnType;
import invmod.common.util.IPolarAngle;
import invmod.common.util.IPosition;

public class SpawnPoint
implements IPosition,
IPolarAngle,
Comparable<IPolarAngle> {
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private int spawnAngle;
    private SpawnType spawnType;

    public SpawnPoint(int x, int y, int z, int angle, SpawnType type) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.spawnAngle = angle;
        this.spawnType = type;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public int getYCoord() {
        return this.yCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
    }

    @Override
    public int getAngle() {
        return this.spawnAngle;
    }

    public SpawnType getType() {
        return this.spawnType;
    }

    @Override
    public int compareTo(IPolarAngle polarAngle) {
        if (this.spawnAngle < polarAngle.getAngle()) {
            return -1;
        }
        if (this.spawnAngle > polarAngle.getAngle()) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        return "Spawn#" + (Object)((Object)this.spawnType) + "#" + this.xCoord + "," + this.yCoord + "," + this.zCoord + "#" + this.spawnAngle;
    }
}

