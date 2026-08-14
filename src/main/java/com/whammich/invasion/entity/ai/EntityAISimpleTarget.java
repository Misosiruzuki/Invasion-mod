package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMLiving;
import com.whammich.invasion.entity.IMGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class EntityAISimpleTarget extends NearestAttackableTargetGoal<Player> {
    private final EntityIMLiving imMob;

    public EntityAISimpleTarget(EntityIMLiving mob, int interval) {
        super(mob, Player.class, interval, true, false, null);
        this.imMob = mob;
    }

    @Override
    public void start() {
        super.start();
        LivingEntity t = imMob.getTarget();
        if (t != null) {
            imMob.setAIGoal(IMGoal.TARGET_ENTITY);
        }
    }
}
