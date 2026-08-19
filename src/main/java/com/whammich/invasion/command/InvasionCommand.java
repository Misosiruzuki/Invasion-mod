package com.whammich.invasion.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusTracker;
import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
                        .then(Commands.literal("power")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 100000))
                                        .executes(ctx -> setPower(ctx, IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("damage")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 1000))
                                        .executes(ctx -> damageNexus(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "amount")))))
                        .then(Commands.literal("cook")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 1200))
                                        .executes(ctx -> setCook(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "value")))))
                        .then(Commands.literal("sethp")
                                .then(Commands.argument("hp", IntegerArgumentType.integer(0, 1000))
                                        .executes(ctx -> setHp(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "hp")))))
                        .then(Commands.literal("damping")
                                .then(Commands.literal("weak").executes(ctx -> setDamping(ctx, "weak")))
                                .then(Commands.literal("strong").executes(ctx -> setDamping(ctx, "strong")))
                                .then(Commands.literal("clear").executes(ctx -> setDamping(ctx, "clear"))))
                        .then(Commands.literal("activate")
                                .then(Commands.literal("strong").executes(InvasionCommand::activateStrong)))
                        .then(Commands.literal("catalyst")
                                .then(Commands.literal("strong").executes(ctx -> setCatalyst(ctx, "strong")))
                                .then(Commands.literal("unstable").executes(ctx -> setCatalyst(ctx, "unstable")))
                                .then(Commands.literal("stable").executes(ctx -> setCatalyst(ctx, "stable")))
                                .then(Commands.literal("clear").executes(ctx -> setCatalyst(ctx, "clear"))))
                        .then(Commands.literal("craft")
                                .then(Commands.literal("diamond").executes(ctx -> craftFlux(ctx, "flux_to_diamond")))
                                .then(Commands.literal("iron").executes(ctx -> craftFlux(ctx, "flux_to_iron_ingot")))
                                .then(Commands.literal("redstone").executes(ctx -> craftFlux(ctx, "flux_to_redstone")))
                                .then(Commands.literal("lapis").executes(ctx -> craftFlux(ctx, "flux_to_lapis"))))
                        .executes(InvasionCommand::help)
        );
    }

    private static int help(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("--- Invasion commands ---"), false);
        src.sendSuccess(() -> Component.literal("/invasion begin [wave]  — start invasion at wave"), false);
        src.sendSuccess(() -> Component.literal("/invasion continuous         — start continuous mode"), false);
        src.sendSuccess(() -> Component.literal("/invasion continuous soon [t] — schedule attack in t ticks (test)"), false);
        src.sendSuccess(() -> Component.literal("/invasion power <n>          — set powerLevel (test)"), false);
        src.sendSuccess(() -> Component.literal("/invasion damage <n>         — damage focus nexus HP (test P4)"), false);
        src.sendSuccess(() -> Component.literal("/invasion sethp <n>          — set focus nexus HP (test P4)"), false);
        src.sendSuccess(() -> Component.literal("/invasion damping weak|strong|clear — catalyst slot (test, P1)"), false);
        src.sendSuccess(() -> Component.literal("/invasion activate strong — Strong Catalyst skip-timer (test)"), false);
        src.sendSuccess(() -> Component.literal("/invasion catalyst strong|unstable|stable|clear — fill nexus slot"), false);
        src.sendSuccess(() -> Component.literal("/invasion craft diamond|iron|redstone|lapis — RecipeManager craft"), false);
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

    private static int setPower(CommandContext<CommandSourceStack> ctx, int value) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        nexus.debugSetPowerLevel(value);
        float diff = 1.0F + value / 4500.0F;
        src.sendSuccess(
                () -> Component.literal("Set powerLevel=" + value + " (difficulty~" + diff + ")"),
                true);
        return 1;
    }

    private static int setDamping(CommandContext<CommandSourceStack> ctx, String kind) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        ItemStack stack;
        if ("weak".equals(kind)) {
            stack = new ItemStack(ItemRegistry.DAMPING_AGENT_WEAK.get());
        } else if ("strong".equals(kind)) {
            stack = new ItemStack(ItemRegistry.DAMPING_AGENT_STRONG.get());
        } else {
            stack = ItemStack.EMPTY;
        }
        nexus.debugSetCatalystSlot(stack);
        src.sendSuccess(() -> Component.literal("Catalyst slot damping: " + kind), true);
        return 1;
    }

    private static int activateStrong(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        nexus.debugActivateStrongCatalyst();
        NexusTracker.setActiveNexus(nexus);
        src.sendSuccess(() -> Component.literal("Strong Catalyst activation (expect wave 10)"), true);
        return 1;
    }

    /** Put a catalyst in the nexus input slot (player-like; activation still needs time). */
    private static int setCatalyst(CommandContext<CommandSourceStack> ctx, String kind) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus. Look at / place a nexus nearby."));
            return 0;
        }
        ItemStack stack;
        switch (kind) {
            case "strong" -> stack = new ItemStack(ItemRegistry.CATALYST_STRONG.get());
            case "unstable" -> stack = new ItemStack(ItemRegistry.NEXUS_CATALYST_UNSTABLE.get());
            case "stable" -> stack = new ItemStack(ItemRegistry.NEXUS_CATALYST_STABLE.get());
            default -> stack = ItemStack.EMPTY;
        }
        nexus.debugSetCatalystSlot(stack);
        src.sendSuccess(() -> Component.literal("Nexus catalyst slot: " + kind), true);
        return 1;
    }

    /**
     * Craft via the real RecipeManager entry (invasion:flux_to_*), consuming flux from the player.
     * Play-faithful automated check that datapack recipes are loaded and matchable.
     */
    private static int craftFlux(CommandContext<CommandSourceStack> ctx, String recipePath) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer player;
        try {
            player = src.getPlayerOrException();
        } catch (Exception e) {
            src.sendFailure(Component.literal("Player required for craft"));
            return 0;
        }
        ServerLevel level = player.serverLevel();
        ResourceLocation id = new ResourceLocation("invasion", recipePath);
        var opt = level.getRecipeManager().byKey(id);
        if (opt.isEmpty()) {
            src.sendFailure(Component.literal("Recipe not found: " + id));
            return 0;
        }
        if (!(opt.get() instanceof net.minecraft.world.item.crafting.CraftingRecipe crafting)) {
            src.sendFailure(Component.literal("Not a crafting recipe: " + id));
            return 0;
        }

        // Build a transient 3x3 from the shaped pattern of known flux recipes.
        net.minecraft.world.inventory.CraftingContainer container =
                new net.minecraft.world.inventory.TransientCraftingContainer(
                        new net.minecraft.world.inventory.AbstractContainerMenu(null, -1) {
                            @Override
                            public ItemStack quickMoveStack(Player p, int index) {
                                return ItemStack.EMPTY;
                            }

                            @Override
                            public boolean stillValid(Player p) {
                                return true;
                            }
                        }, 3, 3);

        int fluxNeeded = switch (recipePath) {
            case "flux_to_diamond" -> 4;
            case "flux_to_iron_ingot" -> 1;
            case "flux_to_redstone", "flux_to_lapis" -> 2;
            default -> 4;
        };
        // Pattern placement matching 1.7 / JSON recipes
        switch (recipePath) {
            case "flux_to_diamond" -> {
                container.setItem(1, flux(1));
                container.setItem(3, flux(1));
                container.setItem(5, flux(1));
                container.setItem(7, flux(1));
            }
            case "flux_to_iron_ingot" -> container.setItem(4, flux(1));
            case "flux_to_redstone" -> {
                container.setItem(3, flux(1));
                container.setItem(5, flux(1));
            }
            case "flux_to_lapis" -> {
                container.setItem(1, flux(1));
                container.setItem(7, flux(1));
            }
            default -> {
            }
        }

        if (!crafting.matches(container, level)) {
            src.sendFailure(Component.literal("Recipe did not match assembled grid: " + id));
            return 0;
        }
        ItemStack result = crafting.assemble(container, level.registryAccess());
        if (result.isEmpty()) {
            src.sendFailure(Component.literal("Recipe produced empty result: " + id));
            return 0;
        }
        if (!player.getInventory().contains(new ItemStack(ItemRegistry.RIFT_FLUX.get()))) {
            // count flux
        }
        int have = 0;
        for (ItemStack s : player.getInventory().items) {
            if (s.is(ItemRegistry.RIFT_FLUX.get())) {
                have += s.getCount();
            }
        }
        if (have < fluxNeeded) {
            src.sendFailure(Component.literal("Need " + fluxNeeded + " Rift Flux (have " + have + ")"));
            return 0;
        }
        int left = fluxNeeded;
        for (int i = 0; i < player.getInventory().items.size() && left > 0; i++) {
            ItemStack s = player.getInventory().items.get(i);
            if (!s.is(ItemRegistry.RIFT_FLUX.get())) {
                continue;
            }
            int take = Math.min(left, s.getCount());
            s.shrink(take);
            left -= take;
        }
        if (!player.getInventory().add(result.copy())) {
            player.drop(result.copy(), false);
        }
        src.sendSuccess(
                () -> Component.literal("Crafted " + result.getCount() + "x " + result.getHoverName().getString()
                        + " via " + id),
                true);
        return 1;
    }

    private static ItemStack flux(int count) {
        return new ItemStack(ItemRegistry.RIFT_FLUX.get(), count);
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

    private static int damageNexus(CommandSourceStack src, int amount) {
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        int before = nexus.getHp();
        nexus.attackNexus(amount);
        int after = nexus.getHp();
        src.sendSuccess(() -> Component.literal("Damaged nexus " + amount + " (hp " + before + " -> " + after + ")"), true);
        return 1;
    }

    private static int setCook(CommandSourceStack src, int value) {
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        nexus.debugSetCookTime(value);
        src.sendSuccess(() -> Component.literal("Set cookTime=" + nexus.getCookTime()), true);
        return 1;
    }

    private static int setHp(CommandSourceStack src, int hp) {
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        nexus.debugSetHp(hp);
        src.sendSuccess(() -> Component.literal("Set nexus hp=" + nexus.getHp() + "/" + nexus.getMaxHp()), true);
        return 1;
    }

}
