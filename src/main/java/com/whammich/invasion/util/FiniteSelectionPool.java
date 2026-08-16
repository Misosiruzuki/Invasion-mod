package com.whammich.invasion.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Finite-count selection pool (1.7 invmod.common.util.FiniteSelectionPool).
 * Each entry may be drawn a fixed number of times before the pool regenerates.
 */
public class FiniteSelectionPool<T> implements ISelect<T> {
    private final List<Pair<ISelect<T>, Integer>> currentPool = new ArrayList<>();
    private final List<Integer> originalPool = new ArrayList<>();
    private int totalAmount;
    private int originalAmount;
    private final Random rand = new Random();

    public void addEntry(T entry, int amount) {
        addEntry(new SingleSelection<>(entry), amount);
    }

    public void addEntry(ISelect<T> entry, int amount) {
        currentPool.add(new Pair<>(entry, amount));
        originalPool.add(amount);
        originalAmount = (totalAmount += amount);
    }

    @Override
    public T selectNext() {
        if (totalAmount < 1) {
            regeneratePool();
        }
        if (totalAmount < 1) {
            return null;
        }
        float r = rand.nextInt(totalAmount);
        for (Pair<ISelect<T>, Integer> entry : currentPool) {
            int amountLeft = entry.getVal2();
            if (r < amountLeft) {
                entry.setVal2(amountLeft - 1);
                totalAmount -= 1;
                return entry.getVal1().selectNext();
            }
            r -= amountLeft;
        }
        return null;
    }

    public FiniteSelectionPool<T> clonePool() {
        FiniteSelectionPool<T> clone = new FiniteSelectionPool<>();
        for (int i = 0; i < currentPool.size(); i++) {
            clone.addEntry(currentPool.get(i).getVal1(), originalPool.get(i));
        }
        return clone;
    }

    @Override
    public void reset() {
        regeneratePool();
    }

    private void regeneratePool() {
        totalAmount = originalAmount;
        for (int i = 0; i < currentPool.size(); i++) {
            currentPool.get(i).setVal2(originalPool.get(i));
        }
    }
}
