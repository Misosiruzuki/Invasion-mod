package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.item.ItemBowSearing;
import com.whammich.invasion.item.ItemHammerEngineer;
import com.whammich.invasion.item.ItemProbe;
import com.whammich.invasion.item.ItemStrangeBone;
import com.whammich.invasion.item.ItemSwordInfused;
import com.whammich.invasion.item.ItemTrap;
import com.whammich.invasion.item.ItemWandDebug;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * All Invasion items. Material subtypes from 1.7.10 are separate items (1.20.1 style).
 * Spawn eggs use ForgeSpawnEggItem with fixed colours for the 12 main hostiles.
 */
public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<Item> CATALYST_MIXTURE_UNSTABLE = material("catalyst_mixture_unstable");
    public static final RegistryObject<Item> CATALYST_MIXTURE_STABLE = material("catalyst_mixture_stable");
    public static final RegistryObject<Item> NEXUS_CATALYST_UNSTABLE = material("nexus_catalyst_unstable");
    public static final RegistryObject<Item> NEXUS_CATALYST_STABLE = material("nexus_catalyst_stable");
    public static final RegistryObject<Item> CATALYST_STRONG = material("catalyst_strong");
    public static final RegistryObject<Item> DAMPING_AGENT_WEAK = material("damping_agent_weak");
    public static final RegistryObject<Item> DAMPING_AGENT_STRONG = material("damping_agent_strong");
    public static final RegistryObject<Item> REMNANTS_SMALL = material("remnants_small");
    public static final RegistryObject<Item> RIFT_FLUX = material("rift_flux");
    public static final RegistryObject<Item> PHASE_CRYSTAL = material("phase_crystal");

    public static final RegistryObject<Item> SEARING_BOW = ITEMS.register("searing_bow",
            () -> new ItemBowSearing(new Item.Properties().stacksTo(1).durability(384)));

    public static final RegistryObject<Item> ENGY_HAMMER = ITEMS.register("engy_hammer",
            () -> new ItemHammerEngineer(new Item.Properties().stacksTo(1).durability(250)));

    public static final RegistryObject<Item> PROBE = ITEMS.register("probe",
            () -> new ItemProbe(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STRANGE_BONE = ITEMS.register("strange_bone",
            () -> new ItemStrangeBone(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> INFUSED_SWORD = ITEMS.register("infused_sword",
            () -> new ItemSwordInfused(new Item.Properties().stacksTo(1)));

    /** Empty trap (D-17 / C-03 input). */
    public static final RegistryObject<Item> TRAP = ITEMS.register("trap",
            () -> new ItemTrap(new Item.Properties().stacksTo(16), ItemTrap.TrapKind.EMPTY));
    /** Rift trap charged via Nexus (C-03 / D-19). */
    public static final RegistryObject<Item> TRAP_RIFT = ITEMS.register("trap_rift",
            () -> new ItemTrap(new Item.Properties().stacksTo(16), ItemTrap.TrapKind.RIFT));
    /** Flame trap (D-21). */
    public static final RegistryObject<Item> TRAP_FLAME = ITEMS.register("trap_flame",
            () -> new ItemTrap(new Item.Properties().stacksTo(16), ItemTrap.TrapKind.FLAME));

    public static final RegistryObject<Item> DEBUG_WAND = ITEMS.register("debug_wand",
            () -> new ItemWandDebug(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SPAWN_EGG_ZOMBIE = spawnEgg(
            "im_zombie_spawn_egg", EntityRegistry.ZOMBIE, 0x3B622F, 0x799C65);
    public static final RegistryObject<Item> SPAWN_EGG_ZOMBIE_PIGMAN = spawnEgg(
            "im_zombie_pigman_spawn_egg", EntityRegistry.ZOMBIE_PIGMAN, 0xE0A060, 0x5C4033);
    public static final RegistryObject<Item> SPAWN_EGG_SPIDER = spawnEgg(
            "im_spider_spawn_egg", EntityRegistry.SPIDER, 0x0A0A0A, 0xA00000);
    public static final RegistryObject<Item> SPAWN_EGG_SKELETON = spawnEgg(
            "im_skeleton_spawn_egg", EntityRegistry.SKELETON, 0xC1C1C1, 0x494949);
    public static final RegistryObject<Item> SPAWN_EGG_CREEPER = spawnEgg(
            "im_creeper_spawn_egg", EntityRegistry.CREEPER, 0x0DA70B, 0x000000);
    public static final RegistryObject<Item> SPAWN_EGG_IMP = spawnEgg(
            "im_imp_spawn_egg", EntityRegistry.IMP, 0x7A1F1F, 0xE05030);
    public static final RegistryObject<Item> SPAWN_EGG_WOLF = spawnEgg(
            "im_wolf_spawn_egg", EntityRegistry.WOLF, 0xD7D3D3, 0x3D3D3D);
    public static final RegistryObject<Item> SPAWN_EGG_PIG_ENGINEER = spawnEgg(
            "im_pig_engineer_spawn_egg", EntityRegistry.PIG_ENGINEER, 0xF0A0A0, 0x555555);
    public static final RegistryObject<Item> SPAWN_EGG_THROWER = spawnEgg(
            "im_thrower_spawn_egg", EntityRegistry.THROWER, 0x5A3A1A, 0xC0A060);
    public static final RegistryObject<Item> SPAWN_EGG_BURROWER = spawnEgg(
            "im_burrower_spawn_egg", EntityRegistry.BURROWER, 0x4A3A2A, 0x8B6914);
    public static final RegistryObject<Item> SPAWN_EGG_BIRD = spawnEgg(
            "im_bird_spawn_egg", EntityRegistry.BIRD, 0x2A4A6A, 0xE8E0D0);
    public static final RegistryObject<Item> SPAWN_EGG_GIANT_BIRD = spawnEgg(
            "im_giant_bird_spawn_egg", EntityRegistry.GIANT_BIRD, 0x1A2A3A, 0xC0B090);

    private ItemRegistry() {
    }

    private static RegistryObject<Item> material(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(64)));
    }

    private static RegistryObject<Item> spawnEgg(
            String name,
            RegistryObject<? extends net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>> type,
            int primary,
            int secondary) {
        return ITEMS.register(name,
                () -> new ForgeSpawnEggItem(type, primary, secondary, new Item.Properties()));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
