package com.whammich.invasion.entity.ai;

import com.whammich.invasion.entity.EntityIMEgg;
import com.whammich.invasion.entity.EntityIMSpider;
import com.whammich.invasion.registry.EntityRegistry;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EntityAILayEgg extends Goal {
    private final EntityIMSpider spider;
    private int cooldown;

    public EntityAILayEgg(EntityIMSpider spider) {
        this.spider = spider;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        return spider.getTier() >= 2 && spider.getTarget() == null && spider.onGround();
    }

    @Override
    public void start() {
        if (spider.level().isClientSide) return;
        EntityIMEgg egg = EntityRegistry.EGG.get().create(spider.level());
        if (egg != null) {
            egg.moveTo(spider.getX(), spider.getY(), spider.getZ(), 0, 0);
            spider.level().addFreshEntity(egg);
        }
        spider.spawnOffspring();
        cooldown = 400;
    }
}
