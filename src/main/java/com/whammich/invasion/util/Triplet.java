package com.whammich.invasion.util;

public final class Triplet<A, B, C> {
    private A val1;
    private B val2;
    private C val3;

    public Triplet(A a, B b, C c) {
        this.val1 = a;
        this.val2 = b;
        this.val3 = c;
    }

    public A getVal1() { return val1; }
    public B getVal2() { return val2; }
    public C getVal3() { return val3; }
    public void setVal1(A v) { val1 = v; }
    public void setVal2(B v) { val2 = v; }
    public void setVal3(C v) { val3 = v; }
}
