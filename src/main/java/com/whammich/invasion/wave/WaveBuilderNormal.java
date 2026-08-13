package com.whammich.invasion.wave;

import com.whammich.invasion.util.RandomSelectionPool;

import java.util.ArrayList;
import java.util.List;

public class WaveBuilderNormal implements IWaveSource {

    @Override
    public Wave generate(int waveNumber, int nexusLevel) {
        int duration = 90_000 + waveNumber * 5_000;
        int rest = 45_000;
        int amount = 4 + waveNumber + nexusLevel;

        EntityPattern zombie = new EntityPattern(IMEntityType.ZOMBIE);
        zombie.addTier(1, 3.0F);
        zombie.addTier(2, Math.min(waveNumber, 5));
        zombie.addTexture(0, 1.0F);

        EntityPattern pig = new EntityPattern(IMEntityType.ZOMBIE_PIGMAN);
        pig.addTier(1, 1.0F);
        pig.addTexture(0, 1.0F);

        RandomSelectionPool<IEntityIMPattern> pool = new RandomSelectionPool<>();
        pool.addEntry(zombie, 3.0F);
        if (waveNumber >= 3) pool.addEntry(pig, 1.0F);

        List<WaveEntry> entries = new ArrayList<>();
        entries.add(new WaveEntry(2_000, duration - 5_000, amount, 2_500, pool));
        return new Wave(duration, rest, entries);
    }
}
