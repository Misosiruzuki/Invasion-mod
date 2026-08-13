package com.whammich.invasion.entity;

import net.minecraft.world.entity.Mob;

public class NavigatorFlying extends NavigatorIM implements INavigationFlying {
    private boolean flying;
    public NavigatorFlying(Mob mob) { super(mob); }
    @Override public void setFlying(boolean flying) { this.flying = flying; }
    @Override public boolean isFlying() { return flying; }
}
