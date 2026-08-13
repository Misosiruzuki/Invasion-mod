package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.INexusAccess;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityIMFlying extends EntityIMLiving {
    private FlyState flyState = FlyState.GROUNDED;
    private NavigatorFlying flyingNav;

    protected EntityIMFlying(EntityType<? extends EntityIMFlying> type, Level level) {
        super(type, level);
        this.flyingNav = new NavigatorFlying(this);
        this.moveControl = new IMMoveHelperFlying(this);
    }

    protected EntityIMFlying(EntityType<? extends EntityIMFlying> type, Level level, INexusAccess nexus) {
        super(type, level, nexus);
        this.flyingNav = new NavigatorFlying(this);
        this.moveControl = new IMMoveHelperFlying(this);
    }

    public FlyState getFlyState() { return flyState; }

    public void setFlyState(FlyState flyState) {
        this.flyState = flyState;
        flyingNav.setFlying(flyState != FlyState.GROUNDED && flyState != FlyState.LANDING);
    }

    public NavigatorFlying getFlyingNavigator() { return flyingNav; }
}
