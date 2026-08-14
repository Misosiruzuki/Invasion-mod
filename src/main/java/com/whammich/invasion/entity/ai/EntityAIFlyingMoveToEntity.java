package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIFlyingMoveToEntity extends Goal {
    private final EntityIMFlying mob;
    private final double speed;

    public EntityAIFlyingMoveToEntity(EntityIMFlying mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null && mob.getTarget().isAlive();
    }

    @Override
    public void start() {
        mob.setFlyState(FlyState.FLYING);
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        Vec3 dir = t.position().add(0, 1.5, 0).subtract(mob.position()).normalize().scale(speed);
        mob.setDeltaMovement(dir);
        mob.getLookControl().setLookAt(t, 30, 30);
    }
}
