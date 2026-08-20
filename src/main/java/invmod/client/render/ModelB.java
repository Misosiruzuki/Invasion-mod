/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MathHelper
 */
package invmod.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(value=Side.CLIENT)
public class ModelB
extends ModelBase {
    private ModelRenderer batHead;
    private ModelRenderer batBody;
    private ModelRenderer batRightWing;
    private ModelRenderer batLeftWing;
    private ModelRenderer batOuterRightWing;
    private ModelRenderer batOuterLeftWing;

    public ModelB() {
        this.field_78090_t = 64;
        this.field_78089_u = 64;
        this.batHead = new ModelRenderer((ModelBase)this, 0, 0);
        this.batHead.func_78789_a(-3.0f, -3.0f, -3.0f, 6, 6, 6);
        ModelRenderer modelrenderer = new ModelRenderer((ModelBase)this, 24, 0);
        modelrenderer.func_78789_a(-4.0f, -6.0f, -2.0f, 3, 4, 1);
        this.batHead.func_78792_a(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer((ModelBase)this, 24, 0);
        modelrenderer1.field_78809_i = true;
        modelrenderer1.func_78789_a(1.0f, -6.0f, -2.0f, 3, 4, 1);
        this.batHead.func_78792_a(modelrenderer1);
        this.batBody = new ModelRenderer((ModelBase)this, 0, 16);
        this.batBody.func_78789_a(-3.0f, 4.0f, -3.0f, 6, 12, 6);
        this.batBody.func_78784_a(0, 34).func_78789_a(-5.0f, 16.0f, 0.0f, 10, 6, 1);
        this.batRightWing = new ModelRenderer((ModelBase)this, 42, 0);
        this.batRightWing.func_78789_a(-12.0f, 1.0f, 1.5f, 10, 16, 1);
        this.batOuterRightWing = new ModelRenderer((ModelBase)this, 24, 16);
        this.batOuterRightWing.func_78793_a(-12.0f, 1.0f, 1.5f);
        this.batOuterRightWing.func_78789_a(-8.0f, 1.0f, 0.0f, 8, 12, 1);
        this.batLeftWing = new ModelRenderer((ModelBase)this, 42, 0);
        this.batLeftWing.field_78809_i = true;
        this.batLeftWing.func_78789_a(2.0f, 1.0f, 1.5f, 10, 16, 1);
        this.batOuterLeftWing = new ModelRenderer((ModelBase)this, 24, 16);
        this.batOuterLeftWing.field_78809_i = true;
        this.batOuterLeftWing.func_78793_a(12.0f, 1.0f, 1.5f);
        this.batOuterLeftWing.func_78789_a(0.0f, 1.0f, 0.0f, 8, 12, 1);
        this.batBody.func_78792_a(this.batRightWing);
        this.batBody.func_78792_a(this.batLeftWing);
        this.batRightWing.func_78792_a(this.batOuterRightWing);
        this.batLeftWing.func_78792_a(this.batOuterLeftWing);
    }

    public int getBatSize() {
        return 36;
    }

    public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.batHead.field_78795_f = par6 / 57.295776f;
        this.batHead.field_78796_g = par5 / 57.295776f;
        this.batHead.field_78808_h = 0.0f;
        this.batHead.func_78793_a(0.0f, 0.0f, 0.0f);
        this.batRightWing.func_78793_a(0.0f, 0.0f, 0.0f);
        this.batLeftWing.func_78793_a(0.0f, 0.0f, 0.0f);
        this.batBody.field_78795_f = 0.7853982f + MathHelper.func_76134_b((float)(par4 * 0.1f)) * 0.15f;
        this.batBody.field_78796_g = 0.0f;
        this.batRightWing.field_78796_g = MathHelper.func_76134_b((float)(par4 * 1.3f)) * 3.141593f * 0.25f;
        this.batLeftWing.field_78796_g = -this.batRightWing.field_78796_g;
        this.batOuterRightWing.field_78796_g = this.batRightWing.field_78796_g * 0.5f;
        this.batOuterLeftWing.field_78796_g = -this.batRightWing.field_78796_g * 0.5f;
        this.batHead.func_78785_a(par7);
        this.batBody.func_78785_a(par7);
    }
}

