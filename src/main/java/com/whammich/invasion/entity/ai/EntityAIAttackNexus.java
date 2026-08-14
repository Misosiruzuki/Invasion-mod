package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.IMGoal;
import com.whammich.invasion.nexus.INexusAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/** Deal damage to the Nexus when adjacent. */
public class EntityAIAttackNexus extends Goal {
    private final EntityIMMob mob;
    private int attackCooldown;

    public EntityAIAttackNexus(EntityIMMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (mob.getAIGoal() != IMGoal.BREAK_NEXUS) {
            return false;
        }
        INexusAccess nexus = mob.getNexus();
        if (nexus == null || nexus.getHp() <= 0) {
            return false;
        }
        return mob.findDistanceToNexus() < 4.0D * 4.0D;
    }

    @Override
    public void tick() {
        INexusAccess nexus = mob.getNexus();
        if (nexus == null) {
            return;
        }
        BlockPos pos = nexus.getBlockPosition();
        mob.getLookControl().setLookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (--attackCooldown <= 0) {
            attackCooldown = 20;
            nexus.attackNexus(Math.max(1, mob.getTier()));
            mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }
}
