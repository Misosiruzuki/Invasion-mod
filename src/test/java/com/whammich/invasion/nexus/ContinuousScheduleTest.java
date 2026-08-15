package com.whammich.invasion.nexus;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousScheduleTest {

    @Test
    void nextAttackTimeUsesNightAnchorAndDays() {
        long worldTime = 1000L; // day 0
        Random fixed = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0; // always min days
            }
        };
        long next = ContinuousSchedule.computeNextAttackTime(worldTime, 2, 3, fixed);
        assertEquals(14_000L + 2L * 24_000L, next);
    }

    @Test
    void nextAttackTimeRespectsMaxDays() {
        long worldTime = 25_000L; // day 1
        Random fixed = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1; // max
            }
        };
        long next = ContinuousSchedule.computeNextAttackTime(worldTime, 2, 4, fixed);
        long dayStart = 24_000L;
        assertEquals(dayStart + 14_000L + 4L * 24_000L, next);
    }

    @Test
    void crossedDuskDetectsTransition() {
        assertTrue(ContinuousSchedule.crossedDusk(11_999L, 12_000L));
        assertFalse(ContinuousSchedule.crossedDusk(12_000L, 12_100L));
        assertFalse(ContinuousSchedule.crossedDusk(5_000L, 6_000L));
    }

    @Test
    void difficultyAndFluxScaleWithPower() {
        assertEquals(1.0F, ContinuousSchedule.difficultyFromPower(0), 0.0001F);
        assertTrue(ContinuousSchedule.difficultyFromPower(4500) > 1.9F);
        assertEquals(5, ContinuousSchedule.continuousFluxIncrement(0));
        assertTrue(ContinuousSchedule.continuousFluxIncrement(1550) >= 10);
    }

    @Test
    void modeConstantsMatchLegacy() {
        assertEquals(0, NexusMode.IDLE);
        assertEquals(1, NexusMode.INVASION);
        assertEquals(2, NexusMode.CONTINUOUS);
        assertEquals(3, NexusMode.CONTINUOUS_ATTACK);
        assertEquals(4, NexusMode.ACTIVATING_STABLE);
        assertTrue(NexusMode.isRunning(NexusMode.INVASION));
        assertTrue(NexusMode.isRunning(NexusMode.CONTINUOUS));
        assertFalse(NexusMode.isRunning(NexusMode.IDLE));
    }
}
