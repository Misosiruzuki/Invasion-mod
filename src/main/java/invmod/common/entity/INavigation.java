/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import net.minecraft.entity.Entity;

public interface INavigation
extends INotifyTask {
    public PathAction getCurrentWorkingAction();

    public void setSpeed(float var1);

    public Path getPathToXYZ(double var1, double var3, double var5, float var7);

    public boolean tryMoveToXYZ(double var1, double var3, double var5, float var7, float var8);

    public Path getPathTowardsXZ(double var1, double var3, int var5, int var6, int var7);

    public boolean tryMoveTowardsXZ(double var1, double var3, int var5, int var6, int var7, float var8);

    public Path getPathToEntity(Entity var1, float var2);

    public boolean tryMoveToEntity(Entity var1, float var2, float var3);

    public void autoPathToEntity(Entity var1);

    public boolean setPath(Path var1, float var2);

    public boolean isWaitingForTask();

    public Path getPath();

    public void onUpdateNavigation();

    public int getLastActionResult();

    public boolean noPath();

    public int getStuckTime();

    public float getLastPathDistanceToTarget();

    public void clearPath();

    public void haltForTick();

    public Entity getTargetEntity();

    public String getStatus();
}

