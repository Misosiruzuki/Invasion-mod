package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface INavigation {
    boolean tryMoveTo(Entity target, double speed);
    boolean tryMoveTo(BlockPos pos, double speed);
    void clearPath();
    boolean isDone();
    void tick();
}
