package com.whammich.invasion;

import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Main entry point for Invasion on Forge 1.20.1.
 * Port skeleton — game systems are added item-by-item from the port checklist.
 */
@Mod(Reference.MODID)
public class InvasionMod {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

    /**
     * Creative tab (replaces CreativeTabInvasion / CreativeTabInvmod from 1.7.10).
     * Icon is temporary (obsidian) until Nexus block is ported.
     */
    public static final RegistryObject<CreativeModeTab> TAB_INVASION = CREATIVE_MODE_TABS.register(
            "invasion",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.invasion"))
                    .icon(() -> new ItemStack(Items.OBSIDIAN))
                    .displayItems((params, output) -> {
                        // Items/blocks added as they are ported
                    })
                    .build()
    );

    public InvasionMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        CREATIVE_MODE_TABS.register(modBus);
        modBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LogHelper.info("Invasion mod ({}) initializing for Forge 1.20.1", Reference.VERSION);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LogHelper.info("Invasion common setup complete");
    }
}
