package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMThrower;

public class EntityAIThrowerKillEntity extends EntityAIMeleeAttack {
    public EntityAIThrowerKillEntity(EntityIMThrower thrower) {
        super(thrower, 0.9D, 25);
    }
}
