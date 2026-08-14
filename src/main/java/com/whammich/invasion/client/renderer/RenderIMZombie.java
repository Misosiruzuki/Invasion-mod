package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMZombie;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;

public class RenderIMZombie extends HumanoidMobRenderer<EntityIMZombie, HumanoidModel<EntityIMZombie>> {
    public RenderIMZombie(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(EntityIMZombie entity) {
        return InvasionTextures.ZOMBIE;
    }
}
