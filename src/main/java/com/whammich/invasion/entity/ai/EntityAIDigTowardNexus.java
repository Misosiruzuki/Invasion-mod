package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.IMGoal;
import com.whammich.invasion.nexus.INexusAccess;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

/**
 * Minimal dig goal (E-10+): when path to Nexus is blocked by soft blocks, break them.
 * Full 1.7 terrain pathfinding comes later (F-03).
 */
public class EntityAIDigTowardNexus extends Goal {
    private final EntityIMMob mob;
    private int digCooldown;
    private BlockPos digging;

    public EntityAIDigTowardNexus(EntityIMMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!mob.canDig() || mob.getAIGoal() != IMGoal.BREAK_NEXUS) {
            return false;
        }
        INexusAccess nexus = mob.getNexus();
        if (nexus == null || nexus.getHp() <= 0) {
            return false;
        }
        digging = findBlockToDig(nexus.getBlockPosition());
        return digging != null;
    }

    @Override
    public boolean canContinueToUse() {
        return digging != null && mob.canDig() && !mob.level().getBlockState(digging).isAir();
    }

    @Override
    public void tick() {
        if (digging == null) {
            return;
        }
        mob.getLookControl().setLookAt(digging.getX() + 0.5, digging.getY() + 0.5, digging.getZ() + 0.5);
        if (--digCooldown > 0) {
            return;
        }
        digCooldown = 15;
        Level level = mob.level();
        BlockState state = level.getBlockState(digging);
        if (state.isAir() || state.getDestroySpeed(level, digging) < 0) {
            digging = null;
            return;
        }
        // Soft / medium blocks only for this minimal goal
        float hardness = state.getDestroySpeed(level, digging);
        if (hardness > 5.0F) {
            digging = null;
            return;
        }
        level.destroyBlock(digging, true, mob);
        LogHelper.info("IMDig mob={} pos={} block={}",
                mob.getType().getDescriptionId(), digging, state.getBlock());
        if (mob instanceof com.whammich.invasion.entity.ICanDig digger) {
            digger.onBlockDigged(digging);
        }
        digging = null;
    }

    private BlockPos findBlockToDig(BlockPos nexus) {
        Level level = mob.level();
        BlockPos mobPos = mob.blockPosition();
        // Prefer blocks between mob and nexus (same Y or one step)
        int dx = Integer.signum(nexus.getX() - mobPos.getX());
        int dz = Integer.signum(nexus.getZ() - mobPos.getZ());
        BlockPos[] candidates = new BlockPos[]{
                mobPos.offset(dx, 0, dz),
                mobPos.offset(dx, 1, dz),
                mobPos.offset(dx, 0, 0),
                mobPos.offset(0, 0, dz),
                mobPos.above()
        };
        for (BlockPos p : candidates) {
            BlockState st = level.getBlockState(p);
            if (st.isAir() || st.is(Blocks.BEDROCK) || st.is(Blocks.OBSIDIAN)) {
                continue;
            }
            float h = st.getDestroySpeed(level, p);
            if (h >= 0 && h <= 5.0F && mob.distanceToSqr(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5) < 6.25) {
                return p;
            }
        }
        return null;
    }
}
