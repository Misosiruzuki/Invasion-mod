package com.whammich.invasion.nexus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** P2 / B-26..B-27 Strong Catalyst start wave. */
class CatalystLogicTest {

    @Test
    void unstableStartsAtWave1() {
        assertEquals(1, CatalystLogic.invasionStartWave(false));
        assertFalse(CatalystLogic.isStrongStartWave(1));
    }

    @Test
    void strongStartsAtWave10() {
        assertEquals(10, CatalystLogic.invasionStartWave(true));
        assertTrue(CatalystLogic.isStrongStartWave(10));
    }
}
