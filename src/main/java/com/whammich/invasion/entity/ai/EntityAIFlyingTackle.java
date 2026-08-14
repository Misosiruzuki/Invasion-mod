package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import com.whammich.invasion.entity.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIFlyingTackle extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;

    public EntityAIFlyingTackle(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && mob.distanceToSqr(t) < 36;
    }

    @Override
    public void start() {
        mob.setFlyState(FlyState.SWOOPING);
        mob.setAIGoal(Goal.TACKLE_TARGET);
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        Vec3 dir = t.position().subtract(mob.position()).normalize().scale(1.1);
        mob.setDeltaMovement(dir);
        if (mob.distanceToSqr(t) < 3) {
            mob.doHurtTarget(t);
            mob.setDeltaMovement(mob.getDeltaMovement().scale(-0.5));
        }
    }
}
