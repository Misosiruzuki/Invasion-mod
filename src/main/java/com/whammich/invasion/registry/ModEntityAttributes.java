package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.entity.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {

    private ModEntityAttributes() {}

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.ZOMBIE.get(), EntityIMZombie.createAttributes().build());
        event.put(EntityRegistry.ZOMBIE_PIGMAN.get(), EntityIMZombiePigman.createAttributes().build());
        event.put(EntityRegistry.SPIDER.get(), EntityIMSpider.createAttributes().build());
        event.put(EntityRegistry.SKELETON.get(), EntityIMSkeleton.createAttributes().build());
        event.put(EntityRegistry.CREEPER.get(), EntityIMCreeper.createAttributes().build());
        event.put(EntityRegistry.IMP.get(), EntityIMImp.createAttributes().build());
        event.put(EntityRegistry.WOLF.get(), EntityIMWolf.createAttributes().build());
        event.put(EntityRegistry.PIG_ENGINEER.get(), EntityIMPigEngy.createAttributes().build());
        event.put(EntityRegistry.THROWER.get(), EntityIMThrower.createAttributes().build());
        event.put(EntityRegistry.BURROWER.get(), EntityIMBurrower.createAttributes().build());
        event.put(EntityRegistry.BIRD.get(), EntityIMBird.createAttributes().build());
        event.put(EntityRegistry.GIANT_BIRD.get(), EntityIMGiantBird.createAttributes().build());
    }
}
