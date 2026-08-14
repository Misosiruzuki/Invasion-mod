package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIPounce extends Goal {
    private final EntityIMLiving mob;
    private int cooldown;

    public EntityAIPounce(EntityIMLiving mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        LivingEntity t = mob.getTarget();
        return t != null && mob.onGround() && mob.distanceToSqr(t) < 36 && mob.distanceToSqr(t) > 4;
    }

    @Override
    public void start() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;
        Vec3 dir = t.position().subtract(mob.position()).normalize();
        mob.setDeltaMovement(dir.x * 0.8, 0.5, dir.z * 0.8);
        cooldown = 40;
    }
}
