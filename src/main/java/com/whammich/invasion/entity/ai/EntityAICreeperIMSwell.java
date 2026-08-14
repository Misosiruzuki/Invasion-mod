package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class EntityAICreeperIMSwell extends Goal {
    private final EntityIMCreeper creeper;
    private int swell;

    public EntityAICreeperIMSwell(EntityIMCreeper creeper) {
        this.creeper = creeper;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = creeper.getTarget();
        return t != null && creeper.distanceToSqr(t) < 9.0;
    }

    @Override
    public void start() {
        creeper.getNavigation().stop();
        swell = 0;
    }

    @Override
    public void stop() {
        swell = 0;
    }

    @Override
    public void tick() {
        LivingEntity t = creeper.getTarget();
        if (t == null || creeper.distanceToSqr(t) > 49.0) {
            swell = 0;
            return;
        }
        swell++;
        if (swell >= 30 && !creeper.level().isClientSide) {
            creeper.level().explode(creeper, creeper.getX(), creeper.getY(), creeper.getZ(),
                    3.0F + creeper.getTier() * 0.5F, Level.ExplosionInteraction.MOB);
            creeper.discard();
        }
    }
}
