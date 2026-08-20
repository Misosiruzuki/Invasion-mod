/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 */
package invmod.client.render;

import invmod.common.util.PosRotate3D;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBurrower
extends ModelBase {
    ModelRenderer head;
    ModelRenderer seg1;
    ModelRenderer seg2;
    ModelRenderer seg3;

    public ModelBurrower() {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.head = new ModelRenderer((ModelBase)this, 0, 0);
        this.head.func_78789_a(-2.0f, -2.5f, -2.5f, 4, 5, 5);
        this.head.func_78793_a(0.0f, 0.0f, 0.0f);
        this.head.func_78787_b(64, 32);
        this.head.field_78809_i = true;
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.seg1 = new ModelRenderer((ModelBase)this, 0, 0);
        this.seg1.func_78789_a(-2.0f, -2.5f, -2.5f, 4, 5, 5);
        this.seg1.func_78793_a(-4.0f, 0.0f, 0.0f);
        this.seg1.func_78787_b(64, 32);
        this.seg1.field_78809_i = true;
        this.setRotation(this.seg1, 0.0f, 0.0f, 0.0f);
        this.seg2 = new ModelRenderer((ModelBase)this, 0, 0);
        this.seg2.func_78789_a(-2.0f, -2.5f, -2.5f, 4, 5, 5);
        this.seg2.func_78793_a(-8.0f, 0.0f, 0.0f);
        this.seg2.func_78787_b(64, 32);
        this.seg2.field_78809_i = true;
        this.setRotation(this.seg2, 0.0f, 0.0f, 0.0f);
        this.seg3 = new ModelRenderer((ModelBase)this, 0, 0);
        this.seg3.func_78789_a(-2.0f, -2.5f, -2.5f, 4, 5, 5);
        this.seg3.func_78793_a(-12.0f, 0.0f, 0.0f);
        this.seg3.func_78787_b(64, 32);
        this.seg3.field_78809_i = true;
        this.setRotation(this.seg3, 0.0f, 0.0f, 0.0f);
    }

    public void render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale) {
        super.func_78088_a(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modelScale);
        if (pos.length >= 16) {
            this.head.func_78793_a((float)pos[0].getPosX(), (float)pos[0].getPosY(), (float)pos[0].getPosZ());
            this.setRotation(this.head, pos[0].getRotX(), pos[0].getRotY(), pos[0].getRotZ());
            this.seg1.func_78793_a((float)pos[1].getPosX(), (float)pos[1].getPosY(), (float)pos[1].getPosZ());
            this.setRotation(this.seg1, pos[1].getRotX(), pos[1].getRotY(), pos[1].getRotZ());
            this.seg2.func_78793_a((float)pos[2].getPosX(), (float)pos[2].getPosY(), (float)pos[2].getPosZ());
            this.setRotation(this.seg2, pos[2].getRotX(), pos[2].getRotY(), pos[2].getRotZ());
            this.seg3.func_78793_a((float)pos[3].getPosX(), (float)pos[3].getPosY(), (float)pos[3].getPosZ());
            this.setRotation(this.seg3, pos[3].getRotX(), pos[3].getRotY(), pos[3].getRotZ());
            this.head.func_78785_a(modelScale);
            this.seg1.func_78785_a(modelScale);
            this.seg2.func_78785_a(modelScale);
            this.seg3.func_78785_a(modelScale);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }
}

