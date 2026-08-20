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

import invmod.client.render.animation.InterpType;
import invmod.client.render.animation.KeyFrame;
import invmod.client.render.animation.ModelAnimator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBird
extends ModelBase {
    private ModelAnimator animationWingFlap;
    private ModelRenderer body;
    private ModelRenderer rightwing1;
    private ModelRenderer leftwing1;
    private ModelRenderer head;
    private ModelRenderer beak;
    private ModelRenderer leftwing2;
    private ModelRenderer rightwing2;
    private ModelRenderer tail;
    private ModelRenderer legR;
    private ModelRenderer ltoeR;
    private ModelRenderer btoeR;
    private ModelRenderer rtoeR;
    private ModelRenderer thighR;
    private ModelRenderer legL;
    private ModelRenderer ltoeL;
    private ModelRenderer btoeL;
    private ModelRenderer rtoeL;
    private ModelRenderer thighL;

    public ModelBird() {
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        this.body = new ModelRenderer((ModelBase)this, 24, 0);
        this.body.func_78789_a(-3.5f, 0.0f, -3.5f, 7, 12, 7);
        this.body.func_78793_a(3.5f, 7.0f, 3.5f);
        this.body.func_78787_b(64, 32);
        this.body.field_78809_i = true;
        this.setRotation(this.body, 0.0f, 0.0f, 0.0f);
        this.rightwing1 = new ModelRenderer((ModelBase)this, 0, 22);
        this.rightwing1.func_78789_a(-7.0f, -1.0f, -1.0f, 7, 9, 1);
        this.rightwing1.func_78793_a(-3.5f, 2.0f, 3.5f);
        this.rightwing1.func_78787_b(64, 32);
        this.rightwing1.field_78809_i = false;
        this.setRotation(this.rightwing1, 0.0f, 0.0f, 0.0f);
        this.rightwing2 = new ModelRenderer((ModelBase)this, 16, 24);
        this.rightwing2.func_78789_a(-14.0f, -1.0f, -0.5f, 14, 7, 1);
        this.rightwing2.func_78793_a(-7.0f, 0.0f, -0.5f);
        this.rightwing2.func_78787_b(64, 32);
        this.rightwing2.field_78809_i = false;
        this.setRotation(this.rightwing2, 0.0f, 0.0f, 0.0f);
        this.rightwing1.func_78792_a(this.rightwing2);
        this.leftwing1 = new ModelRenderer((ModelBase)this, 0, 22);
        this.leftwing1.func_78789_a(0.0f, -1.0f, -1.0f, 7, 9, 1);
        this.leftwing1.func_78793_a(3.5f, 2.0f, 3.5f);
        this.leftwing1.func_78787_b(64, 32);
        this.leftwing1.field_78809_i = true;
        this.setRotation(this.leftwing1, 0.0f, 0.0f, 0.0f);
        this.leftwing2 = new ModelRenderer((ModelBase)this, 16, 24);
        this.leftwing2.func_78789_a(0.0f, -1.0f, -0.5f, 14, 7, 1);
        this.leftwing2.func_78793_a(7.0f, 0.0f, -0.5f);
        this.leftwing2.func_78787_b(64, 32);
        this.leftwing2.field_78809_i = true;
        this.setRotation(this.leftwing2, 0.0f, 0.0f, 0.0f);
        this.leftwing1.func_78792_a(this.leftwing2);
        this.head = new ModelRenderer((ModelBase)this, 2, 0);
        this.head.func_78789_a(-2.5f, -5.0f, -4.0f, 5, 6, 6);
        this.head.func_78793_a(0.0f, 0.5f, 1.5f);
        this.head.func_78787_b(64, 32);
        this.head.field_78809_i = true;
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.beak = new ModelRenderer((ModelBase)this, 19, 0);
        this.beak.func_78789_a(-0.5f, 0.0f, -2.0f, 1, 2, 2);
        this.beak.func_78793_a(0.0f, -3.0f, -4.0f);
        this.beak.func_78787_b(64, 32);
        this.beak.field_78809_i = true;
        this.setRotation(this.beak, 0.0f, 0.0f, 0.0f);
        this.head.func_78792_a(this.beak);
        this.tail = new ModelRenderer((ModelBase)this, 0, 12);
        this.tail.func_78789_a(-3.0f, 0.0f, 0.0f, 5, 9, 1);
        this.tail.func_78793_a(0.5f, 12.0f, 2.5f);
        this.tail.func_78787_b(64, 32);
        this.tail.field_78809_i = true;
        this.setRotation(this.tail, 0.446143f, 0.0f, 0.0f);
        this.legR = new ModelRenderer((ModelBase)this, 13, 12);
        this.legR.func_78789_a(-0.5f, 0.0f, -0.5f, 1, 5, 1);
        this.legR.func_78793_a(0.0f, 0.0f, 0.0f);
        this.legR.func_78787_b(64, 32);
        this.legR.field_78809_i = false;
        this.setRotation(this.legR, 0.0f, 0.0f, 0.0f);
        this.ltoeR = new ModelRenderer((ModelBase)this, 0, 0);
        this.ltoeR.func_78789_a(0.0f, 0.0f, -2.0f, 1, 1, 2);
        this.ltoeR.func_78793_a(0.2f, 4.0f, 0.0f);
        this.ltoeR.func_78787_b(64, 32);
        this.ltoeR.field_78809_i = false;
        this.setRotation(this.ltoeR, 0.0f, -0.1396263f, 0.0f);
        this.legR.func_78792_a(this.ltoeR);
        this.btoeR = new ModelRenderer((ModelBase)this, 0, 0);
        this.btoeR.func_78789_a(-0.5f, 0.0f, 0.0f, 1, 1, 2);
        this.btoeR.func_78793_a(0.0f, 4.0f, 0.0f);
        this.btoeR.func_78787_b(64, 32);
        this.btoeR.field_78809_i = false;
        this.setRotation(this.btoeR, -0.349066f, 0.0f, 0.0f);
        this.legR.func_78792_a(this.btoeR);
        this.rtoeR = new ModelRenderer((ModelBase)this, 0, 0);
        this.rtoeR.func_78789_a(-1.0f, 0.0f, -2.0f, 1, 1, 2);
        this.rtoeR.func_78793_a(-0.2f, 4.0f, 0.0f);
        this.rtoeR.func_78787_b(64, 32);
        this.rtoeR.field_78809_i = false;
        this.setRotation(this.rtoeR, 0.0f, 0.1396263f, 0.0f);
        this.legR.func_78792_a(this.rtoeR);
        this.thighR = new ModelRenderer((ModelBase)this, 13, 18);
        this.thighR.func_78789_a(-1.0f, 0.0f, -1.0f, 2, 2, 2);
        this.thighR.func_78793_a(-1.5f, 12.0f, -1.0f);
        this.thighR.func_78787_b(64, 32);
        this.thighR.field_78809_i = false;
        this.setRotation(this.thighR, 0.0f, 0.0f, 0.0f);
        this.thighR.func_78792_a(this.legR);
        this.legL = new ModelRenderer((ModelBase)this, 13, 12);
        this.legL.func_78789_a(-0.5f, 0.0f, -0.5f, 1, 5, 1);
        this.legL.func_78793_a(0.0f, 0.0f, 0.0f);
        this.legL.func_78787_b(64, 32);
        this.legL.field_78809_i = true;
        this.setRotation(this.legL, 0.0f, 0.0f, 0.0f);
        this.ltoeL = new ModelRenderer((ModelBase)this, 0, 0);
        this.ltoeL.func_78789_a(0.0f, 0.0f, -2.0f, 1, 1, 2);
        this.ltoeL.func_78793_a(0.2f, 4.0f, 0.0f);
        this.ltoeL.func_78787_b(64, 32);
        this.ltoeL.field_78809_i = true;
        this.setRotation(this.ltoeL, 0.0f, -0.1396263f, 0.0f);
        this.legL.func_78792_a(this.ltoeL);
        this.btoeL = new ModelRenderer((ModelBase)this, 0, 0);
        this.btoeL.func_78789_a(-0.5f, 0.0f, 0.0f, 1, 1, 2);
        this.btoeL.func_78793_a(0.0f, 4.0f, 0.0f);
        this.btoeL.func_78787_b(64, 32);
        this.btoeL.field_78809_i = true;
        this.setRotation(this.btoeL, -0.349066f, 0.0f, 0.0f);
        this.legL.func_78792_a(this.btoeL);
        this.rtoeL = new ModelRenderer((ModelBase)this, 0, 0);
        this.rtoeL.func_78789_a(-1.0f, 0.0f, -2.0f, 1, 1, 2);
        this.rtoeL.func_78793_a(-0.2f, 4.0f, 0.0f);
        this.rtoeL.func_78787_b(64, 32);
        this.rtoeL.field_78809_i = true;
        this.setRotation(this.rtoeL, 0.0f, 0.1396263f, 0.0f);
        this.legL.func_78792_a(this.rtoeL);
        this.thighL = new ModelRenderer((ModelBase)this, 13, 18);
        this.thighL.func_78789_a(-1.0f, 0.0f, -1.0f, 2, 2, 2);
        this.thighL.func_78793_a(1.5f, 12.0f, -1.0f);
        this.thighL.func_78787_b(64, 32);
        this.thighL.field_78809_i = true;
        this.setRotation(this.thighL, 0.0f, 0.0f, 0.0f);
        this.thighL.func_78792_a(this.legL);
        this.body.func_78792_a(this.thighL);
        this.body.func_78792_a(this.thighR);
        this.body.func_78792_a(this.head);
        this.body.func_78792_a(this.tail);
        this.body.func_78792_a(this.rightwing1);
        this.body.func_78792_a(this.leftwing1);
        this.animationWingFlap = new ModelAnimator();
        float frameUnit = 0.01666667f;
        ArrayList<KeyFrame> innerWingFrames = new ArrayList<KeyFrame>(12);
        innerWingFrames.add(new KeyFrame(0.0f, 2.0f, -43.5f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(5.0f * frameUnit, 4.0f, -38.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(10.0f * frameUnit, 5.5f, -27.5f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(15.0f * frameUnit, 5.5f, -7.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(20.0f * frameUnit, 5.5f, 15.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(25.0f * frameUnit, 4.5f, 30.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(30.0f * frameUnit, 2.0f, 38.0f, 9.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(35.0f * frameUnit, 1.0f, 20.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(40.0f * frameUnit, 1.0f, 3.5f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(45.0f * frameUnit, 1.0f, -19.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(50.0f * frameUnit, -3.0f, -38.0f, 0.0f, InterpType.LINEAR));
        innerWingFrames.add(new KeyFrame(55.0f * frameUnit, -1.0f, -48.0f, 0.0f, InterpType.LINEAR));
        KeyFrame.toRadians(innerWingFrames);
        this.animationWingFlap.addPart(this.rightwing1, innerWingFrames);
        List<KeyFrame> copy = KeyFrame.cloneFrames(innerWingFrames);
        KeyFrame.mirrorFramesX(copy);
        this.animationWingFlap.addPart(this.leftwing1, copy);
        ArrayList<KeyFrame> outerWingFrames = new ArrayList<KeyFrame>(13);
        outerWingFrames.add(new KeyFrame(0.0f, 2.0f, 34.5f, 0.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(5.0f * frameUnit, 5.0f, 13.0f, -7.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(10.0f * frameUnit, 7.0f, 8.5f, -10.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(15.0f * frameUnit, 7.5f, -2.5f, -10.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(25.0f * frameUnit, 5.0f, 7.0f, -10.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(30.0f * frameUnit, 2.0f, 15.0f, 0.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(35.0f * frameUnit, -3.0f, 37.0f, 12.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(40.0f * frameUnit, -9.0f, 56.0f, 27.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(45.0f * frameUnit, -13.0f, 68.0f, 28.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(50.0f * frameUnit, -13.5f, 70.0f, 31.5f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(53.0f * frameUnit, -9.0f, 71.0f, 31.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(55.0f * frameUnit, -3.5f, 65.5f, 22.0f, InterpType.LINEAR));
        outerWingFrames.add(new KeyFrame(58.0f * frameUnit, 0.0f, 52.0f, 8.0f, InterpType.LINEAR));
        KeyFrame.toRadians(outerWingFrames);
        this.animationWingFlap.addPart(this.rightwing2, outerWingFrames);
        List<KeyFrame> copy2 = KeyFrame.cloneFrames(outerWingFrames);
        KeyFrame.mirrorFramesX(copy2);
        this.animationWingFlap.addPart(this.leftwing2, copy2);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
        this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        this.body.func_78785_a(f5);
    }

    public void setFlyingAnimations(float flapProgress, float legSweepProgress, float roll) {
        this.animationWingFlap.updateAnimation(flapProgress);
        this.body.field_78795_f = 0.0f;
        this.body.field_78796_g = 0.0f;
        this.body.field_78808_h = -roll / 180.0f * 3.141593f;
        this.thighR.field_78795_f = 0.08726647f * legSweepProgress;
        this.thighL.field_78795_f = 0.08726647f * legSweepProgress;
        this.legR.field_78795_f = 0.08726647f * legSweepProgress;
        this.legL.field_78795_f = 0.08726647f * legSweepProgress;
        this.ltoeR.field_78795_f = 0.8726647f * legSweepProgress;
        this.rtoeR.field_78795_f = 0.8726647f * legSweepProgress;
        this.btoeR.field_78795_f = -0.3490659f + 0.0f * legSweepProgress;
        this.ltoeL.field_78795_f = 0.8726647f * legSweepProgress;
        this.rtoeL.field_78795_f = 0.8726647f * legSweepProgress;
        this.btoeL.field_78795_f = -0.3490659f + 0.0f * legSweepProgress;
        this.body.field_78797_d = 7.0f + MathHelper.func_76134_b((float)(flapProgress * 3.141593f * 2.0f)) * 1.4f;
        ModelRenderer tmp190_187 = this.thighR;
        tmp190_187.field_78795_f = (float)((double)tmp190_187.field_78795_f + (double)MathHelper.func_76134_b((float)(flapProgress * 3.141593f * 2.0f)) * 0.08726646324990228);
        ModelRenderer tmp218_215 = this.thighL;
        tmp218_215.field_78795_f = (float)((double)tmp218_215.field_78795_f + (double)MathHelper.func_76134_b((float)(flapProgress * 3.141593f * 2.0f)) * 0.08726646324990228);
        this.tail.field_78795_f = (float)(0.2617993956013792 + (double)MathHelper.func_76134_b((float)(flapProgress * 3.141593f * 2.0f)) * 0.03490658588512815);
        this.head.field_78795_f = (float)(-0.3141592700403172 - (double)MathHelper.func_76134_b((float)(flapProgress * 3.141593f * 2.0f)) * 0.03490658588512815);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }

    public void func_78087_a(float limbPeriod, float limbMaxMovement, float ticksExisted, float headYaw, float entityPitch, float unitScale, Entity entity) {
        super.func_78087_a(limbPeriod, limbMaxMovement, ticksExisted, headYaw, entityPitch, unitScale, entity);
        this.body.field_78795_f = 1.570796f - entityPitch / 180.0f * 3.141593f;
    }
}

