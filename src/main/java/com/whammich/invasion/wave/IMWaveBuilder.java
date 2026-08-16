package com.whammich.invasion.wave;

import com.whammich.invasion.util.FiniteSelectionPool;
import com.whammich.invasion.util.ISelect;
import com.whammich.invasion.util.RandomSelectionPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Builds invasion and continuous-mode waves.
 * <p>
 * Continuous {@link #generateWave(float, float, int)} matches 1.7.10 group / finale /
 * pattern tables (wiki B-40). Difficulty scaling and lengthSeconds≈240 are B-41 / B-42.
 */
public class IMWaveBuilder implements IWaveSource {

    private static final Map<String, IEntityIMPattern> COMMON_PATTERNS = new HashMap<>();
    private static final Random RAND = new Random();

    static {
        initCommonPatterns();
    }

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
     * Continuous-mode wave (1.7 IMWaveBuilder.generateWave(difficulty, tier, lengthSeconds)).
     *
     * @param difficulty     typically {@code 1 + powerLevel/4500}
     * @param tierLevel      same as difficulty in 1.7 continuous path
     * @param lengthSeconds  typically 240
     */
    public Wave generateWave(float difficulty, float tierLevel, int lengthSeconds) {
        float basicMobsPerSecond = 0.12F * Math.max(0.5F, difficulty);
        int numberOfGroups = 7;
        int numberOfBigGroups = 1;
        float proportionInGroups = 0.5F;
        int len = Math.max(30, lengthSeconds);

        int mobsPerGroup = Math.round(proportionInGroups * basicMobsPerSecond * len
                / (numberOfGroups + numberOfBigGroups * 2));
        int mobsPerBigGroup = mobsPerGroup * 2;
        int remainingMobs = (int) (basicMobsPerSecond * len)
                - mobsPerGroup * numberOfGroups
                - mobsPerBigGroup * numberOfBigGroups;
        int mobsPerSteady = Math.round(0.7F * remainingMobs / numberOfGroups);
        int extraMobsForFinale = Math.round(0.3F * remainingMobs);
        int extraMobsForCleanup = (int) (basicMobsPerSecond * len * 0.2F);
        float timeForGroups = 0.5F;
        int groupTimeInterval = (int) (len * 1000 * timeForGroups / (numberOfGroups + numberOfBigGroups * 3));
        int steadyTimeInterval = (int) (len * 1000 * (1.0F - timeForGroups) / numberOfGroups);

        int time = 0;
        List<WaveEntry> entryList = new ArrayList<>();
        for (int i = 0; i < numberOfGroups; i++) {
            if (RAND.nextInt(2) == 0) {
                entryList.add(new WaveEntry(time, time + 3500, Math.max(1, mobsPerGroup), 500,
                        generateGroupPool(tierLevel), 25, 3));
                time += groupTimeInterval;
                entryList.add(new WaveEntry(time, time + steadyTimeInterval, Math.max(0, mobsPerSteady), 2000,
                        generateSteadyPool(tierLevel), 160, 5));
                time += steadyTimeInterval;
            } else {
                entryList.add(new WaveEntry(time, time + steadyTimeInterval, Math.max(0, mobsPerSteady), 2000,
                        generateSteadyPool(tierLevel), 160, 5));
                time += steadyTimeInterval;
                entryList.add(new WaveEntry(time, time + 5000, Math.max(1, mobsPerGroup), 500,
                        generateGroupPool(tierLevel), 25, 3));
                time += groupTimeInterval;
            }
        }

        time = (int) (time + groupTimeInterval * 0.75D);
        FiniteSelectionPool<IEntityIMPattern> finaleGroup = new FiniteSelectionPool<>();
        finaleGroup.addEntry(getPattern("thrower_t1"), Math.max(1, mobsPerBigGroup / 5));
        generateGroupPool(tierLevel + 0.5F, finaleGroup, Math.max(1, mobsPerBigGroup));
        WaveEntry finale = new WaveEntry(time, time + 8000,
                Math.max(1, mobsPerBigGroup + mobsPerBigGroup / 7), 500, finaleGroup, 45, 3);
        finale.addAlert(0, "A large number of mobs are slipping through the nexus rift!");
        entryList.add(finale);

        int finaleEnd = (int) (time + groupTimeInterval * 2.25F);
        entryList.add(new WaveEntry(time + 5000, finaleEnd, Math.max(0, extraMobsForFinale / 2), 500,
                generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 5000, finaleEnd, Math.max(0, extraMobsForFinale / 2), 500,
                generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 5000, finaleEnd, Math.max(0, extraMobsForFinale / 2), 500,
                generateSteadyPool(tierLevel), 160, 5));
        entryList.add(new WaveEntry(time + 15000, (int) (time + 10000 + groupTimeInterval * 2.25F),
                Math.max(0, extraMobsForCleanup), 500, generateSteadyPool(tierLevel)));
        time = (int) (time + groupTimeInterval * 2.25D);

        return new Wave(time + 16000, groupTimeInterval * 3, entryList);
    }

    private ISelect<IEntityIMPattern> generateGroupPool(float tierLevel) {
        RandomSelectionPool<IEntityIMPattern> newPool = new RandomSelectionPool<>();
        generateGroupPool(tierLevel, newPool, 6.0F);
        return newPool;
    }

    private void generateGroupPool(float tierLevel, FiniteSelectionPool<IEntityIMPattern> startPool, int amount) {
        RandomSelectionPool<IEntityIMPattern> newPool = new RandomSelectionPool<>();
        generateGroupPool(tierLevel, newPool, 6.0F);
        startPool.addEntry(newPool, amount);
    }

    private void generateGroupPool(float tierLevel, RandomSelectionPool<IEntityIMPattern> startPool, float weight) {
        float[] weights = tierWeights(tierLevel);

        RandomSelectionPool<IEntityIMPattern> zombiePool = new RandomSelectionPool<>();
        zombiePool.addEntry(getPattern("zombie_t1_any"), 1.0F * weights[0]);
        zombiePool.addEntry(getPattern("zombie_t2_any_basic"), 2.0F * weights[2]);
        zombiePool.addEntry(getPattern("zombiePigman_t1_any"), 1.0F * weights[3]);

        RandomSelectionPool<IEntityIMPattern> spiderPool = new RandomSelectionPool<>();
        spiderPool.addEntry(getPattern("spider_t1_any"), 1.0F * weights[0]);
        spiderPool.addEntry(getPattern("spider_t2_any"), 2.0F * weights[2]);

        RandomSelectionPool<IEntityIMPattern> basicPool = new RandomSelectionPool<>();
        basicPool.addEntry(zombiePool, 3.1F);
        basicPool.addEntry(spiderPool, 0.7F);
        basicPool.addEntry(getPattern("skeleton_t1_any"), 0.8F);

        RandomSelectionPool<IEntityIMPattern> specialPool = new RandomSelectionPool<>();
        specialPool.addEntry(getPattern("pigengy_t1_any"), 4.0F);
        specialPool.addEntry(getPattern("thrower_t1"), 1.1F * weights[4]);
        specialPool.addEntry(getPattern("zombie_t3_any"), 1.1F * weights[5]);
        specialPool.addEntry(getPattern("creeper_t1_basic"), 0.7F * weights[3]);

        startPool.addEntry(basicPool, weight * 0.8333333F);
        startPool.addEntry(specialPool, weight * 0.1666667F);
    }

    private ISelect<IEntityIMPattern> generateSteadyPool(float tierLevel) {
        float[] weights = tierWeights(tierLevel);

        RandomSelectionPool<IEntityIMPattern> zombiePool = new RandomSelectionPool<>();
        zombiePool.addEntry(getPattern("zombie_t1_any"), 1.0F * weights[0]);
        zombiePool.addEntry(getPattern("zombie_t2_any_basic"), 2.0F * weights[2]);
        zombiePool.addEntry(getPattern("zombiePigman_t1_any"), 1.0F * weights[3]);

        RandomSelectionPool<IEntityIMPattern> spiderPool = new RandomSelectionPool<>();
        spiderPool.addEntry(getPattern("spider_t1_any"), 1.0F * weights[0]);
        spiderPool.addEntry(getPattern("spider_t2_any"), 2.0F * weights[2]);

        RandomSelectionPool<IEntityIMPattern> basicPool = new RandomSelectionPool<>();
        basicPool.addEntry(zombiePool, 3.1F);
        basicPool.addEntry(spiderPool, 0.7F);
        basicPool.addEntry(getPattern("skeleton_t1_any"), 0.8F);

        RandomSelectionPool<IEntityIMPattern> specialPool = new RandomSelectionPool<>();
        specialPool.addEntry(getPattern("pigengy_t1_any"), 3.0F);
        specialPool.addEntry(getPattern("zombie_t3_any"), 1.1F * weights[5]);
        specialPool.addEntry(getPattern("creeper_t1_basic"), 0.8F * weights[3]);

        RandomSelectionPool<IEntityIMPattern> pool = new RandomSelectionPool<>();
        pool.addEntry(basicPool, 9.0F);
        pool.addEntry(specialPool, 1.0F);
        return pool;
    }

    private static float[] tierWeights(float tierLevel) {
        float[] weights = new float[6];
        for (int i = 0; i < 6; i++) {
            if (tierLevel - i * 0.5F > 0.0F) {
                weights[i] = (tierLevel - i <= 1.0F ? tierLevel - i * 0.5F : 1.0F);
            }
        }
        return weights;
    }

    private static IEntityIMPattern getPattern(String name) {
        IEntityIMPattern p = COMMON_PATTERNS.get(name);
        if (p == null) {
            EntityPattern fallback = new EntityPattern(IMEntityType.ZOMBIE);
            fallback.addTier(1, 1.0F);
            return fallback;
        }
        return p;
    }

    private static void initCommonPatterns() {
        EntityPattern zombieT1Any = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT1Any.addTier(1, 1.0F);
        zombieT1Any.addFlavour(0, 3.0F);
        zombieT1Any.addFlavour(1, 1.0F);

        EntityPattern zombieT2Basic = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Basic.addTier(2, 1.0F);
        zombieT2Basic.addFlavour(0, 2.0F);
        zombieT2Basic.addFlavour(1, 1.0F);
        zombieT2Basic.addFlavour(2, 0.4F);

        EntityPattern zombieT2Plain = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Plain.addTier(2, 1.0F);
        zombieT2Plain.addFlavour(0, 1.0F);

        EntityPattern zombieT2Tar = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT2Tar.addTier(2, 1.0F);
        zombieT2Tar.addFlavour(2, 1.0F);
        zombieT2Tar.addTexture(5, 1.0F);

        EntityPattern zombieT3Any = new EntityPattern(IMEntityType.ZOMBIE);
        zombieT3Any.addTier(3, 1.0F);
        zombieT3Any.addTexture(0, 1.0F);

        EntityPattern zombiePigmanT1Any = new EntityPattern(IMEntityType.ZOMBIE_PIGMAN);
        zombiePigmanT1Any.addTier(1, 1.0F);
        zombiePigmanT1Any.addFlavour(0, 1.0F);

        EntityPattern zombiePigmanT2Any = new EntityPattern(IMEntityType.ZOMBIE_PIGMAN);
        zombiePigmanT2Any.addTier(2, 1.0F);
        zombiePigmanT2Any.addFlavour(0, 1.0F);

        EntityPattern zombiePigmanT3Any = new EntityPattern(IMEntityType.ZOMBIE_PIGMAN);
        zombiePigmanT3Any.addTier(3, 1.0F);
        zombiePigmanT3Any.addFlavour(0, 1.0F);

        EntityPattern spiderT1Any = new EntityPattern(IMEntityType.SPIDER);
        spiderT1Any.addTier(1, 1.0F);

        EntityPattern spiderT2Any = new EntityPattern(IMEntityType.SPIDER);
        spiderT2Any.addTier(2, 1.0F);
        spiderT2Any.addFlavour(0, 1.0F);
        spiderT2Any.addFlavour(1, 1.0F);

        EntityPattern pigEngyT1Any = new EntityPattern(IMEntityType.PIG_ENGINEER);
        pigEngyT1Any.addTier(1, 1.0F);

        EntityPattern skeletonT1Any = new EntityPattern(IMEntityType.SKELETON);
        skeletonT1Any.addTier(1, 1.0F);

        EntityPattern throwerT1 = new EntityPattern(IMEntityType.THROWER);
        throwerT1.addTier(1, 1.0F);

        EntityPattern throwerT2 = new EntityPattern(IMEntityType.THROWER);
        throwerT2.addTier(2, 1.0F);

        EntityPattern burrower = new EntityPattern(IMEntityType.BURROWER);
        burrower.addTier(1, 1.0F);

        EntityPattern creeper = new EntityPattern(IMEntityType.CREEPER);
        creeper.addTier(1, 1.0F);

        EntityPattern imp = new EntityPattern(IMEntityType.IMP);
        imp.addTier(1, 1.0F);

        COMMON_PATTERNS.put("zombie_t1_any", zombieT1Any);
        COMMON_PATTERNS.put("zombie_t2_any_basic", zombieT2Basic);
        COMMON_PATTERNS.put("zombie_t2_plain", zombieT2Plain);
        COMMON_PATTERNS.put("zombie_t2_tar", zombieT2Tar);
        COMMON_PATTERNS.put("zombie_t3_any", zombieT3Any);
        COMMON_PATTERNS.put("zombiePigman_t1_any", zombiePigmanT1Any);
        COMMON_PATTERNS.put("zombiePigman_t2_any", zombiePigmanT2Any);
        COMMON_PATTERNS.put("zombiePigman_t3_any", zombiePigmanT3Any);
        COMMON_PATTERNS.put("spider_t1_any", spiderT1Any);
        COMMON_PATTERNS.put("spider_t2_any", spiderT2Any);
        COMMON_PATTERNS.put("pigengy_t1_any", pigEngyT1Any);
        COMMON_PATTERNS.put("skeleton_t1_any", skeletonT1Any);
        COMMON_PATTERNS.put("thrower_t1", throwerT1);
        COMMON_PATTERNS.put("thrower_t2", throwerT2);
        COMMON_PATTERNS.put("burrower", burrower);
        COMMON_PATTERNS.put("creeper_t1_basic", creeper);
        COMMON_PATTERNS.put("imp_t1", imp);
    }
}
