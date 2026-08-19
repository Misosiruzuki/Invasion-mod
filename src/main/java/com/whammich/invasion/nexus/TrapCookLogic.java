package com.whammich.invasion.nexus;

/**
 * Pure cook-step rules for empty trap → rift trap (1.7 TileEntityNexus updateStatus).
 */
public final class TrapCookLogic {
    public static final int COOK_MAX = 1200;

    private TrapCookLogic() {}

    /** Idle / activating: +1 per tick. Active invasion or continuous: +9 per tick. */
    public static int cookStep(int mode) {
        // NexusMode: IDLE=0, INVASION=1, CONTINUOUS=2, CONTINUOUS_ATTACK=3, ACTIVATING_STABLE=4
        if (mode == 0 || mode == 4) {
            return 1;
        }
        return 9;
    }

    public static int advance(int cookTime, int mode) {
        return cookTime + cookStep(mode);
    }

    public static boolean isComplete(int cookTime) {
        return cookTime >= COOK_MAX;
    }
}
