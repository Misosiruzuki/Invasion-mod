package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Supplier;

public class EntityAIFollowEntity extends Goal {
    private final EntityIMLiving mob;
    private final Supplier<LivingEntity> leaderSupplier;
    private final double speed;
    private final float stopDistance;

    public EntityAIFollowEntity(EntityIMLiving mob, Supplier<LivingEntity> leaderSupplier, double speed, float stopDistance) {
        this.mob = mob;
        this.leaderSupplier = leaderSupplier;
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity leader = leaderSupplier.get();
        return leader != null && leader.isAlive() && mob.distanceToSqr(leader) > stopDistance * stopDistance;
    }

    @Override
    public void tick() {
        LivingEntity leader = leaderSupplier.get();
        if (leader != null) {
            mob.getNavigation().moveTo(leader, speed);
        }
    }
}
