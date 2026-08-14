package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMSkeleton;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderIMSkeleton extends HumanoidMobRenderer<EntityIMSkeleton, HumanoidModel<EntityIMSkeleton>> {
    public RenderIMSkeleton(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMSkeleton entity) {
        return InvasionTextures.SKELETON;
    }
}
