/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityLookHelper
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;

public class IMLookHelper
extends EntityLookHelper {
    private final EntityIMLiving a;
    private float b;
    private float c;
    private boolean d = false;
    private double e;
    private double f;
    private double g;

    public IMLookHelper(EntityIMLiving entity) {
        super((EntityLiving)entity);
        this.a = entity;
    }

    public void func_75651_a(Entity par1Entity, float par2, float par3) {
        this.e = par1Entity.field_70165_t;
        this.f = par1Entity instanceof EntityLiving ? par1Entity.field_70163_u + (double)par1Entity.func_70047_e() : (par1Entity.field_70121_D.field_72338_b + par1Entity.field_70121_D.field_72337_e) / 2.0;
        this.g = par1Entity.field_70161_v;
        this.b = par2;
        this.c = par3;
        this.d = true;
    }

    public void func_75650_a(double par1, double par3, double par5, float par7, float par8) {
        this.e = par1;
        this.f = par3;
        this.g = par5;
        this.b = par7;
        this.c = par8;
        this.d = true;
    }

    public void func_75649_a() {
        if (this.d) {
            this.d = false;
            double d0 = this.e - this.a.field_70165_t;
            double d1 = this.f - (this.a.field_70163_u + (double)this.a.func_70047_e());
            double d2 = this.g - this.a.field_70161_v;
            double d3 = MathHelper.func_76133_a((double)(d0 * d0 + d2 * d2));
            float yaw = (float)MathUtil.boundAngle180Deg(this.a.field_70177_z);
            float pitch = (float)MathUtil.boundAngle180Deg(this.a.field_70125_A);
            float yawHeadOffset = (float)(Math.atan2(d2, d0) * 180.0 / Math.PI) - 90.0f - yaw;
            float pitchHeadOffset = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI + 40.0 - (double)pitch);
            float f2 = (float)MathUtil.boundAngle180Deg(yawHeadOffset);
            float yawFinal = f2 > 100.0f || f2 < -100.0f ? 0.0f : f2 / 6.0f;
            this.a.setRotationPitchHead(this.updateRotation(this.a.getRotationPitchHead(), pitchHeadOffset, this.c));
            this.a.setRotationYawHeadIM(this.updateRotation(this.a.getRotationYawHeadIM(), yawFinal, this.b));
        }
    }

    private float updateRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.func_76142_g((float)(par2 - par1));
        if (f3 > par3) {
            f3 = par3;
        }
        if (f3 < -par3) {
            f3 = -par3;
        }
        return par1 + f3;
    }
}

