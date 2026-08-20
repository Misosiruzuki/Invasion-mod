/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package invmod.common.entity;

import invmod.common.entity.INavigation;
import net.minecraft.entity.Entity;

public interface INavigationFlying
extends INavigation {
    public void setMovementType(MoveType var1);

    public void setLandingPath();

    public void setCirclingPath(Entity var1, float var2, float var3);

    public void setCirclingPath(double var1, double var3, double var5, float var7, float var8);

    public float getDistanceToCirclingRadius();

    public boolean isCircling();

    public void setFlySpeed(float var1);

    public void setPitchBias(float var1, float var2);

    public void enableDirectTarget(boolean var1);

    public static enum MoveType {
        PREFER_WALKING,
        MIXED,
        PREFER_FLYING;

    }
}

