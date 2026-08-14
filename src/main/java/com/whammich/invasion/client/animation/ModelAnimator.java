package com.whammich.invasion.client.animation;

import com.whammich.invasion.util.Triplet;
import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelAnimator<T extends Enum<T>> {
    private List<Triplet<ModelPart, Integer, List<KeyFrame>>> parts;
    private float animationPeriod;

    public ModelAnimator() {
        this(1.0F);
    }

    public ModelAnimator(float animationPeriod) {
        this.animationPeriod = animationPeriod;
        this.parts = new ArrayList<>(1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ModelAnimator(Map<T, ModelPart> modelParts, Animation<T> animation) {
        this.animationPeriod = animation.getAnimationPeriod();
        this.parts = new ArrayList<>(animation.getSkeletonType().getEnumConstants().length);
        for (Map.Entry<T, ModelPart> entry : modelParts.entrySet()) {
            List<KeyFrame> keyFrames = animation.getKeyFramesFor(entry.getKey());
            if (keyFrames != null) {
                this.parts.add(new Triplet<>(entry.getValue(), 0, keyFrames));
            }
        }
    }

    public void addPart(ModelPart part, List<KeyFrame> keyFrames) {
        if (validate(keyFrames)) {
            this.parts.add(new Triplet<>(part, 0, keyFrames));
        }
    }

    public void clearParts() {
        this.parts.clear();
    }

    public void updateAnimation(float newTime) {
        for (Triplet<ModelPart, Integer, List<KeyFrame>> entry : this.parts) {
            int prevIndex = entry.getVal2();
            List<KeyFrame> keyFrames = entry.getVal3();
            if (keyFrames == null || keyFrames.isEmpty()) continue;
            KeyFrame prevFrame = keyFrames.get(Math.min(prevIndex, keyFrames.size() - 1));
            KeyFrame nextFrame = null;
            prevIndex++;

            if (prevFrame.getTime() <= newTime) {
                for (; prevIndex < keyFrames.size(); prevIndex++) {
                    KeyFrame keyFrame = keyFrames.get(prevIndex);
                    if (newTime < keyFrame.getTime()) {
                        nextFrame = keyFrame;
                        prevIndex--;
                        break;
                    }
                    prevFrame = keyFrame;
                }
                if (prevIndex >= keyFrames.size()) {
                    prevIndex = keyFrames.size() - 1;
                    nextFrame = keyFrames.get(0);
                }
            } else {
                for (prevIndex = 0; prevIndex < keyFrames.size(); prevIndex++) {
                    KeyFrame keyFrame = keyFrames.get(prevIndex);
                    if (newTime < keyFrame.getTime()) {
                        nextFrame = keyFrame;
                        prevIndex--;
                        prevFrame = keyFrames.get(Math.max(0, prevIndex));
                        break;
                    }
                }
            }
            entry.setVal2(prevIndex);
            if (nextFrame != null) {
                interpolate(prevFrame, nextFrame, newTime, entry.getVal1());
            }
        }
    }

    private void interpolate(KeyFrame prevFrame, KeyFrame nextFrame, float time, ModelPart part) {
        if (prevFrame.getInterpType() == InterpType.LINEAR) {
            float dtPrev = time - prevFrame.getTime();
            float dtFrame = nextFrame.getTime() - prevFrame.getTime();
            if (dtFrame < 0.0F) {
                dtFrame += this.animationPeriod;
            }
            if (dtFrame == 0.0F) return;
            float r = dtPrev / dtFrame;
            part.xRot = prevFrame.getRotX() + r * (nextFrame.getRotX() - prevFrame.getRotX());
            part.yRot = prevFrame.getRotY() + r * (nextFrame.getRotY() - prevFrame.getRotY());
            part.zRot = prevFrame.getRotZ() + r * (nextFrame.getRotZ() - prevFrame.getRotZ());
            if (prevFrame.hasPos()) {
                if (nextFrame.hasPos()) {
                    part.x = prevFrame.getPosX() + r * (nextFrame.getPosX() - prevFrame.getPosX());
                    part.y = prevFrame.getPosY() + r * (nextFrame.getPosY() - prevFrame.getPosY());
                    part.z = prevFrame.getPosZ() + r * (nextFrame.getPosZ() - prevFrame.getPosZ());
                } else {
                    part.x = prevFrame.getPosX();
                    part.y = prevFrame.getPosY();
                    part.z = prevFrame.getPosZ();
                }
            }
        }
    }

    private boolean validate(List<KeyFrame> keyFrames) {
        if (keyFrames.size() < 2) return false;
        if (keyFrames.get(0).getTime() != 0.0F) return false;
        float prevTime = 0;
        for (int i = 1; i < keyFrames.size(); i++) {
            if (keyFrames.get(i).getTime() <= prevTime) return false;
            prevTime = keyFrames.get(i).getTime();
        }
        return true;
    }
}
