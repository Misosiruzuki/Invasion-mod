package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Reference.MODID);

    public static final RegistryObject<EntityType<EntityIMZombie>> ZOMBIE = ENTITIES.register(
            "im_zombie",
            () -> EntityType.Builder.of(EntityIMZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F).clientTrackingRange(8).build("im_zombie"));

    public static final RegistryObject<EntityType<EntityIMZombiePigman>> ZOMBIE_PIGMAN = ENTITIES.register(
            "im_zombie_pigman",
            () -> EntityType.Builder.of(EntityIMZombiePigman::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F).clientTrackingRange(8).build("im_zombie_pigman"));

    public static final RegistryObject<EntityType<EntityIMSpider>> SPIDER = ENTITIES.register(
            "im_spider",
            () -> EntityType.Builder.of(EntityIMSpider::new, MobCategory.MONSTER)
                    .sized(1.4F, 0.9F).clientTrackingRange(8).build("im_spider"));

    public static final RegistryObject<EntityType<EntityIMSkeleton>> SKELETON = ENTITIES.register(
            "im_skeleton",
            () -> EntityType.Builder.of(EntityIMSkeleton::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F).clientTrackingRange(8).build("im_skeleton"));

    public static final RegistryObject<EntityType<EntityIMCreeper>> CREEPER = ENTITIES.register(
            "im_creeper",
            () -> EntityType.Builder.of(EntityIMCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F).clientTrackingRange(8).build("im_creeper"));

    public static final RegistryObject<EntityType<EntityIMImp>> IMP = ENTITIES.register(
            "im_imp",
            () -> EntityType.Builder.of(EntityIMImp::new, MobCategory.MONSTER)
                    .sized(0.5F, 1.2F).clientTrackingRange(8).build("im_imp"));

    public static final RegistryObject<EntityType<EntityIMWolf>> WOLF = ENTITIES.register(
            "im_wolf",
            () -> EntityType.Builder.of(EntityIMWolf::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.85F).clientTrackingRange(8).build("im_wolf"));

    public static final RegistryObject<EntityType<EntityIMPigEngy>> PIG_ENGINEER = ENTITIES.register(
            "im_pig_engineer",
            () -> EntityType.Builder.of(EntityIMPigEngy::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F).clientTrackingRange(8).build("im_pig_engineer"));

    public static final RegistryObject<EntityType<EntityIMThrower>> THROWER = ENTITIES.register(
            "im_thrower",
            () -> EntityType.Builder.of(EntityIMThrower::new, MobCategory.MONSTER)
                    .sized(1.2F, 2.4F).clientTrackingRange(10).build("im_thrower"));

    public static final RegistryObject<EntityType<EntityIMBurrower>> BURROWER = ENTITIES.register(
            "im_burrower",
            () -> EntityType.Builder.of(EntityIMBurrower::new, MobCategory.MONSTER)
                    .sized(1.5F, 1.0F).clientTrackingRange(10).build("im_burrower"));

    public static final RegistryObject<EntityType<EntityIMBird>> BIRD = ENTITIES.register(
            "im_bird",
            () -> EntityType.Builder.of(EntityIMBird::new, MobCategory.MONSTER)
                    .sized(0.8F, 0.8F).clientTrackingRange(10).build("im_bird"));

    public static final RegistryObject<EntityType<EntityIMGiantBird>> GIANT_BIRD = ENTITIES.register(
            "im_giant_bird",
            () -> EntityType.Builder.of(EntityIMGiantBird::new, MobCategory.MONSTER)
                    .sized(2.0F, 1.5F).clientTrackingRange(12).build("im_giant_bird"));

    public static final RegistryObject<EntityType<EntityIMEgg>> EGG = ENTITIES.register(
            "im_egg",
            () -> EntityType.Builder.of(EntityIMEgg::new, MobCategory.MISC)
                    .sized(0.6F, 0.6F).clientTrackingRange(6).build("im_egg"));

    public static final RegistryObject<EntityType<EntityIMBoulder>> BOULDER = ENTITIES.register(
            "im_boulder",
            () -> EntityType.Builder.<EntityIMBoulder>of(EntityIMBoulder::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F).clientTrackingRange(6).build("im_boulder"));

    public static final RegistryObject<EntityType<EntityIMBolt>> BOLT = ENTITIES.register(
            "im_bolt",
            () -> EntityType.Builder.<EntityIMBolt>of(EntityIMBolt::new, MobCategory.MISC)
                    .sized(0.3F, 0.3F).clientTrackingRange(6).build("im_bolt"));

    public static final RegistryObject<EntityType<EntityIMTrap>> TRAP = ENTITIES.register(
            "im_trap",
            () -> EntityType.Builder.of(EntityIMTrap::new, MobCategory.MISC)
                    .sized(0.8F, 0.2F).clientTrackingRange(6).build("im_trap"));

    public static final RegistryObject<EntityType<EntityIMPrimedTNT>> PRIMED_TNT = ENTITIES.register(
            "im_primed_tnt",
            () -> EntityType.Builder.of(EntityIMPrimedTNT::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F).clientTrackingRange(6).build("im_primed_tnt"));

    public static final RegistryObject<EntityType<EntitySFX>> SFX = ENTITIES.register(
            "im_sfx",
            () -> EntityType.Builder.of(EntitySFX::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F).clientTrackingRange(4).build("im_sfx"));

    public static final RegistryObject<EntityType<EntityIMSpawnProxy>> SPAWN_PROXY = ENTITIES.register(
            "im_spawn_proxy",
            () -> EntityType.Builder.of(EntityIMSpawnProxy::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F).clientTrackingRange(4).build("im_spawn_proxy"));

    private EntityRegistry() {}

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
