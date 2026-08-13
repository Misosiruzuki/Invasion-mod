package com.whammich.invasion.wave;

/** Blueprint for one mob spawn (type + tier + angles). */
public class EntityConstruct {
    private final IMEntityType type;
    private final int tier;
    private final int texture;
    private final int flavour;
    private final float scaling;
    private final int minAngle;
    private final int maxAngle;

    public EntityConstruct(IMEntityType type, int tier, int texture, int flavour, float scaling, int minAngle, int maxAngle) {
        this.type = type;
        this.tier = tier;
        this.texture = texture;
        this.flavour = flavour;
        this.scaling = scaling;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    public IMEntityType getType() { return type; }
    public int getTier() { return tier; }
    public int getTexture() { return texture; }
    public int getFlavour() { return flavour; }
    public float getScaling() { return scaling; }
    public int getMinAngle() { return minAngle; }
    public int getMaxAngle() { return maxAngle; }

    @Override
    public String toString() {
        return type + " t" + tier + " ang[" + minAngle + "," + maxAngle + "]";
    }
}
