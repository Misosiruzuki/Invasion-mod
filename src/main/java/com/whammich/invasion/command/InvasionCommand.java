package com.whammich.invasion.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.entity.EntityIMTrap;
import com.whammich.invasion.entity.EntityIMWolf;
import com.whammich.invasion.item.ItemStrangeBone;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.AABB;
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
                        .then(Commands.literal("bindwolf").executes(InvasionCommand::bindWolf))
                        .then(Commands.literal("spawnwolf").executes(InvasionCommand::spawnWolf))
                        .then(Commands.literal("unbindwolf").executes(InvasionCommand::unbindWolf))
                        .then(Commands.literal("probe").executes(InvasionCommand::probeStrength))
                        .then(Commands.literal("adjust").executes(ctx -> adjustRadius(ctx, false)))
                        .then(Commands.literal("adjustdown").executes(ctx -> adjustRadius(ctx, true)))
                        .then(Commands.literal("placetrap")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .executes(ctx -> placeTrap(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "type")))))
                        .then(Commands.literal("puttrap")
                                .executes(ctx -> putTrap(ctx.getSource())))
                        .then(Commands.literal("addkills")
                                .then(Commands.argument("n", IntegerArgumentType.integer(1, 1000))
                                        .executes(ctx -> addKills(ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "n")))))
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


    private static int placeTrap(CommandSourceStack src, String type) {
        try {
            var level = src.getLevel();
            var pos = src.getPosition();
            EntityIMTrap trap = EntityRegistry.TRAP.get().create(level);
            if (trap == null) {
                src.sendFailure(Component.literal("Failed to create trap entity"));
                return 0;
            }
            int trapType = "flame".equalsIgnoreCase(type) ? EntityIMTrap.TYPE_FLAME : EntityIMTrap.TYPE_RIFT;
            // Sit on top of the block under the command source feet (stone platform top).
            double x = Math.floor(pos.x) + 0.5;
            double z = Math.floor(pos.z) + 0.5;
            int floorY = (int) Math.floor(pos.y) - 1;
            double y = floorY + 1.05;
            trap.setPos(x, y, z);
            trap.setTrapType(trapType);
            level.addFreshEntity(trap);
            final int id = trap.getId();
            src.sendSuccess(() -> Component.literal(
                    "Placed " + type + " trap id=" + id + " at " + trap.blockPosition()), true);
            return 1;
        } catch (Exception e) {
            src.sendFailure(Component.literal("placeTrap error: " + e.getMessage()));
            return 0;
        }
    }

    private static int putTrap(CommandSourceStack src) {
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        nexus.debugSetInputTrap();
        src.sendSuccess(() -> Component.literal("Put empty trap in nexus input"), true);
        return 1;
    }

    private static int addKills(CommandSourceStack src, int n) {
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No focus nexus"));
            return 0;
        }
        nexus.debugAddKills(n);
        src.sendSuccess(() -> Component.literal("Added kills, total=" + nexus.getNexusKills()), true);
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





    private static int adjustRadius(CommandContext<CommandSourceStack> ctx, boolean down) {
        CommandSourceStack src = ctx.getSource();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No nexus"));
            return 0;
        }
        int current = nexus.getSpawnRadius();
        int next = down ? current - 8 : current + 8;
        if (next < 32) next = 128;
        if (next > 128) next = 32;
        if (!nexus.setSpawnRadius(next)) {
            src.sendFailure(Component.literal("Cannot change radius while active"));
            return 0;
        }
        int finalNext = next;
        src.sendSuccess(() -> Component.literal("Nexus range changed to: " + finalNext), true);
        LogHelper.info("adjust radius {} -> {}", current, finalNext);
        return 1;
    }

    private static int probeStrength(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer player = src.getPlayer();
        if (player == null) {
            src.sendFailure(Component.literal("Player required"));
            return 0;
        }
        var hit = player.pick(5.0, 0, false);
        if (!(hit instanceof BlockHitResult bhr)) {
            src.sendFailure(Component.literal("Look at a block"));
            return 0;
        }
        BlockPos pos = bhr.getBlockPos();
        var state = player.level().getBlockState(pos);
        float strength = com.whammich.invasion.util.BlockStrength.get(player.level(), pos, state);
        double shown = Math.round((strength + 0.005) * 100.0) / 100.0;
        src.sendSuccess(() -> Component.literal("Block strength: " + shown), true);
        LogHelper.info("probe strength={} at {}", shown, pos);
        return 1;
    }

    /** Unbind nearest IM wolf with a bone (D-34 / VoxPilot). */
    private static int unbindWolf(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer player = src.getPlayer();
        if (player == null) {
            src.sendFailure(Component.literal("Player required"));
            return 0;
        }
        ServerLevel level = player.serverLevel();
        EntityIMWolf im = level.getEntitiesOfClass(EntityIMWolf.class, player.getBoundingBox().inflate(12.0),
                w -> w.isAlive() && w.isNexusBound()).stream().findFirst().orElse(null);
        if (im == null) {
            src.sendFailure(Component.literal("No bound IM wolf nearby"));
            return 0;
        }
        ItemStack bone = player.getMainHandItem();
        boolean creative = player.getAbilities().instabuild;
        if (!creative && !bone.is(net.minecraft.world.item.Items.BONE)) {
            // still allow command path for tests — consume if bone present
        }
        if (im.unbindToVanillaWolf(player)) {
            if (!creative && bone.is(net.minecraft.world.item.Items.BONE)) {
                bone.shrink(1);
            }
            src.sendSuccess(() -> Component.literal("Unbound IM wolf to vanilla wolf"), true);
            return 1;
        }
        src.sendFailure(Component.literal("Unbind failed"));
        return 0;
    }

    /** Spawn an IM wolf bound to nearest nexus (D-33 tests). */
    private static int spawnWolf(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer player = src.getPlayer();
        if (player == null) {
            src.sendFailure(Component.literal("Player required"));
            return 0;
        }
        ServerLevel level = player.serverLevel();
        NexusBlockEntity nexus = resolveNexus(src);
        if (nexus == null) {
            src.sendFailure(Component.literal("No nexus in range"));
            return 0;
        }
        EntityIMWolf wolf = EntityRegistry.WOLF.get().create(level);
        if (wolf == null) {
            src.sendFailure(Component.literal("Failed to create wolf"));
            return 0;
        }
        BlockPos at = player.blockPosition();
        wolf.moveTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5, player.getYRot(), 0);
        wolf.bindToNexus(nexus.getBlockPos());
        wolf.setCustomName(Component.literal("VoxPilotTrack"));
        wolf.setCustomNameVisible(true);
        level.addFreshEntity(wolf);
        src.sendSuccess(() -> Component.literal("Spawned IM wolf bound to nexus " + nexus.getBlockPos()), true);
        LogHelper.info("spawnwolf at {} bound to {}", at, nexus.getBlockPos());
        return 1;
    }

    /** VoxPilot / debug: bind nearest tamed wolf near player to nearby Nexus (D-32). */
    private static int bindWolf(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ServerPlayer player = src.getPlayer();
        if (player == null) {
            src.sendFailure(Component.literal("Player required"));
            return 0;
        }
        ServerLevel level = player.serverLevel();
        AABB box = player.getBoundingBox().inflate(8.0);
        Wolf wolf = level.getEntitiesOfClass(Wolf.class, box, w -> w.isTame() && w.isAlive())
                .stream().findFirst().orElse(null);
        if (wolf == null) {
            src.sendFailure(Component.literal("No tamed wolf nearby"));
            return 0;
        }
        ItemStack stack = player.getMainHandItem();
        ItemStack consume = stack.is(ItemRegistry.STRANGE_BONE.get()) ? stack : null;
        ItemStrangeBone.BindResult result = ItemStrangeBone.tryBindWolf(level, player, wolf, consume);
        if (result == ItemStrangeBone.BindResult.BOUND) {
            src.sendSuccess(() -> Component.literal("Bound wolf to Nexus"), true);
            return 1;
        }
        src.sendFailure(Component.literal("Bind failed: " + result));
        return 0;
    }

}
