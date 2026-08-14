package com.whammich.invasion.client.renderer;

import com.whammich.invasion.Reference;
import net.minecraft.resources.ResourceLocation;

public final class InvasionTextures {
    private InvasionTextures() {}

    public static ResourceLocation of(String path) {
        return new ResourceLocation(Reference.MODID, path);
    }

    public static final ResourceLocation IMP = of("textures/imp.png");
    public static final ResourceLocation THROWER_T1 = of("textures/throwerT1.png");
    public static final ResourceLocation THROWER_T2 = of("textures/throwerT2.png");
    public static final ResourceLocation BURROWER = of("textures/burrower.png");
    public static final ResourceLocation BOULDER = of("textures/boulder.png");
    public static final ResourceLocation EGG = of("textures/spideregg.png");
    public static final ResourceLocation TRAP = of("textures/trap.png");
    public static final ResourceLocation PIG_ENGY = of("textures/pigengT1.png");
    public static final ResourceLocation SPIDER_T2 = of("textures/spiderT2.png");
    public static final ResourceLocation PIG_ZOMBIE = of("textures/pigzombie64x32.png");
    public static final ResourceLocation TEST = of("textures/testmodel.png");
    public static final ResourceLocation ZOMBIE = new ResourceLocation("textures/entity/zombie/zombie.png");
    public static final ResourceLocation SKELETON = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    public static final ResourceLocation CREEPER = new ResourceLocation("textures/entity/creeper/creeper.png");
    public static final ResourceLocation SPIDER = new ResourceLocation("textures/entity/spider/spider.png");
    public static final ResourceLocation WOLF = new ResourceLocation("textures/entity/wolf/wolf.png");
}
