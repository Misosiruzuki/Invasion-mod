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

import invmod.client.render.AnimationRegistry;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.client.render.animation.BonesBirdLegs;
import invmod.client.render.animation.BonesWings;
import invmod.client.render.animation.ModelAnimator;
import invmod.common.util.MathUtil;
import java.util.EnumMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelGiantBird
extends ModelBase {
    private ModelAnimator animationFlap;
    private ModelAnimator animationRun;
    ModelRenderer body = new ModelRenderer((ModelBase)this, 0, 0);
    ModelRenderer rightThigh;
    ModelRenderer rightLeg;
    ModelRenderer rightAnkle;
    ModelRenderer rightToeB;
    ModelRenderer rightClawB;
    ModelRenderer rightToeL;
    ModelRenderer rightClawL;
    ModelRenderer rightToeM;
    ModelRenderer rightClawM;
    ModelRenderer rightToeR;
    ModelRenderer rightClawR;
    ModelRenderer leftThigh;
    ModelRenderer leftLeg;
    ModelRenderer leftAnkle;
    ModelRenderer leftToeB;
    ModelRenderer leftClawB;
    ModelRenderer leftToeL;
    ModelRenderer leftClawL;
    ModelRenderer leftToeM;
    ModelRenderer leftClawM;
    ModelRenderer leftToeR;
    ModelRenderer leftClawR;
    ModelRenderer neck1;
    ModelRenderer neck2;
    ModelRenderer neck3;
    ModelRenderer head;
    ModelRenderer upperBeak;
    ModelRenderer upperBeakTip;
    ModelRenderer lowerBeak;
    ModelRenderer lowerBeakTip;
    ModelRenderer headFeathers;
    ModelRenderer backNeckFeathers;
    ModelRenderer leftNeckFeathers;
    ModelRenderer rightNeckFeathers;
    ModelRenderer leftWing1;
    ModelRenderer leftWing2;
    ModelRenderer leftWing3;
    ModelRenderer tail;
    ModelRenderer rightWing1;
    ModelRenderer rightWing2;
    ModelRenderer rightWing3;

    public ModelGiantBird() {
        this(0.0f);
    }

    public ModelGiantBird(float par1) {
        this.body.func_78787_b(128, 128);
        this.body.func_78789_a(-10.0f, -10.0f, -10.0f, 20, 30, 20);
        this.body.func_78793_a(0.0f, -19.0f, 0.0f);
        this.rightThigh = new ModelRenderer((ModelBase)this, 84, 82);
        this.rightThigh.func_78787_b(128, 128);
        this.rightThigh.func_78789_a(-4.5f, -3.5f, -4.5f, 9, 15, 9);
        this.rightThigh.func_78793_a(-5.0f, 20.0f, -2.0f);
        this.rightLeg = new ModelRenderer((ModelBase)this, 56, 50);
        this.rightLeg.func_78787_b(128, 128);
        this.rightLeg.func_78789_a(-2.0f, -3.0f, -2.0f, 4, 16, 4);
        this.rightLeg.func_78793_a(0.0f, 11.0f, 0.0f);
        this.rightAnkle = new ModelRenderer((ModelBase)this, 16, 16);
        this.rightAnkle.func_78787_b(128, 128);
        this.rightAnkle.func_78789_a(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.rightAnkle.func_78793_a(0.0f, 12.0f, 0.0f);
        this.rightToeB = new ModelRenderer((ModelBase)this, 60, 0);
        this.rightToeB.func_78787_b(128, 128);
        this.rightToeB.func_78789_a(-1.0f, -1.0f, -1.0f, 2, 8, 2);
        this.rightToeB.func_78793_a(0.0f, 0.0f, 2.0f);
        this.rightClawB = new ModelRenderer((ModelBase)this, 0, 11);
        this.rightClawB.func_78787_b(128, 128);
        this.rightClawB.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.rightClawB.func_78793_a(0.0f, 6.0f, 0.0f);
        this.rightToeL = new ModelRenderer((ModelBase)this, 0, 0);
        this.rightToeL.func_78787_b(128, 128);
        this.rightToeL.func_78789_a(-1.0f, 0.5f, -1.0f, 2, 9, 2);
        this.rightToeL.func_78793_a(-0.5f, 0.0f, 1.0f);
        this.rightClawL = new ModelRenderer((ModelBase)this, 0, 11);
        this.rightClawL.func_78787_b(128, 128);
        this.rightClawL.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.rightClawL.func_78793_a(0.0f, 9.0f, 0.0f);
        this.rightToeM = new ModelRenderer((ModelBase)this, 8, 0);
        this.rightToeM.func_78787_b(128, 128);
        this.rightToeM.func_78789_a(-1.0f, 0.0f, -1.0f, 2, 10, 2);
        this.rightToeM.func_78793_a(0.0f, 0.0f, 0.0f);
        this.rightClawM = new ModelRenderer((ModelBase)this, 0, 11);
        this.rightClawM.func_78787_b(128, 128);
        this.rightClawM.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.rightClawM.func_78793_a(0.0f, 9.0f, 0.0f);
        this.rightToeR = new ModelRenderer((ModelBase)this, 0, 0);
        this.rightToeR.func_78787_b(128, 128);
        this.rightToeR.func_78789_a(-1.0f, -0.5f, -1.0f, 2, 9, 2);
        this.rightToeR.func_78793_a(1.0f, 0.0f, 1.0f);
        this.rightClawR = new ModelRenderer((ModelBase)this, 0, 11);
        this.rightClawR.func_78787_b(128, 128);
        this.rightClawR.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.rightClawR.func_78793_a(0.0f, 8.0f, 0.0f);
        this.leftThigh = new ModelRenderer((ModelBase)this, 84, 82);
        this.leftThigh.func_78787_b(128, 128);
        this.leftThigh.func_78789_a(-4.5f, -3.5f, -4.5f, 9, 15, 9);
        this.leftThigh.func_78793_a(5.0f, 20.0f, -2.0f);
        this.leftThigh.field_78809_i = true;
        this.leftLeg = new ModelRenderer((ModelBase)this, 56, 50);
        this.leftLeg.func_78787_b(128, 128);
        this.leftLeg.func_78789_a(-2.0f, -3.0f, -2.0f, 4, 16, 4);
        this.leftLeg.func_78793_a(0.0f, 11.0f, 0.0f);
        this.leftLeg.field_78809_i = true;
        this.leftAnkle = new ModelRenderer((ModelBase)this, 16, 16);
        this.leftAnkle.func_78787_b(128, 128);
        this.leftAnkle.func_78789_a(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.leftAnkle.func_78793_a(0.0f, 12.0f, 0.0f);
        this.leftToeB = new ModelRenderer((ModelBase)this, 60, 0);
        this.leftToeB.func_78787_b(128, 128);
        this.leftToeB.func_78789_a(-1.0f, -1.0f, -1.0f, 2, 8, 2);
        this.leftToeB.func_78793_a(0.0f, 0.0f, 2.0f);
        this.leftClawB = new ModelRenderer((ModelBase)this, 0, 11);
        this.leftClawB.func_78787_b(128, 128);
        this.leftClawB.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.leftClawB.func_78793_a(0.0f, 6.0f, 0.0f);
        this.leftToeL = new ModelRenderer((ModelBase)this, 0, 0);
        this.leftToeL.func_78787_b(128, 128);
        this.leftToeL.func_78789_a(-1.0f, 0.5f, -1.0f, 2, 9, 2);
        this.leftToeL.func_78793_a(0.5f, 0.0f, 1.0f);
        this.leftClawL = new ModelRenderer((ModelBase)this, 0, 11);
        this.leftClawL.func_78787_b(128, 128);
        this.leftClawL.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.leftClawL.func_78793_a(0.0f, 9.0f, 0.0f);
        this.leftToeM = new ModelRenderer((ModelBase)this, 8, 0);
        this.leftToeM.func_78787_b(128, 128);
        this.leftToeM.func_78789_a(-1.0f, 0.0f, -1.0f, 2, 10, 2);
        this.leftToeM.func_78793_a(0.0f, 0.0f, 0.0f);
        this.leftClawM = new ModelRenderer((ModelBase)this, 0, 11);
        this.leftClawM.func_78787_b(128, 128);
        this.leftClawM.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.leftClawM.func_78793_a(0.0f, 9.0f, 0.0f);
        this.leftToeR = new ModelRenderer((ModelBase)this, 0, 0);
        this.leftToeR.func_78787_b(128, 128);
        this.leftToeR.func_78789_a(-1.0f, -0.5f, -1.0f, 2, 9, 2);
        this.leftToeR.func_78793_a(-1.0f, 0.0f, 1.0f);
        this.leftClawR = new ModelRenderer((ModelBase)this, 0, 11);
        this.leftClawR.func_78787_b(128, 128);
        this.leftClawR.func_78789_a(-0.5f, 0.0f, -1.0f, 1, 4, 2);
        this.leftClawR.func_78793_a(0.0f, 8.0f, 0.0f);
        this.neck1 = new ModelRenderer((ModelBase)this, 43, 95);
        this.neck1.func_78787_b(128, 128);
        this.neck1.func_78789_a(-7.0f, -7.0f, -6.5f, 14, 10, 13);
        this.neck1.func_78793_a(0.0f, -10.0f, 1.0f);
        this.neck2 = new ModelRenderer((ModelBase)this, 50, 73);
        this.neck2.func_78787_b(128, 128);
        this.neck2.func_78789_a(-5.0f, -4.0f, -5.0f, 10, 8, 10);
        this.neck2.func_78793_a(0.0f, -8.0f, 0.0f);
        this.neck3 = new ModelRenderer((ModelBase)this, 80, 65);
        this.neck3.func_78787_b(128, 128);
        this.neck3.func_78789_a(-4.0f, -5.5f, -5.0f, 8, 5, 10);
        this.neck3.func_78793_a(0.0f, -2.0f, 0.0f);
        this.head = new ModelRenderer((ModelBase)this, 14, 108);
        this.head.func_78787_b(128, 128);
        this.head.func_78789_a(-4.5f, -5.0f, -9.5f, 9, 8, 11);
        this.head.func_78793_a(0.0f, -4.0f, 0.0f);
        this.upperBeak = new ModelRenderer((ModelBase)this, 54, 118);
        this.upperBeak.func_78787_b(128, 128);
        this.upperBeak.func_78789_a(-2.5f, -1.0f, -3.0f, 5, 2, 6);
        this.upperBeak.func_78793_a(0.0f, -0.8f, -10.0f);
        this.upperBeakTip = new ModelRenderer((ModelBase)this, 70, 118);
        this.upperBeakTip.func_78787_b(128, 128);
        this.upperBeakTip.func_78789_a(-1.0f, -1.0f, -1.0f, 2, 2, 2);
        this.upperBeakTip.func_78793_a(0.0f, 0.0f, -4.0f);
        this.lowerBeak = new ModelRenderer((ModelBase)this, 78, 118);
        this.lowerBeak.func_78787_b(128, 128);
        this.lowerBeak.func_78789_a(-2.5f, -1.0f, -3.0f, 5, 2, 6);
        this.lowerBeak.func_78793_a(0.0f, 1.5f, -10.0f);
        this.lowerBeakTip = new ModelRenderer((ModelBase)this, 76, 121);
        this.lowerBeakTip.func_78787_b(128, 128);
        this.lowerBeakTip.func_78789_a(-1.0f, -0.5f, -1.0f, 2, 1, 2);
        this.lowerBeakTip.func_78793_a(0.0f, -0.5f, -4.0f);
        this.headFeathers = new ModelRenderer((ModelBase)this, -5, 121);
        this.headFeathers.func_78787_b(128, 128);
        this.headFeathers.func_78789_a(-3.5f, 0.0f, -0.5f, 7, 0, 5);
        this.headFeathers.func_78793_a(0.0f, -5.0f, 0.0f);
        this.backNeckFeathers = new ModelRenderer((ModelBase)this, -7, 108);
        this.backNeckFeathers.func_78787_b(128, 128);
        this.backNeckFeathers.func_78789_a(-4.0f, 0.0f, -1.5f, 8, 0, 7);
        this.backNeckFeathers.func_78793_a(0.0f, -3.0f, 5.0f);
        this.leftNeckFeathers = new ModelRenderer((ModelBase)this, -6, 115);
        this.leftNeckFeathers.func_78787_b(128, 128);
        this.leftNeckFeathers.func_78789_a(-3.0f, 0.0f, -1.0f, 6, 0, 6);
        this.leftNeckFeathers.func_78793_a(4.0f, -3.0f, 2.0f);
        this.rightNeckFeathers = new ModelRenderer((ModelBase)this, -6, 115);
        this.rightNeckFeathers.func_78787_b(128, 128);
        this.rightNeckFeathers.func_78789_a(-3.0f, 0.0f, -1.0f, 6, 0, 6);
        this.rightNeckFeathers.func_78793_a(-4.0f, -3.0f, 2.0f);
        this.leftWing1 = new ModelRenderer((ModelBase)this, 0, 50);
        this.leftWing1.field_78809_i = true;
        this.leftWing1.func_78787_b(128, 128);
        this.leftWing1.func_78789_a(-0.5f, -4.5f, -1.5f, 25, 29, 3);
        this.leftWing1.func_78793_a(7.0f, -8.0f, 6.0f);
        this.leftWing2 = new ModelRenderer((ModelBase)this, 0, 82);
        this.leftWing2.field_78809_i = true;
        this.leftWing2.func_78787_b(128, 128);
        this.leftWing2.func_78789_a(-2.5f, -5.0f, -1.0f, 23, 24, 2);
        this.leftWing2.func_78793_a(23.0f, 1.0f, 0.0f);
        this.leftWing3 = new ModelRenderer((ModelBase)this, 80, 0);
        this.leftWing3.field_78809_i = true;
        this.leftWing3.func_78787_b(128, 128);
        this.leftWing3.func_78789_a(-2.5f, -5.0f, -0.5f, 23, 22, 1);
        this.leftWing3.func_78793_a(21.0f, 0.2f, 0.3f);
        this.tail = new ModelRenderer((ModelBase)this, 80, 23);
        this.tail.func_78787_b(128, 128);
        this.tail.func_78789_a(-8.5f, -5.0f, -1.0f, 17, 40, 2);
        this.tail.func_78793_a(0.0f, 19.0f, 8.0f);
        this.rightWing1 = new ModelRenderer((ModelBase)this, 0, 50);
        this.rightWing1.func_78787_b(128, 128);
        this.rightWing1.func_78789_a(-24.5f, -4.5f, -1.5f, 25, 29, 3);
        this.rightWing1.func_78793_a(-7.0f, -8.0f, 6.0f);
        this.rightWing2 = new ModelRenderer((ModelBase)this, 0, 82);
        this.rightWing2.func_78787_b(128, 128);
        this.rightWing2.func_78789_a(-20.5f, -5.0f, -1.0f, 23, 24, 2);
        this.rightWing2.func_78793_a(-23.0f, 1.0f, 0.0f);
        this.rightWing3 = new ModelRenderer((ModelBase)this, 80, 0);
        this.rightWing3.func_78787_b(128, 128);
        this.rightWing3.func_78789_a(-20.5f, -5.0f, -0.5f, 23, 22, 1);
        this.rightWing3.func_78793_a(-21.0f, 0.2f, 0.3f);
        this.rightToeB.func_78792_a(this.rightClawB);
        this.rightToeR.func_78792_a(this.rightClawR);
        this.rightToeM.func_78792_a(this.rightClawM);
        this.rightToeL.func_78792_a(this.rightClawL);
        this.leftToeB.func_78792_a(this.leftClawB);
        this.leftToeR.func_78792_a(this.leftClawR);
        this.leftToeM.func_78792_a(this.leftClawM);
        this.leftToeL.func_78792_a(this.leftClawL);
        this.rightAnkle.func_78792_a(this.rightToeB);
        this.rightAnkle.func_78792_a(this.rightToeR);
        this.rightAnkle.func_78792_a(this.rightToeM);
        this.rightAnkle.func_78792_a(this.rightToeL);
        this.leftAnkle.func_78792_a(this.leftToeB);
        this.leftAnkle.func_78792_a(this.leftToeR);
        this.leftAnkle.func_78792_a(this.leftToeM);
        this.leftAnkle.func_78792_a(this.leftToeL);
        this.rightLeg.func_78792_a(this.rightAnkle);
        this.leftLeg.func_78792_a(this.leftAnkle);
        this.rightThigh.func_78792_a(this.rightLeg);
        this.leftThigh.func_78792_a(this.leftLeg);
        this.upperBeak.func_78792_a(this.upperBeakTip);
        this.lowerBeak.func_78792_a(this.lowerBeakTip);
        this.head.func_78792_a(this.upperBeak);
        this.head.func_78792_a(this.lowerBeak);
        this.head.func_78792_a(this.headFeathers);
        this.neck3.func_78792_a(this.head);
        this.neck3.func_78792_a(this.leftNeckFeathers);
        this.neck3.func_78792_a(this.rightNeckFeathers);
        this.neck3.func_78792_a(this.backNeckFeathers);
        this.neck2.func_78792_a(this.neck3);
        this.neck1.func_78792_a(this.neck2);
        this.leftWing2.func_78792_a(this.leftWing3);
        this.leftWing1.func_78792_a(this.leftWing2);
        this.rightWing2.func_78792_a(this.rightWing3);
        this.rightWing1.func_78792_a(this.rightWing2);
        this.body.func_78792_a(this.rightThigh);
        this.body.func_78792_a(this.leftThigh);
        this.body.func_78792_a(this.neck1);
        this.body.func_78792_a(this.tail);
        this.body.func_78792_a(this.leftWing1);
        this.body.func_78792_a(this.rightWing1);
        EnumMap<BonesBirdLegs, ModelRenderer> legMap = new EnumMap<BonesBirdLegs, ModelRenderer>(BonesBirdLegs.class);
        legMap.put(BonesBirdLegs.LEFT_KNEE, this.leftThigh);
        legMap.put(BonesBirdLegs.RIGHT_KNEE, this.rightThigh);
        legMap.put(BonesBirdLegs.LEFT_ANKLE, this.leftLeg);
        legMap.put(BonesBirdLegs.RIGHT_ANKLE, this.rightLeg);
        legMap.put(BonesBirdLegs.LEFT_METATARSOPHALANGEAL_ARTICULATIONS, this.leftAnkle);
        legMap.put(BonesBirdLegs.RIGHT_METATARSOPHALANGEAL_ARTICULATIONS, this.rightAnkle);
        legMap.put(BonesBirdLegs.LEFT_BACK_CLAW, this.leftToeB);
        legMap.put(BonesBirdLegs.RIGHT_BACK_CLAW, this.rightToeB);
        this.animationRun = new ModelAnimator<BonesBirdLegs>(legMap, AnimationRegistry.instance().getAnimation("bird_run"));
        EnumMap<BonesWings, ModelRenderer> wingMap = new EnumMap<BonesWings, ModelRenderer>(BonesWings.class);
        wingMap.put(BonesWings.LEFT_SHOULDER, this.leftWing1);
        wingMap.put(BonesWings.RIGHT_SHOULDER, this.rightWing1);
        wingMap.put(BonesWings.LEFT_ELBOW, this.leftWing2);
        wingMap.put(BonesWings.RIGHT_ELBOW, this.rightWing2);
        this.animationFlap = new ModelAnimator<BonesWings>(wingMap, AnimationRegistry.instance().getAnimation("wing_flap_2_piece"));
        this.setRotation(this.body, 0.7f, 0.0f, 0.0f);
        this.setRotation(this.rightThigh, -0.39f, 0.0f, 0.09f);
        this.setRotation(this.leftThigh, -0.39f, 0.0f, -0.09f);
        this.setRotation(this.rightLeg, -0.72f, 0.0f, 0.0f);
        this.setRotation(this.leftLeg, -0.72f, 0.0f, 0.0f);
        this.setRotation(this.rightAnkle, 0.1f, 0.2f, 0.0f);
        this.setRotation(this.leftAnkle, 0.1f, -0.2f, 0.0f);
        this.setRotation(this.rightToeB, 1.34f, 0.0f, 0.0f);
        this.setRotation(this.rightToeR, -0.8f, -0.28f, -0.28f);
        this.setRotation(this.rightToeM, -0.8f, 0.0f, 0.0f);
        this.setRotation(this.rightToeL, -0.8f, 0.28f, 0.28f);
        this.setRotation(this.leftToeB, 1.34f, 0.0f, 0.0f);
        this.setRotation(this.leftToeR, -0.8f, 0.28f, 0.28f);
        this.setRotation(this.leftToeM, -0.8f, 0.0f, 0.0f);
        this.setRotation(this.leftToeL, -0.8f, -0.28f, -0.28f);
        this.setRotation(this.rightClawB, -36.0f, 0.0f, 0.0f);
        this.setRotation(this.neck1, -0.18f, 0.0f, 0.0f);
        this.setRotation(this.neck2, 0.52f, 0.0f, 0.0f);
        this.setRotation(this.neck3, 0.26f, 0.0f, 0.0f);
        this.setRotation(this.head, -0.97f, 0.0f, 0.0f);
        this.setRotation(this.headFeathers, 0.38f, 0.0f, 0.0f);
        this.setRotation(this.backNeckFeathers, -1.11f, 0.0f, 0.0f);
        this.setRotation(this.leftNeckFeathers, -0.85f, -1.87f, 0.39f);
        this.setRotation(this.rightNeckFeathers, -0.85f, 1.87f, -0.39f);
        this.setRotation(this.tail, 0.3f, 0.0f, 0.0f);
    }

    public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.body.func_78785_a(par7);
    }

    public void setFlyingAnimations(AnimationState wingState, AnimationState legState, float roll, float headYaw, float headPitch, float parTick) {
        AnimationAction legAction = legState.getCurrentAction();
        AnimationAction wingAction = wingState.getCurrentAction();
        float flapProgress = wingState.getCurrentAnimationTimeInterp(parTick);
        float legProgress = legState.getCurrentAnimationTimeInterp(parTick);
        this.animationFlap.updateAnimation(flapProgress);
        this.animationRun.updateAnimation(legProgress);
        if (legAction == AnimationAction.RUN && legProgress >= 0.109195f && legProgress < 0.5373563f) {
            legProgress = (float)((double)legProgress + 0.0);
            float t = (float)Math.PI * 8 * legProgress / 0.7967914f;
            this.body.field_78795_f += (float)(-Math.cos(t) * 0.1);
            this.neck1.field_78795_f += (float)(Math.cos(t) * 0.08);
            this.body.field_78800_c += -((float)(Math.cos(t) * 1.0));
        }
        if (wingAction == AnimationAction.WINGFLAP) {
            float flapCycle = flapProgress / 0.2714932f;
            this.body.field_78797_d += MathHelper.func_76134_b((float)(flapCycle * 3.141593f * 2.0f)) * 1.4f;
            ModelRenderer tmp208_205 = this.rightThigh;
            tmp208_205.field_78795_f = (float)((double)tmp208_205.field_78795_f + (double)MathHelper.func_76134_b((float)(flapCycle * 3.141593f * 2.0f)) * 0.08726646324990228);
            ModelRenderer tmp238_235 = this.leftThigh;
            tmp238_235.field_78795_f = (float)((double)tmp238_235.field_78795_f + (double)MathHelper.func_76134_b((float)(flapCycle * 3.141593f * 2.0f)) * 0.08726646324990228);
            ModelRenderer tmp268_265 = this.tail;
            tmp268_265.field_78795_f = (float)((double)tmp268_265.field_78795_f + (double)MathHelper.func_76134_b((float)(flapCycle * 3.141593f * 2.0f)) * 0.03490658588512815);
        }
        this.body.field_78808_h = -roll / 180.0f * 3.141593f;
        if ((headPitch = (float)MathUtil.boundAngle180Deg(headPitch)) > 37.16f) {
            headPitch = 37.16f;
        } else if (headPitch < -56.65f) {
            headPitch = -56.65f;
        }
        float pitchFactor = (headPitch + 56.65f) / 93.8f;
        this.head.field_78795_f += -0.96f + pitchFactor * -0.1400001f;
        this.neck3.field_78795_f += 0.378f + pitchFactor * -0.528f;
        this.neck2.field_78795_f += 0.4f + pitchFactor * -0.4f;
        this.neck1.field_78795_f += 0.513f + pitchFactor * -0.613f;
        headYaw = (float)MathUtil.boundAngle180Deg(headYaw);
        if (headYaw > 30.5f) {
            headYaw = 30.5f;
        } else if (headYaw < -30.5f) {
            headYaw = -30.5f;
        }
        float yawFactor = (headYaw + 30.5f) / 61.0f;
        this.head.field_78808_h += 0.8f + yawFactor * 2.0f * -0.8f;
        this.neck3.field_78808_h += 0.38f + yawFactor * 2.0f * -0.38f;
        this.neck2.field_78808_h += 0.14f + yawFactor * 2.0f * -0.14f;
        this.head.field_78796_g += -0.7f + yawFactor * 2.0f * 0.7f;
        this.neck3.field_78796_g += -0.12f + yawFactor * 2.0f * 0.12f;
    }

    public void func_78087_a(float limbPeriod, float limbMaxMovement, float ticksExisted, float headYaw, float entityPitch, float unitScale, Entity entity) {
        super.func_78087_a(limbPeriod, limbMaxMovement, ticksExisted, headYaw, entityPitch, unitScale, entity);
        ModelRenderer tmp19_16 = this.body;
        tmp19_16.field_78795_f = (float)((double)tmp19_16.field_78795_f + (0.8707963705062867 - (double)(entityPitch / 180.0f * 3.141593f)));
    }

    public void resetSkeleton() {
        this.setRotation(this.body, 0.7f, 0.0f, 0.0f);
        this.setRotation(this.rightThigh, -0.39f, 0.0f, 0.09f);
        this.setRotation(this.leftThigh, -0.39f, 0.0f, -0.09f);
        this.setRotation(this.rightLeg, -0.72f, 0.0f, 0.0f);
        this.setRotation(this.leftLeg, -0.72f, 0.0f, 0.0f);
        this.setRotation(this.rightAnkle, 0.1f, 0.2f, 0.0f);
        this.setRotation(this.leftAnkle, 0.1f, -0.2f, 0.0f);
        this.setRotation(this.rightToeB, 1.34f, 0.0f, 0.0f);
        this.setRotation(this.rightToeR, -0.8f, -0.28f, -0.28f);
        this.setRotation(this.rightToeM, -0.8f, 0.0f, 0.0f);
        this.setRotation(this.rightToeL, -0.8f, 0.28f, 0.28f);
        this.setRotation(this.leftToeB, 1.34f, 0.0f, 0.0f);
        this.setRotation(this.leftToeR, -0.8f, 0.28f, 0.28f);
        this.setRotation(this.leftToeM, -0.8f, 0.0f, 0.0f);
        this.setRotation(this.leftToeL, -0.8f, -0.28f, -0.28f);
        this.setRotation(this.neck1, 0.0f, 0.0f, 0.0f);
        this.setRotation(this.neck2, 0.0f, 0.0f, 0.0f);
        this.setRotation(this.neck3, 0.0f, 0.0f, 0.0f);
        this.setRotation(this.head, 0.0f, 0.0f, 0.0f);
        this.setRotation(this.headFeathers, 0.38f, 0.0f, 0.0f);
        this.setRotation(this.backNeckFeathers, -1.11f, 0.0f, 0.0f);
        this.setRotation(this.leftNeckFeathers, -0.85f, -1.87f, 0.39f);
        this.setRotation(this.rightNeckFeathers, -0.85f, 1.87f, -0.39f);
        this.setRotation(this.tail, 0.3f, 0.0f, 0.0f);
        this.body.func_78793_a(0.0f, -27.0f, 0.0f);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }
}

