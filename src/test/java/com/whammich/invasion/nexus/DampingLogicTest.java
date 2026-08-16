package com.whammich.invasion.nexus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** P1 / B-23..B-25 pure damping rules. */
class DampingLogicTest {

    @Test
    void weakDampingBlocksPowerGain() {
        assertEquals(10, DampingLogic.applyPowerTickGain(10, true));
        assertEquals(11, DampingLogic.applyPowerTickGain(10, false));
    }

    @Test
    void strongDampingDrainsWhenNotAttacking() {
        assertEquals(9, DampingLogic.applyStrongDrain(10, true, false));
        assertEquals(10, DampingLogic.applyStrongDrain(10, true, true));
        assertEquals(10, DampingLogic.applyStrongDrain(10, false, false));
    }

    @Test
    void strongDampingToNegativeTriggersShutdown() {
        int p = DampingLogic.applyStrongDrain(0, true, false);
        assertEquals(-1, p);
        assertTrue(DampingLogic.shouldShutdownAfterDrain(p));
        assertFalse(DampingLogic.shouldShutdownAfterDrain(0));
    }
}
