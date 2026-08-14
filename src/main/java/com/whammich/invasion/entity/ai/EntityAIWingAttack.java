package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class EntityAIWingAttack extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;
    private int cooldown;

    public EntityAIWingAttack(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        LivingEntity t = mob.getTarget();
        return t != null && mob.distanceToSqr(t) < 9;
    }

    @Override
    public void start() {
        LivingEntity t = mob.getTarget();
        if (t != null) {
            mob.doHurtTarget(t);
            cooldown = 25;
        }
    }
}
