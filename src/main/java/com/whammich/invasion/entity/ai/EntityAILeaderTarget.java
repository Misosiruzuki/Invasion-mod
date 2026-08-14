package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.ILeader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class EntityAILeaderTarget extends TargetGoal {
    private final EntityIMLiving mob;
    private LivingEntity leaderTarget;

    public EntityAILeaderTarget(EntityIMLiving mob) {
        super(mob, false);
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        AABB box = mob.getBoundingBox().inflate(24);
        List<LivingEntity> leaders = mob.level().getEntitiesOfClass(LivingEntity.class, box,
                e -> e instanceof ILeader l && l.isLeader());
        for (LivingEntity leader : leaders) {
            LivingEntity t = leader.getLastHurtMob();
            if (t == null && leader instanceof EntityIMLiving im) {
                t = im.getTarget();
            }
            if (t != null && t.isAlive()) {
                this.leaderTarget = t;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        mob.setTarget(this.leaderTarget);
        super.start();
    }
}
