/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMMob;
import invmod.common.entity.Goal;
import invmod.common.entity.Path;
import invmod.common.entity.PathNode;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIGoToNexus
extends EntityAIBase {
    private EntityIMMob theEntity;
    private IPosition lastPathRequestPos;
    private int pathRequestTimer;
    private int pathFailedCount;

    public EntityAIGoToNexus(EntityIMMob entity) {
        this.theEntity = entity;
        this.lastPathRequestPos = new CoordsInt(0, -128, 0);
        this.pathRequestTimer = 0;
        this.pathFailedCount = 0;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.BREAK_NEXUS;
    }

    public void func_75249_e() {
        this.setPathToNexus();
    }

    public void func_75246_d() {
        if (this.pathFailedCount > 1) {
            this.wanderToNexus();
        }
        if (this.theEntity.getNavigatorNew().noPath() || this.theEntity.getNavigatorNew().getStuckTime() > 40) {
            this.setPathToNexus();
        }
    }

    private void setPathToNexus() {
        INexusAccess nexus = this.theEntity.getNexus();
        if (nexus != null && this.pathRequestTimer-- <= 0) {
            boolean pathSet = false;
            double distance = this.theEntity.findDistanceToNexus();
            if (distance > 2000.0) {
                pathSet = this.theEntity.getNavigatorNew().tryMoveTowardsXZ(nexus.getXCoord(), nexus.getZCoord(), 1, 6, 4, this.theEntity.getMoveSpeedStat());
            } else if (distance > 1.5) {
                pathSet = this.theEntity.getNavigatorNew().tryMoveToXYZ(nexus.getXCoord(), nexus.getYCoord(), nexus.getZCoord(), 1.0f, this.theEntity.getMoveSpeedStat());
            }
            if (!pathSet || this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0f && Distance.distanceBetween(this.lastPathRequestPos, this.theEntity) < 3.5) {
                ++this.pathFailedCount;
                this.pathRequestTimer = 40 * this.pathFailedCount + this.theEntity.field_70170_p.field_73012_v.nextInt(10);
            } else {
                this.pathFailedCount = 0;
                this.pathRequestTimer = 20;
            }
            this.lastPathRequestPos = new CoordsInt(this.theEntity.getXCoord(), this.theEntity.getYCoord(), this.theEntity.getZCoord());
        }
    }

    private boolean pathTooShort() {
        Path path = this.theEntity.getNavigatorNew().getPath();
        if (path != null) {
            PathNode pos = path.getFinalPathPoint();
            return this.theEntity.func_70092_e(pos.getXCoord(), pos.getYCoord(), pos.getZCoord()) < 4.0;
        }
        return true;
    }

    protected void wanderToNexus() {
        INexusAccess nexus = this.theEntity.getNexus();
        this.theEntity.getMoveHelper().func_75642_a((double)nexus.getXCoord() + 0.5, nexus.getYCoord(), (double)nexus.getZCoord() + 0.5, this.theEntity.getMoveSpeedStat());
    }
}

