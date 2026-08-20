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

public class ModelEgg
extends ModelBase {
    ModelRenderer base;
    ModelRenderer l3s4;
    ModelRenderer l3s2;
    ModelRenderer l3s3;
    ModelRenderer l3s1;
    ModelRenderer top;
    ModelRenderer l4s1;
    ModelRenderer l4s4;
    ModelRenderer l2s3;
    ModelRenderer l2s4;
    ModelRenderer l2s1;
    ModelRenderer l4s2;
    ModelRenderer l4s3;
    ModelRenderer l2s2;
    ModelRenderer l1s4;
    ModelRenderer l1s1;
    ModelRenderer l1s2;
    ModelRenderer l1s3;

    public ModelEgg() {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.top = new ModelRenderer((ModelBase)this, 0, 8);
        this.top.func_78789_a(2.0f, -11.0f, 2.0f, 5, 1, 5);
        this.top.func_78793_a(0.0f, 0.0f, 0.0f);
        this.top.func_78787_b(64, 32);
        this.top.field_78809_i = true;
        this.setRotation(this.top, 0.0f, 0.0f, 0.0f);
        this.l4s4 = new ModelRenderer((ModelBase)this, 28, 23);
        this.l4s4.func_78789_a(1.0f, -10.0f, 2.0f, 1, 3, 6);
        this.l4s4.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l4s4.func_78787_b(64, 32);
        this.l4s4.field_78809_i = true;
        this.setRotation(this.l4s4, 0.0f, 0.0f, 0.0f);
        this.l4s3 = new ModelRenderer((ModelBase)this, 0, 24);
        this.l4s3.func_78789_a(1.0f, -10.0f, 1.0f, 6, 3, 1);
        this.l4s3.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l4s3.func_78787_b(64, 32);
        this.l4s3.field_78809_i = true;
        this.setRotation(this.l4s3, 0.0f, 0.0f, 0.0f);
        this.l4s2 = new ModelRenderer((ModelBase)this, 28, 23);
        this.l4s2.func_78789_a(7.0f, -10.0f, 1.0f, 1, 3, 6);
        this.l4s2.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l4s2.func_78787_b(64, 32);
        this.l4s2.field_78809_i = true;
        this.setRotation(this.l4s2, 0.0f, 0.0f, 0.0f);
        this.l4s1 = new ModelRenderer((ModelBase)this, 0, 24);
        this.l4s1.func_78789_a(2.0f, -10.0f, 7.0f, 6, 3, 1);
        this.l4s1.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l4s1.func_78787_b(64, 32);
        this.l4s1.field_78809_i = true;
        this.setRotation(this.l4s1, 0.0f, 0.0f, 0.0f);
        this.l3s4 = new ModelRenderer((ModelBase)this, 10, 22);
        this.l3s4.func_78789_a(0.0f, -7.0f, 1.0f, 1, 2, 8);
        this.l3s4.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l3s4.func_78787_b(64, 32);
        this.l3s4.field_78809_i = true;
        this.setRotation(this.l3s4, 0.0f, 0.0f, 0.0f);
        this.l3s3 = new ModelRenderer((ModelBase)this, 0, 21);
        this.l3s3.func_78789_a(0.0f, -7.0f, 0.0f, 8, 2, 1);
        this.l3s3.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l3s3.func_78787_b(64, 32);
        this.l3s3.field_78809_i = true;
        this.setRotation(this.l3s3, 0.0f, 0.0f, 0.0f);
        this.l3s2 = new ModelRenderer((ModelBase)this, 10, 22);
        this.l3s2.func_78789_a(8.0f, -7.0f, 0.0f, 1, 2, 8);
        this.l3s2.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l3s2.func_78787_b(64, 32);
        this.l3s2.field_78809_i = true;
        this.setRotation(this.l3s2, 0.0f, 0.0f, 0.0f);
        this.l3s1 = new ModelRenderer((ModelBase)this, 0, 21);
        this.l3s1.func_78789_a(1.0f, -7.0f, 8.0f, 8, 2, 1);
        this.l3s1.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l3s1.func_78787_b(64, 32);
        this.l3s1.field_78809_i = true;
        this.setRotation(this.l3s1, 0.0f, 0.0f, 0.0f);
        this.l2s4 = new ModelRenderer((ModelBase)this, 20, 10);
        this.l2s4.func_78789_a(-1.0f, -5.0f, 0.0f, 1, 4, 9);
        this.l2s4.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l2s4.func_78787_b(64, 32);
        this.l2s4.field_78809_i = true;
        this.setRotation(this.l2s4, 0.0f, 0.0f, 0.0f);
        this.l2s3 = new ModelRenderer((ModelBase)this, 0, 16);
        this.l2s3.func_78789_a(0.0f, -5.0f, -1.0f, 9, 4, 1);
        this.l2s3.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l2s3.func_78787_b(64, 32);
        this.l2s3.field_78809_i = true;
        this.setRotation(this.l2s3, 0.0f, 0.0f, 0.0f);
        this.l2s2 = new ModelRenderer((ModelBase)this, 20, 10);
        this.l2s2.func_78789_a(9.0f, -5.0f, 0.0f, 1, 4, 9);
        this.l2s2.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l2s2.func_78787_b(64, 32);
        this.l2s2.field_78809_i = true;
        this.setRotation(this.l2s2, 0.0f, 0.0f, 0.0f);
        this.l2s1 = new ModelRenderer((ModelBase)this, 0, 16);
        this.l2s1.func_78789_a(0.0f, -5.0f, 9.0f, 9, 4, 1);
        this.l2s1.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l2s1.func_78787_b(64, 32);
        this.l2s1.field_78809_i = true;
        this.setRotation(this.l2s1, 0.0f, 0.0f, 0.0f);
        this.l1s4 = new ModelRenderer((ModelBase)this, 28, 0);
        this.l1s4.func_78789_a(0.0f, -1.0f, 1.0f, 1, 1, 8);
        this.l1s4.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l1s4.func_78787_b(64, 32);
        this.l1s4.field_78809_i = true;
        this.setRotation(this.l1s4, 0.0f, 0.0f, 0.0f);
        this.l1s3 = new ModelRenderer((ModelBase)this, 0, 14);
        this.l1s3.func_78789_a(0.0f, -1.0f, 0.0f, 8, 1, 1);
        this.l1s3.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l1s3.func_78787_b(64, 32);
        this.l1s3.field_78809_i = true;
        this.setRotation(this.l1s3, 0.0f, 0.0f, 0.0f);
        this.l1s2 = new ModelRenderer((ModelBase)this, 28, 0);
        this.l1s2.func_78789_a(8.0f, -1.0f, 0.0f, 1, 1, 8);
        this.l1s2.func_78793_a(0.0f, 0.0f, 0.0f);
        this.l1s2.func_78787_b(64, 32);
        this.l1s2.field_78809_i = true;
        this.setRotation(this.l1s2, 0.0f, 0.0f, 0.0f);
        this.l1s1 = new ModelRenderer((ModelBase)this, 0, 14);
        this.l1s1.func_78789_a(0.0f, 0.0f, 0.0f, 8, 1, 1);
        this.l1s1.func_78793_a(1.0f, -1.0f, 8.0f);
        this.l1s1.func_78787_b(64, 32);
        this.l1s1.field_78809_i = true;
        this.setRotation(this.l1s1, 0.0f, 0.0f, 0.0f);
        this.base = new ModelRenderer((ModelBase)this, 0, 0);
        this.base.func_78789_a(1.0f, 0.0f, 1.0f, 7, 1, 7);
        this.base.func_78793_a(0.0f, 0.0f, 0.0f);
        this.base.func_78787_b(64, 32);
        this.base.field_78809_i = true;
        this.setRotation(this.base, 0.0f, 0.0f, 0.0f);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.base.func_78785_a(f5);
        this.l3s4.func_78785_a(f5);
        this.l3s2.func_78785_a(f5);
        this.l3s3.func_78785_a(f5);
        this.l3s1.func_78785_a(f5);
        this.top.func_78785_a(f5);
        this.l4s1.func_78785_a(f5);
        this.l4s4.func_78785_a(f5);
        this.l2s3.func_78785_a(f5);
        this.l2s4.func_78785_a(f5);
        this.l2s1.func_78785_a(f5);
        this.l4s2.func_78785_a(f5);
        this.l4s3.func_78785_a(f5);
        this.l2s2.func_78785_a(f5);
        this.l1s4.func_78785_a(f5);
        this.l1s1.func_78785_a(f5);
        this.l1s2.func_78785_a(f5);
        this.l1s3.func_78785_a(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }

    public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.func_78087_a(f, f1, f2, f3, f4, f5, entity);
    }
}

