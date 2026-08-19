package com.whammich.invasion.nexus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrapCookLogicTest {
    @Test void idleStepIsOne() {
        assertEquals(1, TrapCookLogic.cookStep(0));
        assertEquals(1, TrapCookLogic.cookStep(4));
    }
    @Test void activeStepIsNine() {
        assertEquals(9, TrapCookLogic.cookStep(1));
        assertEquals(9, TrapCookLogic.cookStep(2));
        assertEquals(9, TrapCookLogic.cookStep(3));
    }
    @Test void completesAt1200() {
        assertFalse(TrapCookLogic.isComplete(1199));
        assertTrue(TrapCookLogic.isComplete(1200));
    }
}
