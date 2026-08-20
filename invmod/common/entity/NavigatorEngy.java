/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.IPathSource;
import invmod.common.entity.NavigatorIM;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.Distance;
import net.minecraft.world.IBlockAccess;

public class NavigatorEngy
extends NavigatorIM {
    private final EntityIMPigEngy pigEntity;

    public NavigatorEngy(EntityIMPigEngy entity, IPathSource pathSource) {
        super(entity, pathSource);
        this.pigEntity = entity;
        this.setNoMaintainPos();
    }

    @Override
    protected Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius) {
        float maxSearchRange;
        Object terrainCache = this.getChunkCache(entity.getXCoord(), entity.getYCoord(), entity.getZCoord(), x, y, z, 16.0f);
        INexusAccess nexus = this.pigEntity.getNexus();
        if (nexus != null) {
            IBlockAccessExtended terrainCacheExt = nexus.getAttackerAI().wrapEntityData((IBlockAccess)terrainCache);
            nexus.getAttackerAI().addScaffoldDataTo(terrainCacheExt);
            terrainCache = terrainCacheExt;
        }
        if (this.pathSource.canPathfindNice(IPathSource.PathPriority.HIGH, maxSearchRange = 12.0f + (float)Distance.distanceBetween(entity, x, y, z), this.pathSource.getSearchDepth(), this.pathSource.getQuickFailDepth())) {
            return this.pathSource.createPath(entity, x, y, z, targetRadius, maxSearchRange, (IBlockAccess)terrainCache);
        }
        return null;
    }

    @Override
    protected boolean handlePathAction() {
        if (!this.actionCleared) {
            this.resetStatus();
            return this.getLastActionResult() == 0;
        }
        if (this.activeNode.action == PathAction.LADDER_UP_PX || this.activeNode.action == PathAction.LADDER_UP_NX || this.activeNode.action == PathAction.LADDER_UP_PZ || this.activeNode.action == PathAction.LADDER_UP_NZ) {
            if (this.pigEntity.getTerrainBuildEngy().askBuildLadder(this.activeNode, this)) {
                return this.setDoingTaskAndHold();
            }
        } else if (this.activeNode.action == PathAction.BRIDGE) {
            if (this.pigEntity.getTerrainBuildEngy().askBuildBridge(this.activeNode, this)) {
                return this.setDoingTaskAndHold();
            }
        } else if (this.activeNode.action == PathAction.SCAFFOLD_UP) {
            if (this.pigEntity.getTerrainBuildEngy().askBuildScaffoldLayer(this.activeNode, this)) {
                return this.setDoingTaskAndHoldOnPoint();
            }
        } else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_PX ? this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 0, 1, this) : (this.activeNode.action == PathAction.LADDER_TOWER_UP_NX ? this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 1, 1, this) : (this.activeNode.action == PathAction.LADDER_TOWER_UP_PZ ? this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 2, 1, this) : this.activeNode.action == PathAction.LADDER_TOWER_UP_NZ && this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 3, 1, this)))) {
            return this.setDoingTaskAndHold();
        }
        this.nodeActionFinished = true;
        return true;
    }
}

