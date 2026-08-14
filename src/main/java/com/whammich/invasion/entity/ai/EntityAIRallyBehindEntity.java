package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.ILeader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class EntityAIRallyBehindEntity extends Goal {
    private final EntityIMLiving mob;
    private final double speed;
    private LivingEntity leader;

    public EntityAIRallyBehindEntity(EntityIMLiving mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        AABB box = mob.getBoundingBox().inflate(16);
        List<LivingEntity> list = mob.level().getEntitiesOfClass(LivingEntity.class, box,
                e -> e instanceof ILeader l && l.isLeader() && e != mob);
        if (list.isEmpty()) {
            leader = null;
            return false;
        }
        leader = list.get(0);
        return mob.distanceToSqr(leader) > 9;
    }

    @Override
    public void tick() {
        if (leader != null) {
            mob.getNavigation().moveTo(leader, speed);
        }
    }
}
