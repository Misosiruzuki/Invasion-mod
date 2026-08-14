package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.IMGoal;
import com.whammich.invasion.nexus.INexusAccess;

/**
 * High-level coordinator: prefer nexus assault when bound, else hunt players.
 * Attach after goal registration or call from mob tick.
 */
public class AttackerAI {
    private final EntityIMMob mob;

    public AttackerAI(EntityIMMob mob) {
        this.mob = mob;
    }

    public void update() {
        INexusAccess nexus = mob.getNexus();
        if (nexus != null && nexus.isActivated() && nexus.getHp() > 0) {
            if (mob.getTarget() == null || mob.getTarget().distanceToSqr(mob) > 64) {
                mob.setAIGoal(IMGoal.BREAK_NEXUS);
            }
        } else if (mob.getTarget() != null) {
            mob.setAIGoal(IMGoal.MELEE_TARGET);
        } else {
            mob.setAIGoal(IMGoal.NONE);
        }
    }
}
