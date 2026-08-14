package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMBoulder;
import com.whammich.invasion.entity.EntityIMThrower;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAIRandomBoulder extends Goal {
    private final EntityIMThrower thrower;
    private int cooldown;

    public EntityAIRandomBoulder(EntityIMThrower thrower) {
        this.thrower = thrower;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        LivingEntity t = thrower.getTarget();
        return t != null && thrower.distanceToSqr(t) < 64 * 64 && thrower.distanceToSqr(t) > 9;
    }

    @Override
    public void start() {
        LivingEntity t = thrower.getTarget();
        if (t == null || thrower.level().isClientSide) return;
        EntityIMBoulder boulder = new EntityIMBoulder(thrower.level(), thrower);
        double dx = t.getX() - thrower.getX();
        double dy = t.getY(0.3) - boulder.getY();
        double dz = t.getZ() - thrower.getZ();
        double h = Math.sqrt(dx * dx + dz * dz);
        boulder.shoot(dx, dy + h * 0.2, dz, 1.1F, 4.0F);
        thrower.level().addFreshEntity(boulder);
        cooldown = 60;
    }
}
