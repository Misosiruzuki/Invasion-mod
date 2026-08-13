package com.whammich.invasion.wave;

public class InvMobConstruct {
    private final int texture;
    private final int tier;
    private final int flavour;
    private final float scaling;

    public InvMobConstruct(int texture, int tier, int flavour, float scaling) {
        this.texture = texture;
        this.tier = tier;
        this.flavour = flavour;
        this.scaling = scaling;
    }

    public int getTexture() { return texture; }
    public int getTier() { return tier; }
    public int getFlavour() { return flavour; }
    public float getScaling() { return scaling; }
}
