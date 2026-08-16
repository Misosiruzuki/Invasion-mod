package com.whammich.invasion.nexus;

/**
 * Pure continuous-mode damping rules (1.7 TileEntityNexus doContinuous / wiki B-23..B-25).
 * Catalyst-slot item checks stay on the block entity; this class only encodes the power math.
 */
public final class DampingLogic {
    private DampingLogic() {}

    /** B-23: weak damping in slot blocks the periodic power +1 (flux still generates). */
    public static int applyPowerTickGain(int powerLevel, boolean weakDampingInSlot) {
        if (weakDampingInSlot) {
            return powerLevel;
        }
        return powerLevel + 1;
    }

    /**
     * B-24 / B-25: strong damping drains 1 power per continuous tick while not mid-attack.
     * When result is below 0, caller should shutdown (stop continuous).
     *
     * @return new power level (may be -1 → shutdown)
     */
    public static int applyStrongDrain(int powerLevel, boolean strongDampingInSlot, boolean continuousAttackActive) {
        if (!strongDampingInSlot || continuousAttackActive) {
            return powerLevel;
        }
        if (powerLevel < 0) {
            return powerLevel;
        }
        return powerLevel - 1;
    }

    public static boolean shouldShutdownAfterDrain(int powerAfterDrain) {
        return powerAfterDrain < 0;
    }
}
