/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 */
package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTest
extends ModelBase {
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;

    public ModelTest() {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.Shape1 = new ModelRenderer((ModelBase)this, 0, 0);
        this.Shape1.func_78789_a(-3.0f, -1.5f, -1.5f, 7, 3, 3);
        this.Shape1.func_78793_a(0.0f, 0.0f, 0.0f);
        this.Shape1.func_78787_b(64, 32);
        this.setRotation(this.Shape1, 0.0f, 0.0f, 0.0f);
        this.Shape2 = new ModelRenderer((ModelBase)this, 0, 8);
        this.Shape2.func_78789_a(4.0f, -0.5f, -0.5f, 1, 1, 1);
        this.Shape2.func_78793_a(0.0f, 0.0f, 0.0f);
        this.Shape2.func_78787_b(64, 32);
        this.setRotation(this.Shape2, 0.0f, 0.0f, 0.0f);
        this.Shape3 = new ModelRenderer((ModelBase)this, 0, 6);
        this.Shape3.func_78789_a(2.0f, 1.5f, -0.5f, 1, 1, 1);
        this.Shape3.func_78793_a(0.0f, 0.0f, 0.0f);
        this.Shape3.func_78787_b(64, 32);
        this.setRotation(this.Shape3, 0.0f, 0.0f, 0.0f);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.Shape1.func_78785_a(f5);
        this.Shape2.func_78785_a(f5);
        this.Shape3.func_78785_a(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }

    public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.setRotation(this.Shape1, f, f1, f2);
        this.setRotation(this.Shape2, f, f1, f2);
        this.setRotation(this.Shape3, f, f1, f2);
    }
}

