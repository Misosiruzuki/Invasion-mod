package com.whammich.invasion;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Forge 1.20.1 config (replaces ConfigHandler / Config / ConfigInvasion from 1.7.10).
 * Uses ForgeConfigSpec; values load via ModConfigEvent.
 */
@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigHandler {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    private ConfigHandler() {
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            // Values are already available via COMMON.* ConfigValues
        }
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            // React to runtime config changes if needed
        }
    }

    /**
     * Common (both sides) settings — port of general / nexus / night-spawn options.
     */
    public static final class Common {
        public final ForgeConfigSpec.BooleanValue updateMessagesEnabled;
        public final ForgeConfigSpec.BooleanValue destructedBlocksDrop;
        public final ForgeConfigSpec.BooleanValue craftItemsEnabled;
        public final ForgeConfigSpec.BooleanValue enableLogging;

        public final ForgeConfigSpec.IntValue minDaysToAttack;
        public final ForgeConfigSpec.IntValue maxDaysToAttack;

        public final ForgeConfigSpec.BooleanValue nightSpawnsEnabled;
        public final ForgeConfigSpec.IntValue nightMobSpawnChance;
        public final ForgeConfigSpec.IntValue nightMobMaxGroupSize;
        public final ForgeConfigSpec.IntValue nightMobSightRange;
        public final ForgeConfigSpec.IntValue nightMobSenseRange;
        public final ForgeConfigSpec.IntValue mobLimitOverride;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push("general");
            updateMessagesEnabled = builder
                    .comment("Show update messages")
                    .define("updateMessagesEnabled", true);
            destructedBlocksDrop = builder
                    .comment("Blocks destroyed by invaders drop items")
                    .define("destructedBlocksDrop", true);
            craftItemsEnabled = builder
                    .comment("Enable crafting recipes for Invasion items")
                    .define("craftItemsEnabled", true);
            enableLogging = builder
                    .comment("Extra debug logging")
                    .define("enableLogging", false);
            builder.pop();

            builder.comment("Nexus continuous mode").push("nexus");
            minDaysToAttack = builder
                    .comment("Minimum days before continuous-mode attack")
                    .defineInRange("minDaysToAttack", 2, 0, 100);
            maxDaysToAttack = builder
                    .comment("Maximum days before continuous-mode attack")
                    .defineInRange("maxDaysToAttack", 3, 0, 100);
            builder.pop();

            builder.comment("Nighttime mob spawning (does not affect Nexus waves)").push("night_spawns");
            nightSpawnsEnabled = builder
                    .comment("Enable extra night Invasion mob spawns")
                    .define("nightSpawnsEnabled", false);
            nightMobSpawnChance = builder
                    .comment("Higher = more common night spawns")
                    .defineInRange("nightMobSpawnChance", 35, 0, 100);
            nightMobMaxGroupSize = builder
                    .comment("Max mobs that may spawn together at night")
                    .defineInRange("nightMobMaxGroupSize", 3, 1, 16);
            nightMobSightRange = builder
                    .comment("How far night mobs can see a player")
                    .defineInRange("nightMobSightRange", 20, 1, 128);
            nightMobSenseRange = builder
                    .comment("How far night mobs can sense a player through walls")
                    .defineInRange("nightMobSenseRange", 12, 1, 128);
            mobLimitOverride = builder
                    .comment("Override for global mob cap related to Invasion night spawns")
                    .defineInRange("mobLimitOverride", 70, 1, 500);
            builder.pop();
        }
    }
}
