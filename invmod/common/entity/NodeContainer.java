/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.entity.PathNode;

public class NodeContainer {
    private PathNode[] pathPoints = new PathNode[1024];
    private int count = 0;

    public PathNode addPoint(PathNode pathpoint) {
        if (pathpoint.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            PathNode[] apathpoint = new PathNode[this.count << 1];
            System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
            this.pathPoints = apathpoint;
        }
        this.pathPoints[this.count] = pathpoint;
        pathpoint.index = this.count;
        this.sortBack(this.count++);
        return pathpoint;
    }

    public void clearPath() {
        this.count = 0;
    }

    public PathNode dequeue() {
        PathNode pathpoint = this.pathPoints[0];
        this.pathPoints[0] = this.pathPoints[--this.count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }
        pathpoint.index = -1;
        return pathpoint;
    }

    public void changeDistance(PathNode pathpoint, float f) {
        float f1 = pathpoint.distanceToTarget;
        pathpoint.distanceToTarget = f;
        if (f < f1) {
            this.sortBack(pathpoint.index);
        } else {
            this.sortForward(pathpoint.index);
        }
    }

    private void sortBack(int i) {
        PathNode pathpoint = this.pathPoints[i];
        float f = pathpoint.distanceToTarget;
        while (i > 0) {
            int j = i - 1 >> 1;
            PathNode pathpoint1 = this.pathPoints[j];
            if (f >= pathpoint1.distanceToTarget) break;
            this.pathPoints[i] = pathpoint1;
            pathpoint1.index = i;
            i = j;
        }
        this.pathPoints[i] = pathpoint;
        pathpoint.index = i;
    }

    private void sortForward(int i) {
        PathNode pathpoint = this.pathPoints[i];
        float f = pathpoint.distanceToTarget;
        while (true) {
            float f2;
            PathNode pathpoint2;
            int j = 1 + (i << 1);
            int k = j + 1;
            if (j >= this.count) break;
            PathNode pathpoint1 = this.pathPoints[j];
            float f1 = pathpoint1.distanceToTarget;
            if (k >= this.count) {
                pathpoint2 = null;
                f2 = 1.0f;
            } else {
                pathpoint2 = this.pathPoints[k];
                f2 = pathpoint2.distanceToTarget;
            }
            if (f1 < f2) {
                if (f1 >= f) break;
                this.pathPoints[i] = pathpoint1;
                pathpoint1.index = i;
                i = j;
                continue;
            }
            if (f2 >= f || pathpoint2 == null) break;
            this.pathPoints[i] = pathpoint2;
            pathpoint2.index = i;
            i = k;
        }
        this.pathPoints[i] = pathpoint;
        pathpoint.index = i;
    }

    public boolean isPathEmpty() {
        return this.count == 0;
    }
}

