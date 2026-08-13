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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * All Invasion items. Material subtypes from 1.7.10 are separate items (1.20.1 style).
 * ItemSpawnEgg deferred until entities exist.
 */
public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    // --- Materials (former ItemMaterials metas) ---
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

    // --- Tools / weapons / special ---
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

    public static final RegistryObject<Item> TRAP = ITEMS.register("trap",
            () -> new ItemTrap(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DEBUG_WAND = ITEMS.register("debug_wand",
            () -> new ItemWandDebug(new Item.Properties().stacksTo(1)));

    private ItemRegistry() {
    }

    private static RegistryObject<Item> material(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(64)));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
