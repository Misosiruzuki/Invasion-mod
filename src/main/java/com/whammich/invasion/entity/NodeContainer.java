package com.whammich.invasion.entity;

import java.util.PriorityQueue;

public class NodeContainer {
    private final PriorityQueue<PathNode> open = new PriorityQueue<>((a, b) -> Float.compare(a.total(), b.total()));
    public void add(PathNode node) { open.offer(node); }
    public PathNode poll() { return open.poll(); }
    public boolean isEmpty() { return open.isEmpty(); }
    public void clear() { open.clear(); }
}
