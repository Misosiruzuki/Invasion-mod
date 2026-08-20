/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.nexus;

import invmod.common.nexus.IWaveSource;
import invmod.common.nexus.Wave;

public class WaveBuilderNormal
implements IWaveSource {
    @Override
    public Wave getWave() {
        boolean difficulty = false;
        int lengthSeconds = 0;
        float basicMobsPerSecond = 0.12f * (float)difficulty;
        int numberOfGroups = 7;
        int numberOfBigGroups = 1;
        float proportionInGroups = 0.5f;
        int mobsPerGroup = Math.round(proportionInGroups * basicMobsPerSecond * (float)lengthSeconds / (float)(numberOfGroups + numberOfBigGroups * 2));
        int mobsPerBigGroup = mobsPerGroup * 2;
        int remainingMobs = (int)(basicMobsPerSecond * (float)lengthSeconds) - mobsPerGroup * numberOfGroups - mobsPerBigGroup * numberOfBigGroups;
        int mobsPerSteady = Math.round(0.7f * (float)remainingMobs / (float)numberOfGroups);
        int extraMobsForFinale = Math.round(0.3f * (float)remainingMobs);
        int extraMobsForCleanup = (int)(basicMobsPerSecond * (float)lengthSeconds * 0.2f);
        float timeForGroups = 0.5f;
        int groupTimeInterval = (int)((float)(lengthSeconds * 1000) * timeForGroups / (float)(numberOfGroups + numberOfBigGroups * 3));
        int steadyTimeInterval = (int)((float)(lengthSeconds * 1000) * (1.0f - timeForGroups) / (float)numberOfGroups);
        return null;
    }
}

