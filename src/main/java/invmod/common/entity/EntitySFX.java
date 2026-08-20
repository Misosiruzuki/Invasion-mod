/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySFX
extends Entity {
    private int lifespan = 200;

    public EntitySFX(World world) {
        super(world);
    }

    public EntitySFX(World world, double x, double y, double z) {
        super(world);
        this.field_70165_t = x;
        this.field_70163_u = y;
        this.field_70161_v = z;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.lifespan-- <= 0) {
            this.func_70106_y();
        }
    }

    public void func_70103_a(byte byte0) {
        if (byte0 == 0 || byte0 == 1 || byte0 != 2) {
            // empty if block
        }
    }

    public void func_70088_a() {
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
    }
}

