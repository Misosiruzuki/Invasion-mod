package com.whammich.invasion.client.animation;

import com.whammich.invasion.util.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class KeyFrame {
    private float time, rotX, rotY, rotZ, posX, posY, posZ;
    private InterpType interpType;
    private boolean hasPos;

    public KeyFrame(float time, float rotX, float rotY, float rotZ, InterpType interpType) {
        this(time, rotX, rotY, rotZ, 0, 0, 0, interpType);
        this.hasPos = false;
    }

    public KeyFrame(float time, float rotX, float rotY, float rotZ, float posX, float posY, float posZ, InterpType interpType) {
        this.time = time; this.rotX = rotX; this.rotY = rotY; this.rotZ = rotZ;
        this.posX = posX; this.posY = posY; this.posZ = posZ;
        this.interpType = interpType; this.hasPos = true;
    }

    public static List<KeyFrame> cloneFrames(List<KeyFrame> keyFrames) {
        return new ArrayList<>(keyFrames);
    }

    public static void toRadians(List<KeyFrame> keyFrames) {
        float rad = 0.01745329F;
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame k = iter.next();
            KeyFrame n = new KeyFrame(k.time, k.rotX * rad, k.rotY * rad, k.rotZ * rad, k.posX, k.posY, k.posZ, k.interpType);
            n.hasPos = k.hasPos;
            iter.set(n);
        }
    }

    public static void mirrorFramesX(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame k = iter.next();
            KeyFrame n = new KeyFrame(k.time, k.rotX, -k.rotY, -k.rotZ, -k.posX, k.posY, k.posZ, k.interpType);
            n.hasPos = k.hasPos;
            iter.set(n);
        }
    }

    public static void mirrorFramesY(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame k = iter.next();
            KeyFrame n = new KeyFrame(k.time, -k.rotX, k.rotY, -k.rotZ, k.posX, -k.posY, k.posZ, k.interpType);
            n.hasPos = k.hasPos;
            iter.set(n);
        }
    }

    public static void mirrorFramesZ(List<KeyFrame> keyFrames) {
        ListIterator<KeyFrame> iter = keyFrames.listIterator();
        while (iter.hasNext()) {
            KeyFrame k = iter.next();
            KeyFrame n = new KeyFrame(k.time, -k.rotX, -k.rotY, k.rotZ, k.posX, k.posY, -k.posZ, k.interpType);
            n.hasPos = k.hasPos;
            iter.set(n);
        }
    }

    /** Circular offset of frames within [start,end]; full legacy algorithm preserved in spirit. */
    public static void offsetFramesCircular(List<KeyFrame> keyFrames, float start, float end, float offset) {
        if (keyFrames.size() < 1) return;
        float diff = end - start;
        offset %= diff;
        // simple rotate times within window
        List<KeyFrame> copy = cloneFrames(keyFrames);
        keyFrames.clear();
        for (KeyFrame k : copy) {
            if (k.time >= start && k.time <= end) {
                float t = k.time + offset;
                if (t > end) t -= diff;
                if (t < start) t += diff;
                KeyFrame n = new KeyFrame(t, k.rotX, k.rotY, k.rotZ, k.posX, k.posY, k.posZ, k.interpType);
                n.hasPos = k.hasPos;
                keyFrames.add(n);
            } else {
                keyFrames.add(k);
            }
        }
        keyFrames.sort((a, b) -> Float.compare(a.time, b.time));
    }

    public float getTime() { return time; }
    public float getRotX() { return rotX; }
    public float getRotY() { return rotY; }
    public float getRotZ() { return rotZ; }
    public float getPosX() { return posX; }
    public float getPosY() { return posY; }
    public float getPosZ() { return posZ; }
    public InterpType getInterpType() { return interpType; }
    public boolean hasPos() { return hasPos; }

    @Override
    public String toString() {
        return "(" + time + ", " + rotX + ", " + rotY + ", " + rotZ + ")";
    }
}
