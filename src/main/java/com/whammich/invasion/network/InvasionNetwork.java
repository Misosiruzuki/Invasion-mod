package com.whammich.invasion.network;

import com.whammich.invasion.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * SimpleChannel + minimal packets (NexusStatus S→C).
 */
public final class InvasionNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int nextId = 0;

    private InvasionNetwork() {}

    public static void register() {
        CHANNEL.registerMessage(
                nextId++,
                NexusStatusPacket.class,
                NexusStatusPacket::encode,
                NexusStatusPacket::decode,
                NexusStatusPacket::handle
        );
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAll(Object packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static int nextId() {
        return nextId++;
    }
}
