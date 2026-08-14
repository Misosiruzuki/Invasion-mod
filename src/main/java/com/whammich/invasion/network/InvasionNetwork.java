package com.whammich.invasion.network;

import com.whammich.invasion.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * SimpleChannel skeleton (group 10). Concrete packets register here as needed.
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
        // Packets register here, e.g.:
        // CHANNEL.registerMessage(nextId++, ExamplePacket.class, ExamplePacket::encode, ExamplePacket::decode, ExamplePacket::handle);
        // Currently no packets — channel is reserved for future client/server sync.
    }

    public static int nextId() {
        return nextId++;
    }
}
