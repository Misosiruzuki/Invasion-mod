package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMFlying;
import com.whammich.invasion.entity.FlyState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIStoop extends net.minecraft.world.entity.ai.goal.Goal {
    private final EntityIMFlying mob;

    public EntityAIStoop(EntityIMFlying mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getFlyState() == FlyState.FLYING && mob.onGround();
    }

    @Override
    public void start() {
        mob.setFlyState(FlyState.LANDING);
        Vec3 v = mob.getDeltaMovement();
        mob.setDeltaMovement(v.x, -0.2, v.z);
    }

    @Override
    public void stop() {
        if (mob.onGround()) {
            mob.setFlyState(FlyState.GROUNDED);
        }
    }
}
