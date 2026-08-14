package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;

public class EntityAIMeleeFight extends EntityAIMeleeAttack {
    public EntityAIMeleeFight(EntityIMLiving mob, double speed, int attackInterval) {
        super(mob, speed, attackInterval);
    }
}
