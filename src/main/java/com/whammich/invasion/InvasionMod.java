package com.whammich.invasion;

import com.whammich.invasion.proxy.CommonProxy;
import com.whammich.invasion.proxy.ProxyInit;
import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(Reference.MODID)
public class InvasionMod {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_INVASION = CREATIVE_MODE_TABS.register(
            "invasion",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.invasion"))
                    .icon(() -> new ItemStack(ItemRegistry.NEXUS_CATALYST_STABLE.get()))
                    .displayItems((params, output) -> {
                        ItemRegistry.ITEMS.getEntries().forEach(ro -> output.accept(ro.get()));
                    })
                    .build()
    );

    public static final CommonProxy PROXY = ProxyInit.create();

    public InvasionMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        ItemRegistry.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        PROXY.register(modBus);
        modBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LogHelper.info("Invasion mod ({}) initializing for Forge 1.20.1", Reference.VERSION);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LogHelper.info("Invasion common setup complete (logging={})",
                ConfigHandler.COMMON.enableLogging.get());
    }
}
