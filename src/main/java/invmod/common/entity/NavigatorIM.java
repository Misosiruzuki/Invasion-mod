/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.ChunkCache
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.INavigation;
import invmod.common.entity.IPathSource;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class NavigatorIM
implements INotifyTask,
INavigation {
    protected static final int XZPATH_HORIZONTAL_SEARCH = 1;
    protected static final double ENTITY_TRACKING_TOLERANCE = 0.1;
    protected static final double MINIMUM_PROGRESS = 0.01;
    protected final EntityIMLiving theEntity;
    protected IPathSource pathSource;
    protected Path path;
    protected PathNode activeNode;
    protected Vec3 entityCentre;
    protected Entity pathEndEntity;
    protected Vec3 pathEndEntityLastPos;
    protected float moveSpeed;
    protected float pathSearchLimit;
    protected boolean noSunPathfind;
    protected int totalTicks;
    protected Vec3 lastPos;
    private Vec3 holdingPos;
    protected boolean nodeActionFinished;
    private boolean canSwim;
    protected boolean waitingForNotify;
    protected boolean actionCleared;
    protected double lastDistance;
    protected int ticksStuck;
    private boolean maintainPosOnWait;
    private int lastActionResult;
    private boolean haltMovement;
    private boolean autoPathToEntity;

    public NavigatorIM(EntityIMLiving entity, IPathSource pathSource) {
        this.theEntity = entity;
        this.pathSource = pathSource;
        this.noSunPathfind = false;
        this.lastPos = Vec3.func_72443_a((double)0.0, (double)0.0, (double)0.0);
        this.pathEndEntityLastPos = Vec3.func_72443_a((double)0.0, (double)0.0, (double)0.0);
        this.lastDistance = 0.0;
        this.ticksStuck = 0;
        this.canSwim = false;
        this.waitingForNotify = false;
        this.actionCleared = true;
        this.nodeActionFinished = true;
        this.maintainPosOnWait = false;
        this.haltMovement = false;
        this.lastActionResult = 0;
        this.autoPathToEntity = false;
    }

    @Override
    public PathAction getCurrentWorkingAction() {
        if (!this.nodeActionFinished && !this.noPath()) {
            return this.activeNode.action;
        }
        return PathAction.NONE;
    }

    protected boolean isMaintainingPos() {
        return this.maintainPosOnWait;
    }

    protected void setNoMaintainPos() {
        this.maintainPosOnWait = false;
    }

    protected void setMaintainPosOnWait(Vec3 pos) {
        this.holdingPos = pos;
        this.maintainPosOnWait = true;
    }

    @Override
    public void setSpeed(float par1) {
        this.moveSpeed = par1;
    }

    public boolean isAutoPathingToEntity() {
        return this.autoPathToEntity;
    }

    @Override
    public Entity getTargetEntity() {
        return this.pathEndEntity;
    }

    @Override
    public Path getPathToXYZ(double x, double y, double z, float targetRadius) {
        if (!this.canNavigate()) {
            return null;
        }
        return this.createPath(this.theEntity, MathHelper.func_76128_c((double)x), (int)y, MathHelper.func_76128_c((double)z), targetRadius);
    }

    @Override
    public boolean tryMoveToXYZ(double x, double y, double z, float targetRadius, float speed) {
        this.ticksStuck = 0;
        Path newPath = this.getPathToXYZ(MathHelper.func_76128_c((double)x), (int)y, MathHelper.func_76128_c((double)z), targetRadius);
        if (newPath != null) {
            return this.setPath(newPath, speed);
        }
        return false;
    }

    @Override
    public Path getPathTowardsXZ(double x, double z, int min, int max, int verticalRange) {
        Path entityPath;
        Vec3 target;
        if (this.canNavigate() && (target = this.findValidPointNear(x, z, min, max, verticalRange)) != null && (entityPath = this.getPathToXYZ(target.field_72450_a, target.field_72448_b, target.field_72449_c, 0.0f)) != null) {
            return entityPath;
        }
        return null;
    }

    @Override
    public boolean tryMoveTowardsXZ(double x, double z, int min, int max, int verticalRange, float speed) {
        this.ticksStuck = 0;
        Path newPath = this.getPathTowardsXZ(MathHelper.func_76128_c((double)x), MathHelper.func_76128_c((double)z), min, max, verticalRange);
        if (newPath != null) {
            return this.setPath(newPath, speed);
        }
        return false;
    }

    @Override
    public Path getPathToEntity(Entity targetEntity, float targetRadius) {
        if (!this.canNavigate()) {
            return null;
        }
        return this.createPath(this.theEntity, MathHelper.func_76128_c((double)targetEntity.field_70165_t), MathHelper.func_76128_c((double)targetEntity.field_70121_D.field_72338_b), MathHelper.func_76128_c((double)targetEntity.field_70161_v), targetRadius);
    }

    @Override
    public boolean tryMoveToEntity(Entity targetEntity, float targetRadius, float speed) {
        Path newPath = this.getPathToEntity(targetEntity, targetRadius);
        if (newPath != null) {
            if (this.setPath(newPath, speed)) {
                this.pathEndEntity = targetEntity;
                return true;
            }
            this.pathEndEntity = null;
            return false;
        }
        return false;
    }

    @Override
    public void autoPathToEntity(Entity target) {
        this.autoPathToEntity = true;
        this.pathEndEntity = target;
    }

    @Override
    public boolean setPath(Path newPath, float speed) {
        block4: {
            block5: {
                CoordsInt size;
                block3: {
                    if (newPath == null) {
                        this.path = null;
                        this.theEntity.onPathSet();
                        return false;
                    }
                    this.moveSpeed = speed;
                    this.lastDistance = this.getDistanceToActiveNode();
                    this.ticksStuck = 0;
                    this.resetStatus();
                    size = this.theEntity.getCollideSize();
                    this.entityCentre = Vec3.func_72443_a((double)((double)size.getXCoord() * 0.5), (double)0.0, (double)((double)size.getZCoord() * 0.5));
                    this.path = newPath;
                    this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                    if (this.activeNode.action == PathAction.NONE) break block3;
                    this.nodeActionFinished = false;
                    break block4;
                }
                if (size.getXCoord() > 1 || size.getZCoord() > 1) break block5;
                this.path.incrementPathIndex();
                if (this.path.isFinished()) break block4;
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                if (this.activeNode.action == PathAction.NONE) break block4;
                this.nodeActionFinished = false;
                break block4;
            }
            while (this.theEntity.func_70092_e((double)this.activeNode.xCoord + this.entityCentre.field_72450_a, (double)this.activeNode.yCoord + this.entityCentre.field_72448_b, (double)this.activeNode.zCoord + this.entityCentre.field_72449_c) > (double)this.theEntity.field_70130_N) {
                this.path.incrementPathIndex();
                if (this.path.isFinished()) break;
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                if (this.activeNode.action == PathAction.NONE) continue;
                this.nodeActionFinished = false;
            }
        }
        if (this.noSunPathfind) {
            this.removeSunnyPath();
        }
        this.theEntity.onPathSet();
        return true;
    }

    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public boolean isWaitingForTask() {
        return this.waitingForNotify;
    }

    @Override
    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (this.autoPathToEntity) {
            this.updateAutoPathToEntity();
        }
        if (this.noPath()) {
            this.noPathFollow();
            return;
        }
        if (this.waitingForNotify) {
            if (this.isMaintainingPos()) {
                this.theEntity.getMoveHelper().func_75642_a(this.holdingPos.field_72450_a, this.holdingPos.field_72448_b, this.holdingPos.field_72449_c, this.moveSpeed);
            }
            return;
        }
        if (this.canNavigate() && this.nodeActionFinished) {
            double distance = this.getDistanceToActiveNode();
            if (this.lastDistance - distance > 0.01) {
                this.lastDistance = distance;
                --this.ticksStuck;
            } else {
                ++this.ticksStuck;
            }
            int pathIndex = this.path.getCurrentPathIndex();
            this.pathFollow();
            if (this.noPath()) {
                return;
            }
            if (this.path.getCurrentPathIndex() != pathIndex) {
                this.lastDistance = this.getDistanceToActiveNode();
                this.ticksStuck = 0;
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                if (this.activeNode.action != PathAction.NONE) {
                    this.nodeActionFinished = false;
                }
            }
        }
        if (this.nodeActionFinished) {
            if (!this.isPositionClearFrom(this.theEntity.getXCoord(), this.theEntity.getYCoord(), this.theEntity.getZCoord(), this.activeNode.xCoord, this.activeNode.yCoord, this.activeNode.zCoord, this.theEntity) && this.theEntity.onPathBlocked(this.path, this)) {
                this.setDoingTaskAndHoldOnPoint();
            }
            if (!this.haltMovement) {
                if (this.pathEndEntity != null && this.pathEndEntity.field_70163_u - this.theEntity.field_70163_u <= 0.0 && this.theEntity.func_70092_e(this.pathEndEntity.field_70165_t, this.pathEndEntity.field_70121_D.field_72338_b, this.pathEndEntity.field_70161_v) < 4.5) {
                    this.theEntity.getMoveHelper().func_75642_a(this.pathEndEntity.field_70165_t, this.pathEndEntity.field_70121_D.field_72338_b, this.pathEndEntity.field_70161_v, this.moveSpeed);
                } else {
                    this.theEntity.getMoveHelper().func_75642_a((double)this.activeNode.xCoord + this.entityCentre.field_72450_a, (double)this.activeNode.yCoord + this.entityCentre.field_72448_b, (double)this.activeNode.zCoord + this.entityCentre.field_72449_c, this.moveSpeed);
                }
            } else {
                this.haltMovement = false;
            }
        } else if (!this.handlePathAction()) {
            this.clearPath();
        }
    }

    @Override
    public void notifyTask(int result) {
        this.waitingForNotify = false;
        this.lastActionResult = result;
    }

    @Override
    public int getLastActionResult() {
        return this.lastActionResult;
    }

    @Override
    public boolean noPath() {
        return this.path == null || this.path.isFinished();
    }

    @Override
    public int getStuckTime() {
        return this.ticksStuck;
    }

    @Override
    public float getLastPathDistanceToTarget() {
        if (this.noPath()) {
            if (this.path != null && this.path.getIntendedTarget() != null) {
                PathNode node = this.path.getIntendedTarget();
                return (float)this.theEntity.func_70011_f(node.xCoord, node.yCoord, node.zCoord);
            }
            return 0.0f;
        }
        return this.path.getFinalPathPoint().distanceTo(this.path.getIntendedTarget());
    }

    @Override
    public void clearPath() {
        this.path = null;
        this.autoPathToEntity = false;
        this.resetStatus();
    }

    @Override
    public void haltForTick() {
        this.haltMovement = true;
    }

    @Override
    public String getStatus() {
        String s = "";
        if (this.autoPathToEntity) {
            s = s + "Auto:";
        }
        if (this.noPath()) {
            s = s + "NoPath:";
            return s;
        }
        s = s + "Pathing:";
        s = s + "Node[" + this.path.getCurrentPathIndex() + "/" + this.path.getCurrentPathLength() + "]:";
        if (!this.nodeActionFinished && this.activeNode != null) {
            s = s + "Action[" + (Object)((Object)this.activeNode.action) + "]:";
        }
        return s;
    }

    protected Path createPath(EntityIMLiving entity, Entity target, float targetRadius) {
        return this.createPath(entity, MathHelper.func_76128_c((double)target.field_70165_t), (int)target.field_70163_u, MathHelper.func_76128_c((double)target.field_70161_v), targetRadius);
    }

    protected Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius) {
        float maxSearchRange;
        this.theEntity.setCurrentTargetPos(new CoordsInt(x, y, z));
        Object terrainCache = this.getChunkCache(entity.getXCoord(), entity.getYCoord(), entity.getZCoord(), x, y, z, 16.0f);
        INexusAccess nexus = entity.getNexus();
        if (nexus != null) {
            terrainCache = nexus.getAttackerAI().wrapEntityData((IBlockAccess)terrainCache);
        }
        if (this.pathSource.canPathfindNice(IPathSource.PathPriority.HIGH, maxSearchRange = 12.0f + (float)Distance.distanceBetween(entity, x, y, z), this.pathSource.getSearchDepth(), this.pathSource.getQuickFailDepth())) {
            return this.pathSource.createPath(entity, x, y, z, targetRadius, maxSearchRange, (IBlockAccess)terrainCache);
        }
        return null;
    }

    protected void pathFollow() {
        int index;
        Vec3 vec3d = this.getEntityPosition();
        int maxNextLegIndex = this.path.getCurrentPathIndex() - 1;
        PathNode nextPoint = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
        if (nextPoint.yCoord == (int)vec3d.field_72448_b && maxNextLegIndex < this.path.getCurrentPathLength() - 1) {
            int prevIndex;
            boolean canConsolidate = true;
            if ((prevIndex = ++maxNextLegIndex - 2) >= 0 && this.path.getPathPointFromIndex((int)prevIndex).action != PathAction.NONE) {
                canConsolidate = false;
            }
            if (canConsolidate && this.theEntity.canStandAt((IBlockAccess)this.theEntity.field_70170_p, MathHelper.func_76128_c((double)this.theEntity.field_70165_t), MathHelper.func_76128_c((double)this.theEntity.field_70163_u), MathHelper.func_76128_c((double)this.theEntity.field_70161_v))) {
                while (maxNextLegIndex < this.path.getCurrentPathLength() - 1 && this.path.getPathPointFromIndex((int)maxNextLegIndex).yCoord == (int)vec3d.field_72448_b && this.path.getPathPointFromIndex((int)maxNextLegIndex).action == PathAction.NONE) {
                    ++maxNextLegIndex;
                }
            }
        }
        float fa = this.theEntity.field_70130_N * 0.5f;
        fa *= fa;
        for (int j = this.path.getCurrentPathIndex(); j <= maxNextLegIndex; ++j) {
            if (!(vec3d.func_72436_e(this.path.getPositionAtIndex((Entity)this.theEntity, j)) < (double)fa)) continue;
            this.path.setCurrentPathIndex(j + 1);
        }
        int xSize = (int)Math.ceil(this.theEntity.field_70130_N);
        int ySize = (int)this.theEntity.field_70131_O + 1;
        int zSize = xSize;
        for (index = maxNextLegIndex; index > this.path.getCurrentPathIndex() && !this.isDirectPathBetweenPoints(vec3d, this.path.getPositionAtIndex((Entity)this.theEntity, index), xSize, ySize, zSize); --index) {
        }
        for (int i = this.path.getCurrentPathIndex() + 1; i < index; ++i) {
            if (this.path.getPathPointFromIndex((int)i).action == PathAction.NONE) continue;
            index = i;
            break;
        }
        if (this.path.getCurrentPathIndex() < index) {
            this.path.setCurrentPathIndex(index);
        }
    }

    protected void noPathFollow() {
    }

    protected void updateAutoPathToEntity() {
        Path newPath;
        double d2;
        double d1;
        if (this.pathEndEntity == null) {
            return;
        }
        boolean wantsUpdate = this.noPath() ? true : (d1 = Distance.distanceBetween(this.pathEndEntity, this.pathEndEntityLastPos)) / (d2 = 6.0 + Distance.distanceBetween((Entity)this.theEntity, this.pathEndEntityLastPos)) > 0.1;
        if (wantsUpdate && (newPath = this.getPathToEntity(this.pathEndEntity, 0.0f)) != null && this.setPath(newPath, this.moveSpeed)) {
            this.pathEndEntityLastPos = Vec3.func_72443_a((double)this.pathEndEntity.field_70165_t, (double)this.pathEndEntity.field_70163_u, (double)this.pathEndEntity.field_70161_v);
        }
    }

    protected double getDistanceToActiveNode() {
        if (this.activeNode != null) {
            double dX = (double)this.activeNode.xCoord + 0.5 - this.theEntity.field_70165_t;
            double dY = (double)this.activeNode.yCoord - this.theEntity.field_70163_u;
            double dZ = (double)this.activeNode.zCoord + 0.5 - this.theEntity.field_70161_v;
            return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        }
        return 0.0;
    }

    protected boolean handlePathAction() {
        this.nodeActionFinished = true;
        return true;
    }

    protected boolean setDoingTask() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        return true;
    }

    protected boolean setDoingTaskAndHold() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        this.setMaintainPosOnWait(Vec3.func_72443_a((double)this.theEntity.field_70165_t, (double)this.theEntity.field_70163_u, (double)this.theEntity.field_70161_v));
        this.theEntity.setIsHoldingIntoLadder(true);
        return true;
    }

    protected boolean setDoingTaskAndHoldOnPoint() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        this.setMaintainPosOnWait(Vec3.func_72443_a((double)((double)this.activeNode.getXCoord() + 0.5), (double)this.activeNode.getYCoord(), (double)((double)this.activeNode.getZCoord() + 0.5)));
        this.theEntity.setIsHoldingIntoLadder(true);
        return true;
    }

    protected void resetStatus() {
        this.setNoMaintainPos();
        this.theEntity.setIsHoldingIntoLadder(false);
        this.nodeActionFinished = true;
        this.actionCleared = true;
        this.waitingForNotify = false;
    }

    protected Vec3 getEntityPosition() {
        return Vec3.func_72443_a((double)this.theEntity.field_70165_t, (double)this.getPathableYPos(), (double)this.theEntity.field_70161_v);
    }

    protected EntityIMLiving getEntity() {
        return this.theEntity;
    }

    private int getPathableYPos() {
        if (!this.theEntity.func_70090_H() || !this.canSwim) {
            return (int)(this.theEntity.field_70121_D.field_72338_b + 0.5);
        }
        int i = (int)this.theEntity.field_70121_D.field_72338_b;
        Block block = this.theEntity.field_70170_p.func_147439_a(MathHelper.func_76128_c((double)this.theEntity.field_70165_t), i, MathHelper.func_76128_c((double)this.theEntity.field_70161_v));
        int k = 0;
        while (block == Blocks.field_150355_j || block == Blocks.field_150358_i) {
            block = this.theEntity.field_70170_p.func_147439_a(MathHelper.func_76128_c((double)this.theEntity.field_70165_t), ++i, MathHelper.func_76128_c((double)this.theEntity.field_70161_v));
            if (++k <= 16) continue;
            return (int)this.theEntity.field_70121_D.field_72338_b;
        }
        return i;
    }

    protected boolean canNavigate() {
        return true;
    }

    protected boolean isInLiquid() {
        return this.theEntity.func_70090_H() || this.theEntity.func_70058_J();
    }

    protected Vec3 findValidPointNear(double x, double z, int min, int max, int verticalRange) {
        double xOffset = x - this.theEntity.field_70165_t;
        double zOffset = z - this.theEntity.field_70161_v;
        double h = Math.sqrt(xOffset * xOffset + zOffset * zOffset);
        if (h < 0.5) {
            return null;
        }
        double distance = min + this.theEntity.func_70681_au().nextInt(max - min);
        int xi = MathHelper.func_76128_c((double)(xOffset * (distance / h) + this.theEntity.field_70165_t));
        int zi = MathHelper.func_76128_c((double)(zOffset * (distance / h) + this.theEntity.field_70161_v));
        int y = MathHelper.func_76128_c((double)this.theEntity.field_70163_u);
        Object entityPath = null;
        int vertical = 0;
        while (vertical < verticalRange) {
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (!this.theEntity.canStandAtAndIsValid((IBlockAccess)this.theEntity.field_70170_p, xi + i, y + vertical, zi + j)) continue;
                    return Vec3.func_72443_a((double)(xi + i), (double)(y + vertical), (double)(zi + j));
                }
            }
            vertical = vertical > 0 ? vertical * -1 : vertical * -1 + 1;
        }
        return null;
    }

    protected void removeSunnyPath() {
        if (this.theEntity.field_70170_p.func_72937_j(MathHelper.func_76128_c((double)this.theEntity.field_70165_t), (int)(this.theEntity.field_70121_D.field_72338_b + 0.5), MathHelper.func_76128_c((double)this.theEntity.field_70161_v))) {
            return;
        }
        for (int i = 0; i < this.path.getCurrentPathLength(); ++i) {
            PathNode pathpoint = this.path.getPathPointFromIndex(i);
            if (!this.theEntity.field_70170_p.func_72937_j(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)) continue;
            this.path.setCurrentPathLength(i - 1);
            return;
        }
    }

    protected boolean isDirectPathBetweenPoints(Vec3 pos1, Vec3 pos2, int xSize, int ySize, int zSize) {
        int x = MathHelper.func_76128_c((double)pos1.field_72450_a);
        int z = MathHelper.func_76128_c((double)pos1.field_72449_c);
        double dX = pos2.field_72450_a - pos1.field_72450_a;
        double dZ = pos2.field_72449_c - pos1.field_72449_c;
        double dXZsq = dX * dX + dZ * dZ;
        if (dXZsq < 1.0E-8) {
            return false;
        }
        double scale = 1.0 / Math.sqrt(dXZsq);
        if (!this.isSafeToStandAt(x, (int)pos1.field_72448_b, z, xSize += 2, ySize, zSize += 2, pos1, dX *= scale, dZ *= scale)) {
            return false;
        }
        xSize -= 2;
        zSize -= 2;
        double xIncrement = 1.0 / Math.abs(dX);
        double zIncrement = 1.0 / Math.abs(dZ);
        double xOffset = (double)(x * 1) - pos1.field_72450_a;
        double zOffset = (double)(z * 1) - pos1.field_72449_c;
        if (dX >= 0.0) {
            xOffset += 1.0;
        }
        if (dZ >= 0.0) {
            zOffset += 1.0;
        }
        xOffset /= dX;
        zOffset /= dZ;
        byte xDirection = (byte)(dX >= 0.0 ? 1 : -1);
        byte zDirection = (byte)(dZ >= 0.0 ? 1 : -1);
        int x2 = MathHelper.func_76128_c((double)pos2.field_72450_a);
        int z2 = MathHelper.func_76128_c((double)pos2.field_72449_c);
        int xDiff = x2 - x;
        int i = z2 - z;
        while (xDiff * xDirection > 0 || i * zDirection > 0) {
            if (xOffset < zOffset) {
                xOffset += xIncrement;
                xDiff = x2 - (x += xDirection);
            } else {
                zOffset += zIncrement;
                i = z2 - (z += zDirection);
            }
            if (this.isSafeToStandAt(x, (int)pos1.field_72448_b, z, xSize, ySize, zSize, pos1, dX, dZ)) continue;
            return false;
        }
        return true;
    }

    protected boolean isSafeToStandAt(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, Vec3 entityPostion, double par8, double par10) {
        int i = xOffset - xSize / 2;
        int j = zOffset - zSize / 2;
        if (!this.isPositionClear(i, yOffset, j, xSize, ySize, zSize, entityPostion, par8, par10)) {
            return false;
        }
        for (int k = i; k < i + xSize; ++k) {
            for (int l = j; l < j + zSize; ++l) {
                double d = (double)k + 0.5 - entityPostion.field_72450_a;
                double d1 = (double)l + 0.5 - entityPostion.field_72449_c;
                if (!(d * par8 + d1 * par10 >= 0.0)) continue;
                Block block = this.theEntity.field_70170_p.func_147439_a(k, yOffset - 1, l);
                if (block == Blocks.field_150350_a) {
                    return false;
                }
                Material material = block.func_149688_o();
                if (material == Material.field_151586_h && !this.theEntity.func_70090_H()) {
                    return false;
                }
                if (material == Material.field_151587_i) {
                    return false;
                }
                if (material.func_76220_a()) continue;
                return false;
            }
        }
        return true;
    }

    protected boolean isPositionClear(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, Vec3 entityPostion, double vecX, double vecZ) {
        for (int i = xOffset; i < xOffset + xSize; ++i) {
            for (int j = yOffset; j < yOffset + ySize; ++j) {
                for (int k = zOffset; k < zOffset + zSize; ++k) {
                    Block block;
                    double d = (double)i + 0.5 - entityPostion.field_72450_a;
                    double d1 = (double)k + 0.5 - entityPostion.field_72449_c;
                    if (!(d * vecX + d1 * vecZ >= 0.0) || (block = this.theEntity.field_70170_p.func_147439_a(i, j, k)) == Blocks.field_150350_a || block.func_149655_b((IBlockAccess)this.theEntity.field_70170_p, i, j, k)) continue;
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean isPositionClearFrom(int x1, int y1, int z1, int x2, int y2, int z2, EntityIMLiving entity) {
        Block block;
        if (y2 > y1 && (block = this.theEntity.field_70170_p.func_147439_a(x1, y1 + entity.getCollideSize().getYCoord(), z1)) != Blocks.field_150350_a && !block.func_149655_b((IBlockAccess)this.theEntity.field_70170_p, x1, y1 + entity.getCollideSize().getYCoord(), z1)) {
            return false;
        }
        return this.isPositionClear(x2, y2, z2, entity);
    }

    protected boolean isPositionClear(int x, int y, int z, EntityIMLiving entity) {
        CoordsInt size = entity.getCollideSize();
        return this.isPositionClear(x, y, z, size.getXCoord(), size.getYCoord(), size.getZCoord());
    }

    protected boolean isPositionClear(int x, int y, int z, int xSize, int ySize, int zSize) {
        for (int i = x; i < x + xSize; ++i) {
            for (int j = y; j < y + ySize; ++j) {
                for (int k = z; k < z + zSize; ++k) {
                    Block block = this.theEntity.field_70170_p.func_147439_a(i, j, k);
                    if (block == Blocks.field_150350_a || block.func_149655_b((IBlockAccess)this.theEntity.field_70170_p, i, j, k)) continue;
                    return false;
                }
            }
        }
        return true;
    }

    protected ChunkCache getChunkCache(int x1, int y1, int z1, int x2, int y2, int z2, float axisExpand) {
        int cZ2;
        int cZ1;
        int cY2;
        int cY1;
        int cX2;
        int cX1;
        int d = (int)axisExpand;
        if (x1 < x2) {
            cX1 = x1 - d;
            cX2 = x2 + d;
        } else {
            cX2 = x1 + d;
            cX1 = x2 - d;
        }
        if (y1 < y2) {
            cY1 = y1 - d;
            cY2 = y2 + d;
        } else {
            cY2 = y1 + d;
            cY1 = y2 - d;
        }
        if (z1 < z2) {
            cZ1 = z1 - d;
            cZ2 = z2 + d;
        } else {
            cZ2 = z1 + d;
            cZ1 = z2 - d;
        }
        return new ChunkCache(this.theEntity.field_70170_p, cX1, cY1, cZ1, cX2, cY2, cZ2, 0);
    }
}

