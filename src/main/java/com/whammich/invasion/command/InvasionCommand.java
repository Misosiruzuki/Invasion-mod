package com.whammich.invasion.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusTracker;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Brigadier port of legacy InvasionCommand: begin / end / range / status / help.
 */
public final class InvasionCommand {
    private InvasionCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("invasion")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("help").executes(InvasionCommand::help))
                        .then(Commands.literal("begin")
                                .then(Commands.argument("wave", IntegerArgumentType.integer(1, 100))
                                        .executes(ctx -> begin(ctx, IntegerArgumentType.getInteger(ctx, "wave"))))
                                .executes(ctx -> begin(ctx, 1)))
                        .then(Commands.literal("end").executes(InvasionCommand::end))
                        .then(Commands.literal("range")
                                .then(Commands.argument("radius", IntegerArgumentType.integer(32, 128))
                                        .executes(ctx -> range(ctx, IntegerArgumentType.getInteger(ctx, "radius")))))
                        .then(Commands.literal("status").executes(InvasionCommand::status))
                        .then(Commands.literal("nexusstatus").executes(InvasionCommand::nexusStatus))
                        .executes(InvasionCommand::help)
        );
    }

    private static int help(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("--- Invasion commands ---"), false);
        src.sendSuccess(() -> Component.literal("/invasion begin [wave]  — start invasion at wave"), false);
        src.sendSuccess(() -> Component.literal("/invasion end            — emergency stop"), false);
        src.sendSuccess(() -> Component.literal("/invasion range <32-128> — set spawn radius"), false);
        src.sendSuccess(() -> Component.literal("/invasion status         — focus nexus active?"), false);
        src.sendSuccess(() -> Component.literal("/invasion nexusstatus    — debug dump"), false);
        return 1;
    }

    private static int begin(CommandContext<CommandSourceStack> ctx, int wave) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = NexusTracker.getFocusNexus();
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus and interact first."));
            return 0;
        }
        try {
            nexus.debugStartInvasion(wave);
            NexusTracker.setActiveNexus(nexus);
            String name = src.getTextName();
            src.sendSuccess(() -> Component.literal("Started invasion at wave " + wave), true);
            if (src.getServer() != null) {
                NexusTracker.broadcastToAll(src.getServer(), name + " started invasion at wave " + wave);
            }
            return 1;
        } catch (Exception e) {
            LogHelper.warn("Failed to begin invasion: {}", e.getMessage());
            src.sendFailure(Component.literal("Failed: " + e.getMessage()));
            return 0;
        }
    }

    private static int end(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = NexusTracker.getActiveNexus();
        if (nexus == null) {
            nexus = NexusTracker.getFocusNexus();
        }
        if (nexus == null) {
            src.sendFailure(Component.literal("No invasion to end"));
            return 0;
        }
        nexus.emergencyStop();
        NexusTracker.setActiveNexus(null);
        String name = src.getTextName();
        src.sendSuccess(() -> Component.literal("Invasion ended"), true);
        if (src.getServer() != null) {
            NexusTracker.broadcastToAll(src.getServer(), name + " ended invasion");
        }
        return 1;
    }

    private static int range(CommandContext<CommandSourceStack> ctx, int radius) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = NexusTracker.getFocusNexus();
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        if (!nexus.setSpawnRadius(radius)) {
            src.sendFailure(Component.literal("Can't change range while nexus is active"));
            return 0;
        }
        src.sendSuccess(() -> Component.literal("Set nexus range to " + radius), true);
        return 1;
    }

    private static int status(CommandContext<CommandSourceStack> ctx) {
        NexusBlockEntity nexus = NexusTracker.getFocusNexus();
        boolean active = nexus != null && nexus.isActivated();
        ctx.getSource().sendSuccess(
                () -> Component.literal("focus nexus active: " + active
                        + (nexus != null ? " @ " + nexus.getBlockPosition() : "")),
                false);
        return 1;
    }

    private static int nexusStatus(CommandContext<CommandSourceStack> ctx) {
        NexusBlockEntity nexus = NexusTracker.getFocusNexus();
        if (nexus == null) {
            ctx.getSource().sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        String dump = nexus.debugStatus();
        ctx.getSource().sendSuccess(() -> Component.literal(dump), false);
        return 1;
    }
}
