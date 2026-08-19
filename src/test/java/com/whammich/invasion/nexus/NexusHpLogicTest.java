package com.whammich.invasion.nexus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NexusHpLogicTest {

    @Test
    void applyDamageReducesHp() {
        assertEquals(90, NexusHpLogic.applyDamage(100, 10));
    }

    @Test
    void applyDamageClampsAtZero() {
        assertEquals(0, NexusHpLogic.applyDamage(5, 20));
    }

    @Test
    void applyDamageIgnoresNonPositive() {
        assertEquals(50, NexusHpLogic.applyDamage(50, 0));
        assertEquals(50, NexusHpLogic.applyDamage(50, -3));
    }

    @Test
    void shouldEndWhenHpZeroOrBelow() {
        assertTrue(NexusHpLogic.shouldEndInvasion(0));
        assertTrue(NexusHpLogic.shouldEndInvasion(-1));
        assertFalse(NexusHpLogic.shouldEndInvasion(1));
    }

    @Test
    void bindHalfExtentUsesSpawnRadiusPlusMargin() {
        assertEquals(62, NexusHpLogic.bindHalfExtent(52));
        assertEquals(10, NexusHpLogic.bindHalfExtent(0));
    }

    @Test
    void deathLinkKillsWhenInRadius() {
        assertTrue(NexusHpLogic.shouldKillPlayerOnDestroy(true, 1000L, null));
    }

    @Test
    void deathLinkKillsWhenRecentlyBound() {
        assertTrue(NexusHpLogic.shouldKillPlayerOnDestroy(false, 1000L, 500L));
        assertFalse(NexusHpLogic.shouldKillPlayerOnDestroy(false, 400_000L, 0L));
    }

    @Test
    void deathLinkIgnoresUnboundOutside() {
        assertFalse(NexusHpLogic.shouldKillPlayerOnDestroy(false, 1000L, null));
    }
}
