/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util;

import invmod.common.util.ISelect;

public class SingleSelection<T>
implements ISelect<T> {
    private T object;

    public SingleSelection(T object) {
        this.object = object;
    }

    @Override
    public T selectNext() {
        return this.object;
    }

    @Override
    public void reset() {
    }

    public String toString() {
        return this.object.toString();
    }
}

