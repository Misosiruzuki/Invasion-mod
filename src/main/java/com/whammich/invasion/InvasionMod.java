package com.whammich.invasion;

import com.whammich.invasion.proxy.CommonProxy;
import com.whammich.invasion.proxy.ProxyInit;
import com.whammich.invasion.registry.BlockEntityRegistry;
import com.whammich.invasion.registry.BlockRegistry;
import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.command.InvasionCommand;
import com.whammich.invasion.network.InvasionNetwork;
import com.whammich.invasion.util.LogHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.whammich.invasion.item.ItemProbe;
import net.minecraft.world.InteractionResult;
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
                    .icon(() -> new ItemStack(BlockRegistry.NEXUS.get()))
                    .displayItems((params, output) -> {
                        // Nexus first, then remaining items in registry order
                        output.accept(BlockRegistry.NEXUS.get());
                        ItemRegistry.ITEMS.getEntries().forEach(ro -> {
                            if (ro.get() != BlockRegistry.NEXUS.get().asItem()) {
                                output.accept(ro.get());
                            }
                        });
                    })
                    .build()
    );

    public static final CommonProxy PROXY = ProxyInit.create();

    public InvasionMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        BlockRegistry.register(modBus);
        BlockEntityRegistry.register(modBus);
        ItemRegistry.register(modBus);
        MenuRegistry.register(modBus);
        EntityRegistry.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        PROXY.register(modBus);
        modBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LogHelper.info("Invasion mod ({}) initializing for Forge 1.20.1", Reference.VERSION);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(InvasionNetwork::register);
        LogHelper.info("Invasion common setup complete (logging={}, network=ready)",
                ConfigHandler.COMMON.enableLogging.get());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        InvasionCommand.register(event.getDispatcher());
    }

    /** 1.7 onItemUseFirst parity: Adjuster/Probe before Nexus GUI opens. */
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (ItemProbe.handleRightClickBlock(event.getEntity(), event.getLevel(), event.getPos(), event.getItemStack())) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
        }
    }

}
