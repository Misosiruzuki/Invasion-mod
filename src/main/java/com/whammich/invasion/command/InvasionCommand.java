package com.whammich.invasion.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusTracker;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Brigadier port of 1.7 {@code invmod.common.InvasionCommand} only.
 * Subcommands must match 1.7.10 — do not add test-only helpers that did not exist there.
 *
 * <p>1.7 set: help, begin, end, range, spawnertest, pointcontainertest, wavebuildertest,
 * nexusstatus, bolt, status.
 */
public final class InvasionCommand {
    private static final double LOOK_RANGE = 8.0;
    private static final int NEAREST_RANGE = 12;

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
                        .then(Commands.literal("bolt")
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                                        .then(Commands.argument("time", IntegerArgumentType.integer(1, 200))
                                                                .executes(ctx -> bolt(ctx,
                                                                        IntegerArgumentType.getInteger(ctx, "x"),
                                                                        IntegerArgumentType.getInteger(ctx, "y"),
                                                                        IntegerArgumentType.getInteger(ctx, "z"),
                                                                        IntegerArgumentType.getInteger(ctx, "time"))))
                                                        .executes(ctx -> bolt(ctx,
                                                                IntegerArgumentType.getInteger(ctx, "x"),
                                                                IntegerArgumentType.getInteger(ctx, "y"),
                                                                IntegerArgumentType.getInteger(ctx, "z"),
                                                                40))))
                                        .executes(ctx -> bolt(ctx, 0, 0, 0, 40)))
                                .executes(ctx -> bolt(ctx, 0, 0, 0, 40)))
                        .then(Commands.literal("spawnertest")
                                .then(Commands.argument("startWave", IntegerArgumentType.integer(1, 100))
                                        .then(Commands.argument("endWave", IntegerArgumentType.integer(1, 100))
                                                .executes(ctx -> spawnerTest(ctx,
                                                        IntegerArgumentType.getInteger(ctx, "startWave"),
                                                        IntegerArgumentType.getInteger(ctx, "endWave"))))
                                        .executes(ctx -> spawnerTest(ctx,
                                                IntegerArgumentType.getInteger(ctx, "startWave"), 11)))
                                .executes(ctx -> spawnerTest(ctx, 1, 11)))
                        .then(Commands.literal("pointcontainertest").executes(InvasionCommand::pointContainerTest))
                        .then(Commands.literal("wavebuildertest")
                                .then(Commands.argument("difficulty", FloatArgumentType.floatArg(0.1f, 10f))
                                        .then(Commands.argument("tier", FloatArgumentType.floatArg(0.1f, 10f))
                                                .then(Commands.argument("lengthSeconds", IntegerArgumentType.integer(1, 3600))
                                                        .executes(ctx -> waveBuilderTest(ctx,
                                                                FloatArgumentType.getFloat(ctx, "difficulty"),
                                                                FloatArgumentType.getFloat(ctx, "tier"),
                                                                IntegerArgumentType.getInteger(ctx, "lengthSeconds"))))
                                                .executes(ctx -> waveBuilderTest(ctx,
                                                        FloatArgumentType.getFloat(ctx, "difficulty"),
                                                        FloatArgumentType.getFloat(ctx, "tier"),
                                                        160)))
                                        .executes(ctx -> waveBuilderTest(ctx,
                                                FloatArgumentType.getFloat(ctx, "difficulty"), 1.0f, 160)))
                                .executes(ctx -> waveBuilderTest(ctx, 1.0f, 1.0f, 160)))
        );
    }

    private static int help(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        // Match 1.7 help text
        src.sendSuccess(() -> Component.literal("--- Showing Invasion help page 1 of 1 ---"), false);
        src.sendSuccess(() -> Component.literal("/begin x to start a wave"), false);
        src.sendSuccess(() -> Component.literal("/end to end the invasion"), false);
        src.sendSuccess(() -> Component.literal("/range x to set the spawn range"), false);
        return 1;
    }

    private static NexusBlockEntity resolveNexus(CommandSourceStack src) {
        NexusBlockEntity focus = NexusTracker.getFocusNexus();
        if (isUsable(focus)) {
            return focus;
        }
        ServerPlayer player = src.getPlayer();
        if (player != null) {
            HitResult hit = player.pick(LOOK_RANGE, 0.0f, false);
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof NexusBlockEntity nexus && isUsable(nexus)) {
                    NexusTracker.setFocusNexus(nexus);
                    return nexus;
                }
            }
            NexusBlockEntity nearest = findNearestNexus(player.serverLevel(), player.blockPosition(), NEAREST_RANGE);
            if (nearest != null) {
                NexusTracker.setFocusNexus(nearest);
                return nearest;
            }
        }
        return null;
    }

    private static boolean isUsable(NexusBlockEntity nexus) {
        return nexus != null && !nexus.isRemoved() && nexus.getLevel() != null;
    }

    private static NexusBlockEntity findNearestNexus(ServerLevel level, BlockPos origin, int range) {
        NexusBlockEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos p = origin.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(p);
                    if (be instanceof NexusBlockEntity nexus) {
                        double d = origin.distSqr(p);
                        if (d < bestDist) {
                            bestDist = d;
                            best = nexus;
                        }
                    }
                }
            }
        }
        return best;
    }

    private static int begin(CommandContext<CommandSourceStack> ctx, int wave) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
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
            nexus = resolveNexus(src);
        }
        if (nexus == null || !nexus.isActivated()) {
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
        NexusBlockEntity nexus = resolveNexus(src);
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
        NexusBlockEntity nexus = resolveNexus(ctx.getSource());
        boolean active = nexus != null && nexus.isActivated();
        ctx.getSource().sendSuccess(
                () -> Component.literal("nexus status:" + active),
                false);
        return 1;
    }

    private static int nexusStatus(CommandContext<CommandSourceStack> ctx) {
        NexusBlockEntity nexus = resolveNexus(ctx.getSource());
        if (nexus == null) {
            ctx.getSource().sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        String dump = nexus.debugStatus();
        ctx.getSource().sendSuccess(() -> Component.literal(dump), false);
        return 1;
    }

    /** 1.7 bolt — offsets from nexus position; createBolt if available, else log. */
    private static int bolt(CommandContext<CommandSourceStack> ctx, int ox, int oy, int oz, int time) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        BlockPos p = nexus.getBlockPos().offset(ox, oy, oz);
        try {
            // Prefer nexus API if ported
            nexus.getClass().getMethod("createBolt", int.class, int.class, int.class, int.class)
                    .invoke(nexus, p.getX(), p.getY(), p.getZ(), time);
            src.sendSuccess(() -> Component.literal("Bolt at " + p + " t=" + time), true);
        } catch (ReflectiveOperationException e) {
            LogHelper.info("bolt requested at {} time={} (createBolt not ported yet)", p, time);
            src.sendSuccess(() -> Component.literal("Bolt stub at " + p + " t=" + time), true);
        }
        return 1;
    }

    private static int spawnerTest(CommandContext<CommandSourceStack> ctx, int startWave, int endWave) {
        LogHelper.info("spawnertest startWave={} endWave={} (Tester port pending)", startWave, endWave);
        ctx.getSource().sendSuccess(
                () -> Component.literal("spawnertest " + startWave + ".." + endWave + " logged"),
                false);
        return 1;
    }

    private static int pointContainerTest(CommandContext<CommandSourceStack> ctx) {
        LogHelper.info("pointcontainertest (Tester port pending)");
        ctx.getSource().sendSuccess(() -> Component.literal("pointcontainertest logged"), false);
        return 1;
    }

    private static int waveBuilderTest(CommandContext<CommandSourceStack> ctx, float difficulty, float tier, int lengthSeconds) {
        LogHelper.info("wavebuildertest difficulty={} tier={} length={}s (Tester port pending)",
                difficulty, tier, lengthSeconds);
        ctx.getSource().sendSuccess(
                () -> Component.literal("wavebuildertest logged"),
                false);
        return 1;
    }
}
