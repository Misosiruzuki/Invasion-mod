/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.nexus;

import invmod.common.mod_Invasion;
import invmod.common.nexus.EntityPattern;
import invmod.common.nexus.IEntityIMPattern;
import invmod.common.nexus.IMEntityType;
import invmod.common.nexus.Wave;
import invmod.common.nexus.WaveEntry;
import invmod.common.util.FiniteSelectionPool;
import invmod.common.util.ISelect;
import invmod.common.util.RandomSelectionPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IMWaveBuilder {
    public static final int WAVES_DEFINED = 11;
    private static final float ZOMBIE_T1_WEIGHT = 1.0f;
    private static final float ZOMBIE_T2_WEIGHT = 2.0f;
    private static final float SPIDER_T1_WEIGHT = 1.0f;
    private static final float SPIDER_T2_WEIGHT = 2.0f;
    private static Map<String, IEntityIMPattern> commonPatterns = new HashMap<String, IEntityIMPattern>();
    private Random rand = new Random();

    public Wave generateWave(float difficulty, float tierLevel, int lengthSeconds) {
        float basicMobsPerSecond = 0.12f * difficulty;
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
        int time = 0;
        ArrayList<WaveEntry> entryList = new ArrayList<WaveEntry>();
        for (int i = 0; i < numberOfGroups; ++i) {
            if (this.rand.nextInt(2) == 0) {
                entryList.add(new WaveEntry(time, time + 3500, mobsPerGroup, 500, this.generateGroupPool(tierLevel), 25, 3));
                time = time + groupTimeInterval;
                entryList.add(new WaveEntry(time, time += steadyTimeInterval, mobsPerSteady, 2000, this.generateSteadyPool(tierLevel), 160, 5));
                continue;
            }
            entryList.add(new WaveEntry(time, time += steadyTimeInterval, mobsPerSteady, 2000, this.generateSteadyPool(tierLevel), 160, 5));
            entryList.add(new WaveEntry(time, time + 5000, mobsPerGroup, 500, this.generateGroupPool(tierLevel), 25, 3));
            time += groupTimeInterval;
        }
        time = (int)((double)time + (double)groupTimeInterval * 0.75);
        FiniteSelectionPool<IEntityIMPattern> finaleGroup = new FiniteSelectionPool<IEntityIMPattern>();
        finaleGroup.addEntry(IMWaveBuilder.getPattern("thrower_t1"), mobsPerBigGroup / 5);
        this.generateGroupPool(tierLevel + 0.5f, finaleGroup, mobsPerBigGroup);
        WaveEntry finale = new WaveEntry(time, time + 8000, mobsPerBigGroup + mobsPerBigGroup / 7, 500, finaleGroup, 45, 3);
        finale.addAlert("A large number of mobs are slipping through the nexus rift!", 0);
        entryList.add(finale);
        entryList.add(new WaveEntry(time + 5000, (int)((float)time + (float)groupTimeInterval * 2.25f), extraMobsForFinale / 2, 500, this.generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 5000, (int)((float)time + (float)groupTimeInterval * 2.25f), extraMobsForFinale / 2, 500, this.generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 5000, (int)((float)time + (float)groupTimeInterval * 2.25f), extraMobsForFinale / 2, 500, this.generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 15000, (int)((float)(time + 10000) + (float)groupTimeInterval * 2.25f), extraMobsForCleanup, 500, this.generateSteadyPool(tierLevel)));
        time = (int)((double)time + (double)groupTimeInterval * 2.25);
        return new Wave(time + 16000, groupTimeInterval * 3, entryList);
    }

    private ISelect<IEntityIMPattern> generateGroupPool(float tierLevel) {
        RandomSelectionPool<IEntityIMPattern> newPool = new RandomSelectionPool<IEntityIMPattern>();
        this.generateGroupPool(tierLevel, newPool, 6.0f);
        return newPool;
    }

    private void generateGroupPool(float tierLevel, FiniteSelectionPool<IEntityIMPattern> startPool, int amount) {
        RandomSelectionPool<IEntityIMPattern> newPool = new RandomSelectionPool<IEntityIMPattern>();
        this.generateGroupPool(tierLevel, newPool, 6.0f);
        startPool.addEntry(newPool, amount);
    }

    private void generateGroupPool(float tierLevel, RandomSelectionPool<IEntityIMPattern> startPool, float weight) {
        float[] weights = new float[6];
        for (int i = 0; i < 6; ++i) {
            if (!(tierLevel - (float)i * 0.5f > 0.0f)) continue;
            weights[i] = tierLevel - (float)i <= 1.0f ? tierLevel - (float)i * 0.5f : 1.0f;
        }
        RandomSelectionPool<IEntityIMPattern> zombiePool = new RandomSelectionPool<IEntityIMPattern>();
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.0f * weights[0]);
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.0f * weights[2]);
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 1.0f * weights[3]);
        RandomSelectionPool<IEntityIMPattern> spiderPool = new RandomSelectionPool<IEntityIMPattern>();
        spiderPool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f * weights[0]);
        spiderPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 2.0f * weights[2]);
        RandomSelectionPool<IEntityIMPattern> basicPool = new RandomSelectionPool<IEntityIMPattern>();
        basicPool.addEntry(zombiePool, 3.1f);
        basicPool.addEntry(spiderPool, 0.7f);
        basicPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.8f);
        RandomSelectionPool<IEntityIMPattern> specialPool = new RandomSelectionPool<IEntityIMPattern>();
        specialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 4.0f);
        specialPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 1.1f * weights[4]);
        specialPool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 1.1f * weights[5]);
        specialPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.7f * weights[3]);
        startPool.addEntry(basicPool, weight * 0.8333333f);
        startPool.addEntry(specialPool, weight * 0.1666667f);
    }

    private ISelect<IEntityIMPattern> generateSteadyPool(float tierLevel) {
        float[] weights = new float[6];
        for (int i = 0; i < 6; ++i) {
            if (!(tierLevel - (float)i * 0.5f > 0.0f)) continue;
            weights[i] = tierLevel - (float)i <= 1.0f ? tierLevel - (float)i * 0.5f : 1.0f;
        }
        RandomSelectionPool<IEntityIMPattern> zombiePool = new RandomSelectionPool<IEntityIMPattern>();
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.0f * weights[0]);
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.0f * weights[2]);
        zombiePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 1.0f * weights[3]);
        RandomSelectionPool<IEntityIMPattern> spiderPool = new RandomSelectionPool<IEntityIMPattern>();
        spiderPool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f * weights[0]);
        spiderPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 2.0f * weights[2]);
        RandomSelectionPool<IEntityIMPattern> basicPool = new RandomSelectionPool<IEntityIMPattern>();
        basicPool.addEntry(zombiePool, 3.1f);
        basicPool.addEntry(spiderPool, 0.7f);
        basicPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.8f);
        RandomSelectionPool<IEntityIMPattern> specialPool = new RandomSelectionPool<IEntityIMPattern>();
        specialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 3.0f);
        specialPool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 1.1f * weights[5]);
        specialPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.8f * weights[3]);
        RandomSelectionPool<IEntityIMPattern> pool = new RandomSelectionPool<IEntityIMPattern>();
        pool.addEntry(basicPool, 9.0f);
        pool.addEntry(specialPool, 1.0f);
        return pool;
    }

    public static IEntityIMPattern getPattern(String s) {
        if (commonPatterns.containsKey(s)) {
            return commonPatterns.get(s);
        }
        mod_Invasion.log("Non-existing pattern name in wave definition: " + s);
        return commonPatterns.get("zombie_t1_any");
    }

    public static boolean isPatternNameValid(String s) {
        return commonPatterns.containsKey(s);
    }

    public Wave generateWave(int waveNumber, int difficulty) {
        return null;
    }

    public static Wave generateMainInvasionWave(int waveNumber) {
        if (waveNumber > 11) {
            return IMWaveBuilder.generateExtendedWave(waveNumber);
        }
        ArrayList<WaveEntry> entryList = new ArrayList<WaveEntry>();
        switch (waveNumber) {
            case 1: {
                RandomSelectionPool<IEntityIMPattern> wave1BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave1BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3.0f);
                wave1BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f);
                WaveEntry wave1Base = new WaveEntry(0, 90000, 8, 2000, wave1BasePool);
                entryList.add(wave1Base);
                FiniteSelectionPool<IEntityIMPattern> wave1BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave1BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave1BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2);
                WaveEntry wave1Engy = new WaveEntry(70000, 73000, 3, 500, wave1BurstPool, 25, 3);
                entryList.add(wave1Engy);
                return new Wave(110000, 15000, entryList);
            }
            case 2: {
                RandomSelectionPool<IEntityIMPattern> wave2BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave2BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3.0f);
                wave2BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f);
                wave2BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave2BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 0.5f);
                wave2BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.038f);
                WaveEntry wave2Base = new WaveEntry(0, 50000, 5, 2000, wave2BasePool, 110, 5);
                entryList.add(wave2Base);
                WaveEntry wave2Base2 = new WaveEntry(50000, 100000, 5, 2000, (ISelect<IEntityIMPattern>)wave2BasePool.clone(), 110, 5);
                entryList.add(wave2Base2);
                RandomSelectionPool<IEntityIMPattern> wave2SpecialPool = new RandomSelectionPool<IEntityIMPattern>();
                wave2SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1.0f);
                WaveEntry wave2Special = new WaveEntry(20000, 23000, 1, 500, wave2SpecialPool);
                entryList.add(wave2Special);
                FiniteSelectionPool<IEntityIMPattern> wave2BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave2BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave2BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2);
                WaveEntry wave2Burst = new WaveEntry(65000, 68000, 3, 500, wave2BurstPool, 25, 2);
                entryList.add(wave2Burst);
                return new Wave(120000, 15000, entryList);
            }
            case 3: {
                RandomSelectionPool<IEntityIMPattern> wave3BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave3BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3.0f);
                wave3BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f);
                wave3BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave3BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.04f);
                WaveEntry wave3Base1 = new WaveEntry(0, 30000, 6, 2000, wave3BasePool, 45, 3);
                entryList.add(wave3Base1);
                WaveEntry wave3Base2 = new WaveEntry(80000, 100000, 5, 2000, (ISelect<IEntityIMPattern>)wave3BasePool.clone(), 45, 3);
                entryList.add(wave3Base2);
                RandomSelectionPool<IEntityIMPattern> wave3SpecialPool = new RandomSelectionPool<IEntityIMPattern>();
                wave3SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1.0f);
                WaveEntry wave3Special = new WaveEntry(10000, 12000, 1, 500, wave3SpecialPool);
                entryList.add(wave3Special);
                FiniteSelectionPool<IEntityIMPattern> wave3BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1);
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_plain"), 1);
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1);
                wave3BurstPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 1);
                WaveEntry wave3Burst = new WaveEntry(50000, 55000, 5, 500, wave3BurstPool, 25, 6);
                wave3Burst.addAlert("A small group of mobs have gathered...", 0);
                entryList.add(wave3Burst);
                return new Wave(120000, 18000, entryList);
            }
            case 4: {
                RandomSelectionPool<IEntityIMPattern> wave4BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave4BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3.0f);
                wave4BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f);
                wave4BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave4BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.058f);
                WaveEntry wave4Base1 = new WaveEntry(0, 50000, 6, 2000, wave4BasePool, 110, 5);
                entryList.add(wave4Base1);
                WaveEntry wave4Base2 = new WaveEntry(50000, 100000, 6, 2000, (ISelect<IEntityIMPattern>)wave4BasePool.clone(), 110, 5);
                entryList.add(wave4Base2);
                FiniteSelectionPool<IEntityIMPattern> wave4SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave4SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave4SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                WaveEntry wave4Special = new WaveEntry(0, 90000, 3, 500, wave4SpecialPool);
                entryList.add(wave4Special);
                FiniteSelectionPool<IEntityIMPattern> wave4BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave4BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave4BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1);
                wave4BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t2_any"), 1);
                WaveEntry wave4Burst = new WaveEntry(70000, 75000, 2, 500, wave4BurstPool, 25, 2);
                entryList.add(wave4Burst);
                return new Wave(120000, 18000, entryList);
            }
            case 5: {
                RandomSelectionPool<IEntityIMPattern> wave5BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave5BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3.0f);
                wave5BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1.0f);
                wave5BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave5BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.054f);
                WaveEntry wave5Base1 = new WaveEntry(0, 40000, 6, 2000, wave5BasePool, 110, 5);
                entryList.add(wave5Base1);
                WaveEntry wave5Base2 = new WaveEntry(40000, 80000, 6, 2000, (ISelect<IEntityIMPattern>)wave5BasePool.clone(), 110, 5);
                entryList.add(wave5Base2);
                RandomSelectionPool<IEntityIMPattern> wave5SpecialPool = new RandomSelectionPool<IEntityIMPattern>();
                wave5SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1.0f);
                wave5SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1.0f);
                wave5SpecialPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 0.5f);
                WaveEntry wave5Special = new WaveEntry(0, 80000, 3, 500, wave5SpecialPool);
                entryList.add(wave5Special);
                FiniteSelectionPool<IEntityIMPattern> wave5BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3);
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1);
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave5BurstPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 1);
                WaveEntry wave5Burst = new WaveEntry(115000, 118000, 8, 500, wave5BurstPool, 35, 5);
                wave5Burst.addAlert("A large number of mobs are slipping through the nexus rift!", 0);
                entryList.add(wave5Burst);
                FiniteSelectionPool<IEntityIMPattern> wave5FinalePool = new FiniteSelectionPool<IEntityIMPattern>();
                wave5FinalePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3);
                wave5FinalePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1);
                wave5FinalePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave5FinalePool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave5FinalePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 1);
                WaveEntry wave5Finale = new WaveEntry(135000, 165000, 7, 500, wave5FinalePool);
                entryList.add(wave5Finale);
                return new Wave(130000, 80000, entryList);
            }
            case 6: {
                RandomSelectionPool<IEntityIMPattern> wave6BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave6BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2.0f);
                wave6BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1.0f);
                wave6BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 0.7f);
                wave6BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave6BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.064f);
                WaveEntry wave6Base1 = new WaveEntry(0, 50000, 7, 2000, wave6BasePool, 110, 5);
                entryList.add(wave6Base1);
                WaveEntry wave6Base2 = new WaveEntry(50000, 100000, 6, 2000, (ISelect<IEntityIMPattern>)wave6BasePool.clone(), 110, 5);
                entryList.add(wave6Base2);
                FiniteSelectionPool<IEntityIMPattern> wave6SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave6SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave6SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                WaveEntry wave6Special = new WaveEntry(0, 90000, 2, 500, wave6SpecialPool);
                entryList.add(wave6Special);
                FiniteSelectionPool<IEntityIMPattern> wave6BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave6BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave6BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave6BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1);
                wave6BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 1);
                WaveEntry wave6Burst = new WaveEntry(70000, 75000, 4, 500, wave6BurstPool, 25, 2);
                entryList.add(wave6Burst);
                return new Wave(110000, 25000, entryList);
            }
            case 7: {
                RandomSelectionPool<IEntityIMPattern> wave7BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave7BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2.0f);
                wave7BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1.0f);
                wave7BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 0.7f);
                wave7BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave7BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.064f);
                WaveEntry wave7Base1 = new WaveEntry(0, 30000, 7, 2000, wave7BasePool, 45, 5);
                entryList.add(wave7Base1);
                FiniteSelectionPool<IEntityIMPattern> wave7SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave7SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave7SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave7SpecialPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 1);
                WaveEntry wave7Special = new WaveEntry(0, 60000, 3, 500, wave7SpecialPool);
                entryList.add(wave7Special);
                FiniteSelectionPool<IEntityIMPattern> wave7BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave7BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave7BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave7BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1);
                wave7BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 1);
                wave7BurstPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                WaveEntry wave7Burst = new WaveEntry(65000, 67000, 5, 500, wave7BurstPool, 45, 2);
                entryList.add(wave7Burst);
                FiniteSelectionPool<IEntityIMPattern> wave7Burst2Pool = new FiniteSelectionPool<IEntityIMPattern>();
                wave7Burst2Pool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave7Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 3);
                WaveEntry wave7Burst2 = new WaveEntry(95000, 97000, 4, 500, wave7Burst2Pool, 45, 2);
                entryList.add(wave7Burst2);
                return new Wave(120000, 36000, entryList);
            }
            case 8: {
                RandomSelectionPool<IEntityIMPattern> wave8BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave8BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2.0f);
                wave8BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 1.5f);
                wave8BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 0.7f);
                wave8BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave8BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.064f);
                WaveEntry wave8Base1 = new WaveEntry(0, 35000, 7, 2000, wave8BasePool, 110, 5);
                entryList.add(wave8Base1);
                WaveEntry wave8Base2 = new WaveEntry(80000, 110000, 4, 2000, (ISelect<IEntityIMPattern>)wave8BasePool.clone(), 110, 5);
                entryList.add(wave8Base2);
                FiniteSelectionPool<IEntityIMPattern> wave8SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave8SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave8SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                WaveEntry wave8Special = new WaveEntry(0, 90000, 2, 500, wave8SpecialPool);
                entryList.add(wave8Special);
                FiniteSelectionPool<IEntityIMPattern> wave8BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave8BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave8BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 3);
                wave8BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2);
                wave8BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave8BurstPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 1);
                WaveEntry wave8Burst = new WaveEntry(60000, 63000, 8, 500, wave8BurstPool, 25, 2);
                wave8Burst.addAlert("A group of mobs have gathered...", 0);
                entryList.add(wave8Burst);
                return new Wave(110000, 30000, entryList);
            }
            case 9: {
                RandomSelectionPool<IEntityIMPattern> wave9BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave9BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2.0f);
                wave9BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.0f);
                wave9BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 0.7f);
                wave9BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave9BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.074f);
                WaveEntry wave9Base1 = new WaveEntry(0, 30000, 7, 2000, wave9BasePool, 45, 5);
                entryList.add(wave9Base1);
                FiniteSelectionPool<IEntityIMPattern> wave9SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave9SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave9SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                WaveEntry wave9Special = new WaveEntry(0, 90000, 3, 500, wave9SpecialPool);
                entryList.add(wave9Special);
                FiniteSelectionPool<IEntityIMPattern> wave9BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave9BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                wave9BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 3);
                wave9BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1);
                wave9BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave9BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 1);
                WaveEntry wave9Burst = new WaveEntry(65000, 67000, 6, 500, wave9BurstPool, 25, 3);
                entryList.add(wave9Burst);
                FiniteSelectionPool<IEntityIMPattern> wave9Burst2Pool = new FiniteSelectionPool<IEntityIMPattern>();
                wave9Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave9Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3);
                wave9Burst2Pool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                WaveEntry wave9Burst2 = new WaveEntry(95000, 97000, 6, 500, wave9Burst2Pool, 45, 2);
                entryList.add(wave9Burst2);
                return new Wave(120000, 35000, entryList);
            }
            case 10: {
                RandomSelectionPool<IEntityIMPattern> wave10BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.5f);
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.2f);
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 0.7f);
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 0.7f);
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave10BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.084f);
                WaveEntry wave10Base1 = new WaveEntry(0, 40000, 9, 2000, wave10BasePool, 110, 5);
                entryList.add(wave10Base1);
                WaveEntry wave10Base2 = new WaveEntry(40000, 80000, 7, 2000, (ISelect<IEntityIMPattern>)wave10BasePool.clone(), 110, 5);
                entryList.add(wave10Base2);
                RandomSelectionPool<IEntityIMPattern> wave10SpecialPool = new RandomSelectionPool<IEntityIMPattern>();
                wave10SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1.0f);
                wave10SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1.0f);
                WaveEntry wave10Special = new WaveEntry(0, 80000, 3, 500, wave10SpecialPool);
                entryList.add(wave10Special);
                FiniteSelectionPool<IEntityIMPattern> wave10BurstPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 2);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 3);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave10BurstPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 1);
                WaveEntry wave10Burst = new WaveEntry(125000, 128000, 12, 500, wave10BurstPool, 35, 5);
                wave10Burst.addAlert("A large number of mobs are slipping through the nexus rift!", 0);
                entryList.add(wave10Burst);
                FiniteSelectionPool<IEntityIMPattern> wave10FinalePool = new FiniteSelectionPool<IEntityIMPattern>();
                wave10FinalePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 2);
                wave10FinalePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave10FinalePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1);
                wave10FinalePool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave10FinalePool.addEntry(IMWaveBuilder.getPattern("spider_t1_any"), 2);
                WaveEntry wave10Finale = new WaveEntry(152000, 170000, 7, 500, wave10FinalePool);
                entryList.add(wave10Finale);
                return new Wave(172000, 60000, entryList);
            }
            case 11: {
                RandomSelectionPool<IEntityIMPattern> wave11BasePool = new RandomSelectionPool<IEntityIMPattern>();
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.5f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.2f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 0.185f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 0.8f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 0.1f);
                wave11BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.064f);
                WaveEntry wave11Base1 = new WaveEntry(0, 30000, 7, 2000, wave11BasePool, 45, 5);
                entryList.add(wave11Base1);
                FiniteSelectionPool<IEntityIMPattern> wave11SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
                wave11SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                wave11SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
                WaveEntry wave11Special = new WaveEntry(0, 90000, 3, 500, wave11SpecialPool);
                entryList.add(wave11Special);
                RandomSelectionPool<IEntityIMPattern> wave11BurstPool = new RandomSelectionPool<IEntityIMPattern>();
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1.0f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 2.0f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any"), 3.0f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.0f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1.0f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 0.8f);
                wave11BurstPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.8f);
                WaveEntry wave11Burst = new WaveEntry(65000, 67000, 7, 500, wave11BurstPool, 25, 3);
                entryList.add(wave11Burst);
                FiniteSelectionPool<IEntityIMPattern> wave11Burst2Pool = new FiniteSelectionPool<IEntityIMPattern>();
                wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
                wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3);
                wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
                WaveEntry wave11Burst2 = new WaveEntry(95000, 97000, 6, 500, wave11Burst2Pool, 45, 2);
                entryList.add(wave11Burst2);
                return new Wave(120000, 35000, entryList);
            }
        }
        return null;
    }

    private static Wave generateExtendedWave(int waveNumber) {
        float mobScale = (float)Math.pow(1.09f, waveNumber - 11);
        float timeScale = 1.0f + (float)(waveNumber - 11) * 0.04f;
        ArrayList<WaveEntry> entryList = new ArrayList<WaveEntry>();
        RandomSelectionPool<IEntityIMPattern> wave11BasePool = new RandomSelectionPool<IEntityIMPattern>();
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.5f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2.2f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 0.26f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 0.8f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t2_any"), 0.5f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t2_any"), 0.054f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 0.7f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 0.18f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("thrower_t2"), 0.054f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.054f);
        wave11BasePool.addEntry(IMWaveBuilder.getPattern("imp_t1"), 0.4f);
        WaveEntry wave11Base1 = new WaveEntry(0, (int)(timeScale * 30000.0f), (int)(mobScale * 7.0f), 2000, wave11BasePool, 45, 5);
        entryList.add(wave11Base1);
        FiniteSelectionPool<IEntityIMPattern> wave11SpecialPool = new FiniteSelectionPool<IEntityIMPattern>();
        wave11SpecialPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 2);
        wave11SpecialPool.addEntry(IMWaveBuilder.getPattern("pigengy_t1_any"), 1);
        WaveEntry wave11Special = new WaveEntry(0, (int)(timeScale * 90000.0f), (int)(mobScale * 3.0f), 500, wave11SpecialPool);
        entryList.add(wave11Special);
        RandomSelectionPool<IEntityIMPattern> wave11BurstPool = new RandomSelectionPool<IEntityIMPattern>();
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t1_any"), 1.5f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t2_any"), 0.7f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombiePigman_t3_any"), 0.35f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any"), 1.5f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1.0f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 1.0f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("skeleton_t1_any"), 1.0f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("thrower_t1"), 0.5f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("thrower_t2"), 0.42f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("zombie_t3_any"), 0.5f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("creeper_t1_basic"), 0.42f);
        wave11BurstPool.addEntry(IMWaveBuilder.getPattern("imp_t1"), 0.4f);
        WaveEntry wave11Burst = new WaveEntry((int)(timeScale * 65000.0f), (int)(timeScale * 67000.0f), (int)(mobScale * 7.0f), 500, wave11BurstPool, 25, 3);
        entryList.add(wave11Burst);
        FiniteSelectionPool<IEntityIMPattern> wave11Burst2Pool = new FiniteSelectionPool<IEntityIMPattern>();
        wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t2_any_basic"), 2);
        wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("zombie_t1_any"), 3);
        wave11Burst2Pool.addEntry(IMWaveBuilder.getPattern("spider_t2_any"), 1);
        WaveEntry wave11Burst2 = new WaveEntry((int)(timeScale * 95000.0f), (int)(timeScale * 97000.0f), (int)(mobScale * 6.0f), 500, wave11Burst2Pool, 45, 2);
        entryList.add(wave11Burst2);
        return new Wave((int)(timeScale * 120000.0f), (int)(timeScale * 35000.0f), entryList);
    }

    static {
        EntityPattern zombieT1Any = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT1Any.addTier(1, 1.0f);
        zombieT1Any.addFlavour(0, 3.0f);
        zombieT1Any.addFlavour(1, 1.0f);
        EntityPattern zombieT2Basic = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Basic.addTier(2, 1.0f);
        zombieT2Basic.addFlavour(0, 2.0f);
        zombieT2Basic.addFlavour(1, 1.0f);
        zombieT2Basic.addFlavour(2, 0.4f);
        EntityPattern zombieT2Plain = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Plain.addTier(2, 1.0f);
        zombieT2Plain.addFlavour(0, 1.0f);
        EntityPattern zombieT2Tar = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Tar.addTier(2, 1.0f);
        zombieT2Tar.addFlavour(2, 1.0f);
        zombieT2Tar.addTexture(5, 1.0f);
        EntityPattern zombieT3Any = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT3Any.addTier(3, 1.0f);
        zombieT3Any.addTexture(0, 1.0f);
        EntityPattern zombiePigmanT1Any = new EntityPattern(IMEntityType.ZOMBIEPIGMAN);
        zombiePigmanT1Any.addTier(1, 1.0f);
        zombiePigmanT1Any.addFlavour(0, 1.0f);
        EntityPattern zombiePigmanT2Any = new EntityPattern(IMEntityType.ZOMBIEPIGMAN);
        zombiePigmanT1Any.addTier(2, 1.0f);
        zombiePigmanT1Any.addFlavour(0, 1.0f);
        EntityPattern zombiePigmanT3Any = new EntityPattern(IMEntityType.ZOMBIEPIGMAN);
        zombiePigmanT1Any.addTier(3, 1.0f);
        zombiePigmanT1Any.addFlavour(0, 1.0f);
        EntityPattern spiderT1Any = new EntityPattern(IMEntityType.SPIDER);
        spiderT1Any.addTier(1, 1.0f);
        EntityPattern spiderT2Any = new EntityPattern(IMEntityType.SPIDER);
        spiderT2Any.addTier(2, 1.0f);
        spiderT2Any.addFlavour(0, 1.0f);
        spiderT2Any.addFlavour(1, 1.0f);
        EntityPattern pigEngyT1Any = new EntityPattern(IMEntityType.PIG_ENGINEER);
        pigEngyT1Any.addTier(1, 1.0f);
        EntityPattern skeletonT1Any = new EntityPattern(IMEntityType.SKELETON);
        skeletonT1Any.addTier(1, 1.0f);
        EntityPattern throwerT1 = new EntityPattern(IMEntityType.THROWER);
        throwerT1.addTier(1, 1.0f);
        EntityPattern throwerT2 = new EntityPattern(IMEntityType.THROWER);
        throwerT2.addTier(2, 1.0f);
        EntityPattern burrower = new EntityPattern(IMEntityType.BURROWER);
        burrower.addTier(1, 1.0f);
        EntityPattern creeper = new EntityPattern(IMEntityType.CREEPER);
        creeper.addTier(1, 1.0f);
        EntityPattern imp = new EntityPattern(IMEntityType.IMP);
        imp.addTier(1, 1.0f);
        commonPatterns.put("zombie_t1_any", zombieT1Any);
        commonPatterns.put("zombie_t2_any_basic", zombieT2Basic);
        commonPatterns.put("zombie_t2_plain", zombieT2Plain);
        commonPatterns.put("zombie_t2_tar", zombieT2Tar);
        commonPatterns.put("zombie_t3_any", zombieT3Any);
        commonPatterns.put("zombie_t3_any", zombieT3Any);
        commonPatterns.put("zombiePigman_t1_any", zombiePigmanT1Any);
        commonPatterns.put("zombiePigman_t2_any", zombiePigmanT2Any);
        commonPatterns.put("zombiePigman_t3_any", zombiePigmanT3Any);
        commonPatterns.put("zombie_t3_any", zombieT3Any);
        commonPatterns.put("zombie_t3_any", zombieT3Any);
        commonPatterns.put("spider_t1_any", spiderT1Any);
        commonPatterns.put("spider_t2_any", spiderT2Any);
        commonPatterns.put("pigengy_t1_any", pigEngyT1Any);
        commonPatterns.put("skeleton_t1_any", skeletonT1Any);
        commonPatterns.put("thrower_t1", throwerT1);
        commonPatterns.put("thrower_t2", throwerT2);
        commonPatterns.put("burrower", burrower);
        commonPatterns.put("creeper_t1_basic", creeper);
        commonPatterns.put("imp_t1", imp);
    }
}

