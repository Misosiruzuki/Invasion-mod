package com.whammich.invasion.util;

public final class Pair<A, B> {
    private final A first;
    private final B second;

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
}
