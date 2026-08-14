package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAICircleTarget extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;
    private final double radius;
    private double angle;

    public EntityAICircleTarget(EntityIMFlying mob, double radius) {
        this.mob = mob;
        this.radius = radius;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null && mob.distanceToSqr(mob.getTarget()) < (radius + 8) * (radius + 8);
    }

    @Override
    public void start() {
        mob.setAIGoal(IMGoal.STAY_AT_RANGE);
        angle = mob.getRandom().nextDouble() * Math.PI * 2;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        angle += 0.05;
        double x = t.getX() + Math.cos(angle) * radius;
        double z = t.getZ() + Math.sin(angle) * radius;
        double y = t.getY() + 3.0;
        Vec3 dir = new Vec3(x, y, z).subtract(mob.position()).normalize().scale(0.35);
        mob.setDeltaMovement(dir);
    }
}
