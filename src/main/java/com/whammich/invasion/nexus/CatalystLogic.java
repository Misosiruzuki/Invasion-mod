package com.whammich.invasion.nexus;

/**
 * Pure catalyst → mode / start-wave rules (wiki B-26 / B-27, 1.7 TileEntityNexus).
 * Strong Catalyst has no crafting recipe (creative-only); unstable starts wave 1, strong starts wave 10.
 */
public final class CatalystLogic {
    private CatalystLogic() {}

    /** Invasion mode starting wave for a non-stable nexus catalyst. */
    public static int invasionStartWave(boolean strongCatalyst) {
        return strongCatalyst ? 10 : 1;
    }

    public static boolean isStrongStartWave(int wave) {
        return wave == 10;
    }
}
