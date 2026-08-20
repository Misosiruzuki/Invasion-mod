/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.util.IPosition;
import java.util.Comparator;

public class ComparatorDistanceFrom
implements Comparator<IPosition> {
    private double x;
    private double y;
    private double z;

    public ComparatorDistanceFrom(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int compare(IPosition pos1, IPosition pos2) {
        double d2;
        double d1 = (this.x - (double)pos1.getXCoord()) * (this.x - (double)pos1.getXCoord()) + (this.y - (double)pos1.getYCoord()) * (this.y - (double)pos1.getYCoord()) + (this.z - (double)pos1.getZCoord()) * (this.z - (double)pos1.getZCoord());
        if (d1 > (d2 = (this.x - (double)pos2.getXCoord()) * (this.x - (double)pos2.getXCoord()) + (this.y - (double)pos2.getYCoord()) * (this.y - (double)pos2.getYCoord()) + (this.z - (double)pos2.getZCoord()) * (this.z - (double)pos2.getZCoord()))) {
            return -1;
        }
        if (d1 < d2) {
            return 1;
        }
        return 0;
    }
}

