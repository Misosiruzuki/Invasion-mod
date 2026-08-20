/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMBurrower;
import invmod.common.entity.IPathSource;
import invmod.common.entity.NavigatorParametric;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import invmod.common.util.PosRotate3D;

public class NavigatorBurrower
extends NavigatorParametric {
    protected PathNode nextNode;
    protected PathNode prevNode;
    protected PathNode[] prevSegmentNodes;
    protected PathNode[] activeSegmentNodes;
    protected PathNode[] nextSegmentNodes;
    protected int[] segmentPathIndices;
    protected int[] segmentTime;
    protected int[] segmentOffsets;
    protected float timePerTick = 0.05f;
    protected Path lastPath;
    protected boolean nodeChanged;

    public NavigatorBurrower(EntityIMBurrower entity, IPathSource pathSource, int segments, int offset) {
        super(entity, pathSource);
        this.prevSegmentNodes = new PathNode[segments];
        this.activeSegmentNodes = new PathNode[segments];
        this.nextSegmentNodes = new PathNode[segments];
        this.segmentPathIndices = new int[segments];
        this.segmentTime = new int[segments];
        this.segmentOffsets = new int[segments];
        this.nodeChanged = false;
        for (int i = 0; i < this.segmentOffsets.length; ++i) {
            this.segmentOffsets[i] = (i + 1) * offset;
        }
    }

    @Override
    protected PosRotate3D entityPositionAtParam(int param) {
        return this.calcAbsolutePositionAndRotation((float)param * this.timePerTick, this.prevNode, this.activeNode, this.nextNode);
    }

    protected PosRotate3D positionAtTime(int tick, PathNode start, PathNode middle, PathNode end) {
        PosRotate3D pos = this.calcPositionAndRotation((float)tick * this.timePerTick, start, middle, end);
        pos.setPosX(pos.getPosX() + (double)middle.xCoord);
        pos.setPosY(pos.getPosY() + (double)middle.yCoord);
        pos.setPosZ(pos.getPosZ() + (double)middle.zCoord);
        return pos;
    }

    @Override
    protected boolean isReadyForNextNode(int ticks) {
        return (double)((float)ticks * this.timePerTick) >= 1.0;
    }

    @Override
    protected void pathFollow(int time) {
        int nextFrontIndex = this.path.getCurrentPathIndex() + 2;
        if (this.isReadyForNextNode(time)) {
            if (nextFrontIndex < this.path.getCurrentPathLength()) {
                this.timeParam = 0;
                this.path.setCurrentPathIndex(nextFrontIndex - 1);
                this.prevNode = this.activeNode;
                this.activeNode = this.nextNode;
                this.nextNode = this.path.getPathPointFromIndex(nextFrontIndex);
                this.nodeChanged = true;
            }
        } else {
            this.timeParam = time;
        }
    }

    protected void doSegmentFollowTo(int ticks, int segmentIndex) {
        ticks += this.segmentOffsets[segmentIndex];
        while (ticks <= 0) {
            ticks += 20;
        }
        int nextFrontIndex = this.segmentPathIndices[segmentIndex] + 2;
        if (this.isReadyForNextNode(ticks)) {
            if (nextFrontIndex < this.path.getCurrentPathLength()) {
                this.segmentPathIndices[segmentIndex] = nextFrontIndex - 1;
                this.prevSegmentNodes[segmentIndex] = this.activeSegmentNodes[segmentIndex];
                this.activeSegmentNodes[segmentIndex] = this.nextSegmentNodes[segmentIndex];
                this.nextSegmentNodes[segmentIndex] = this.segmentPathIndices[segmentIndex] >= 0 ? this.path.getPathPointFromIndex(nextFrontIndex) : this.path.getPathPointFromIndex(0);
                this.segmentTime[segmentIndex] = 0;
            }
        } else {
            this.segmentTime[segmentIndex] = ticks;
        }
        if (this.segmentPathIndices[segmentIndex] >= 0) {
            PosRotate3D pos = this.positionAtTime(this.segmentTime[segmentIndex], this.prevSegmentNodes[segmentIndex], this.activeSegmentNodes[segmentIndex], this.nextSegmentNodes[segmentIndex]);
            ((EntityIMBurrower)this.theEntity).setSegment(segmentIndex, pos);
            if (this.segmentTime[segmentIndex] == 0) {
                ((EntityIMBurrower)this.theEntity).setSegment(segmentIndex, pos);
            }
        }
    }

    @Override
    protected void doMovementTo(int time) {
        PosRotate3D movePos = this.entityPositionAtParam(time);
        this.theEntity.func_70091_d(movePos.getPosX() - this.theEntity.field_70165_t, movePos.getPosY() - this.theEntity.field_70163_u, movePos.getPosZ() - this.theEntity.field_70161_v);
        ((EntityIMBurrower)this.theEntity).setHeadRotation(movePos);
        if (this.nodeChanged) {
            ((EntityIMBurrower)this.theEntity).setHeadRotation(movePos);
            this.nodeChanged = false;
        }
        if (Math.abs(this.theEntity.func_70092_e(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ())) < this.minMoveToleranceSq) {
            for (int segmentIndex = 0; segmentIndex < this.segmentPathIndices.length; ++segmentIndex) {
                this.doSegmentFollowTo(time, segmentIndex);
            }
            this.timeParam = time;
            --this.ticksStuck;
        } else {
            ++this.ticksStuck;
        }
    }

    @Override
    public boolean noPath() {
        return this.path == null || this.path.getCurrentPathIndex() >= this.path.getCurrentPathLength() - 2;
    }

    @Override
    public boolean setPath(Path newPath, float speed) {
        if (newPath == null || newPath.getCurrentPathLength() < 2) {
            this.path = null;
            return false;
        }
        if (this.path == null) {
            this.path = newPath;
            this.prevNode = this.activeNode = this.path.getPathPointFromIndex(0);
            this.nextNode = this.path.getPathPointFromIndex(1);
            if (this.activeNode.action != PathAction.NONE) {
                this.nodeActionFinished = false;
            }
            for (int i = 0; i < this.segmentPathIndices.length; ++i) {
                if (this.activeSegmentNodes[i] != null) continue;
                this.activeSegmentNodes[i] = this.activeNode;
                this.nextSegmentNodes[i] = this.activeNode;
                this.segmentPathIndices[i] = 0;
                this.segmentTime[i] = this.segmentOffsets[i];
                while (this.segmentTime[i] < 0) {
                    int n = i;
                    this.segmentTime[n] = this.segmentTime[n] + 20;
                    int n2 = i;
                    this.segmentPathIndices[n2] = this.segmentPathIndices[n2] - 1;
                }
            }
        }
        int mainIndex = this.path.getCurrentPathIndex();
        if (newPath.getPathPointFromIndex(0).equals(this.activeNode)) {
            if (this.segmentPathIndices.length > 0) {
                int lowestIndex = this.segmentPathIndices[this.segmentPathIndices.length - 1];
                if (lowestIndex < 0) {
                    lowestIndex = 0;
                }
                this.path = this.extendPath(this.path, newPath, lowestIndex, mainIndex);
                this.path.setCurrentPathIndex(mainIndex -= lowestIndex);
                this.nextNode = this.path.getPathPointFromIndex(mainIndex + 1);
                for (int i = 0; i < this.segmentPathIndices.length; ++i) {
                    int n = i;
                    this.segmentPathIndices[n] = this.segmentPathIndices[n] - lowestIndex;
                    if (this.segmentPathIndices[i] != mainIndex) continue;
                    this.nextSegmentNodes[i] = this.nextNode;
                }
            } else {
                this.path = newPath;
                this.path.setCurrentPathIndex(0);
                this.nextNode = this.path.getPathPointFromIndex(1);
            }
        } else {
            this.path = newPath;
            this.prevNode = this.activeNode = this.path.getPathPointFromIndex(0);
            this.nextNode = this.path.getPathPointFromIndex(1);
            if (this.activeNode.action != PathAction.NONE) {
                this.nodeActionFinished = false;
            }
            for (int i = 0; i < this.segmentPathIndices.length; ++i) {
                if (this.activeSegmentNodes[i] != null) continue;
                this.activeSegmentNodes[i] = this.activeNode;
                this.nextSegmentNodes[i] = this.activeNode;
                this.segmentPathIndices[i] = 0;
                this.segmentTime[i] = this.segmentOffsets[i];
                while (this.segmentTime[i] < 0) {
                    int n = i;
                    this.segmentTime[n] = this.segmentTime[n] + 20;
                    int n3 = i;
                    this.segmentPathIndices[n3] = this.segmentPathIndices[n3] - 1;
                }
            }
        }
        this.ticksStuck = 0;
        if (this.noSunPathfind) {
            this.removeSunnyPath();
        }
        return true;
    }

    private PosRotate3D calcAbsolutePositionAndRotation(float time, PathNode start, PathNode middle, PathNode end) {
        PosRotate3D pos = this.calcPositionAndRotation(time, start, middle, end);
        pos.setPosX(pos.getPosX() + (double)middle.xCoord);
        pos.setPosY(pos.getPosY() + (double)middle.yCoord);
        pos.setPosZ(pos.getPosZ() + (double)middle.zCoord);
        return pos;
    }

    private PosRotate3D calcPositionAndRotation(float time, PathNode start, PathNode middle, PathNode end) {
        int vX = end.xCoord - start.xCoord;
        int vY = end.yCoord - start.yCoord;
        int vZ = end.zCoord - start.zCoord;
        int hX = middle.xCoord != start.xCoord ? 1 : -1;
        int hY = middle.yCoord != start.yCoord ? 1 : -1;
        int hZ = middle.zCoord != start.zCoord ? 1 : -1;
        int gX = middle.xCoord != end.xCoord ? 1 : -1;
        int gY = middle.yCoord != end.yCoord ? 1 : -1;
        int gZ = middle.zCoord != end.zCoord ? 1 : -1;
        double xOffset = (double)vX * -0.5 * (double)hX;
        double yOffset = (double)vY * -0.5 * (double)hY;
        double zOffset = (double)vZ * -0.5 * (double)hZ;
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        float rotX = 0.0f;
        float rotY = 0.0f;
        float rotZ = 0.0f;
        if (hX == 1 && gX == 1) {
            posX = (double)(time * (float)vX) * 0.5 + (double)(vX <= 0 ? 1 : 0);
            posY = 0.5;
            posZ = 0.5;
            rotY = vX >= 1 ? 0.0f : 3.141593f;
            return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
        }
        if (hY == 1 && gY == 1) {
            posY = (double)(time * (float)vY) * 0.5 + (double)(vY <= 0 ? 1 : 0);
            posX = 0.5;
            posZ = 0.5;
            return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
        }
        if (hZ == 1 && gZ == 1) {
            posZ = (double)(time * (float)vZ) * 0.5 + (double)(vZ <= 0 ? 1 : 0);
            posY = 0.5;
            posX = 0.5;
            rotY = (float)vZ * 3.141593f / 4.0f;
            return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
        }
        posX = hX == 1 ? (double)(vX * hX) * Math.sin((double)time * 0.5 * Math.PI) * 0.5 + xOffset : (double)(vX * hX) * Math.cos((double)time * 0.5 * Math.PI) * 0.5 + xOffset;
        posY = hY == 1 ? (double)(vY * hY) * Math.sin((double)time * 0.5 * Math.PI) * 0.5 + yOffset : (double)(vY * hY) * Math.cos((double)time * 0.5 * Math.PI) * 0.5 + yOffset;
        posZ = hZ == 1 ? (double)(vZ * hZ) * Math.sin((double)time * 0.5 * Math.PI) * 0.5 + zOffset : (double)(vZ * hZ) * Math.cos((double)time * 0.5 * Math.PI) * 0.5 + zOffset;
        if (hX == 1) {
            float f = rotY = vX == 1 ? 0.0f : 180.0f;
            if (gZ == 1) {
                rotY += time * (float)vZ * (float)vX * 90.0f;
            } else if (gY == 1) {
                rotZ = time * (float)vY * 90.0f;
            }
        } else if (hY == 1) {
            if (gX == 1) {
                rotX = vX == 1 ? 0.0f : 180.0f;
                rotZ = (float)(90 * vY) + time * (float)vX * -90.0f;
            } else if (gZ == 1) {
                rotX = 90.0f;
                rotY = (float)vZ * (time * (float)vY * -90.0f);
                rotZ = -90.0f;
            }
        } else if (hZ == 1) {
            if (gX == 1) {
                rotY = (float)vZ * (90.0f + time * (float)vX * -90.0f);
            } else if (gY == 1) {
                rotX = 90.0f;
                rotY = (float)(-vZ) * (-90.0f + time * (float)vY * -90.0f);
                rotZ = -90.0f;
            }
        }
        return new PosRotate3D(posX += 0.5, posY += 0.5, posZ += 0.5, rotX /= 57.2958f, rotY /= 57.2958f, rotZ /= 57.2958f);
    }

    private PosRotate3D calcStraight(float time, PathNode start, PathNode end) {
        PosRotate3D segment = new PosRotate3D();
        segment.setPosX((double)start.xCoord + 0.5 + (double)(time * (float)(end.xCoord - start.xCoord)) * 0.5);
        segment.setPosY((double)start.yCoord + (double)(time * (float)(end.yCoord - start.yCoord)) * 0.5);
        segment.setPosZ((double)start.zCoord + 0.5 + (double)time * ((double)end.zCoord - (double)start.zCoord * 0.5));
        return segment;
    }

    private Path extendPath(Path path1, Path path2, int lowerBoundP1, int upperBoundP1) {
        int k = upperBoundP1 - lowerBoundP1;
        PathNode[] newPoints = new PathNode[k + path2.getCurrentPathLength()];
        System.arraycopy(path1.points, lowerBoundP1, newPoints, 0, k);
        System.arraycopy(path2.points, 0, newPoints, k, path2.getCurrentPathLength());
        return new Path(newPoints, path2.getIntendedTarget());
    }
}

