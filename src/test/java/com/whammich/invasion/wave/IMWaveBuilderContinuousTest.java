package com.whammich.invasion.wave;

import com.whammich.invasion.nexus.ContinuousSchedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** B-40 / B-41 / B-42: continuous generateWave structure and scaling. */
class IMWaveBuilderContinuousTest {

    @Test
    void difficultyFromPowerMatchesLegacy() {
        assertEquals(1.0F, ContinuousSchedule.difficultyFromPower(0), 0.0001F);
        assertEquals(1.0F + 4500 / 4500.0F, ContinuousSchedule.difficultyFromPower(4500), 0.0001F);
        assertEquals(1.0F + 9000 / 4500.0F, ContinuousSchedule.difficultyFromPower(9000), 0.0001F);
    }

    @Test
    void defaultWaveLengthIs240Seconds() {
        assertEquals(240, ContinuousSchedule.DEFAULT_WAVE_LENGTH_SECONDS);
    }

    @Test
    void generateWaveUsesLengthAndProducesMultipleEntries() {
        IMWaveBuilder builder = new IMWaveBuilder();
        float diff = ContinuousSchedule.difficultyFromPower(0);
        Wave wave = builder.generateWave(diff, diff, ContinuousSchedule.DEFAULT_WAVE_LENGTH_SECONDS);
        assertTrue(wave.getWaveTotalTime() > 60_000, "wave should span well over a minute of millis");
        assertTrue(wave.getTotalMobAmount() > 10, "baseline continuous wave should queue many mobs");
        // ~240s * 0.12 mps * 1.0 difficulty ≈ 28.8 base + groups/finale overhead
        assertTrue(wave.getTotalMobAmount() < 200, "power0 continuous should not be absurdly large");
    }

    @Test
    void higherPowerIncreasesMobCount() {
        IMWaveBuilder builder = new IMWaveBuilder();
        float low = ContinuousSchedule.difficultyFromPower(0);
        float high = ContinuousSchedule.difficultyFromPower(9000);
        Wave lowWave = builder.generateWave(low, low, 240);
        Wave highWave = builder.generateWave(high, high, 240);
        assertTrue(highWave.getTotalMobAmount() > lowWave.getTotalMobAmount(),
                "higher powerLevel difficulty should yield denser continuous waves");
    }
}
