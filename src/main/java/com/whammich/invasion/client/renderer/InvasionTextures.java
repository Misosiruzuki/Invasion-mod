package com.whammich.invasion.client.renderer;

import com.whammich.invasion.Reference;
import net.minecraft.resources.ResourceLocation;

/**
 * Texture ResourceLocations. Paths must be lowercase [a-z0-9/._-] only (1.20.1).
 */
public final class InvasionTextures {
    private InvasionTextures() {}

    public static ResourceLocation of(String path) {
        return new ResourceLocation(Reference.MODID, path);
    }

    /** Vanilla (minecraft) texture. */
    public static ResourceLocation vanilla(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public static final ResourceLocation IMP = of("textures/imp.png");
    public static final ResourceLocation THROWER_T1 = of("textures/throwert1.png");
    public static final ResourceLocation THROWER_T2 = of("textures/throwert2.png");
    public static final ResourceLocation BURROWER = of("textures/burrower.png");
    public static final ResourceLocation BOULDER = of("textures/boulder.png");
    public static final ResourceLocation EGG = of("textures/spideregg.png");
    public static final ResourceLocation TRAP = of("textures/trap.png");
    public static final ResourceLocation PIG_ENGY = of("textures/pigengt1.png");
    public static final ResourceLocation SPIDER_T2 = of("textures/spidert2.png");
    public static final ResourceLocation PIG_ZOMBIE = of("textures/pigzombie64x32.png");
    public static final ResourceLocation TEST = of("textures/testmodel.png");
    public static final ResourceLocation ZOMBIE = vanilla("textures/entity/zombie/zombie.png");
    public static final ResourceLocation SKELETON = vanilla("textures/entity/skeleton/skeleton.png");
    public static final ResourceLocation CREEPER = vanilla("textures/entity/creeper/creeper.png");
    public static final ResourceLocation SPIDER = vanilla("textures/entity/spider/spider.png");
    public static final ResourceLocation WOLF = vanilla("textures/entity/wolf/wolf.png");
}
