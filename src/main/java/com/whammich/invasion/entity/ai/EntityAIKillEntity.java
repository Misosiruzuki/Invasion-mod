package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;

public class EntityAIKillEntity extends EntityAIMeleeAttack {
    public EntityAIKillEntity(EntityIMLiving mob, double speed) {
        super(mob, speed, 20);
    }
}
