package com.whammich.invasion.nexus;

import java.util.Random;

/**
 * Pure continuous-mode schedule helpers (no Minecraft types) for unit tests and Nexus BE.
 * Mirrors 1.7.10 TileEntityNexus nextAttackTime: day boundary + 14000 + days*24000.
 */
public final class ContinuousSchedule {

    /** Ticks from day start until "night looms" window (1.7 used dusk ~12000 for message). */
    public static final long DUSK_TICK = 12_000L;
    /** Preferred attack anchor within the day (1.7 used 14000). */
    public static final long NIGHT_ATTACK_ANCHOR = 14_000L;
    public static final long DAY_LENGTH = 24_000L;

    /** Continuous flux/power cadence (1.7 powerLevelTimer threshold). */
    public static final int POWER_TICK_INTERVAL = 2_200;
    /** Default continuous night wave length in seconds (1.7 doContinuous). */
    public static final int DEFAULT_WAVE_LENGTH_SECONDS = 240;

    private ContinuousSchedule() {
    }

    /**
     * Schedule the next continuous attack at night after {@code minDays}..{@code maxDays} full days.
     *
     * @param worldTime current level game time
     * @param minDays   inclusive minimum days to wait (config)
     * @param maxDays   inclusive maximum days to wait (config)
     * @param random    source for day roll
     * @return absolute world time when the attack should begin
     */
    public static long computeNextAttackTime(long worldTime, int minDays, int maxDays, Random random) {
        int min = Math.max(0, minDays);
        int max = Math.max(min, maxDays);
        int span = max - min;
        int days = min + (span == 0 ? 0 : random.nextInt(span + 1));
        long dayStart = Math.floorDiv(worldTime, DAY_LENGTH) * DAY_LENGTH;
        return dayStart + NIGHT_ATTACK_ANCHOR + (long) days * DAY_LENGTH;
    }

    /**
     * True when the clock crossed dusk this tick window (for "night looms" message).
     */
    public static boolean crossedDusk(long lastWorldTime, long currentWorldTime) {
        int lastTod = (int) Math.floorMod(lastWorldTime, DAY_LENGTH);
        long curTod = Math.floorMod(currentWorldTime, DAY_LENGTH);
        return lastTod < DUSK_TICK && curTod >= DUSK_TICK;
    }

    /**
     * Difficulty / tier used for continuous waves (1.7: 1 + powerLevel/4500).
     */
    public static float difficultyFromPower(int powerLevel) {
        return 1.0F + powerLevel / 4500.0F;
    }

    /**
     * Flux increment per power tick in continuous mode (1.7: 5 + 5*power/1550).
     */
    public static int continuousFluxIncrement(int powerLevel) {
        return 5 + (int) (5 * powerLevel / 1550.0F);
    }
}
