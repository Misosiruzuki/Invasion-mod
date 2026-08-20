/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

public enum PathAction {
    NONE,
    LADDER_UP,
    BRIDGE,
    SWIM,
    DIG,
    LADDER_UP_PX,
    LADDER_UP_NX,
    LADDER_UP_PZ,
    LADDER_UP_NZ,
    LADDER_TOWER_UP_PX,
    LADDER_TOWER_UP_NX,
    LADDER_TOWER_UP_PZ,
    LADDER_TOWER_UP_NZ,
    SCAFFOLD_UP;

    public static final PathAction[] ladderTowerIndexOrient;
    public static final PathAction[] ladderIndexOrient;

    static {
        ladderTowerIndexOrient = new PathAction[]{LADDER_TOWER_UP_PX, LADDER_TOWER_UP_NX, LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ};
        ladderIndexOrient = new PathAction[]{LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ, LADDER_UP_NZ};
    }
}

