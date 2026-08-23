package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.IMGoal;
import com.whammich.invasion.nexus.INexusAccess;
import com.whammich.invasion.util.LogHelper;
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
        // E-04: non-diggers (canDig=false) still attack the nexus block itself
        return mob.findDistanceToNexus() < 3.5D * 3.5D;
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
            int dmg = Math.max(1, mob.getTier());
            int before = nexus.getHp();
            nexus.attackNexus(dmg);
            LogHelper.info("NexusAttack mob={} dig={} dmg={} hp {} -> {}",
                    mob.getType().getDescriptionId(),
                    mob.canDig(),
                    dmg,
                    before,
                    nexus.getHp());
            mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }
}
