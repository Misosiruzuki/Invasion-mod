package com.whammich.invasion.nexus;

/**
 * Nexus runtime modes - values match 1.7.10 TileEntityNexus.
 */
public final class NexusMode {
    public static final int IDLE = 0;
    /** Invasion mode: continuous waves until nexus falls. */
    public static final int INVASION = 1;
    /** Continuous mode: stable, waiting for scheduled night attacks. */
    public static final int CONTINUOUS = 2;
    /** Continuous mode while a night attack wave is in progress. */
    public static final int CONTINUOUS_ATTACK = 3;
    /** Activation gauge filling with a stable catalyst. */
    public static final int ACTIVATING_STABLE = 4;

    private NexusMode() {
    }

    public static boolean isRunning(int mode) {
        return mode == INVASION || mode == CONTINUOUS || mode == CONTINUOUS_ATTACK;
    }

    public static boolean isContinuousFamily(int mode) {
        return mode == CONTINUOUS || mode == CONTINUOUS_ATTACK || mode == ACTIVATING_STABLE;
    }
}
