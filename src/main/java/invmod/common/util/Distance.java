/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Vec3
 */
package invmod.common.util;

import invmod.common.util.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class Distance {
    public static double distanceBetween(IPosition pos1, IPosition pos2) {
        double dX = pos2.getXCoord() - pos1.getXCoord();
        double dY = pos2.getYCoord() - pos1.getYCoord();
        double dZ = pos2.getZCoord() - pos1.getZCoord();
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distanceBetween(IPosition pos1, Vec3 pos2) {
        double dX = pos2.field_72450_a - (double)pos1.getXCoord();
        double dY = pos2.field_72448_b - (double)pos1.getYCoord();
        double dZ = pos2.field_72449_c - (double)pos1.getZCoord();
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distanceBetween(IPosition pos1, double x2, double y2, double z2) {
        double dX = x2 - (double)pos1.getXCoord();
        double dY = y2 - (double)pos1.getYCoord();
        double dZ = z2 - (double)pos1.getZCoord();
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dX = x2 - x1;
        double dY = y2 - y1;
        double dZ = z2 - z1;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distanceBetween(Entity entity, Vec3 pos2) {
        double dX = pos2.field_72450_a - entity.field_70165_t;
        double dY = pos2.field_72448_b - entity.field_70163_u;
        double dZ = pos2.field_72449_c - entity.field_70161_v;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }
}

