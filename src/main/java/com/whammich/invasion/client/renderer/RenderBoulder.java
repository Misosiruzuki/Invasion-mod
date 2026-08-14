package com.whammich.invasion.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.whammich.invasion.client.model.ModelBoulder;
import com.whammich.invasion.entity.EntityIMBoulder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderBoulder extends EntityRenderer<EntityIMBoulder> {
    private final ModelBoulder<EntityIMBoulder> model;

    public RenderBoulder(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelBoulder<>(ctx.bakeLayer(ModelBoulder.LAYER_LOCATION));
        this.shadowRadius = 0.4F;
    }

    @Override
    public void render(EntityIMBoulder entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float spin = (entity.tickCount + partialTicks) * 0.4F;
        poseStack.translate(0.0D, 0.25D, 0.0D);
        poseStack.mulPose(Axis.XP.rotation(spin));
        poseStack.mulPose(Axis.YP.rotation(spin * 0.7F));
        var vc = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityIMBoulder entity) {
        return InvasionTextures.BOULDER;
    }
}
