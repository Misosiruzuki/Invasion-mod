package com.whammich.invasion.nexus;

import com.whammich.invasion.network.InvasionNetwork;
import com.whammich.invasion.network.NexusStatusPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Tracks the focus / active nexus for debug commands (legacy Invasion statics).
 */
public final class NexusTracker {
    private static NexusBlockEntity focusNexus;
    private static NexusBlockEntity activeNexus;

    private NexusTracker() {}

    public static NexusBlockEntity getFocusNexus() {
        return focusNexus;
    }

    public static NexusBlockEntity getActiveNexus() {
        return activeNexus;
    }

    public static void setFocusNexus(NexusBlockEntity nexus) {
        focusNexus = nexus;
    }

    public static void setActiveNexus(NexusBlockEntity nexus) {
        activeNexus = nexus;
        if (nexus != null) {
            syncStatus(nexus);
        }
    }

    public static void clearIfMatches(NexusBlockEntity nexus) {
        if (focusNexus == nexus) {
            focusNexus = null;
        }
        if (activeNexus == nexus) {
            activeNexus = null;
        }
    }

    public static void broadcastToAll(MinecraftServer server, String message) {
        if (server == null) {
            return;
        }
        Component text = Component.literal(message);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(text);
        }
    }

    /** Push current nexus combat snapshot to all online players (S→C). */
    public static void syncStatus(NexusBlockEntity nexus) {
        if (nexus == null || nexus.getLevel() == null || nexus.getLevel().isClientSide) {
            return;
        }
        NexusStatusPacket packet = new NexusStatusPacket(
                nexus.getBlockPosition(),
                nexus.getMode(),
                nexus.getCurrentWave(),
                nexus.getHp(),
                nexus.getMaxHp()
        );
        InvasionNetwork.sendToAll(packet);
    }
}
