package com.whammich.invasion.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * S→C snapshot of nexus combat state for players who do not have the menu open.
 * GUI itself continues to use ContainerData; this is for commands / future HUD.
 */
public final class NexusStatusPacket {
    private final BlockPos pos;
    private final int mode;
    private final int wave;
    private final int hp;
    private final int maxHp;

    public NexusStatusPacket(BlockPos pos, int mode, int wave, int hp, int maxHp) {
        this.pos = pos;
        this.mode = mode;
        this.wave = wave;
        this.hp = hp;
        this.maxHp = maxHp;
    }

    public static void encode(NexusStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarInt(msg.mode);
        buf.writeVarInt(msg.wave);
        buf.writeVarInt(msg.hp);
        buf.writeVarInt(msg.maxHp);
    }

    public static NexusStatusPacket decode(FriendlyByteBuf buf) {
        return new NexusStatusPacket(
                buf.readBlockPos(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt()
        );
    }

    public static void handle(NexusStatusPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ClientNexusStatusCache.update(msg.pos, msg.mode, msg.wave, msg.hp, msg.maxHp);
        });
        ctx.setPacketHandled(true);
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getMode() {
        return mode;
    }

    public int getWave() {
        return wave;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
