package com.whammich.invasion.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {
    private final List<PathNode> nodes = new ArrayList<>();
    private int index;

    public void addNode(PathNode node) { nodes.add(node); }
    public void reverse() { Collections.reverse(nodes); }
    public boolean isFinished() { return index >= nodes.size(); }
    public PathNode getCurrentNode() { return isFinished() ? null : nodes.get(index); }
    public void next() { index++; }
    public void reset() { index = 0; }
    public int length() { return nodes.size(); }
}
