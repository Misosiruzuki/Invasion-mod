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

public class ModelThrower
extends ModelBase {
    public ModelRenderer bipedHead = new ModelRenderer((ModelBase)this, 16, 14);
    public ModelRenderer bipedBody;
    public ModelRenderer bipedBody2;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public boolean heldItemLeft = false;
    public boolean heldItemRight = false;
    public boolean isSneak = false;

    public ModelThrower() {
        this(0.0f);
    }

    public ModelThrower(float f) {
        this(f, 0.0f);
    }

    public ModelThrower(float f, float f1) {
        this.bipedHead.func_78790_a(-2.0f, -2.0f, -2.0f, 4, 2, 4, 0.0f);
        this.bipedHead.func_78793_a(0.0f, 16.0f, 4.0f);
        this.bipedHead.field_78795_f = 0.0f;
        this.bipedHead.field_78796_g = 0.0f;
        this.bipedHead.field_78808_h = 0.0f;
        this.bipedHead.field_78809_i = false;
        this.bipedBody = new ModelRenderer((ModelBase)this, 0, 1);
        this.bipedBody.func_78790_a(-7.0f, 2.0f, -4.0f, 12, 4, 9, 0.0f);
        this.bipedBody.func_78793_a(-0.4f, 16.0f, 3.0f);
        this.bipedBody.field_78795_f = 0.0f;
        this.bipedBody.field_78796_g = 0.0f;
        this.bipedBody.field_78808_h = 0.0f;
        this.bipedBody.field_78809_i = false;
        this.bipedRightArm = new ModelRenderer((ModelBase)this, 39, 22);
        this.bipedRightArm.func_78790_a(-3.0f, 0.0f, -1.466667f, 3, 7, 3, 0.0f);
        this.bipedRightArm.func_78793_a(-6.566667f, 16.0f, 5.0f);
        this.bipedRightArm.field_78795_f = 0.0f;
        this.bipedRightArm.field_78796_g = 0.0f;
        this.bipedRightArm.field_78808_h = 0.0f;
        this.bipedRightArm.field_78809_i = false;
        this.bipedLeftArm = new ModelRenderer((ModelBase)this, 40, 16);
        this.bipedLeftArm.func_78790_a(0.0f, 0.0f, -1.0f, 2, 4, 2, 0.0f);
        this.bipedLeftArm.func_78793_a(5.0f, 16.0f, 5.0f);
        this.bipedLeftArm.field_78795_f = 0.0f;
        this.bipedLeftArm.field_78796_g = 0.0f;
        this.bipedLeftArm.field_78808_h = 0.0f;
        this.bipedLeftArm.field_78809_i = false;
        this.bipedRightLeg = new ModelRenderer((ModelBase)this, 0, 14);
        this.bipedRightLeg.func_78790_a(-2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f);
        this.bipedRightLeg.func_78793_a(-4.066667f, 22.0f, 4.0f);
        this.bipedRightLeg.field_78795_f = 0.0f;
        this.bipedRightLeg.field_78796_g = 0.0f;
        this.bipedRightLeg.field_78808_h = 0.0f;
        this.bipedRightLeg.field_78809_i = false;
        this.bipedLeftLeg = new ModelRenderer((ModelBase)this, 0, 14);
        this.bipedLeftLeg.func_78790_a(-2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f);
        this.bipedLeftLeg.func_78793_a(3.0f, 22.0f, 4.0f);
        this.bipedLeftLeg.field_78795_f = 0.0f;
        this.bipedLeftLeg.field_78796_g = 0.0f;
        this.bipedLeftLeg.field_78808_h = 0.0f;
        this.bipedLeftLeg.field_78809_i = false;
        this.bipedBody2 = new ModelRenderer((ModelBase)this, 0, 23);
        this.bipedBody2.func_78790_a(-3.666667f, 0.0f, 0.0f, 12, 2, 7, 0.0f);
        this.bipedBody2.func_78793_a(-3.0f, 16.0f, 0.0f);
        this.bipedBody2.field_78795_f = 0.0f;
        this.bipedBody2.field_78796_g = 0.0f;
        this.bipedBody2.field_78808_h = 0.0f;
        this.bipedBody2.field_78809_i = false;
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.bipedHead.func_78785_a(f5);
        this.bipedBody.func_78785_a(f5);
        this.bipedBody2.func_78785_a(f5);
        this.bipedRightArm.func_78785_a(f5);
        this.bipedLeftArm.func_78785_a(f5);
        this.bipedRightLeg.func_78785_a(f5);
        this.bipedLeftLeg.func_78785_a(f5);
    }

    public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.bipedHead.field_78796_g = f3 / 57.29578f;
        this.bipedHead.field_78795_f = f4 / 57.29578f;
        this.bipedRightArm.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 2.0f * f1 * 0.5f;
        this.bipedLeftArm.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 2.0f * f1 * 0.5f;
        this.bipedRightArm.field_78808_h = 0.0f;
        this.bipedLeftArm.field_78808_h = 0.0f;
        this.bipedRightLeg.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f)) * 1.4f * f1;
        this.bipedLeftLeg.field_78795_f = MathHelper.func_76134_b((float)(f * 0.6662f + 3.141593f)) * 1.4f * f1;
        this.bipedRightLeg.field_78796_g = 0.0f;
        this.bipedLeftLeg.field_78796_g = 0.0f;
        if (this.field_78093_q) {
            this.bipedRightArm.field_78795_f += -0.6283185f;
            this.bipedLeftArm.field_78795_f += -0.6283185f;
            this.bipedRightLeg.field_78795_f = -1.256637f;
            this.bipedLeftLeg.field_78795_f = -1.256637f;
            this.bipedRightLeg.field_78796_g = 0.314159f;
            this.bipedLeftLeg.field_78796_g = -0.314159f;
        }
        if (this.heldItemLeft) {
            this.bipedLeftArm.field_78795_f = this.bipedLeftArm.field_78795_f * 0.5f - 0.314159f;
        }
        if (this.heldItemRight) {
            this.bipedRightArm.field_78795_f = this.bipedRightArm.field_78795_f * 0.5f - 0.314159f;
        }
        this.bipedRightArm.field_78796_g = 0.0f;
        this.bipedLeftArm.field_78796_g = 0.0f;
        this.bipedRightArm.field_78808_h += MathHelper.func_76134_b((float)(f2 * 0.09f)) * 0.05f + 0.05f;
        this.bipedLeftArm.field_78808_h -= MathHelper.func_76134_b((float)(f2 * 0.09f)) * 0.05f + 0.05f;
        this.bipedRightArm.field_78795_f += MathHelper.func_76126_a((float)(f2 * 0.067f)) * 0.05f;
        this.bipedLeftArm.field_78795_f -= MathHelper.func_76126_a((float)(f2 * 0.067f)) * 0.05f;
    }
}

