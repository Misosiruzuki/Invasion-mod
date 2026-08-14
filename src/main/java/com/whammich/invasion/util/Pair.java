package com.whammich.invasion.util;

/** Simple mutable pair (legacy Pair; val2 is mutable for FiniteSelectionPool). */
public final class Pair<A, B> {
    private final A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getVal1() {
        return first;
    }

    public B getVal2() {
        return second;
    }

    public void setVal2(B second) {
        this.second = second;
    }
}
