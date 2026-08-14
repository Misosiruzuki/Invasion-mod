package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMCreeper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderIMCreeper extends HumanoidMobRenderer<EntityIMCreeper, HumanoidModel<EntityIMCreeper>> {
    public RenderIMCreeper(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMCreeper entity) {
        return InvasionTextures.CREEPER;
    }
}
