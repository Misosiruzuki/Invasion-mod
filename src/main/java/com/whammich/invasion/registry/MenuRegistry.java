package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.nexus.NexusMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Reference.MODID);

    public static final RegistryObject<MenuType<NexusMenu>> NEXUS =
            MENUS.register("nexus", () -> IForgeMenuType.create(NexusMenu::new));

    private MenuRegistry() {
    }

    public static void register(IEventBus modBus) {
        MENUS.register(modBus);
    }
}
