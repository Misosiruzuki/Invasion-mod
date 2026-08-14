package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMMob;
import com.whammich.invasion.entity.Goal;
import com.whammich.invasion.nexus.INexusAccess;

public class AttackerAI {
    private final EntityIMMob mob;

    public AttackerAI(EntityIMMob mob) {
        this.mob = mob;
    }

    public void update() {
        INexusAccess nexus = mob.getNexus();
        if (nexus != null && nexus.isActivated() && nexus.getHp() > 0) {
            if (mob.getTarget() == null || mob.getTarget().distanceToSqr(mob) > 64) {
                mob.setAIGoal(Goal.BREAK_NEXUS);
            }
        } else if (mob.getTarget() != null) {
            mob.setAIGoal(Goal.MELEE_TARGET);
        } else {
            mob.setAIGoal(Goal.NONE);
        }
    }
}
