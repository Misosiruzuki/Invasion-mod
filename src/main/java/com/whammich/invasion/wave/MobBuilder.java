package com.whammich.invasion.wave;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import com.whammich.invasion.registry.EntityRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public class MobBuilder {

    public Optional<Entity> createMob(ServerLevel level, EntityConstruct construct) {
        EntityType<?> type = switch (construct.getType()) {
            case ZOMBIE -> EntityRegistry.ZOMBIE.get();
            case ZOMBIE_PIGMAN -> EntityRegistry.ZOMBIE_PIGMAN.get();
            case SPIDER -> EntityRegistry.SPIDER.get();
            case SKELETON -> EntityRegistry.SKELETON.get();
            case CREEPER -> EntityRegistry.CREEPER.get();
            case IMP -> EntityRegistry.IMP.get();
            case WOLF -> EntityRegistry.WOLF.get();
            case PIG_ENGINEER -> EntityRegistry.PIG_ENGINEER.get();
            case THROWER -> EntityRegistry.THROWER.get();
            case BURROWER -> EntityRegistry.BURROWER.get();
            case GIANT_BIRD -> EntityRegistry.GIANT_BIRD.get();
            case EGG -> EntityRegistry.EGG.get();
            default -> EntityRegistry.ZOMBIE.get();
        };
        Entity entity = type.create(level);
        if (entity == null) {
            LogHelper.warn("Failed to create entity for {}", construct);
            return Optional.empty();
        }
        if (entity instanceof EntityIMLiving living) {
            living.setTier(Math.max(1, construct.getTier()));
            living.setAIGoal(IMGoal.BREAK_NEXUS);
        }
        return Optional.of(entity);
    }
}
