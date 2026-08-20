/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

public class Version {
    public int major;
    public int minor;
    public int build;

    public Version(int majorNum, int minorNum, int buildNum) {
        this.major = majorNum;
        this.minor = minorNum;
        this.build = buildNum;
    }

    public byte comparedState(Version version) {
        if (version.major > this.major) {
            return -1;
        }
        if (version.major == this.major) {
            if (version.minor > this.minor) {
                return -1;
            }
            if (version.minor == this.minor) {
                if (version.build > this.build) {
                    return -1;
                }
                if (version.build == this.build) {
                    return 0;
                }
                return 1;
            }
            return 1;
        }
        return 1;
    }

    public static Version get(String s) {
        String[] parts = s.split("\\.");
        if (parts.length != 3) {
            return null;
        }
        for (String i : parts) {
            char[] arr$ = i.toCharArray();
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                Character c = Character.valueOf(arr$[i$]);
                if (Character.isDigit(c.charValue())) continue;
                return null;
            }
        }
        int[] digits = new int[3];
        for (int i = 0; i < 3; ++i) {
            digits[i] = Integer.parseInt(parts[i]);
        }
        return new Version(digits[0], digits[1], digits[2]);
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.build;
    }
}

