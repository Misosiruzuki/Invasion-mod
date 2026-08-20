/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.nexus;

import invmod.common.nexus.EntityConstruct;
import invmod.common.nexus.IEntityIMPattern;
import invmod.common.nexus.IMEntityType;
import invmod.common.util.RandomSelectionPool;

public class EntityPattern
implements IEntityIMPattern {
    private IMEntityType entityType;
    private RandomSelectionPool<Integer> tierPool;
    private RandomSelectionPool<Integer> texturePool;
    private RandomSelectionPool<Integer> flavourPool;
    private static final int DEFAULT_TIER = 1;
    private static final int DEFAULT_FLAVOUR = 0;
    private static final int OPEN_TEXTURE = 0;
    private static final int OPEN_SCALING = 0;

    public EntityPattern(IMEntityType entityType) {
        this.entityType = entityType;
        this.tierPool = new RandomSelectionPool();
        this.texturePool = new RandomSelectionPool();
        this.flavourPool = new RandomSelectionPool();
    }

    @Override
    public EntityConstruct generateEntityConstruct() {
        return this.generateEntityConstruct(-180, 180);
    }

    @Override
    public EntityConstruct generateEntityConstruct(int minAngle, int maxAngle) {
        Integer flavour;
        Integer texture;
        Integer tier = this.tierPool.selectNext();
        if (tier == null) {
            tier = 1;
        }
        if ((texture = this.texturePool.selectNext()) == null) {
            texture = 0;
        }
        if ((flavour = this.flavourPool.selectNext()) == null) {
            flavour = 0;
        }
        return new EntityConstruct(this.entityType, tier, texture, flavour, 0.0f, minAngle, maxAngle);
    }

    public void addTier(int tier, float weight) {
        this.tierPool.addEntry(tier, weight);
    }

    public void addTexture(int texture, float weight) {
        this.texturePool.addEntry(texture, weight);
    }

    public void addFlavour(int flavour, float weight) {
        this.flavourPool.addEntry(flavour, weight);
    }

    public String toString() {
        return "EntityIMPattern@" + Integer.toHexString(this.hashCode()) + "#" + (Object)((Object)this.entityType);
    }
}

