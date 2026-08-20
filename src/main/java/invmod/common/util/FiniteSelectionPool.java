/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.util.ISelect;
import invmod.common.util.Pair;
import invmod.common.util.SingleSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FiniteSelectionPool<T>
implements ISelect<T> {
    private List<Pair<ISelect<T>, Integer>> currentPool = new ArrayList<Pair<ISelect<T>, Integer>>();
    private List<Integer> originalPool = new ArrayList<Integer>();
    private int totalAmount = 0;
    private int originalAmount;
    private Random rand = new Random();

    public void addEntry(T entry, int amount) {
        SingleSelection<T> selection = new SingleSelection<T>(entry);
        this.addEntry(selection, amount);
    }

    public void addEntry(ISelect<T> entry, int amount) {
        this.currentPool.add(new Pair<ISelect<T>, Integer>(entry, amount));
        this.originalPool.add(amount);
        this.originalAmount = this.totalAmount += amount;
    }

    @Override
    public T selectNext() {
        if (this.totalAmount < 1) {
            this.regeneratePool();
        }
        float r = this.rand.nextInt(this.totalAmount);
        for (Pair<ISelect<T>, Integer> entry : this.currentPool) {
            int amountLeft = entry.getVal2();
            if (r < (float)amountLeft) {
                entry.setVal2(amountLeft - 1);
                --this.totalAmount;
                return entry.getVal1().selectNext();
            }
            r -= (float)amountLeft;
        }
        return null;
    }

    public FiniteSelectionPool<T> clone() {
        FiniteSelectionPool<T> clone = new FiniteSelectionPool<T>();
        for (int i = 0; i < this.currentPool.size(); ++i) {
            clone.addEntry(this.currentPool.get(i).getVal1(), (int)this.originalPool.get(i));
        }
        return clone;
    }

    @Override
    public void reset() {
        this.regeneratePool();
    }

    public String toString() {
        String s = "FiniteSelectionPool@" + Integer.toHexString(this.hashCode()) + "#Size=" + this.currentPool.size();
        for (int i = 0; i < this.currentPool.size(); ++i) {
            s = s + "\n\tEntry " + i + "   Amount: " + this.originalPool.get(i);
            s = s + "\n\t" + this.currentPool.get(i).getVal1().toString();
        }
        return s;
    }

    private void regeneratePool() {
        this.totalAmount = this.originalAmount;
        for (int i = 0; i < this.currentPool.size(); ++i) {
            this.currentPool.get(i).setVal2(this.originalPool.get(i));
        }
    }
}

