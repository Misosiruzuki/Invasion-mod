/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package invmod.common.util;

import java.util.Comparator;
import net.minecraft.entity.Entity;

public class ComparatorEntityDistanceFrom
implements Comparator<Entity> {
    private double x;
    private double y;
    private double z;

    public ComparatorEntityDistanceFrom(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int compare(Entity entity1, Entity entity2) {
        double d1 = (this.x - entity1.field_70165_t) * (this.x - entity1.field_70165_t) + (this.y - entity1.field_70163_u) * (this.y - entity1.field_70163_u) + (this.z - entity1.field_70161_v) * (this.z - entity1.field_70161_v);
        double d2 = (this.x - entity2.field_70165_t) * (this.x - entity2.field_70165_t) + (this.y - entity2.field_70163_u) * (this.y - entity2.field_70163_u) + (this.z - entity2.field_70161_v) * (this.z - entity2.field_70161_v);
        if (d1 > d2) {
            return -1;
        }
        if (d1 < d2) {
            return 1;
        }
        return 0;
    }
}

