package com.whammich.invasion.proxy;

import com.whammich.invasion.client.NexusScreen;
import com.whammich.invasion.client.model.ModelBoulder;
import com.whammich.invasion.client.renderer.*;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Interim ClientProxy: humanoid placeholders + Boulder custom model.
 * Full unique models (Imp/Thrower/Bird/...) land in follow-up commits as files sync.
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
        event.enqueueWork(() -> MenuScreens.register(MenuRegistry.NEXUS.get(), NexusScreen::new));
    }

    private void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelBoulder.LAYER_LOCATION, ModelBoulder::createBodyLayer);
        LogHelper.debug("ClientProxy registered model layer definitions (interim)");
    }

    private void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ZOMBIE.get(), RenderIMZombie::new);
        event.registerEntityRenderer(EntityRegistry.ZOMBIE_PIGMAN.get(), RenderIMZombiePigman::new);
        event.registerEntityRenderer(EntityRegistry.SKELETON.get(), RenderIMSkeleton::new);
        event.registerEntityRenderer(EntityRegistry.CREEPER.get(), RenderIMCreeper::new);
        event.registerEntityRenderer(EntityRegistry.WOLF.get(), RenderIMWolf::new);
        event.registerEntityRenderer(EntityRegistry.PIG_ENGINEER.get(), RenderPigEngy::new);
        event.registerEntityRenderer(EntityRegistry.SPIDER.get(), RenderSpiderIM::new);
        // Unique models pending full file sync — use humanoid placeholders for compile green
        event.registerEntityRenderer(EntityRegistry.IMP.get(), RenderIMZombie::new);
        event.registerEntityRenderer(EntityRegistry.THROWER.get(), RenderIMZombie::new);
        event.registerEntityRenderer(EntityRegistry.BURROWER.get(), RenderSpiderIM::new);
        event.registerEntityRenderer(EntityRegistry.BIRD.get(), RenderIMWolf::new);
        event.registerEntityRenderer(EntityRegistry.GIANT_BIRD.get(), RenderIMWolf::new);
        event.registerEntityRenderer(EntityRegistry.BOULDER.get(), RenderBoulder::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EGG.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.TRAP.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.PRIMED_TNT.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SFX.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPAWN_PROXY.get(), NoopRenderer::new);
        LogHelper.debug("ClientProxy registered entity renderers (group 9 interim)");
    }
}
