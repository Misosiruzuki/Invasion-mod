/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Vec3
 */
package invmod.common.entity;

import invmod.common.entity.PathNode;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class Path {
    protected final PathNode[] points;
    private PathNode intendedTarget;
    private int pathLength;
    private int pathIndex;
    private float totalCost;

    public Path(PathNode[] apathpoint) {
        this.points = apathpoint;
        this.pathLength = apathpoint.length;
        if (apathpoint.length > 0) {
            this.intendedTarget = apathpoint[apathpoint.length - 1];
        }
    }

    public Path(PathNode[] apathpoint, PathNode intendedTarget) {
        this.points = apathpoint;
        this.pathLength = apathpoint.length;
        this.intendedTarget = intendedTarget;
    }

    public float getTotalPathCost() {
        return this.points[this.pathLength - 1].totalPathDistance;
    }

    public void incrementPathIndex() {
        ++this.pathIndex;
    }

    public boolean isFinished() {
        return this.pathIndex >= this.points.length;
    }

    public PathNode getFinalPathPoint() {
        if (this.pathLength > 0) {
            return this.points[this.pathLength - 1];
        }
        return null;
    }

    public PathNode getPathPointFromIndex(int par1) {
        return this.points[par1];
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public void setCurrentPathLength(int par1) {
        this.pathLength = par1;
    }

    public int getCurrentPathIndex() {
        return this.pathIndex;
    }

    public void setCurrentPathIndex(int par1) {
        this.pathIndex = par1;
    }

    public PathNode getIntendedTarget() {
        return this.intendedTarget;
    }

    public Vec3 getPositionAtIndex(Entity entity, int index) {
        double d = (double)this.points[index].xCoord + (double)((int)(entity.field_70130_N + 1.0f)) * 0.5;
        double d1 = this.points[index].yCoord;
        double d2 = (double)this.points[index].zCoord + (double)((int)(entity.field_70130_N + 1.0f)) * 0.5;
        return Vec3.func_72443_a((double)d, (double)d1, (double)d2);
    }

    public Vec3 getCurrentNodeVec3d(Entity entity) {
        return this.getPositionAtIndex(entity, this.pathIndex);
    }

    public Vec3 destination() {
        return Vec3.func_72443_a((double)this.points[this.points.length - 1].xCoord, (double)this.points[this.points.length - 1].yCoord, (double)this.points[this.points.length - 1].zCoord);
    }

    public boolean equalsPath(Path par1PathEntity) {
        if (par1PathEntity == null) {
            return false;
        }
        if (par1PathEntity.points.length != this.points.length) {
            return false;
        }
        for (int i = 0; i < this.points.length; ++i) {
            if (this.points[i].xCoord == par1PathEntity.points[i].xCoord && this.points[i].yCoord == par1PathEntity.points[i].yCoord && this.points[i].zCoord == par1PathEntity.points[i].zCoord) continue;
            return false;
        }
        return true;
    }

    public boolean isDestinationSame(Vec3 par1Vec3D) {
        PathNode pathpoint = this.getFinalPathPoint();
        if (pathpoint == null) {
            return false;
        }
        return pathpoint.xCoord == (int)par1Vec3D.field_72450_a && pathpoint.zCoord == (int)par1Vec3D.field_72449_c;
    }
}

