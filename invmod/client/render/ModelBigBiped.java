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

public class ModelBigBiped
extends ModelBase {
    private ModelRenderer head;
    private ModelRenderer body;
    private ModelRenderer rightArm;
    private ModelRenderer leftArm;
    private ModelRenderer rightLeg;
    private ModelRenderer leftLeg;
    private ModelRenderer headwear;
    private int heldItemLeft;
    private int heldItemRight;
    private boolean isSneaking = false;
    private boolean aimedBow;

    public ModelBigBiped() {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.head = new ModelRenderer((ModelBase)this, 0, 0);
        this.head.func_78789_a(-3.533333f, -7.0f, -3.5f, 7, 7, 7);
        this.head.func_78793_a(0.0f, 0.0f, 0.0f);
        this.head.func_78787_b(64, 32);
        this.head.field_78809_i = true;
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.body = new ModelRenderer((ModelBase)this, 16, 15);
        this.body.func_78789_a(-5.0f, 0.0f, -3.0f, 10, 12, 5);
        this.body.func_78793_a(0.0f, 0.0f, 0.0f);
        this.body.func_78787_b(64, 32);
        this.body.field_78809_i = true;
        this.setRotation(this.body, 0.0f, 0.0f, 0.0f);
        this.rightArm = new ModelRenderer((ModelBase)this, 46, 15);
        this.rightArm.func_78789_a(-3.0f, -2.0f, -2.0f, 4, 13, 4);
        this.rightArm.func_78793_a(-6.0f, 2.0f, 0.0f);
        this.rightArm.func_78787_b(64, 32);
        this.rightArm.field_78809_i = true;
        this.setRotation(this.rightArm, 0.0f, 0.0f, 0.0f);
        this.leftArm = new ModelRenderer((ModelBase)this, 46, 15);
        this.leftArm.func_78789_a(-1.0f, -2.0f, -2.0f, 4, 13, 4);
        this.leftArm.func_78793_a(6.0f, 2.0f, 0.0f);
        this.leftArm.func_78787_b(64, 32);
        this.leftArm.field_78809_i = true;
        this.setRotation(this.leftArm, 0.0f, 0.0f, 0.0f);
        this.rightLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.rightLeg.func_78789_a(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.rightLeg.func_78793_a(-2.0f, 12.0f, 0.0f);
        this.rightLeg.func_78787_b(64, 32);
        this.rightLeg.field_78809_i = true;
        this.setRotation(this.rightLeg, 0.0f, 0.0f, 0.0f);
        this.leftLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.leftLeg.func_78789_a(-2.0f, 0.0f, -2.0f, 4, 12, 4);
        this.leftLeg.func_78793_a(2.0f, 12.0f, 0.0f);
        this.leftLeg.func_78787_b(64, 32);
        this.leftLeg.field_78809_i = true;
        this.setRotation(this.leftLeg, 0.0f, 0.0f, 0.0f);
        this.headwear = new ModelRenderer((ModelBase)this, 32, 0);
        this.headwear.func_78790_a(-3.533333f, -7.0f, -3.5f, 7, 7, 7, 0.5f);
        this.headwear.func_78793_a(0.0f, 0.0f, 0.0f);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.head.func_78785_a(f5);
        this.body.func_78785_a(f5);
        this.rightArm.func_78785_a(f5);
        this.leftArm.func_78785_a(f5);
        this.rightLeg.func_78785_a(f5);
        this.leftLeg.func_78785_a(f5);
    }

    public void setSneaking(boolean flag) {
        this.isSneaking = flag;
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }

    public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
        this.head.field_78796_g = par4 / 57.295776f;
        this.head.field_78795_f = par5 / 57.295776f;
        this.headwear.field_78796_g = this.head.field_78796_g;
        this.headwear.field_78795_f = this.head.field_78795_f;
        this.rightArm.field_78795_f = MathHelper.func_76134_b((float)(par1 * 0.6662f + 3.141593f)) * 2.0f * par2 * 0.5f;
        this.leftArm.field_78795_f = MathHelper.func_76134_b((float)(par1 * 0.6662f)) * 2.0f * par2 * 0.5f;
        this.rightArm.field_78808_h = 0.0f;
        this.leftArm.field_78808_h = 0.0f;
        this.rightLeg.field_78795_f = MathHelper.func_76134_b((float)(par1 * 0.6662f)) * 1.4f * par2;
        this.leftLeg.field_78795_f = MathHelper.func_76134_b((float)(par1 * 0.6662f + 3.141593f)) * 1.4f * par2;
        this.rightLeg.field_78796_g = 0.0f;
        this.leftLeg.field_78796_g = 0.0f;
        if (this.field_78093_q) {
            this.rightArm.field_78795_f += -0.6283186f;
            this.leftArm.field_78795_f += -0.6283186f;
            this.rightLeg.field_78795_f = -1.256637f;
            this.leftLeg.field_78795_f = -1.256637f;
            this.rightLeg.field_78796_g = 0.3141593f;
            this.leftLeg.field_78796_g = -0.3141593f;
        }
        if (this.heldItemLeft != 0) {
            this.leftArm.field_78795_f = this.leftArm.field_78795_f * 0.5f - 0.3141593f * (float)this.heldItemLeft;
        }
        if (this.heldItemRight != 0) {
            this.rightArm.field_78795_f = this.rightArm.field_78795_f * 0.5f - 0.3141593f * (float)this.heldItemRight;
        }
        this.rightArm.field_78796_g = 0.0f;
        this.leftArm.field_78796_g = 0.0f;
        if (this.field_78095_p > -9990.0f) {
            float f = this.field_78095_p;
            this.body.field_78796_g = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f) * 3.141593f * 2.0f)) * 0.2f;
            this.rightArm.field_78798_e = MathHelper.func_76126_a((float)this.body.field_78796_g) * 5.0f;
            this.rightArm.field_78800_c = -MathHelper.func_76134_b((float)this.body.field_78796_g) * 5.0f;
            this.leftArm.field_78798_e = -MathHelper.func_76126_a((float)this.body.field_78796_g) * 5.0f;
            this.leftArm.field_78800_c = MathHelper.func_76134_b((float)this.body.field_78796_g) * 5.0f;
            this.rightArm.field_78796_g += this.body.field_78796_g;
            this.leftArm.field_78796_g += this.body.field_78796_g;
            this.leftArm.field_78795_f += this.body.field_78796_g;
            f = 1.0f - this.field_78095_p;
            f *= f;
            f *= f;
            f = 1.0f - f;
            float f2 = MathHelper.func_76126_a((float)(f * 3.141593f));
            float f4 = MathHelper.func_76126_a((float)(this.field_78095_p * 3.141593f)) * -(this.head.field_78795_f - 0.7f) * 0.75f;
            ModelRenderer tmp570_567 = this.rightArm;
            tmp570_567.field_78795_f = (float)((double)tmp570_567.field_78795_f - ((double)f2 * 1.2 + (double)f4));
            this.rightArm.field_78796_g += this.body.field_78796_g * 2.0f;
            this.rightArm.field_78808_h = MathHelper.func_76126_a((float)(this.field_78095_p * 3.141593f)) * -0.4f;
        }
        if (this.isSneaking) {
            this.body.field_78795_f = 0.7f;
            this.body.field_78797_d = 1.5f;
            this.rightLeg.field_78795_f -= 0.0f;
            this.leftLeg.field_78795_f -= 0.0f;
            this.rightArm.field_78795_f += 0.4f;
            this.leftArm.field_78795_f += 0.4f;
            this.rightLeg.field_78798_e = 7.0f;
            this.leftLeg.field_78798_e = 7.0f;
            this.rightLeg.field_78797_d = 12.0f;
            this.leftLeg.field_78797_d = 12.0f;
            this.rightArm.field_78797_d = 3.5f;
            this.leftArm.field_78797_d = 3.5f;
            this.head.field_78797_d = 3.0f;
        } else {
            this.body.field_78795_f = 0.0f;
            this.body.field_78797_d = 0.0f;
            this.rightLeg.field_78798_e = 0.0f;
            this.leftLeg.field_78798_e = 0.0f;
            this.rightLeg.field_78797_d = 12.0f;
            this.leftLeg.field_78797_d = 12.0f;
            this.rightArm.field_78797_d = 2.0f;
            this.leftArm.field_78797_d = 2.0f;
            this.head.field_78797_d = 0.0f;
            this.rightArm.field_78800_c = -6.0f;
            this.leftArm.field_78800_c = 6.0f;
        }
        this.rightArm.field_78808_h += MathHelper.func_76134_b((float)(par3 * 0.09f)) * 0.05f + 0.05f;
        this.leftArm.field_78808_h -= MathHelper.func_76134_b((float)(par3 * 0.09f)) * 0.05f + 0.05f;
        this.rightArm.field_78795_f += MathHelper.func_76126_a((float)(par3 * 0.067f)) * 0.05f;
        this.leftArm.field_78795_f -= MathHelper.func_76126_a((float)(par3 * 0.067f)) * 0.05f;
        if (this.aimedBow) {
            float f1 = 0.0f;
            float f3 = 0.0f;
            this.rightArm.field_78808_h = 0.0f;
            this.leftArm.field_78808_h = 0.0f;
            this.rightArm.field_78796_g = -(0.1f - f1 * 0.6f) + this.head.field_78796_g;
            this.leftArm.field_78796_g = 0.1f - f1 * 0.6f + this.head.field_78796_g + 0.4f;
            this.rightArm.field_78795_f = -1.570796f + this.head.field_78795_f;
            this.leftArm.field_78795_f = -1.570796f + this.head.field_78795_f;
            this.rightArm.field_78795_f -= f1 * 1.2f - f3 * 0.4f;
            this.leftArm.field_78795_f -= f1 * 1.2f - f3 * 0.4f;
            this.rightArm.field_78808_h += MathHelper.func_76134_b((float)(par3 * 0.09f)) * 0.05f + 0.05f;
            this.leftArm.field_78808_h -= MathHelper.func_76134_b((float)(par3 * 0.09f)) * 0.05f + 0.05f;
            this.rightArm.field_78795_f += MathHelper.func_76126_a((float)(par3 * 0.067f)) * 0.05f;
            this.leftArm.field_78795_f -= MathHelper.func_76126_a((float)(par3 * 0.067f)) * 0.05f;
        }
    }

    public void itemArmPostRender(float scale) {
        this.rightArm.func_78794_c(scale);
    }
}

