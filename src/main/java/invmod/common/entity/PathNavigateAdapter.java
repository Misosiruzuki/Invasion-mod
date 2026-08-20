/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.pathfinding.PathEntity
 *  net.minecraft.pathfinding.PathNavigate
 */
package invmod.common.entity;

import invmod.common.entity.NavigatorIM;
import invmod.common.entity.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;

public class PathNavigateAdapter
extends PathNavigate {
    private NavigatorIM navigator;

    public PathNavigateAdapter(NavigatorIM navigator) {
        super((EntityLiving)navigator.getEntity(), navigator.getEntity().field_70170_p);
        this.navigator = navigator;
    }

    public void func_75501_e() {
        this.navigator.onUpdateNavigation();
    }

    public boolean func_75500_f() {
        return this.navigator.noPath();
    }

    public void func_75499_g() {
        this.navigator.clearPath();
    }

    public void func_75489_a(double speed) {
        this.navigator.setSpeed((float)speed);
    }

    public boolean func_75492_a(double x, double y, double z, double movespeed) {
        return this.navigator.tryMoveToXYZ(x, y, z, 0.0f, (float)movespeed);
    }

    public boolean func_75497_a(Entity entity, double movespeed) {
        return this.navigator.tryMoveToEntity(entity, 0.0f, (float)movespeed);
    }

    public boolean setPath(Path entity, float movespeed) {
        return this.navigator.setPath(entity, movespeed);
    }

    public boolean func_75484_a(PathEntity entity, double movespeed) {
        return false;
    }

    public PathEntity func_75488_a(double x, double y, double z) {
        return null;
    }

    public void func_75491_a(boolean par1) {
    }

    public boolean func_75486_a() {
        return false;
    }

    public void func_75498_b(boolean par1) {
    }

    public void func_75490_c(boolean par1) {
    }

    public boolean func_75507_c() {
        return false;
    }

    public void func_75504_d(boolean par1) {
    }

    public void func_75495_e(boolean par1) {
    }

    public PathEntity func_75494_a(Entity par1EntityLiving) {
        return null;
    }

    public PathEntity func_75505_d() {
        return null;
    }
}

