package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAIMeleeAttack extends Goal {
    private final EntityIMLiving mob;
    private final double speed;
    private final int attackInterval;
    private int ticksUntilNextAttack;

    public EntityAIMeleeAttack(EntityIMLiving mob, double speed, int attackInterval) {
        this.mob = mob;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive()
                && (mob.getAIGoal() == IMGoal.MELEE_TARGET || mob.getAIGoal() == IMGoal.TARGET_ENTITY
                || mob.getAIGoal() == IMGoal.NONE || mob.getAIGoal() == IMGoal.BREAK_NEXUS);
    }

    @Override
    public void start() {
        mob.setAIGoal(IMGoal.MELEE_TARGET);
        ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        if (mob.getAIGoal() == IMGoal.MELEE_TARGET) {
            mob.setAIGoal(IMGoal.NONE);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return;
        }
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getNavigation().moveTo(target, speed);
        ticksUntilNextAttack = Math.max(0, ticksUntilNextAttack - 1);
        double reach = mob.getBbWidth() * 2.0F * mob.getBbWidth() * 2.0F + target.getBbWidth();
        if (mob.distanceToSqr(target) <= reach && ticksUntilNextAttack <= 0) {
            mob.doHurtTarget(target);
            ticksUntilNextAttack = attackInterval;
        }
    }
}
