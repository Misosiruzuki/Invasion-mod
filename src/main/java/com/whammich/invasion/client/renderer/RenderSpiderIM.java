package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMSpider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderSpiderIM extends HumanoidMobRenderer<EntityIMSpider, HumanoidModel<EntityIMSpider>> {
    public RenderSpiderIM(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.65F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMSpider entity) {
        return InvasionTextures.SPIDER_T2;
    }
}
