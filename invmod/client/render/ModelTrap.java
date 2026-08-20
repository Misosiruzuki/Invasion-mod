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

public class ModelTrap
extends ModelBase {
    ModelRenderer Core;
    ModelRenderer CoreFire;
    ModelRenderer Clasp1a;
    ModelRenderer Clasp1b;
    ModelRenderer Clasp2b;
    ModelRenderer Clasp2a;
    ModelRenderer Clasp3a;
    ModelRenderer Clasp3b;
    ModelRenderer Clasp4a;
    ModelRenderer Clasp4b;
    ModelRenderer Base;
    ModelRenderer BaseS1;
    ModelRenderer BaseS2;

    public ModelTrap() {
        this.field_78090_t = 32;
        this.field_78089_u = 32;
        this.Core = new ModelRenderer((ModelBase)this, 0, 13);
        this.Core.func_78789_a(0.0f, 0.0f, 0.0f, 4, 2, 4);
        this.Core.func_78793_a(-2.0f, -2.0f, -2.0f);
        this.Core.func_78787_b(32, 32);
        this.Core.field_78809_i = true;
        this.CoreFire = new ModelRenderer((ModelBase)this, 5, 7);
        this.CoreFire.func_78789_a(0.0f, 0.0f, 0.0f, 4, 2, 4);
        this.CoreFire.func_78793_a(-2.0f, -2.0f, -2.0f);
        this.CoreFire.func_78787_b(32, 32);
        this.CoreFire.field_78809_i = true;
        this.setRotation(this.Core, 0.0f, 0.0f, 0.0f);
        this.Clasp1a = new ModelRenderer((ModelBase)this, 0, 0);
        this.Clasp1a.func_78789_a(0.0f, 0.0f, 0.0f, 2, 2, 1);
        this.Clasp1a.func_78793_a(-1.0f, -2.0f, 2.0f);
        this.Clasp1a.func_78787_b(32, 32);
        this.Clasp1a.field_78809_i = true;
        this.setRotation(this.Clasp1a, 0.0f, 0.0f, 0.0f);
        this.Clasp1b = new ModelRenderer((ModelBase)this, 0, 7);
        this.Clasp1b.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.Clasp1b.func_78793_a(-1.0f, -1.0f, 3.0f);
        this.Clasp1b.func_78787_b(32, 32);
        this.Clasp1b.field_78809_i = true;
        this.setRotation(this.Clasp1b, 0.0f, 0.0f, 0.0f);
        this.Clasp2b = new ModelRenderer((ModelBase)this, 0, 19);
        this.Clasp2b.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.Clasp2b.func_78793_a(3.0f, -1.0f, -1.0f);
        this.Clasp2b.func_78787_b(32, 32);
        this.Clasp2b.field_78809_i = true;
        this.setRotation(this.Clasp2b, 0.0f, 0.0f, 0.0f);
        this.Clasp2a = new ModelRenderer((ModelBase)this, 0, 3);
        this.Clasp2a.func_78789_a(0.0f, 0.0f, 0.0f, 1, 2, 2);
        this.Clasp2a.func_78793_a(2.0f, -2.0f, -1.0f);
        this.Clasp2a.func_78787_b(32, 32);
        this.Clasp2a.field_78809_i = true;
        this.setRotation(this.Clasp2a, 0.0f, 0.0f, 0.0f);
        this.Clasp3a = new ModelRenderer((ModelBase)this, 0, 0);
        this.Clasp3a.func_78789_a(0.0f, 0.0f, 0.0f, 2, 2, 1);
        this.Clasp3a.func_78793_a(-1.0f, -2.0f, -3.0f);
        this.Clasp3a.func_78787_b(32, 32);
        this.Clasp3a.field_78809_i = true;
        this.setRotation(this.Clasp3a, 0.0f, 0.0f, 0.0f);
        this.Clasp3b = new ModelRenderer((ModelBase)this, 0, 7);
        this.Clasp3b.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.Clasp3b.func_78793_a(-1.0f, -1.0f, -5.0f);
        this.Clasp3b.func_78787_b(32, 32);
        this.Clasp3b.field_78809_i = true;
        this.setRotation(this.Clasp3b, 0.0f, 0.0f, 0.0f);
        this.Clasp4a = new ModelRenderer((ModelBase)this, 0, 3);
        this.Clasp4a.func_78789_a(0.0f, 0.0f, 0.0f, 1, 2, 2);
        this.Clasp4a.func_78793_a(-3.0f, -2.0f, -1.0f);
        this.Clasp4a.func_78787_b(32, 32);
        this.Clasp4a.field_78809_i = true;
        this.setRotation(this.Clasp4a, 0.0f, 0.0f, 0.0f);
        this.Clasp4b = new ModelRenderer((ModelBase)this, 0, 19);
        this.Clasp4b.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.Clasp4b.func_78793_a(-5.0f, -1.0f, -1.0f);
        this.Clasp4b.func_78787_b(32, 32);
        this.Clasp4b.field_78809_i = true;
        this.setRotation(this.Clasp4b, 0.0f, 0.0f, 0.0f);
        this.Base = new ModelRenderer((ModelBase)this, 0, 23);
        this.Base.func_78789_a(0.0f, 0.0f, 0.0f, 4, 1, 2);
        this.Base.func_78793_a(-2.0f, -1.0f, -1.0f);
        this.Base.func_78787_b(32, 32);
        this.Base.field_78809_i = true;
        this.setRotation(this.Base, 0.0f, 0.0f, 0.0f);
        this.BaseS1 = new ModelRenderer((ModelBase)this, 0, 27);
        this.BaseS1.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 1);
        this.BaseS1.func_78793_a(-1.0f, -1.0f, 1.0f);
        this.BaseS1.func_78787_b(32, 32);
        this.BaseS1.field_78809_i = true;
        this.setRotation(this.BaseS1, 0.0f, 0.0f, 0.0f);
        this.BaseS2 = new ModelRenderer((ModelBase)this, 0, 27);
        this.BaseS2.func_78789_a(0.0f, 0.0f, 0.0f, 2, 1, 1);
        this.BaseS2.func_78793_a(-1.0f, -1.0f, -2.0f);
        this.BaseS2.func_78787_b(32, 32);
        this.BaseS2.field_78809_i = true;
        this.setRotation(this.BaseS2, 0.0f, 0.0f, 0.0f);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean isEmpty, int type) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        if (!isEmpty) {
            if (type == 1) {
                this.Core.func_78785_a(f5);
            } else if (type == 2) {
                this.CoreFire.func_78785_a(f5);
            }
        }
        this.Clasp1a.func_78785_a(f5);
        this.Clasp1b.func_78785_a(f5);
        this.Clasp2b.func_78785_a(f5);
        this.Clasp2a.func_78785_a(f5);
        this.Clasp3a.func_78785_a(f5);
        this.Clasp3b.func_78785_a(f5);
        this.Clasp4a.func_78785_a(f5);
        this.Clasp4b.func_78785_a(f5);
        this.Base.func_78785_a(f5);
        this.BaseS1.func_78785_a(f5);
        this.BaseS2.func_78785_a(f5);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.render(entity, f, f1, f2, f3, f4, f5, false, 0);
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

