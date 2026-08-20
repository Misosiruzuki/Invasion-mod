/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.util.IPolarAngle;

public class PolarAngle
implements IPolarAngle {
    private int angle;

    public PolarAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public int getAngle() {
        return this.angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}

