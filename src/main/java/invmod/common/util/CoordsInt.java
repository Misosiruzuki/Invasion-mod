/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.util.IPosition;

public class CoordsInt
implements IPosition {
    public static final int[] offsetAdjX = new int[]{1, -1, 0, 0};
    public static final int[] offsetAdjZ = new int[]{0, 0, 1, -1};
    public static final int[] offsetAdj2X = new int[]{2, 2, -1, -1, 1, 0, 0, 1};
    public static final int[] offsetAdj2Z = new int[]{0, 1, 1, 0, 2, 2, -1, -1};
    public static final int[] offsetRing1X = new int[]{1, 0, -1, -1, -1, 0, 1, 1};
    public static final int[] offsetRing1Z = new int[]{1, 1, 1, 0, -1, -1, -1, 0};
    private int x;
    private int y;
    private int z;

    public CoordsInt(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getXCoord() {
        return this.x;
    }

    @Override
    public int getYCoord() {
        return this.y;
    }

    @Override
    public int getZCoord() {
        return this.z;
    }
}

