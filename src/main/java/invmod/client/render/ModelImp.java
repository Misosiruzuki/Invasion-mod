/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MathHelper
 */
package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelImp
extends ModelBase {
    ModelRenderer head = new ModelRenderer((ModelBase)this, 44, 0);
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer rshin;
    ModelRenderer rfoot;
    ModelRenderer lshin;
    ModelRenderer lfoot;
    ModelRenderer rhorn;
    ModelRenderer lhorn;
    ModelRenderer bodymid;
    ModelRenderer neck;
    ModelRenderer bodychest;
    ModelRenderer tail;
    ModelRenderer tail2;

    public ModelImp() {
        this(0.0f);
    }

    public ModelImp(float f) {
        this(f, 0.0f);
    }

    public ModelImp(float f, float f1) {
        this.head.func_78789_a(-2.733333f, -3.0f, -2.0f, 5, 3, 4);
        this.head.func_78793_a(-0.4f, 9.8f, -3.3f);
        this.head.field_78795_f = 0.15807f;
        this.head.field_78796_g = 0.0f;
        this.head.field_78808_h = 0.0f;
        this.head.field_78809_i = false;
        this.body = new ModelRenderer((ModelBase)this, 23, 1);
        this.body.func_78789_a(-4.0f, 0.0f, -4.0f, 7, 4, 3);
        this.body.func_78793_a(0.0f, 9.1f, -0.8666667f);
        this.body.field_78795_f = 0.64346f;
        this.body.field_78796_g = 0.0f;
        this.body.field_78808_h = 0.0f;
        this.body.field_78809_i = false;
        this.rightarm = new ModelRenderer((ModelBase)this, 26, 9);
        this.rightarm.func_78789_a(-2.0f, -0.7333333f, -1.133333f, 2, 7, 2);
        this.rightarm.func_78793_a(-4.0f, 10.8f, -2.066667f);
        this.rightarm.field_78795_f = 0.0f;
        this.rightarm.field_78796_g = 0.0f;
        this.rightarm.field_78808_h = 0.0f;
        this.rightarm.field_78809_i = false;
        this.leftarm = new ModelRenderer((ModelBase)this, 18, 9);
        this.leftarm.func_78789_a(0.0f, -0.8666667f, -1.0f, 2, 7, 2);
        this.leftarm.func_78793_a(3.0f, 10.8f, -2.1f);
        this.leftarm.field_78795_f = 0.0f;
        this.leftarm.field_78796_g = 0.0f;
        this.leftarm.field_78808_h = 0.0f;
        this.leftarm.field_78809_i = false;
        this.rightleg = new ModelRenderer((ModelBase)this, 0, 17);
        this.rightleg.func_78789_a(-1.0f, 0.0f, -2.0f, 2, 4, 3);
        this.rightleg.func_78793_a(-2.0f, 16.9f, -1.0f);
        this.rightleg.field_78795_f = -0.15807f;
        this.rightleg.field_78796_g = 0.0f;
        this.rightleg.field_78808_h = 0.0f;
        this.rightleg.field_78809_i = false;
        this.leftleg = new ModelRenderer((ModelBase)this, 0, 24);
        this.leftleg.func_78789_a(-1.0f, 0.0f, -2.0f, 2, 4, 3);
        this.leftleg.func_78793_a(1.0f, 17.0f, -1.0f);
        this.leftleg.field_78795_f = -0.15919f;
        this.leftleg.field_78796_g = 0.0f;
        this.leftleg.field_78808_h = 0.0f;
        this.leftleg.field_78809_i = false;
        this.rshin = new ModelRenderer((ModelBase)this, 10, 17);
        this.rshin.func_78789_a(-2.0f, 0.6f, -4.4f, 2, 3, 2);
        this.rshin.func_78793_a(-1.0f, 16.9f, -1.0f);
        this.rshin.field_78795_f = 0.82623f;
        this.rshin.field_78796_g = 0.0f;
        this.rshin.field_78808_h = 0.0f;
        this.rshin.field_78809_i = false;
        this.rfoot = new ModelRenderer((ModelBase)this, 18, 18);
        this.rfoot.func_78789_a(-2.0f, 4.2f, -1.0f, 2, 3, 2);
        this.rfoot.func_78793_a(-1.0f, 16.9f, -1.0f);
        this.rfoot.field_78795_f = -0.01403f;
        this.rfoot.field_78796_g = 0.0f;
        this.rfoot.field_78808_h = 0.0f;
        this.rfoot.field_78809_i = false;
        this.lshin = new ModelRenderer((ModelBase)this, 10, 22);
        this.lshin.func_78789_a(-1.0f, 0.6f, -4.433333f, 2, 3, 2);
        this.lshin.func_78793_a(1.0f, 17.0f, -1.0f);
        this.lshin.field_78795_f = 0.82461f;
        this.lshin.field_78796_g = 0.0f;
        this.lshin.field_78808_h = 0.0f;
        this.lshin.field_78809_i = false;
        this.lfoot = new ModelRenderer((ModelBase)this, 10, 27);
        this.lfoot.func_78789_a(-1.0f, 4.2f, -1.0f, 2, 3, 2);
        this.lfoot.func_78793_a(1.0f, 17.0f, -1.0f);
        this.lfoot.field_78795_f = -0.01214f;
        this.lfoot.field_78796_g = 0.0f;
        this.lfoot.field_78808_h = 0.0f;
        this.lfoot.field_78809_i = false;
        this.rhorn = new ModelRenderer((ModelBase)this, 0, 0);
        this.rhorn.func_78789_a(1.0f, -4.0f, 1.5f, 1, 1, 1);
        this.rhorn.func_78793_a(-0.4f, 0.0f, -3.3f);
        this.rhorn.field_78809_i = false;
        this.lhorn = new ModelRenderer((ModelBase)this, 0, 2);
        this.lhorn.func_78789_a(-1.0f, -4.0f, 1.5f, 1, 1, 1);
        this.lhorn.func_78793_a(-0.4f, 0.0f, -3.3f);
        this.lhorn.field_78809_i = false;
        this.bodymid = new ModelRenderer((ModelBase)this, 1, 1);
        this.bodymid.func_78789_a(0.0f, 0.0f, 0.0f, 7, 5, 3);
        this.bodymid.func_78793_a(-4.0f, 12.46667f, -2.266667f);
        this.bodymid.field_78795_f = -0.15807f;
        this.bodymid.field_78796_g = 0.0f;
        this.bodymid.field_78808_h = 0.0f;
        this.bodymid.field_78809_i = false;
        this.neck = new ModelRenderer((ModelBase)this, 44, 7);
        this.neck.func_78789_a(0.0f, 0.0f, 0.0f, 3, 2, 2);
        this.neck.func_78793_a(-2.0f, 9.6f, -4.033333f);
        this.neck.field_78795_f = 0.27662f;
        this.neck.field_78796_g = 0.0f;
        this.neck.field_78808_h = 0.0f;
        this.neck.field_78809_i = false;
        this.bodychest = new ModelRenderer((ModelBase)this, 0, 9);
        this.bodychest.func_78789_a(0.0f, -1.0f, 0.0f, 7, 6, 2);
        this.bodychest.func_78793_a(-4.0f, 12.36667f, -3.8f);
        this.bodychest.field_78795_f = 0.31614f;
        this.bodychest.field_78796_g = 0.0f;
        this.bodychest.field_78808_h = 0.0f;
        this.bodychest.field_78809_i = false;
        this.tail = new ModelRenderer((ModelBase)this, 18, 23);
        this.tail.func_78789_a(0.0f, 0.0f, 0.0f, 1, 8, 1);
        this.tail.func_78793_a(-1.0f, 15.0f, -0.6666667f);
        this.tail.field_78795_f = 0.47304f;
        this.tail.field_78796_g = 0.0f;
        this.tail.field_78808_h = 0.0f;
        this.tail.field_78809_i = false;
        this.tail2 = new ModelRenderer((ModelBase)this, 22, 23);
        this.tail2.func_78789_a(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.tail2.func_78793_a(-1.0f, 22.1f, 2.9f);
        this.tail2.field_78795_f = 1.38309f;
        this.tail2.field_78796_g = 0.0f;
        this.tail2.field_78808_h = 0.0f;
        this.tail2.field_78809_i = false;
        this.head.func_78792_a(this.lhorn);
        this.head.func_78792_a(this.rhorn);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.head.func_78785_a(f5);
        this.body.func_78785_a(f5);
        this.rightarm.func_78785_a(f5);
        this.leftarm.func_78785_a(f5);
        this.rightleg.func_78785_a(f5);
        this.leftleg.func_78785_a(f5);
        this.rshin.func_78785_a(f5);
        this.rfoot.func_78785_a(f5);
        this.lshin.func_78785_a(f5);
        this.lfoot.func_78785_a(f5);
        this.bodymid.func_78785_a(f5);
        this.neck.func_78785_a(f5);
        this.bodychest.func_78785_a(f5);
        this.tail.func_78785_a(f5);
        this.tail2.func_78785_a(f5);
    }

    public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.head.field_78796_g = f3 / 57.29578f;
        this.head.field_78795_f = f4 / 57.29578f;
        this.rightarm.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 2.0f * f1 * 0.5f;
        this.leftarm.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 2.0f * f1 * 0.5f;
        this.rightarm.field_78808_h = 0.0f;
        this.leftarm.field_78808_h = 0.0f;
        this.rightleg.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 1.4f * f1 - 0.158f;
        this.rshin.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 1.4f * f1 + 0.82623f;
        this.rfoot.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 1.4f * f1 - 0.01403f;
        this.leftleg.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 1.4f * f1 - 0.15919f;
        this.lshin.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 1.4f * f1 + 0.82461f;
        this.lfoot.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 1.4f * f1 - 0.01214f;
        this.rightleg.field_78796_g = 0.0f;
        this.rshin.field_78796_g = 0.0f;
        this.rfoot.field_78796_g = 0.0f;
        this.leftleg.field_78796_g = 0.0f;
        this.lshin.field_78796_g = 0.0f;
        this.lfoot.field_78796_g = 0.0f;
        this.rightarm.field_78796_g = 0.0f;
        this.leftarm.field_78796_g = 0.0f;
        this.rightarm.field_78808_h += MathHelper.func_76134_b((float)(f2 * 0.09f)) * 0.05f + 0.05f;
        this.leftarm.field_78808_h -= MathHelper.func_76134_b((float)(f2 * 0.09f)) * 0.05f + 0.05f;
        this.rightarm.field_78795_f += MathHelper.func_76126_a((float)(f2 * 0.067f)) * 0.05f;
        this.leftarm.field_78795_f -= MathHelper.func_76126_a((float)(f2 * 0.067f)) * 0.05f;
    }
}

