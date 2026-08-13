package com.whammich.invasion.wave;

import com.whammich.invasion.util.LogHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/** Turns EntityConstruct into a live Entity. Empty until group 6/7. */
public class MobBuilder {

    public Optional<Entity> createMob(ServerLevel level, EntityConstruct construct) {
        LogHelper.debug("MobBuilder: deferred spawn for {}", construct);
        return Optional.empty();
    }
}
