/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelRenderer
 */
package invmod.client.render.animation;

import invmod.client.render.animation.Animation;
import invmod.client.render.animation.InterpType;
import invmod.client.render.animation.KeyFrame;
import invmod.common.util.Triplet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.ModelRenderer;

public class ModelAnimator<T extends Enum<T>> {
    private List<Triplet<ModelRenderer, Integer, List<KeyFrame>>> parts;
    private float animationPeriod;

    public ModelAnimator() {
        this(1.0f);
    }

    public ModelAnimator(float animationPeriod) {
        this.animationPeriod = animationPeriod;
        this.parts = new ArrayList<Triplet<ModelRenderer, Integer, List<KeyFrame>>>(1);
    }

    public ModelAnimator(Map<T, ModelRenderer> modelParts, Animation<T> animation) {
        this.animationPeriod = animation.getAnimationPeriod();
        this.parts = new ArrayList<Triplet<ModelRenderer, Integer, List<KeyFrame>>>(((Enum[])animation.getSkeletonType().getEnumConstants()).length);
        for (Map.Entry<T, ModelRenderer> entry : modelParts.entrySet()) {
            List<KeyFrame> keyFrames = animation.getKeyFramesFor((Enum)entry.getKey());
            if (keyFrames == null) continue;
            this.parts.add(new Triplet<ModelRenderer, Integer, List<KeyFrame>>(entry.getValue(), 0, keyFrames));
        }
    }

    public void addPart(ModelRenderer part, List<KeyFrame> keyFrames) {
        if (this.validate(keyFrames)) {
            this.parts.add(new Triplet<ModelRenderer, Integer, List<KeyFrame>>(part, 0, keyFrames));
        }
    }

    public void clearParts() {
        this.parts.clear();
    }

    public void updateAnimation(float newTime) {
        for (Triplet<ModelRenderer, Integer, List<KeyFrame>> entry : this.parts) {
            KeyFrame nextFrame;
            KeyFrame prevFrame;
            int prevIndex;
            block5: {
                KeyFrame keyFrame;
                List<KeyFrame> keyFrames;
                block4: {
                    prevIndex = entry.getVal2();
                    keyFrames = entry.getVal3();
                    prevFrame = keyFrames.get(prevIndex++);
                    nextFrame = null;
                    if (!(prevFrame.getTime() <= newTime)) break block4;
                    while (prevIndex < keyFrames.size()) {
                        keyFrame = keyFrames.get(prevIndex);
                        if (newTime < keyFrame.getTime()) {
                            nextFrame = keyFrame;
                            --prevIndex;
                            break;
                        }
                        prevFrame = keyFrame;
                        ++prevIndex;
                    }
                    if (prevIndex < keyFrames.size()) break block5;
                    prevIndex = keyFrames.size() - 1;
                    nextFrame = keyFrames.get(0);
                    break block5;
                }
                for (prevIndex = 0; prevIndex < keyFrames.size(); ++prevIndex) {
                    keyFrame = keyFrames.get(prevIndex);
                    if (!(newTime < keyFrame.getTime())) continue;
                    nextFrame = keyFrame;
                    prevFrame = keyFrames.get(--prevIndex);
                    break;
                }
            }
            entry.setVal2(prevIndex);
            this.interpolate(prevFrame, nextFrame, newTime, entry.getVal1());
        }
    }

    private void interpolate(KeyFrame prevFrame, KeyFrame nextFrame, float time, ModelRenderer part) {
        if (prevFrame.getInterpType() == InterpType.LINEAR) {
            float dtPrev = time - prevFrame.getTime();
            float dtFrame = nextFrame.getTime() - prevFrame.getTime();
            if (dtFrame < 0.0f) {
                dtFrame += this.animationPeriod;
            }
            float r = dtPrev / dtFrame;
            part.field_78795_f = prevFrame.getRotX() + r * (nextFrame.getRotX() - prevFrame.getRotX());
            part.field_78796_g = prevFrame.getRotY() + r * (nextFrame.getRotY() - prevFrame.getRotY());
            part.field_78808_h = prevFrame.getRotZ() + r * (nextFrame.getRotZ() - prevFrame.getRotZ());
            if (prevFrame.hasPos()) {
                if (nextFrame.hasPos()) {
                    part.field_78800_c = prevFrame.getPosX() + r * (nextFrame.getPosX() - prevFrame.getPosX());
                    part.field_78797_d = prevFrame.getPosY() + r * (nextFrame.getPosY() - prevFrame.getPosY());
                    part.field_78798_e = prevFrame.getPosZ() + r * (nextFrame.getPosZ() - prevFrame.getPosZ());
                } else {
                    part.field_78800_c = prevFrame.getPosX();
                    part.field_78797_d = prevFrame.getPosY();
                    part.field_78798_e = prevFrame.getPosZ();
                }
            }
        }
    }

    private boolean validate(List<KeyFrame> keyFrames) {
        if (keyFrames.size() < 2) {
            return false;
        }
        if (keyFrames.get(0).getTime() != 0.0f) {
            return false;
        }
        boolean prevTime = false;
        for (int i = 1; i < keyFrames.size(); ++i) {
            if (!(keyFrames.get(i).getTime() <= (float)prevTime)) continue;
            return false;
        }
        return true;
    }
}

