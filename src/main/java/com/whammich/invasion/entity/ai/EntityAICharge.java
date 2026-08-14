package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAICharge extends Goal {
    private final EntityIMLiving mob;
    private final double speed;
    private int chargeTicks;

    public EntityAICharge(EntityIMLiving mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive() && mob.distanceToSqr(t) < 16 * 16 && mob.distanceToSqr(t) > 4;
    }

    @Override
    public void start() {
        chargeTicks = 30;
        mob.setAIGoal(IMGoal.TARGET_ENTITY);
    }

    @Override
    public boolean canContinueToUse() {
        return chargeTicks > 0 && mob.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        chargeTicks--;
        mob.getLookControl().setLookAt(t, 30, 30);
        Vec3 dir = t.position().subtract(mob.position()).normalize().scale(speed * 1.5);
        mob.setDeltaMovement(dir.x, mob.getDeltaMovement().y, dir.z);
        if (mob.distanceToSqr(t) < 2.5) {
            mob.doHurtTarget(t);
            chargeTicks = 0;
        }
    }
}
