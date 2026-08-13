package com.whammich.invasion.wave;

import com.whammich.invasion.util.RandomSelectionPool;

public class EntityPattern implements IEntityIMPattern {
    private final IMEntityType entityType;
    private final RandomSelectionPool<Integer> tierPool = new RandomSelectionPool<>();
    private final RandomSelectionPool<Integer> texturePool = new RandomSelectionPool<>();
    private final RandomSelectionPool<Integer> flavourPool = new RandomSelectionPool<>();

    public EntityPattern(IMEntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public EntityConstruct generateEntityConstruct() {
        return generateEntityConstruct(-180, 180);
    }

    @Override
    public EntityConstruct generateEntityConstruct(int minAngle, int maxAngle) {
        Integer tier = tierPool.selectNext();
        if (tier == null) tier = 1;
        Integer texture = texturePool.selectNext();
        if (texture == null) texture = 0;
        Integer flavour = flavourPool.selectNext();
        if (flavour == null) flavour = 0;
        return new EntityConstruct(entityType, tier, texture, flavour, 0.0F, minAngle, maxAngle);
    }

    public void addTier(int tier, float weight) { tierPool.addEntry(tier, weight); }
    public void addTexture(int texture, float weight) { texturePool.addEntry(texture, weight); }
    public void addFlavour(int flavour, float weight) { flavourPool.addEntry(flavour, weight); }
    public IMEntityType getEntityType() { return entityType; }
}
