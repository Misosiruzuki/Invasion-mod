package com.whammich.invasion.proxy;

import com.whammich.invasion.client.NexusScreen;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.registry.MenuRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    private static final ResourceLocation ZOMBIE_TEX = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation SPIDER_TEX = new ResourceLocation("textures/entity/spider/spider.png");
    private static final ResourceLocation SKELETON_TEX = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation CREEPER_TEX = new ResourceLocation("textures/entity/creeper/creeper.png");
    private static final ResourceLocation WOLF_TEX = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation PIGLIN_TEX = new ResourceLocation("textures/entity/piglin/piglin.png");

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
        event.registerEntityRenderer(EntityRegistry.ZOMBIE.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.ZOMBIE_PIGMAN.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.SKELETON.get(), ctx -> humanoid(ctx, SKELETON_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.CREEPER.get(), ctx -> humanoid(ctx, CREEPER_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.IMP.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 0.4F));
        event.registerEntityRenderer(EntityRegistry.PIG_ENGINEER.get(), ctx -> humanoid(ctx, PIGLIN_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.THROWER.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 0.7F));
        event.registerEntityRenderer(EntityRegistry.SPIDER.get(), ctx -> humanoid(ctx, SPIDER_TEX, 0.65F));
        event.registerEntityRenderer(EntityRegistry.BURROWER.get(), ctx -> humanoid(ctx, SPIDER_TEX, 0.8F));
        event.registerEntityRenderer(EntityRegistry.WOLF.get(), ctx -> humanoid(ctx, WOLF_TEX, 0.5F));
        event.registerEntityRenderer(EntityRegistry.BIRD.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 0.4F));
        event.registerEntityRenderer(EntityRegistry.GIANT_BIRD.get(), ctx -> humanoid(ctx, ZOMBIE_TEX, 1.0F));
        event.registerEntityRenderer(EntityRegistry.BOULDER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EGG.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.TRAP.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.PRIMED_TNT.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SFX.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPAWN_PROXY.get(), NoopRenderer::new);
        LogHelper.debug("ClientProxy registered entity renderers (placeholder humanoid)");
    }

    private void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}
