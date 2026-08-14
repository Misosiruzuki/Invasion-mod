package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import com.whammich.invasion.entity.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAISwoop extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;
    private int ticks;

    public EntityAISwoop(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && mob.getY() > t.getY() + 2 && mob.distanceToSqr(t) < 100;
    }

    @Override
    public void start() {
        ticks = 25;
        mob.setFlyState(FlyState.SWOOPING);
        mob.setAIGoal(Goal.SWOOP);
    }

    @Override
    public boolean canContinueToUse() {
        return ticks > 0 && mob.getTarget() != null;
    }

    @Override
    public void tick() {
        ticks--;
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        Vec3 dir = t.position().subtract(mob.position()).normalize().scale(1.2);
        mob.setDeltaMovement(dir.x, Math.min(dir.y, -0.3), dir.z);
        if (mob.distanceToSqr(t) < 4) {
            mob.doHurtTarget(t);
            ticks = 0;
        }
    }

    @Override
    public void stop() {
        mob.setFlyState(FlyState.FLYING);
    }
}
