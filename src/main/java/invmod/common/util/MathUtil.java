/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

public class MathUtil {
    public static boolean floatEquals(float f1, float f2, float tolerance) {
        float diff = f1 - f2;
        if (diff >= 0.0f) {
            return diff < tolerance;
        }
        return -diff < tolerance;
    }

    public static double boundAnglePiRad(double angle) {
        if ((angle %= Math.PI * 2) >= Math.PI) {
            angle -= Math.PI * 2;
        } else if (angle < -Math.PI) {
            angle += Math.PI * 2;
        }
        return angle;
    }

    public static double boundAngle180Deg(double angle) {
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        } else if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static float interpRotationRad(float rot1, float rot2, float t) {
        return MathUtil.interpWrapped(rot1, rot2, t, -3.141593f, 3.141593f);
    }

    public static float interpRotationDeg(float rot1, float rot2, float t) {
        return MathUtil.interpWrapped(rot1, rot2, t, -180.0f, 180.0f);
    }

    public static float interpWrapped(float val1, float val2, float t, float min, float max) {
        float dVal;
        for (dVal = val2 - val1; dVal < min; dVal += max - min) {
        }
        while (dVal >= max) {
            dVal -= max - min;
        }
        return val1 + t * dVal;
    }

    public static float unpackFloat(int i) {
        return Float.intBitsToFloat(i);
    }

    public static int packFloat(float f) {
        return Float.floatToIntBits(f);
    }

    public static int packAnglesDeg(float a1, float a2, float a3, float a4) {
        return MathUtil.packBytes((byte)(a1 / 360.0f * 256.0f), (byte)(a2 / 360.0f * 256.0f), (byte)(a3 / 360.0f * 256.0f), (byte)(a4 / 360.0f * 256.0f));
    }

    public static float unpackAnglesDeg_1(int i) {
        return (float)MathUtil.unpackBytes_1(i) * 360.0f / 256.0f;
    }

    public static float unpackAnglesDeg_2(int i) {
        return (float)MathUtil.unpackBytes_2(i) * 360.0f / 256.0f;
    }

    public static float unpackAnglesDeg_3(int i) {
        return (float)MathUtil.unpackBytes_3(i) * 360.0f / 256.0f;
    }

    public static float unpackAnglesDeg_4(int i) {
        return (float)MathUtil.unpackBytes_4(i) * 360.0f / 256.0f;
    }

    public static int packBytes(int i1, int i2, int i3, int i4) {
        return i1 << 24 & 0xFF000000 | i2 << 16 & 0xFF0000 | i3 << 8 & 0xFF00 | i4 & 0xFF;
    }

    public static byte unpackBytes_1(int i) {
        return (byte)(i >>> 24);
    }

    public static byte unpackBytes_2(int i) {
        return (byte)(i >>> 16 & 0xFF);
    }

    public static byte unpackBytes_3(int i) {
        return (byte)(i >>> 8 & 0xFF);
    }

    public static byte unpackBytes_4(int i) {
        return (byte)(i & 0xFF);
    }

    public static int packShorts(int i1, int i2) {
        return i1 << 16 | i2 & 0xFFFF;
    }

    public static short unhopackSrts_1(int i) {
        return (short)(i >>> 16);
    }

    public static int unpackShorts_2(int i) {
        return (short)(i & 0xFFFF);
    }
}

