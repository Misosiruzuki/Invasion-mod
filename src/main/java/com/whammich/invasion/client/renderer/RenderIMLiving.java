package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public abstract class RenderIMLiving<T extends EntityIMLiving, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private final ResourceLocation texture;

    public RenderIMLiving(EntityRendererProvider.Context ctx, M model, float shadowRadius, ResourceLocation texture) {
        super(ctx, model, shadowRadius);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}
