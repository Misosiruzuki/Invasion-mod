package com.whammich.invasion.nexus;

/**
 * Pure Nexus HP rules (Wiki B-03..B-05 / 1.7 TileEntityNexus.attackNexus + theEnd).
 * Side effects (despawn, player kill, messages) stay on the block entity.
 */
public final class NexusHpLogic {
    /** Default max HP (1.7 TileEntityNexus and Wiki-facing default). */
    public static final int DEFAULT_MAX_HP = 100;

    /** Players inside spawnRadius + this margin are life-linked (1.7 boundingBoxToRadius). */
    public static final int BIND_RADIUS_MARGIN = 10;

    /** Bound-player window used by 1.7 theEnd (5 minutes). */
    public static final long BIND_FRESH_MS = 300_000L;

    private NexusHpLogic() {}

    /** Apply damage; HP never goes below 0. */
    public static int applyDamage(int hp, int damage) {
        if (damage <= 0) {
            return Math.max(0, hp);
        }
        return Math.max(0, hp - damage);
    }

    /** True when this hit should trigger end-of-invasion (HP crossed to 0). */
    public static boolean shouldEndInvasion(int hpAfterDamage) {
        return hpAfterDamage <= 0;
    }

    /**
     * Axis half-extent for bind / kill AABB: spawnRadius + margin (1.7).
     */
    public static int bindHalfExtent(int spawnRadius) {
        return Math.max(0, spawnRadius) + BIND_RADIUS_MARGIN;
    }

    /**
     * Whether a player should die when the Nexus is destroyed (death-result link).
     * 1.7: bound within last 5 minutes. Distance-only mode: currently inside radius.
     */
    public static boolean shouldKillPlayerOnDestroy(boolean currentlyInRadius, long nowMs, Long lastBoundMs) {
        if (currentlyInRadius) {
            return true;
        }
        if (lastBoundMs == null) {
            return false;
        }
        return nowMs - lastBoundMs < BIND_FRESH_MS;
    }
}
