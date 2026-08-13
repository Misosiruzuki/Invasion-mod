package com.whammich.invasion.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

public class NavigatorIM implements INavigation {
    protected final Mob mob;
    protected final PathNavigation vanilla;

    public NavigatorIM(Mob mob) {
        this.mob = mob;
        this.vanilla = mob.getNavigation();
    }

    @Override public boolean tryMoveTo(Entity target, double speed) { return vanilla.moveTo(target, speed); }
    @Override public boolean tryMoveTo(BlockPos pos, double speed) {
        return vanilla.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, speed);
    }
    @Override public void clearPath() { vanilla.stop(); }
    @Override public boolean isDone() { return vanilla.isDone(); }
    @Override public void tick() {}
    public Path getVanillaPath() { return vanilla.getPath(); }
    public PathNavigation getVanilla() { return vanilla; }
}
