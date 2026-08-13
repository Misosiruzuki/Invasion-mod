package com.whammich.invasion.proxy;

import com.whammich.invasion.client.NexusScreen;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void register(IEventBus modBus) {
        super.register(modBus);
        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onRegisterRenderers);
        modBus.addListener(this::onRegisterLayerDefinitions);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        LogHelper.debug("ClientProxy client setup");
        event.enqueueWork(() -> {
            MenuScreens.register(MenuRegistry.NEXUS.get(), NexusScreen::new);
        });
    }

    private void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        LogHelper.debug("ClientProxy register entity renderers (none yet)");
    }

    private void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}
