package com.whammich.invasion.wave;

import com.whammich.invasion.util.RandomSelectionPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds invasion and continuous-mode waves.
 * <p>
 * {@link #generateWave(float, float, int)} is a simplified continuous-mode builder (P0 / A2).
 * Full 1.7 pattern tables are tracked as wiki-parity B-40-B-42.
 */
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

    /**
     * Continuous-mode wave from difficulty / tier (1.7 signature).
     * Scales mob count and duration from difficulty; tier unlocks stronger entries.
     * Not a full port of 1.7 group/finale tables (see B-40).
     */
    public Wave generateWave(float difficulty, float tierLevel, int lengthSeconds) {
        float diff = Math.max(0.5F, difficulty);
        float tier = Math.max(0.5F, tierLevel);
        int seconds = Math.max(30, lengthSeconds);
        int durationMs = seconds * 1_000;
        int restMs = 45_000;
        int amount = Math.max(4, Math.round((4 + tier * 3.0F) * diff));

        EntityPattern zombie = new EntityPattern(IMEntityType.ZOMBIE);
        zombie.addTier(1, 3.0F);
        zombie.addTier(2, Math.min(5.0F, tier * 2.0F));
        zombie.addTexture(0, 1.0F);

        EntityPattern pig = new EntityPattern(IMEntityType.ZOMBIE_PIGMAN);
        pig.addTier(1, 1.0F);
        pig.addTexture(0, 1.0F);

        EntityPattern spider = new EntityPattern(IMEntityType.SPIDER);
        spider.addTier(1, 1.0F);
        spider.addTexture(0, 1.0F);

        RandomSelectionPool<IEntityIMPattern> pool = new RandomSelectionPool<>();
        pool.addEntry(zombie, 3.0F);
        if (tier >= 1.2F) {
            pool.addEntry(pig, 1.0F);
        }
        if (tier >= 1.5F) {
            pool.addEntry(spider, 1.5F);
        }

        List<WaveEntry> entries = new ArrayList<>();
        int end = Math.max(5_000, durationMs - 5_000);
        entries.add(new WaveEntry(2_000, end, amount, 2_500, pool));
        return new Wave(durationMs, restMs, entries);
    }
}
