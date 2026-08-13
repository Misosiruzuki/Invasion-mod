package com.whammich.invasion.wave;

import com.whammich.invasion.util.RandomSelectionPool;

import java.util.ArrayList;
import java.util.List;

public class WaveBuilderSpiders implements IWaveSource {

    @Override
    public Wave generate(int waveNumber, int nexusLevel) {
        int duration = 75_000 + waveNumber * 4_000;
        int rest = 40_000;
        int amount = 6 + waveNumber * 2;

        EntityPattern spider = new EntityPattern(IMEntityType.SPIDER);
        spider.addTier(1, 2.0F);
        spider.addTier(2, 1.0F);
        spider.addTexture(0, 1.0F);

        RandomSelectionPool<IEntityIMPattern> pool = new RandomSelectionPool<>();
        pool.addEntry(spider, 1.0F);

        List<WaveEntry> entries = new ArrayList<>();
        entries.add(new WaveEntry(1_000, duration - 3_000, amount, 1_800, pool, -180, 180, 1));
        return new Wave(duration, rest, entries);
    }
}
