/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IMMoveHelper;
import invmod.common.entity.Path;
import invmod.common.entity.PathNode;
import invmod.common.util.CoordsInt;
import net.minecraft.util.MathHelper;

public class IMMoveHelperSpider
extends IMMoveHelper {
    public IMMoveHelperSpider(EntityIMLiving par1EntityLiving) {
        super(par1EntityLiving);
    }

    @Override
    protected int getClimbFace(double x, double y, double z) {
        int mobX = MathHelper.func_76128_c((double)(x - (double)(this.a.field_70130_N / 2.0f)));
        int mobY = MathHelper.func_76128_c((double)y);
        int mobZ = MathHelper.func_76128_c((double)(z - (double)(this.a.field_70130_N / 2.0f)));
        int index = 0;
        Path path = this.a.getNavigatorNew().getPath();
        if (path != null && !path.isFinished()) {
            PathNode currentPoint = path.getPathPointFromIndex(path.getCurrentPathIndex());
            int pathLength = path.getCurrentPathLength();
            for (int i = path.getCurrentPathIndex(); i < pathLength; ++i) {
                PathNode point = path.getPathPointFromIndex(i);
                if (point.xCoord > currentPoint.xCoord) break;
                if (point.xCoord < currentPoint.xCoord) {
                    index = 2;
                    break;
                }
                if (point.zCoord > currentPoint.zCoord) {
                    index = 4;
                    break;
                }
                if (point.zCoord >= currentPoint.zCoord) continue;
                index = 6;
                break;
            }
        }
        for (int count = 0; count < 8; ++count) {
            if (this.a.field_70170_p.func_147445_c(mobX + CoordsInt.offsetAdj2X[index], mobY, mobZ + CoordsInt.offsetAdj2Z[index], true)) {
                return index / 2;
            }
            if (++index <= 7) continue;
            index = 0;
        }
        return -1;
    }
}

