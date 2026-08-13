package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.INexusAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityIMMob extends EntityIMLiving {
    protected EntityIMMob(EntityType<? extends EntityIMMob> type, Level level) {
        super(type, level);
    }

    protected EntityIMMob(EntityType<? extends EntityIMMob> type, Level level, INexusAccess nexus) {
        super(type, level, nexus);
    }
}
