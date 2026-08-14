package com.whammich.invasion.proxy;

import com.whammich.invasion.client.NexusScreen;
import com.whammich.invasion.client.model.ModelBoulder;
import com.whammich.invasion.client.renderer.*;
import com.whammich.invasion.entity.*;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Group 9 interim: dedicated renderers for registered types + generic humanoid for the rest.
 * Unique Model* classes exist locally and will replace placeholders as files finish syncing.
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

    private static <T extends Mob> HumanoidMobRenderer<T, HumanoidModel<T>> humanoid(
            EntityRendererProvider.Context ctx, ResourceLocation texture, float shadow) {
        return new HumanoidMobRenderer<>(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), shadow) {
            @Override
            public ResourceLocation getTextureLocation(T entity) {
                return texture;
            }
        };
    }

    private void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ZOMBIE.get(), RenderIMZombie::new);
        event.registerEntityRenderer(EntityRegistry.ZOMBIE_PIGMAN.get(), RenderIMZombiePigman::new);
        event.registerEntityRenderer(EntityRegistry.SKELETON.get(), RenderIMSkeleton::new);
        event.registerEntityRenderer(EntityRegistry.CREEPER.get(), RenderIMCreeper::new);
        event.registerEntityRenderer(EntityRegistry.WOLF.get(), RenderIMWolf::new);
        event.registerEntityRenderer(EntityRegistry.PIG_ENGINEER.get(), RenderPigEngy::new);
        event.registerEntityRenderer(EntityRegistry.SPIDER.get(), RenderSpiderIM::new);

        event.registerEntityRenderer(EntityRegistry.IMP.get(),
                ctx -> humanoid(ctx, InvasionTextures.IMP, 0.4F));
        event.registerEntityRenderer(EntityRegistry.THROWER.get(),
                ctx -> humanoid(ctx, InvasionTextures.THROWER_T1, 0.7F));
        event.registerEntityRenderer(EntityRegistry.BURROWER.get(),
                ctx -> humanoid(ctx, InvasionTextures.BURROWER, 0.8F));
        event.registerEntityRenderer(EntityRegistry.BIRD.get(),
                ctx -> humanoid(ctx, InvasionTextures.TEST, 0.4F));
        event.registerEntityRenderer(EntityRegistry.GIANT_BIRD.get(),
                ctx -> humanoid(ctx, InvasionTextures.TEST, 1.0F));

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
