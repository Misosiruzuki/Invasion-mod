/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.RenderBlocks
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.client.render.ModelBigBiped;
import invmod.common.entity.EntityIMZombie;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderIMZombie
extends RenderLiving {
    private static final ResourceLocation t_old = new ResourceLocation("invmod:textures/zombie_old.png");
    private static final ResourceLocation t_T1a = new ResourceLocation("invmod:textures/zombieT1a.png");
    private static final ResourceLocation t_pig = new ResourceLocation("invmod:textures/pigzombie64x32.png");
    private static final ResourceLocation t_T2 = new ResourceLocation("invmod:textures/zombieT2.png");
    private static final ResourceLocation t_T2a = new ResourceLocation("invmod:textures/zombieT2a.png");
    private static final ResourceLocation t_T3 = new ResourceLocation("invmod:textures/zombieT3.png");
    private static final ResourceLocation t_tar = new ResourceLocation("invmod:textures/zombietar.png");
    protected ModelBiped modelBiped;
    protected ModelBigBiped modelBigBiped;

    public RenderIMZombie(ModelBiped model, float par2) {
        this(model, par2, 1.0f);
    }

    public RenderIMZombie(ModelBiped model, float par2, float par3) {
        super((ModelBase)model, par2);
        this.modelBiped = model;
        this.modelBigBiped = new ModelBigBiped();
    }

    public void func_76986_a(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
        if (entity instanceof EntityIMZombie) {
            if (((EntityIMZombie)entity).isBigRenderTempHack()) {
                this.field_77045_g = this.modelBigBiped;
                this.modelBigBiped.setSneaking(entity.func_70093_af());
            } else {
                this.field_77045_g = this.modelBiped;
            }
            super.func_76986_a(entity, par2, par4, par6, par8, par9);
        }
    }

    protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
        float f = ((EntityIMZombie)par1EntityLiving).scaleAmount();
        GL11.glScalef((float)f, (float)((2.0f + f) / 3.0f), (float)f);
    }

    protected void func_77029_c(EntityLivingBase entity, float par2) {
        super.func_77029_c(entity, par2);
        ItemStack itemstack = entity.func_70694_bm();
        if (itemstack != null) {
            GL11.glPushMatrix();
            if (((EntityIMZombie)entity).isBigRenderTempHack()) {
                this.modelBigBiped.itemArmPostRender(0.0625f);
            } else {
                this.modelBiped.field_78112_f.func_78794_c(0.0625f);
            }
            GL11.glTranslatef((float)-0.0625f, (float)0.4375f, (float)0.0625f);
            if (RenderBlocks.func_147739_a((int)Block.func_149634_a((Item)itemstack.func_77973_b()).func_149645_b())) {
                float f = 0.5f;
                GL11.glTranslatef((float)0.0f, (float)0.1875f, (float)-0.3125f);
                GL11.glRotatef((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glScalef((float)(f *= 0.75f), (float)(-f), (float)f);
            } else if (itemstack.func_77973_b() == Items.field_151031_f) {
                float f1 = 0.625f;
                GL11.glTranslatef((float)0.0f, (float)0.125f, (float)0.3125f);
                GL11.glRotatef((float)-20.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glScalef((float)f1, (float)(-f1), (float)f1);
                GL11.glRotatef((float)-100.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            } else if (itemstack.func_77973_b().func_77662_d()) {
                float f2 = 0.625f;
                GL11.glTranslatef((float)0.0f, (float)0.1875f, (float)0.0f);
                GL11.glScalef((float)f2, (float)(-f2), (float)f2);
                GL11.glRotatef((float)-100.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            } else {
                float f3 = 0.375f;
                GL11.glTranslatef((float)0.25f, (float)0.1875f, (float)-0.1875f);
                GL11.glScalef((float)f3, (float)f3, (float)f3);
                GL11.glRotatef((float)60.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            this.field_76990_c.field_78721_f.func_78443_a(entity, itemstack, 0);
            if (itemstack.func_77973_b().func_77623_v()) {
                for (int x = 1; x < itemstack.func_77973_b().getRenderPasses(itemstack.func_77960_j()); ++x) {
                    this.field_76990_c.field_78721_f.func_78443_a(entity, itemstack, x);
                }
            }
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getTexture(EntityIMZombie entity) {
        switch (entity.getTextureId()) {
            case 0: {
                return t_old;
            }
            case 1: {
                return t_T1a;
            }
            case 2: {
                return t_T2;
            }
            case 3: {
                return t_pig;
            }
            case 4: {
                return t_T2a;
            }
            case 5: {
                return t_tar;
            }
            case 6: {
                return t_T3;
            }
        }
        return t_old;
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return this.getTexture((EntityIMZombie)entity);
    }
}

