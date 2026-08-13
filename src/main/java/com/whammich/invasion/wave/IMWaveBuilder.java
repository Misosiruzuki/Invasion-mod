package com.whammich.invasion.wave;

public class IMWaveBuilder implements IWaveSource {

    private final WaveBuilderNormal normal = new WaveBuilderNormal();
    private final WaveBuilderSpiders spiders = new WaveBuilderSpiders();

    @Override
    public Wave generate(int waveNumber, int nexusLevel) {
        if (waveNumber > 0 && waveNumber % 5 == 0) {
            return spiders.generate(waveNumber, nexusLevel);
        }
        return normal.generate(waveNumber, nexusLevel);
    }
}
