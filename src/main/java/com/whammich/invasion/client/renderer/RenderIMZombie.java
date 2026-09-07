package com.whammich.invasion.client.renderer;

import com.whammich.invasion.entity.EntityIMZombie;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Port of 1.7 {@code invmod.client.render.RenderIMZombie#getTexture}.
 * Uses HumanoidModel + ZOMBIE layer (ZombieModel requires T extends Zombie; EntityIMZombie is Monster).
 * Textures: textures/entity/zombiet1a.png etc.
 */
public class RenderIMZombie extends HumanoidMobRenderer<EntityIMZombie, HumanoidModel<EntityIMZombie>> {
    public RenderIMZombie(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityIMZombie entity) {
        return switch (entity.getTextureId()) {
            case 1 -> InvasionTextures.ZOMBIE_T1A;
            case 2 -> InvasionTextures.ZOMBIE_T2;
            case 3 -> InvasionTextures.PIG_ZOMBIE;
            case 4 -> InvasionTextures.ZOMBIE_T2A;
            case 5 -> InvasionTextures.ZOMBIE_TAR;
            case 6 -> InvasionTextures.ZOMBIE_T3;
            case 0 -> InvasionTextures.ZOMBIE_OLD;
            default -> InvasionTextures.ZOMBIE_T1A;
        };
    }
}
