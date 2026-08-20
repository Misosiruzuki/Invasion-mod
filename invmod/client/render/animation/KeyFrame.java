/*
 * Decompiled with CFR 0.152.
 */
package invmod.client.render.animation;

import invmod.client.render.animation.InterpType;
import invmod.common.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class KeyFrame {
    private float time;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float posX;
    private float posY;
    private float posZ;
    private InterpType interpType;
    private float[][] mods;
    private boolean hasPos;

    public KeyFrame(float time, float rotX, float rotY, float rotZ, InterpType interpType) {
        this(time, rotX, rotY, rotZ, 0.0f, 0.0f, 0.0f, interpType);
        this.hasPos = false;
    }

    public KeyFrame(float time, float rotX, float rotY, float rotZ, float posX, float posY, float posZ, InterpType interpType) {
        this.time = time;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.interpType = interpType;
        this.hasPos = true;
    }

    public float getTime() {
        return this.time;
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public float getPosZ() {
        return this.posZ;
    }

    public InterpType getInterpType() {
        return this.interpType;
    }

    public boolean hasPos() {
        return this.hasPos;
    }

    public String toString() {
        return "(" + this.time + ", " + this.rotX + ", " + this.rotY + ", " + this.rotZ + ")";
    }

    public static List<KeyFrame> cloneFrames(List<KeyFrame> keyFrames) {
        return new ArrayList<KeyFrame>(keyFrames);
    }

    public static void toRadians(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            float radDeg = 0.01745329f;
            KeyFrame keyFrame = iter.next();
            KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), keyFrame.getRotX() * radDeg, keyFrame.getRotY() * radDeg, keyFrame.getRotZ() * radDeg, keyFrame.getPosX(), keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());
            newFrame.hasPos = keyFrame.hasPos;
            iter.set(newFrame);
        }
    }

    public static void mirrorFramesX(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame keyFrame = iter.next();
            KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), keyFrame.getRotX(), -keyFrame.getRotY(), -keyFrame.getRotZ(), -keyFrame.getPosX(), keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());
            newFrame.hasPos = keyFrame.hasPos;
            iter.set(newFrame);
        }
    }

    public static void mirrorFramesY(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame keyFrame = iter.next();
            KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), -keyFrame.getRotX(), keyFrame.getRotY(), -keyFrame.getRotZ(), keyFrame.getPosX(), -keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());
            newFrame.hasPos = keyFrame.hasPos;
            iter.set(newFrame);
        }
    }

    public static void mirrorFramesZ(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame keyFrame = iter.next();
            KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), -keyFrame.getRotX(), -keyFrame.getRotY(), keyFrame.getRotZ(), keyFrame.getPosX(), keyFrame.getPosY(), -keyFrame.getPosZ(), keyFrame.getInterpType());
            newFrame.hasPos = keyFrame.hasPos;
            iter.set(newFrame);
        }
    }

    public static void offsetFramesCircular(List<KeyFrame> keyFrames, float start, float end, float offset) {
        KeyFrame fencepostStart;
        if (keyFrames.size() < 1) {
            return;
        }
        float diff = end - start;
        float k1 = end - (offset %= diff);
        List<KeyFrame> copy = KeyFrame.cloneFrames(keyFrames);
        keyFrames.clear();
        KeyFrame currFrame2 = null;
        ListIterator<KeyFrame> iter = copy.listIterator();
        while (iter.hasNext() && !((currFrame2 = iter.next()).getTime() >= start)) {
            keyFrames.add(currFrame2);
        }
        ArrayList<KeyFrame> buffer = new ArrayList<KeyFrame>();
        buffer.add(currFrame2);
        while (iter.hasNext() && !((currFrame2 = iter.next()).getTime() >= k1)) {
            buffer.add(currFrame2);
        }
        if (!MathUtil.floatEquals(currFrame2.getTime(), k1, 0.001f)) {
            iter.previous();
            KeyFrame prev = iter.previous();
            float dt = k1 - prev.getTime();
            float dtFrame = currFrame2.getTime() - prev.getTime();
            float r = dt / dtFrame;
            float x = prev.getRotX() + r * (currFrame2.getRotX() - prev.getRotX());
            float y = prev.getRotY() + r * (currFrame2.getRotY() - prev.getRotY());
            float z = prev.getRotZ() + r * (currFrame2.getRotZ() - prev.getRotZ());
            fencepostStart = new KeyFrame(start, x, y, z, InterpType.LINEAR);
        } else {
            fencepostStart = currFrame2;
        }
        keyFrames.add(fencepostStart);
        while (iter.hasNext()) {
            currFrame2 = iter.next();
            if (!(currFrame2.getTime() <= end)) continue;
            float t = currFrame2.getTime() + offset - diff;
            KeyFrame newFrame = new KeyFrame(t, currFrame2.getRotX(), currFrame2.getRotY(), currFrame2.getRotZ(), currFrame2.getPosX(), currFrame2.getPosY(), currFrame2.getPosZ(), InterpType.LINEAR);
            newFrame.hasPos = currFrame2.hasPos;
            keyFrames.add(newFrame);
        }
        for (KeyFrame currFrame2 : buffer) {
            float t = currFrame2.getTime() + offset;
            KeyFrame newFrame = new KeyFrame(t, currFrame2.getRotX(), currFrame2.getRotY(), currFrame2.getRotZ(), currFrame2.getPosX(), currFrame2.getPosY(), currFrame2.getPosZ(), InterpType.LINEAR);
            newFrame.hasPos = currFrame2.hasPos;
            keyFrames.add(newFrame);
        }
        keyFrames.add(new KeyFrame(end, fencepostStart.getRotX(), fencepostStart.getRotY(), fencepostStart.getRotZ(), InterpType.LINEAR));
        while (iter.hasNext()) {
            keyFrames.add(iter.next());
        }
    }
}

