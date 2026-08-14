package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/** Prefer players when nexus pathing is unavailable. */
public class EntityAITargetOnNoNexusPath extends NearestAttackableTargetGoal<Player> {
    private final EntityIMLiving imMob;

    public EntityAITargetOnNoNexusPath(EntityIMLiving mob) {
        super(mob, Player.class, true);
        this.imMob = mob;
    }

    @Override
    public boolean canUse() {
        if (imMob.getNexus() != null && imMob.getAIGoal() == IMGoal.BREAK_NEXUS
                && !imMob.getNavigation().isDone()) {
            return false;
        }
        return super.canUse();
    }
}
