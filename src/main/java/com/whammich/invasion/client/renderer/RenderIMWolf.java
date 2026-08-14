package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMWolf;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderIMWolf extends HumanoidMobRenderer<EntityIMWolf, HumanoidModel<EntityIMWolf>> {
    public RenderIMWolf(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMWolf entity) {
        return InvasionTextures.WOLF;
    }
}
