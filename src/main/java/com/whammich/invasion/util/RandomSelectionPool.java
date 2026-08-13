package com.whammich.invasion.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelectionPool<T> implements ISelect<T> {
    private final List<Pair<ISelect<T>, Float>> pool = new ArrayList<>();
    private float totalWeight;
    private final Random rand = new Random();

    public void addEntry(T entry, float weight) {
        addEntry(new SingleSelection<>(entry), weight);
    }

    public void addEntry(ISelect<T> entry, float weight) {
        pool.add(new Pair<>(entry, weight));
        totalWeight += weight;
    }

    @Override
    public T selectNext() {
        if (pool.isEmpty() || totalWeight <= 0) {
            return null;
        }
        float r = rand.nextFloat() * totalWeight;
        for (Pair<ISelect<T>, Float> entry : pool) {
            if (r < entry.getVal2()) {
                return entry.getVal1().selectNext();
            }
            r -= entry.getVal2();
        }
        return pool.get(0).getVal1().selectNext();
    }

    @Override
    public void reset() {
    }
}
