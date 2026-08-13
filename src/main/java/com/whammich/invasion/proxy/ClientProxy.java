package com.whammich.invasion.proxy;

import com.whammich.invasion.util.LogHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only initialization.
 * Replaces 1.7.10 ClientProxy (entity renderer registration, animation load, etc.).
 * Instantiated only on the physical client; do not reference from common code paths
 * that run on a dedicated server.
 */
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
        event.enqueueWork(this::onClientSetupEnqueue);
    }

    private void onClientSetupEnqueue() {
        // Key bindings, screen bindings, item property overrides — as features are ported.
    }

    private void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        // Entity renderer registration will be filled when entities are ported.
        LogHelper.debug("ClientProxy register entity renderers (none yet)");
    }

    private void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Model layer definitions will be filled when models are ported.
    }
}
