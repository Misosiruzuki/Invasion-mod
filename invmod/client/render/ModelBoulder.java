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

public class ModelBoulder
extends ModelBase {
    ModelRenderer boulder = new ModelRenderer((ModelBase)this, 0, 0);

    public ModelBoulder() {
        this.boulder.func_78789_a(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        this.boulder.func_78793_a(0.0f, 0.0f, 0.0f);
        this.boulder.field_78795_f = 0.0f;
        this.boulder.field_78796_g = 0.0f;
        this.boulder.field_78808_h = 0.0f;
        this.boulder.field_78809_i = false;
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.boulder.func_78785_a(f5);
    }

    public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.boulder.field_78795_f = f;
        this.boulder.field_78796_g = f1;
        this.boulder.field_78808_h = f2;
    }
}

