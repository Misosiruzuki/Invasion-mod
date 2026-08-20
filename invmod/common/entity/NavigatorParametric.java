/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IPathSource;
import invmod.common.entity.NavigatorIM;
import invmod.common.entity.PathAction;
import invmod.common.util.PosRotate3D;

public abstract class NavigatorParametric
extends NavigatorIM {
    protected double minMoveToleranceSq = 21.0;
    protected int timeParam = 0;

    public NavigatorParametric(EntityIMLiving entity, IPathSource pathSource) {
        super(entity, pathSource);
    }

    public void onUpdateNavigation(int paramElapsed) {
        ++this.totalTicks;
        if (this.noPath() || this.waitingForNotify) {
            return;
        }
        if (this.canNavigate() && this.nodeActionFinished) {
            int pathIndex = this.path.getCurrentPathIndex();
            this.pathFollow(this.timeParam + paramElapsed);
            this.doMovementTo(this.timeParam);
            if (this.path.getCurrentPathIndex() != pathIndex) {
                this.ticksStuck = 0;
                if (this.activeNode.action != PathAction.NONE) {
                    this.nodeActionFinished = false;
                }
            }
        }
        if (this.nodeActionFinished) {
            if (!this.isPositionClear(this.activeNode.xCoord, this.activeNode.yCoord, this.activeNode.zCoord, this.theEntity)) {
                if (this.theEntity.onPathBlocked(this.path, this)) {
                    this.setDoingTaskAndHold();
                } else {
                    this.clearPath();
                }
            }
        } else {
            this.handlePathAction();
        }
    }

    @Override
    public void onUpdateNavigation() {
        this.onUpdateNavigation(1);
    }

    protected void doMovementTo(int param) {
        PosRotate3D movePos = this.entityPositionAtParam(param);
        this.theEntity.func_70091_d(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ());
        if (Math.abs(this.theEntity.func_70092_e(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ())) < this.minMoveToleranceSq) {
            this.timeParam = param;
            --this.ticksStuck;
        } else {
            ++this.ticksStuck;
        }
    }

    protected abstract PosRotate3D entityPositionAtParam(int var1);

    protected abstract boolean isReadyForNextNode(int var1);

    protected void pathFollow(int param) {
        int nextIndex = this.path.getCurrentPathIndex() + 1;
        if (this.isReadyForNextNode(param)) {
            if (nextIndex < this.path.getCurrentPathLength()) {
                this.timeParam = 0;
                this.path.setCurrentPathIndex(nextIndex);
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
            }
        } else {
            this.timeParam = param;
        }
    }

    @Override
    protected void pathFollow() {
        this.pathFollow(0);
    }
}

