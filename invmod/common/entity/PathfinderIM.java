/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.IntHashMap
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.entity.NodeContainer;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.IBlockAccess;

public class PathfinderIM {
    private static PathfinderIM pathfinder = new PathfinderIM();
    private IBlockAccess worldMap;
    private NodeContainer path = new NodeContainer();
    private IntHashMap pointMap = new IntHashMap();
    private PathNode[] pathOptions = new PathNode[32];
    private PathNode finalTarget;
    private float targetRadius;
    private int pathsIndex;
    private float searchRange;
    private int nodeLimit;
    private int nodesOpened;

    public static synchronized Path createPath(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth) {
        return pathfinder.createEntityPathTo(entity, x, y, z, x2, y2, z2, targetRadius, maxSearchRange, iblockaccess, searchDepth, quickFailDepth);
    }

    public Path createEntityPathTo(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth) {
        PathNode target;
        this.worldMap = iblockaccess;
        this.nodeLimit = searchDepth;
        this.nodesOpened = 1;
        this.searchRange = maxSearchRange;
        this.path.clearPath();
        this.pointMap.func_76046_c();
        PathNode start = this.openPoint(x, y, z);
        this.finalTarget = target = this.openPoint(x2, y2, z2);
        this.targetRadius = targetRadius;
        Path pathentity = this.addToPath(entity, start, target);
        return pathentity;
    }

    private Path addToPath(IPathfindable entity, PathNode start, PathNode target) {
        start.totalPathDistance = 0.0f;
        start.distanceToTarget = start.distanceToNext = start.distanceTo(target);
        this.path.clearPath();
        this.path.addPoint(start);
        PathNode previousPoint = start;
        boolean loops = false;
        long elapsed = 0L;
        while (!this.path.isPathEmpty()) {
            if (this.nodesOpened > this.nodeLimit) {
                return this.createEntityPath(start, previousPoint);
            }
            PathNode examiningPoint = this.path.dequeue();
            float distanceToTarget = examiningPoint.distanceTo(target);
            if (distanceToTarget < this.targetRadius + 0.1f) {
                return this.createEntityPath(start, examiningPoint);
            }
            if (distanceToTarget < previousPoint.distanceTo(target)) {
                previousPoint = examiningPoint;
            }
            examiningPoint.isFirst = true;
            int i = this.findPathOptions(entity, examiningPoint, target);
            for (int j = 0; j < i; ++j) {
                PathNode newPoint = this.pathOptions[j];
                float actualCost = examiningPoint.totalPathDistance + entity.getBlockPathCost(examiningPoint, newPoint, this.worldMap);
                if (newPoint.isAssigned() && !(actualCost < newPoint.totalPathDistance)) continue;
                newPoint.setPrevious(examiningPoint);
                newPoint.totalPathDistance = actualCost;
                newPoint.distanceToNext = this.estimateDistance(newPoint, target);
                if (newPoint.isAssigned()) {
                    this.path.changeDistance(newPoint, newPoint.totalPathDistance + newPoint.distanceToNext);
                    continue;
                }
                newPoint.distanceToTarget = newPoint.totalPathDistance + newPoint.distanceToNext;
                this.path.addPoint(newPoint);
            }
        }
        if (previousPoint == start) {
            return null;
        }
        return this.createEntityPath(start, previousPoint);
    }

    public void addNode(int x, int y, int z, PathAction action) {
        PathNode node = this.openPoint(x, y, z, action);
        if (node != null && !node.isFirst && node.distanceTo(this.finalTarget) < this.searchRange) {
            this.pathOptions[this.pathsIndex++] = node;
        }
    }

    private float estimateDistance(PathNode start, PathNode target) {
        return (float)(Math.abs(target.xCoord - start.xCoord) + Math.abs(target.yCoord - start.yCoord)) + (float)Math.abs(target.zCoord - start.zCoord) * 1.01f;
    }

    protected PathNode openPoint(int x, int y, int z) {
        return this.openPoint(x, y, z, PathAction.NONE);
    }

    protected PathNode openPoint(int x, int y, int z, PathAction action) {
        int hash = PathNode.makeHash(x, y, z, action);
        PathNode pathpoint = (PathNode)this.pointMap.func_76041_a(hash);
        if (pathpoint == null) {
            pathpoint = new PathNode(x, y, z, action);
            this.pointMap.func_76038_a(hash, (Object)pathpoint);
            ++this.nodesOpened;
        }
        return pathpoint;
    }

    private int findPathOptions(IPathfindable entity, PathNode pathpoint, PathNode target) {
        this.pathsIndex = 0;
        entity.getPathOptionsFromNode(this.worldMap, pathpoint, this);
        return this.pathsIndex;
    }

    private Path createEntityPath(PathNode pathpoint, PathNode pathpoint1) {
        int i = 1;
        PathNode pathpoint2 = pathpoint1;
        while (pathpoint2.getPrevious() != null) {
            ++i;
            pathpoint2 = pathpoint2.getPrevious();
        }
        PathNode[] apathpoint = new PathNode[i];
        PathNode pathpoint3 = pathpoint1;
        apathpoint[--i] = pathpoint3;
        while (pathpoint3.getPrevious() != null) {
            pathpoint3 = pathpoint3.getPrevious();
            apathpoint[--i] = pathpoint3;
        }
        return new Path(apathpoint, this.finalTarget);
    }
}

