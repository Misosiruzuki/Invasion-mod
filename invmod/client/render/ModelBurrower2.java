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

public class ModelBurrower2
extends ModelBase {
    ModelRenderer head;
    ModelRenderer[] segments;

    public ModelBurrower2(int numberOfSegments) {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.head = new ModelRenderer((ModelBase)this, 0, 0);
        this.head.func_78789_a(-1.0f, -3.0f, -3.0f, 2, 6, 6);
        this.head.func_78793_a(0.0f, 0.0f, 0.0f);
        this.head.func_78787_b(64, 32);
        this.head.field_78809_i = true;
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.segments = new ModelRenderer[numberOfSegments];
        for (int i = 0; i < numberOfSegments; ++i) {
            this.segments[i] = new ModelRenderer((ModelBase)this, 0, 0);
            if (i % 2 == 0) {
                this.segments[i].func_78789_a(-0.5f, -3.5f, -3.5f, 2, 7, 7);
            } else {
                this.segments[i].func_78789_a(-0.5f, -2.5f, -2.5f, 2, 5, 5);
            }
            this.segments[i].func_78793_a(-4.0f, 0.0f, 0.0f);
            this.segments[i].func_78787_b(64, 32);
            this.segments[i].field_78809_i = true;
            this.setRotation(this.segments[i], 0.0f, 0.0f, 0.0f);
        }
    }

    public void render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale) {
        super.func_78088_a(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modelScale);
        this.head.func_78793_a((float)pos[0].getPosX(), (float)pos[0].getPosY(), (float)pos[0].getPosZ());
        this.setRotation(this.head, pos[0].getRotX(), pos[0].getRotY(), pos[0].getRotZ());
        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i].func_78793_a((float)pos[i + 1].getPosX(), (float)pos[i + 1].getPosY(), (float)pos[i + 1].getPosZ());
            this.setRotation(this.segments[i], pos[i + 1].getRotX(), pos[i + 1].getRotY(), pos[i + 1].getRotZ());
            this.segments[i].func_78785_a(modelScale);
        }
        this.head.func_78785_a(modelScale);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }
}

