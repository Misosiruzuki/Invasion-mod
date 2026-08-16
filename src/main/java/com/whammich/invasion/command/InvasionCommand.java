package com.whammich.invasion.command;

import com.mojang.brigadier.CommandDispatcher;
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
 * Brigadier port of legacy InvasionCommand: begin / end / range / status / help.
 * Focus resolution: existing focus → look-at block → nearest nexus in range (for automation / VoxPilot).
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
                        .then(Commands.literal("continuous")
                                .then(Commands.literal("soon")
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 1200))
                                                .executes(ctx -> continuousSoon(ctx, IntegerArgumentType.getInteger(ctx, "ticks"))))
                                        .executes(ctx -> continuousSoon(ctx, 40)))
                                .executes(InvasionCommand::beginContinuous))
                        .executes(InvasionCommand::help)
        );
    }

    private static int help(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("--- Invasion commands ---"), false);
        src.sendSuccess(() -> Component.literal("/invasion begin [wave]  — start invasion at wave"), false);
        src.sendSuccess(() -> Component.literal("/invasion continuous         — start continuous mode"), false);
        src.sendSuccess(() -> Component.literal("/invasion continuous soon [t] — schedule attack in t ticks (test)"), false);
        src.sendSuccess(() -> Component.literal("/invasion end            — emergency stop"), false);
        src.sendSuccess(() -> Component.literal("/invasion range <32-128> — set spawn radius"), false);
        src.sendSuccess(() -> Component.literal("/invasion status         — focus nexus active?"), false);
        src.sendSuccess(() -> Component.literal("/invasion nexusstatus    — debug dump"), false);
        return 1;
    }

    /**
     * Resolve a nexus for debug commands without requiring a prior GUI open.
     * Order: valid focus → block under crosshair → nearest nexus within {@link #NEAREST_RANGE}.
     */
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
                if (be instanceof NexusBlockEntity nexus) {
                    NexusTracker.setFocusNexus(nexus);
                    return nexus;
                }
            }

            NexusBlockEntity nearest = findNearestNexus(player.serverLevel(), player.blockPosition(), NEAREST_RANGE);
            if (nearest != null) {
                NexusTracker.setFocusNexus(nearest);
                return nearest;
            }
        } else {
            // CommandSourceStack#getLevel() is already ServerLevel on 1.20.1 (not Level).
            ServerLevel level = src.getLevel();
            BlockPos origin = BlockPos.containing(src.getPosition());
            NexusBlockEntity nearest = findNearestNexus(level, origin, NEAREST_RANGE);
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
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof NexusBlockEntity nexus) {
                        double d = origin.distSqr(pos);
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

    private static int beginContinuous(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        nexus.debugStartContinuous();
        NexusTracker.setActiveNexus(nexus);
        src.sendSuccess(() -> Component.literal("Started continuous mode (next attack scheduled)"), true);
        return 1;
    }

    /** VoxPilot / debug: continuous mode with next attack in a few ticks. */
    private static int continuousSoon(CommandContext<CommandSourceStack> ctx, int ticks) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        nexus.debugStartContinuousAttackSoon(ticks);
        NexusTracker.setActiveNexus(nexus);
        src.sendSuccess(
                () -> Component.literal("Continuous mode; next attack in ~" + ticks + " ticks"),
                true);
        return 1;
    }

    private static int end(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = NexusTracker.getActiveNexus();
        if (!isUsable(nexus)) {
            nexus = resolveNexus(src);
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
        if (nexus != null) {
            NexusTracker.syncStatus(nexus);
        }
        ctx.getSource().sendSuccess(
                () -> Component.literal("focus nexus active: " + active
                        + (nexus != null ? " @ " + nexus.getBlockPosition() : "")),
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
}
