package com.whammich.invasion.util;

public final class MathUtil {
    private MathUtil() {}

    public static boolean floatEquals(float f1, float f2, float tolerance) {
        float diff = f1 - f2;
        return diff >= 0.0F ? diff < tolerance : -diff < tolerance;
    }

    public static double boundAnglePiRad(double angle) {
        angle %= Math.PI * 2.0;
        if (angle >= Math.PI) angle -= Math.PI * 2.0;
        else if (angle < -Math.PI) angle += Math.PI * 2.0;
        return angle;
    }

    public static double boundAngle180Deg(double angle) {
        angle %= 360.0;
        if (angle >= 180.0) angle -= 360.0;
        else if (angle < -180.0) angle += 360.0;
        return angle;
    }

    public static float interpRotationRad(float rot1, float rot2, float t) {
        return interpWrapped(rot1, rot2, t, -(float) Math.PI, (float) Math.PI);
    }

    public static float interpRotationDeg(float rot1, float rot2, float t) {
        return interpWrapped(rot1, rot2, t, -180.0F, 180.0F);
    }

    public static float interpWrapped(float val1, float val2, float t, float min, float max) {
        float dVal = val2 - val1;
        float range = max - min;
        while (dVal < min) dVal += range;
        while (dVal >= max) dVal -= range;
        return val1 + t * dVal;
    }

    public static float unpackFloat(int i) {
        return Float.intBitsToFloat(i);
    }

    public static int packFloat(float f) {
        return Float.floatToIntBits(f);
    }

    public static int packBytes(int i1, int i2, int i3, int i4) {
        return (i1 << 24) & 0xFF000000 | (i2 << 16) & 0xFF0000 | (i3 << 8) & 0xFF00 | i4 & 0xFF;
    }

    public static byte unpackBytes_1(int i) { return (byte) (i >>> 24); }
    public static byte unpackBytes_2(int i) { return (byte) (i >>> 16); }
    public static byte unpackBytes_3(int i) { return (byte) (i >>> 8); }
    public static byte unpackBytes_4(int i) { return (byte) i; }

    public static int packAnglesDeg(float a1, float a2, float a3, float a4) {
        return packBytes(
                (byte) (int) (a1 / 360.0F * 256.0F),
                (byte) (int) (a2 / 360.0F * 256.0F),
                (byte) (int) (a3 / 360.0F * 256.0F),
                (byte) (int) (a4 / 360.0F * 256.0F));
    }

    public static float unpackAnglesDeg_1(int i) { return unpackBytes_1(i) * 360.0F / 256.0F; }
    public static float unpackAnglesDeg_2(int i) { return unpackBytes_2(i) * 360.0F / 256.0F; }
    public static float unpackAnglesDeg_3(int i) { return unpackBytes_3(i) * 360.0F / 256.0F; }
    public static float unpackAnglesDeg_4(int i) { return unpackBytes_4(i) * 360.0F / 256.0F; }
}
