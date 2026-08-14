package com.whammich.invasion.proxy;

import com.whammich.invasion.client.NexusScreen;
import com.whammich.invasion.client.model.*;
import com.whammich.invasion.client.renderer.*;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
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
        event.enqueueWork(() -> MenuScreens.register(MenuRegistry.NEXUS.get(), NexusScreen::new));
    }

    private void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelBoulder.LAYER_LOCATION, ModelBoulder::createBodyLayer);
        event.registerLayerDefinition(ModelEgg.LAYER_LOCATION, ModelEgg::createBodyLayer);
        event.registerLayerDefinition(ModelTrap.LAYER_LOCATION, ModelTrap::createBodyLayer);
        event.registerLayerDefinition(ModelImp.LAYER_LOCATION, ModelImp::createBodyLayer);
        event.registerLayerDefinition(ModelThrower.LAYER_LOCATION, ModelThrower::createBodyLayer);
        event.registerLayerDefinition(ModelBurrower.LAYER_LOCATION, ModelBurrower::createBodyLayer);
        event.registerLayerDefinition(ModelBurrower2.LAYER_LOCATION, ModelBurrower::createBodyLayer);
        event.registerLayerDefinition(ModelBird.LAYER_LOCATION, ModelBird::createBodyLayer);
        event.registerLayerDefinition(ModelGiantBird.LAYER_LOCATION, ModelGiantBird::createBodyLayer);
        event.registerLayerDefinition(ModelVulture.LAYER_LOCATION, ModelGiantBird::createBodyLayer);
        event.registerLayerDefinition(ModelQuetzalcoatlus.LAYER_LOCATION, ModelGiantBird::createBodyLayer);
        event.registerLayerDefinition(ModelB.LAYER_LOCATION, ModelB::createBodyLayer);
        event.registerLayerDefinition(ModelBigBiped.LAYER_LOCATION, ModelBigBiped::createBodyLayer);
        event.registerLayerDefinition(ModelTest.LAYER_LOCATION, ModelB::createBodyLayer);
        event.registerLayerDefinition(ModelIMSkeleton.LAYER_LOCATION, ModelIMSkeleton::createBodyLayer);
        LogHelper.debug("ClientProxy registered model layer definitions");
    }

    private void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ZOMBIE.get(), RenderIMZombie::new);
        event.registerEntityRenderer(EntityRegistry.ZOMBIE_PIGMAN.get(), RenderIMZombiePigman::new);
        event.registerEntityRenderer(EntityRegistry.SKELETON.get(), RenderIMSkeleton::new);
        event.registerEntityRenderer(EntityRegistry.CREEPER.get(), RenderIMCreeper::new);
        event.registerEntityRenderer(EntityRegistry.IMP.get(), RenderImp::new);
        event.registerEntityRenderer(EntityRegistry.PIG_ENGINEER.get(), RenderPigEngy::new);
        event.registerEntityRenderer(EntityRegistry.THROWER.get(), RenderThrower::new);
        event.registerEntityRenderer(EntityRegistry.SPIDER.get(), RenderSpiderIM::new);
        event.registerEntityRenderer(EntityRegistry.BURROWER.get(), RenderBurrower::new);
        event.registerEntityRenderer(EntityRegistry.WOLF.get(), RenderIMWolf::new);
        event.registerEntityRenderer(EntityRegistry.BIRD.get(), RenderB::new);
        event.registerEntityRenderer(EntityRegistry.GIANT_BIRD.get(), RenderGiantBird::new);
        event.registerEntityRenderer(EntityRegistry.BOULDER.get(), RenderBoulder::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), RenderBolt::new);
        event.registerEntityRenderer(EntityRegistry.EGG.get(), RenderEgg::new);
        event.registerEntityRenderer(EntityRegistry.TRAP.get(), RenderTrap::new);
        event.registerEntityRenderer(EntityRegistry.PRIMED_TNT.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SFX.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPAWN_PROXY.get(), NoopRenderer::new);
        LogHelper.debug("ClientProxy registered entity renderers (group 9)");
    }
}
