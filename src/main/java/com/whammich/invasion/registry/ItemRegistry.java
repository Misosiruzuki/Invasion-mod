package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Item DeferredRegister hub.
 * Replaces 1.7.10 ItemRegistry; individual items are registered in group 3+.
 */
public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    private ItemRegistry() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
