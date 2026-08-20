/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.mod_Invasion;
import invmod.common.util.ISelect;
import invmod.common.util.Pair;
import invmod.common.util.SingleSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelectionPool<EntityIMLiving>
implements ISelect<EntityIMLiving> {
    private List<Pair<ISelect<EntityIMLiving>, Float>> pool = new ArrayList<Pair<ISelect<EntityIMLiving>, Float>>();
    private float totalWeight = 0.0f;
    private Random rand = new Random();

    public void addEntry(EntityIMLiving entry, float weight) {
        SingleSelection<EntityIMLiving> selection = new SingleSelection<EntityIMLiving>(entry);
        this.addEntry((ISelect<EntityIMLiving>)selection, weight);
    }

    public void addEntry(ISelect<EntityIMLiving> entry, float weight) {
        this.pool.add(new Pair<ISelect<EntityIMLiving>, Float>(entry, Float.valueOf(weight)));
        this.totalWeight += weight;
    }

    @Override
    public EntityIMLiving selectNext() {
        float r = this.rand.nextFloat() * this.totalWeight;
        for (Pair<ISelect<EntityIMLiving>, Float> entry : this.pool) {
            if (r < entry.getVal2().floatValue()) {
                return entry.getVal1().selectNext();
            }
            r -= entry.getVal2().floatValue();
        }
        if (this.pool.size() > 0) {
            mod_Invasion.log("RandomSelectionPool invalid setup or rounding error. Failing safe.");
            return this.pool.get(0).getVal1().selectNext();
        }
        return null;
    }

    public RandomSelectionPool<EntityIMLiving> clone() {
        RandomSelectionPool<EntityIMLiving> clone = new RandomSelectionPool<EntityIMLiving>();
        for (Pair<ISelect<EntityIMLiving>, Float> entry : this.pool) {
            clone.addEntry(entry.getVal1(), entry.getVal2().floatValue());
        }
        return clone;
    }

    @Override
    public void reset() {
    }

    public String toString() {
        String s = "RandomSelectionPool@" + Integer.toHexString(this.hashCode()) + "#Size=" + this.pool.size();
        for (int i = 0; i < this.pool.size(); ++i) {
            s = s + "\n\tEntry " + i + "   Weight: " + this.pool.get(i).getVal2();
            s = s + "\n\t" + this.pool.get(i).getVal1().toString();
        }
        return s;
    }
}

