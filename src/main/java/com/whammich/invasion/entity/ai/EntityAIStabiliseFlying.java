package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIStabiliseFlying extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;

    public EntityAIStabiliseFlying(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    @Override
    public boolean canUse() {
        return mob.getFlyState() == FlyState.FLYING || mob.getFlyState() == FlyState.TAKING_OFF;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        Vec3 v = mob.getDeltaMovement();
        mob.setDeltaMovement(v.x * 0.98, v.y * 0.9, v.z * 0.98);
        if (!mob.onGround() && v.y < -0.05) {
            mob.setDeltaMovement(v.x, Math.max(v.y, -0.3), v.z);
        }
    }
}
