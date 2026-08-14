package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.Goal;
import com.whammich.invasion.nexus.INexusAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/** Move toward the bound Nexus when goal is BREAK_NEXUS. */
public class EntityAIGoToNexus extends Goal {
    private final EntityIMMob mob;
    private int pathCooldown;
    private int failCount;

    public EntityAIGoToNexus(EntityIMMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getAIGoal() == Goal.BREAK_NEXUS && mob.getNexus() != null;
    }

    @Override
    public void start() {
        tryPath();
    }

    @Override
    public void tick() {
        if (pathCooldown > 0) {
            pathCooldown--;
        }
        if (mob.getNavigation().isDone() || pathCooldown <= 0) {
            tryPath();
        }
    }

    private void tryPath() {
        INexusAccess nexus = mob.getNexus();
        if (nexus == null) {
            return;
        }
        BlockPos pos = nexus.getBlockPosition();
        double dist = mob.findDistanceToNexus();
        boolean ok;
        if (dist > 1.5D * 1.5D) {
            ok = mob.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0D);
        } else {
            ok = true;
        }
        if (!ok) {
            failCount++;
            pathCooldown = 40 * Math.min(failCount, 5);
            double angle = mob.getRandom().nextDouble() * Math.PI * 2;
            double r = 4 + mob.getRandom().nextDouble() * 4;
            mob.getNavigation().moveTo(
                    pos.getX() + Math.cos(angle) * r,
                    pos.getY(),
                    pos.getZ() + Math.sin(angle) * r,
                    0.9D);
        } else {
            failCount = 0;
            pathCooldown = 20;
        }
    }
}
