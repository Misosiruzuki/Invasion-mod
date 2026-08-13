package com.whammich.invasion.wave;

import java.util.ArrayList;
import java.util.List;

public class WaveContainer {
    private final List<Wave> waves = new ArrayList<>();

    public void addWave(Wave wave) { waves.add(wave); }

    public Wave getWave(int index) {
        if (index < 0 || index >= waves.size()) return null;
        return waves.get(index);
    }

    public int size() { return waves.size(); }
}
