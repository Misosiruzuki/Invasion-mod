package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Arrow;

import java.util.EnumSet;

public class EntityAIKillWithArrow extends Goal {
    private final EntityIMLiving mob;
    private final double speed;
    private final int attackInterval;
    private int cooldown;

    public EntityAIKillWithArrow(EntityIMLiving mob, double speed, int attackInterval) {
        this.mob = mob;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;
        mob.setAIGoal(Goal.STAY_AT_RANGE);
        double dist = mob.distanceToSqr(target);
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (dist > 12 * 12) {
            mob.getNavigation().moveTo(target, speed);
        } else if (dist < 6 * 6) {
            mob.getMoveControl().strafe(-0.5F, 0);
        } else {
            mob.getNavigation().stop();
        }
        if (--cooldown <= 0 && dist < 20 * 20) {
            cooldown = attackInterval;
            Arrow arrow = new Arrow(mob.level(), mob);
            arrow.setPos(mob.getX(), mob.getEyeY() - 0.1, mob.getZ());
            double dx = target.getX() - mob.getX();
            double dy = target.getY(0.333) - arrow.getY();
            double dz = target.getZ() - mob.getZ();
            double horiz = Math.sqrt(dx * dx + dz * dz);
            arrow.shoot(dx, dy + horiz * 0.2, dz, 1.6F, 8.0F);
            mob.level().addFreshEntity(arrow);
            mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }
}
