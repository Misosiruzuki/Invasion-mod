package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIFlyingStrike extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;

    public EntityAIFlyingStrike(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && mob.distanceToSqr(t) < 25;
    }

    @Override
    public void start() {
        mob.setFlyState(FlyState.SWOOPING);
        mob.setAIGoal(IMGoal.FLYING_STRIKE);
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        Vec3 dir = t.position().subtract(mob.position()).normalize().scale(0.9);
        mob.setDeltaMovement(dir.x, dir.y - 0.1, dir.z);
        if (mob.distanceToSqr(t) < 4) {
            mob.doHurtTarget(t);
        }
    }

    @Override
    public void stop() {
        mob.setFlyState(FlyState.FLYING);
    }
}
