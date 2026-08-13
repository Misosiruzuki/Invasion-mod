package com.whammich.invasion.proxy;

import com.whammich.invasion.util.LogHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Common (both sides) initialization hooks.
 * Replaces 1.7.10 CommonProxy; no SidedProxy on 1.20.1.
 */
public class CommonProxy {

    /**
     * Register listeners that run on both client and dedicated server.
     */
    public void register(IEventBus modBus) {
        modBus.addListener(this::onCommonSetup);
    }

    protected void onCommonSetup(final FMLCommonSetupEvent event) {
        LogHelper.debug("CommonProxy common setup");
        event.enqueueWork(this::onCommonSetupEnqueue);
    }

    /**
     * Work that must run on the main thread after common setup.
     */
    protected void onCommonSetupEnqueue() {
        // Recipes, dispensers, etc. will be registered here as systems are ported.
    }
}
