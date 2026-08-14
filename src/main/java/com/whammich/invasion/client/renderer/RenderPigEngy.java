package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMPigEngy;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderPigEngy extends HumanoidMobRenderer<EntityIMPigEngy, HumanoidModel<EntityIMPigEngy>> {
    public RenderPigEngy(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMPigEngy entity) {
        return InvasionTextures.PIG_ENGY;
    }
}
