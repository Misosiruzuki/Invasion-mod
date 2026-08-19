package com.whammich.invasion.nexus;

import com.whammich.invasion.ConfigHandler;
import com.whammich.invasion.registry.BlockEntityRegistry;
import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.util.LogHelper;
import com.whammich.invasion.wave.IMWaveSpawner;
import com.whammich.invasion.wave.Wave;
import com.whammich.invasion.wave.WaveSpawnerException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.whammich.invasion.entity.EntityIMLiving;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;

/**
 * Nexus block entity - invasion and continuous modes (1.7.10 mode integers).
 * P0: continuous skeleton (mode 2/3/4, powerLevel, nextAttackTime, slow flux).
 * Damping agent effects deferred to P1.
 */
public class NexusBlockEntity extends BaseContainerBlockEntity implements INexusAccess, MenuProvider {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;

    public static final int DATA_MODE = 0;
    public static final int DATA_ACTIVATION = 1;
    public static final int DATA_WAVE = 2;
    public static final int DATA_LEVEL = 3;
    public static final int DATA_KILLS = 4;
    public static final int DATA_RADIUS = 5;
    public static final int DATA_GENERATION = 6;
    public static final int DATA_HP = 7;
    public static final int DATA_MAX_HP = 8;
    public static final int DATA_COOK = 9;
    public static final int DATA_COUNT = 10;

    public static final int ACTIVATION_MAX = 400;
    public static final int GENERATION_MAX = 3000;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private int activationTimer;
    private int currentWave;
    private int spawnRadius = 52;
    private int nexusLevel = 1;
    private int nexusKills;
    private int generation;
    private int cookTime;
    private int maxHp = NexusHpLogic.DEFAULT_MAX_HP;
    private int hp = NexusHpLogic.DEFAULT_MAX_HP;
    /** Player UUID -> last bind time (ms). Wiki B-05 / 1.7 boundPlayers. */
    private final Map<UUID, Long> boundPlayers = new HashMap<>();
    private int mode;
    private boolean activated;

    private int powerLevel;
    private int powerLevelTimer;
    private long nextAttackTime;
    private long lastWorldTime;
    private boolean continuousAttack;
    private boolean nightLoomWarned;

    private IMWaveSpawner waveSpawner;
    private int waveRestTimer;

    private final Random continuousRandom = new Random();

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_MODE -> mode;
                case DATA_ACTIVATION -> activationTimer;
                case DATA_WAVE -> currentWave;
                case DATA_LEVEL -> nexusLevel;
                case DATA_KILLS -> nexusKills;
                case DATA_RADIUS -> spawnRadius;
                case DATA_GENERATION -> generation;
                case DATA_HP -> hp;
                case DATA_MAX_HP -> maxHp;
                case DATA_COOK -> cookTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_MODE -> mode = value;
                case DATA_ACTIVATION -> activationTimer = value;
                case DATA_WAVE -> currentWave = value;
                case DATA_LEVEL -> nexusLevel = value;
                case DATA_KILLS -> nexusKills = value;
                case DATA_RADIUS -> spawnRadius = value;
                case DATA_GENERATION -> generation = value;
                case DATA_HP -> hp = value;
                case DATA_MAX_HP -> maxHp = value;
                case DATA_COOK -> cookTime = value;
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public NexusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NEXUS.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NexusBlockEntity nexus) {
        nexus.tickActivation();
        nexus.tickCook();
        nexus.tickGeneration();
        nexus.tickWaves();
        if (nexus.activated) {
            nexus.bindPlayersInRadius();
        }
        nexus.syncActiveBlockState(level, pos, state);
    }

    private IMWaveSpawner spawner() {
        if (waveSpawner == null) {
            waveSpawner = new IMWaveSpawner(this);
            waveSpawner.setSpawnRadius(spawnRadius);
        }
        return waveSpawner;
    }

    private void tickWaves() {
        if (!activated) {
            return;
        }
        if (mode == NexusMode.INVASION) {
            tickInvasionWaves();
        } else if (mode == NexusMode.CONTINUOUS) {
            tickContinuousWaiting();
        } else if (mode == NexusMode.CONTINUOUS_ATTACK) {
            tickContinuousAttack();
        }
    }

    private void tickInvasionWaves() {
        try {
            if (spawner().isActive() && !spawner().isWaveComplete()) {
                spawner().spawn(50);
            } else if (spawner().isWaveComplete()) {
                if (waveRestTimer <= 0) {
                    waveRestTimer = spawner().getWaveRestTime();
                    if (waveRestTimer <= 0) {
                        waveRestTimer = 45_000;
                    }
                }
                waveRestTimer -= 50;
                if (waveRestTimer <= 0) {
                    currentWave++;
                    if (currentWave > nexusLevel) {
                        nexusLevel = currentWave;
                    }
                    spawner().beginNextWave(currentWave);
                    setChanged();
                }
            }
        } catch (WaveSpawnerException e) {
            LogHelper.warn("Wave error: {}", e.getMessage());
            notifyNearby(Component.literal(e.getMessage()));
        }
    }

    private void tickContinuousWaiting() {
        if (level == null || level.isClientSide) {
            return;
        }
        long currentTime = level.getGameTime();
        if (lastWorldTime == 0L) {
            lastWorldTime = currentTime;
        }

        applyContinuousPowerAndDamping(false);

        if (ContinuousSchedule.crossedDusk(lastWorldTime, currentTime)
                && currentTime + ContinuousSchedule.DUSK_TICK > nextAttackTime
                && !nightLoomWarned) {
            notifyNearby(Component.translatable("message.invasion.nexus.night_looms"));
            nightLoomWarned = true;
        }

        if (lastWorldTime > currentTime) {
            nextAttackTime -= (lastWorldTime - currentTime);
        }
        lastWorldTime = currentTime;

        // Shutdown from strong damping may leave continuous mode.
        if (mode != NexusMode.CONTINUOUS) {
            return;
        }

        if (currentTime >= nextAttackTime) {
            beginContinuousAttack();
        }
    }

    private void tickContinuousAttack() {
        // 1.7: flux/power timer still runs during attack; strong damping does not drain mid-attack.
        applyContinuousPowerAndDamping(true);
        if (mode != NexusMode.CONTINUOUS_ATTACK) {
            return;
        }
        try {
            if (spawner().isActive() && !spawner().isWaveComplete()) {
                spawner().spawn(50);
            } else if (spawner().isWaveComplete()) {
                endContinuousAttack();
            }
        } catch (WaveSpawnerException e) {
            LogHelper.warn("Continuous wave error: {}", e.getMessage());
            endContinuousAttack();
        }
    }

    /**
     * 1.7 doContinuous power / damping (B-23..B-25).
     * Weak damping in input slot: power does not rise (flux still generates).
     * Strong damping: power drains each tick while not mid continuous-attack; power &lt; 0 → stop.
     *
     * @param continuousAttackActive true during mode CONTINUOUS_ATTACK
     */
    private void applyContinuousPowerAndDamping(boolean continuousAttackActive) {
        powerLevelTimer += 50;
        if (powerLevelTimer > ContinuousSchedule.POWER_TICK_INTERVAL) {
            powerLevelTimer -= ContinuousSchedule.POWER_TICK_INTERVAL;
            generateFlux(ContinuousSchedule.continuousFluxIncrement(powerLevel));
            powerLevel = DampingLogic.applyPowerTickGain(powerLevel, hasWeakDampingInCatalystSlot());
            setChanged();
        }

        int after = DampingLogic.applyStrongDrain(
                powerLevel, hasStrongDampingInCatalystSlot(), continuousAttackActive);
        if (after != powerLevel) {
            powerLevel = after;
            setChanged();
        }
        if (DampingLogic.shouldShutdownAfterDrain(powerLevel)) {
            LogHelper.info("Strong damping reduced power below 0; stopping nexus @ {}", worldPosition);
            emergencyStop();
            notifyNearby(Component.translatable("message.invasion.nexus.damped_shutdown"));
        }
    }

    private boolean hasWeakDampingInCatalystSlot() {
        ItemStack stack = items.get(SLOT_INPUT);
        return !stack.isEmpty() && stack.is(ItemRegistry.DAMPING_AGENT_WEAK.get());
    }

    private boolean hasStrongDampingInCatalystSlot() {
        ItemStack stack = items.get(SLOT_INPUT);
        return !stack.isEmpty() && stack.is(ItemRegistry.DAMPING_AGENT_STRONG.get());
    }

    /** Test / VoxPilot: put damping agent in catalyst slot without GUI. */
    public void debugSetCatalystSlot(ItemStack stack) {
        items.set(SLOT_INPUT, stack == null ? ItemStack.EMPTY : stack.copy());
        setChanged();
    }

    private void beginContinuousAttack() {
        if (level == null || level.isClientSide) {
            return;
        }
        if (!spawner().isReady()) {
            return;
        }
        float difficulty = ContinuousSchedule.difficultyFromPower(powerLevel);
        float tier = difficulty;
        try {
            Wave wave = spawner().getWaveBuilder().generateWave(
                    difficulty, tier, ContinuousSchedule.DEFAULT_WAVE_LENGTH_SECONDS);
            currentWave = Math.max(1, Math.round(difficulty * 3));
            if (currentWave > nexusLevel) {
                nexusLevel = currentWave;
            }
            spawner().beginNextWave(wave);
            mode = NexusMode.CONTINUOUS_ATTACK;
            continuousAttack = true;
            nightLoomWarned = false;
            notifyNearby(Component.translatable("message.invasion.nexus.continuous_attack"));
            setChanged();
            NexusTracker.syncStatus(this);
            LogHelper.info("Continuous attack started (power={}, diff={}) @ {}", powerLevel, difficulty, worldPosition);
        } catch (WaveSpawnerException e) {
            LogHelper.warn("Failed continuous attack: {}", e.getMessage());
            notifyNearby(Component.literal(e.getMessage()));
            scheduleNextContinuousAttack();
        }
    }

    private void endContinuousAttack() {
        continuousAttack = false;
        mode = NexusMode.CONTINUOUS;
        if (waveSpawner != null) {
            waveSpawner.stop();
        }
        scheduleNextContinuousAttack();
        notifyNearby(Component.translatable("message.invasion.nexus.continuous_calm"));
        setChanged();
        NexusTracker.syncStatus(this);
    }

    private void scheduleNextContinuousAttack() {
        if (level == null) {
            return;
        }
        int minDays = ConfigHandler.COMMON.minDaysToAttack.get();
        int maxDays = ConfigHandler.COMMON.maxDaysToAttack.get();
        nextAttackTime = ContinuousSchedule.computeNextAttackTime(
                level.getGameTime(), minDays, maxDays, continuousRandom);
        lastWorldTime = level.getGameTime();
        nightLoomWarned = false;
        setChanged();
    }

    private void tickActivation() {
        ItemStack input = items.get(SLOT_INPUT);
        if (mode != NexusMode.IDLE && mode != NexusMode.ACTIVATING_STABLE) {
            return;
        }
        if (input.isEmpty() || !isCatalyst(input)) {
            if (activationTimer != 0 || mode == NexusMode.ACTIVATING_STABLE) {
                activationTimer = 0;
                if (mode == NexusMode.ACTIVATING_STABLE) {
                    mode = NexusMode.IDLE;
                }
                setChanged();
            }
            return;
        }

        boolean stable = input.is(ItemRegistry.NEXUS_CATALYST_STABLE.get());
        boolean strong = input.is(ItemRegistry.CATALYST_STRONG.get());
        mode = stable ? NexusMode.ACTIVATING_STABLE : NexusMode.IDLE;
        activationTimer++;
        if (activationTimer >= ACTIVATION_MAX) {
            input.shrink(1);
            if (input.isEmpty()) {
                items.set(SLOT_INPUT, ItemStack.EMPTY);
            }
            activationTimer = 0;
            if (stable) {
                beginContinuous();
            } else {
                beginInvasion(CatalystLogic.invasionStartWave(strong));
            }
        }
        setChanged();
    }

    private void beginInvasion(int startWave) {
        activated = true;
        mode = NexusMode.INVASION;
        activationTimer = 0;
        currentWave = Math.max(1, startWave);
        waveRestTimer = 0;
        continuousAttack = false;
        hp = maxHp;
        NexusTracker.setFocusNexus(this);
        NexusTracker.setActiveNexus(this);
        try {
            spawner().beginNextWave(currentWave);
            notifyNearby(Component.translatable("message.invasion.nexus.invasion_started"));
            LogHelper.info("Nexus invasion started at wave {} @ {}", currentWave, worldPosition);
        } catch (WaveSpawnerException e) {
            LogHelper.warn("Failed to start invasion: {}", e.getMessage());
            notifyNearby(Component.literal(e.getMessage()));
            mode = NexusMode.IDLE;
            activated = false;
        }
        setChanged();
        NexusTracker.syncStatus(this);
    }

    private void beginContinuous() {
        activated = true;
        mode = NexusMode.CONTINUOUS;
        activationTimer = 0;
        continuousAttack = false;
        powerLevel = Math.max(0, powerLevel);
        powerLevelTimer = 0;
        hp = maxHp;
        NexusTracker.setFocusNexus(this);
        NexusTracker.setActiveNexus(this);
        scheduleNextContinuousAttack();
        notifyNearby(Component.translatable("message.invasion.nexus.continuous_stable"));
        LogHelper.info("Nexus continuous mode started @ {} nextAttack={}", worldPosition, nextAttackTime);
        setChanged();
        NexusTracker.syncStatus(this);
    }

    private static boolean isCatalyst(ItemStack stack) {
        return stack.is(ItemRegistry.NEXUS_CATALYST_STABLE.get())
                || stack.is(ItemRegistry.NEXUS_CATALYST_UNSTABLE.get())
                || stack.is(ItemRegistry.CATALYST_STRONG.get());
    }

    private void syncActiveBlockState(Level level, BlockPos pos, BlockState state) {
        boolean want = activated && NexusMode.isRunning(mode);
        if (state.hasProperty(BlockNexus.ACTIVE) && state.getValue(BlockNexus.ACTIVE) != want) {
            level.setBlock(pos, state.setValue(BlockNexus.ACTIVE, want), 3);
        }
    }

    private void tickGeneration() {
        if (!activated || mode != NexusMode.INVASION) {
            return;
        }
        generateFlux(1);
    }

    private void generateFlux(int increment) {
        generation += increment;
        if (generation < GENERATION_MAX) {
            setChanged();
            return;
        }
        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            items.set(SLOT_OUTPUT, new ItemStack(ItemRegistry.RIFT_FLUX.get()));
            generation -= GENERATION_MAX;
        } else if (output.is(ItemRegistry.RIFT_FLUX.get()) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            generation -= GENERATION_MAX;
        }
        if (generation > GENERATION_MAX) {
            generation = GENERATION_MAX;
        }
        setChanged();
    }

    private void tickCook() {
        ItemStack input = items.get(SLOT_INPUT);
        ItemStack output = items.get(SLOT_OUTPUT);
        if (input.isEmpty()) {
            cookTime = 0;
            return;
        }
        boolean canCook = input.is(ItemRegistry.CATALYST_MIXTURE_STABLE.get())
                && (output.isEmpty() || (output.is(ItemRegistry.NEXUS_CATALYST_STABLE.get())
                && output.getCount() < output.getMaxStackSize()));
        if (!canCook) {
            cookTime = 0;
            return;
        }
        cookTime++;
        if (cookTime >= 200) {
            cookTime = 0;
            input.shrink(1);
            if (output.isEmpty()) {
                items.set(SLOT_OUTPUT, new ItemStack(ItemRegistry.NEXUS_CATALYST_STABLE.get()));
            } else {
                output.grow(1);
            }
            setChanged();
        }
    }

    private void notifyNearby(Component message) {
        if (level == null || level.isClientSide) {
            return;
        }
        double r = spawnRadius + 16.0;
        AABB box = new AABB(worldPosition).inflate(r, 64.0, r);
        List<Player> players = level.getEntitiesOfClass(Player.class, box);
        for (Player player : players) {
            player.displayClientMessage(message, false);
        }
    }

    public void tryActivate(Player player) {
        NexusTracker.setFocusNexus(this);
        if (activated || (mode != NexusMode.IDLE && mode != NexusMode.ACTIVATING_STABLE)) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (!isCatalyst(held)) {
            return;
        }
        boolean stable = held.is(ItemRegistry.NEXUS_CATALYST_STABLE.get());
        boolean strong = held.is(ItemRegistry.CATALYST_STRONG.get());
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
        activationTimer = 0;
        if (stable) {
            beginContinuous();
        } else {
            beginInvasion(CatalystLogic.invasionStartWave(strong));
        }
        if (level != null && !level.isClientSide) {
            player.displayClientMessage(Component.translatable("block.invasion.nexus.activating"), true);
        }
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    public void attackNexus(int damage) {
        if (level != null && level.isClientSide) {
            return;
        }
        int before = hp;
        hp = NexusHpLogic.applyDamage(hp, damage);
        setChanged();
        if (NexusHpLogic.shouldEndInvasion(hp) && before > 0) {
            theEnd();
        }
    }

    /**
     * Wiki B-04/B-05 + 1.7 theEnd: end invasion, kill linked players, kill invasion mobs.
     * Block entity and block remain for reactivation.
     */
    private void theEnd() {
        if (level == null || level.isClientSide) {
            return;
        }
        notifyNearby(Component.translatable("message.invasion.nexus.destroyed"));
        killLinkedPlayers();
        killAllInvasionMobs();
        emergencyStop();
        hp = 0;
        setChanged();
        LogHelper.info("Nexus destroyed (theEnd) @ {}", worldPosition);
    }

    /** Bind players currently inside spawnRadius+10 (1.7 bindPlayers). */
    public void bindPlayersInRadius() {
        if (level == null || level.isClientSide || !activated) {
            return;
        }
        AABB box = radiusBox();
        long now = System.currentTimeMillis();
        for (Player player : level.getEntitiesOfClass(Player.class, box)) {
            UUID id = player.getUUID();
            Long prev = boundPlayers.get(id);
            if (prev == null || now - prev > NexusHpLogic.BIND_FRESH_MS) {
                notifyNearby(Component.translatable("message.invasion.nexus.player_bound", player.getName()));
            }
            boundPlayers.put(id, now);
        }
    }

    private AABB radiusBox() {
        int half = NexusHpLogic.bindHalfExtent(spawnRadius);
        BlockPos p = worldPosition;
        return new AABB(
                p.getX() - half, p.getY() - half, p.getZ() - half,
                p.getX() + half + 1, p.getY() + half + 1, p.getZ() + half + 1);
    }

    private void killLinkedPlayers() {
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        long now = System.currentTimeMillis();
        AABB box = radiusBox();
        DamageSource magic = server.damageSources().magic();
        for (Player player : server.getEntitiesOfClass(Player.class, box.inflate(8))) {
            boolean inRadius = box.contains(player.position());
            Long boundAt = boundPlayers.get(player.getUUID());
            if (NexusHpLogic.shouldKillPlayerOnDestroy(inRadius, now, boundAt)) {
                player.hurt(magic, 500.0F);
            }
        }
        boundPlayers.clear();
    }

    private void killAllInvasionMobs() {
        if (level == null || level.isClientSide) {
            return;
        }
        AABB box = radiusBox();
        DamageSource magic = level instanceof ServerLevel s ? s.damageSources().magic() : null;
        for (EntityIMLiving mob : level.getEntitiesOfClass(EntityIMLiving.class, box)) {
            if (magic != null) {
                mob.hurt(magic, 500.0F);
            } else {
                mob.discard();
            }
        }
    }

    /** Test / VoxPilot: set HP without ending. */
    public void debugSetHp(int value) {
        hp = Math.max(0, Math.min(maxHp, value));
        setChanged();
    }

    @Override
    public void registerMobDied() {
        nexusKills++;
        if (mode == NexusMode.INVASION) {
            generation++;
        }
        setChanged();
    }

    @Override
    public boolean isActivating() {
        return (mode == NexusMode.IDLE || mode == NexusMode.ACTIVATING_STABLE)
                && activationTimer > 0
                && activationTimer < ACTIVATION_MAX;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public int getActivationTimer() {
        return activationTimer;
    }

    @Override
    public int getSpawnRadius() {
        return spawnRadius;
    }

    @Override
    public int getNexusKills() {
        return nexusKills;
    }

    @Override
    public int getGeneration() {
        return generation;
    }

    @Override
    public int getNexusLevel() {
        return nexusLevel;
    }

    @Override
    public int getCurrentWave() {
        return currentWave;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public long getNextAttackTime() {
        return nextAttackTime;
    }

    public boolean isContinuousAttack() {
        return continuousAttack;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public BlockPos getBlockPosition() {
        return worldPosition;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.invasion.nexus");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new NexusMenu(id, inv, this, dataAccess);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("Mode", mode);
        tag.putInt("ActivationTimer", activationTimer);
        tag.putInt("Wave", currentWave);
        tag.putInt("NexusLevel", nexusLevel);
        tag.putInt("Kills", nexusKills);
        tag.putInt("Radius", spawnRadius);
        tag.putInt("Generation", generation);
        tag.putInt("CookTime", cookTime);
        tag.putInt("Hp", hp);
        tag.putInt("MaxHp", maxHp);
        tag.putBoolean("Activated", activated);
        tag.putInt("PowerLevel", powerLevel);
        tag.putInt("PowerLevelTimer", powerLevelTimer);
        tag.putLong("NextAttackTime", nextAttackTime);
        tag.putLong("LastWorldTime", lastWorldTime);
        tag.putBoolean("ContinuousAttack", continuousAttack);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        mode = tag.getInt("Mode");
        activationTimer = tag.getInt("ActivationTimer");
        currentWave = tag.getInt("Wave");
        nexusLevel = tag.getInt("NexusLevel");
        nexusKills = tag.getInt("Kills");
        spawnRadius = tag.contains("Radius") ? tag.getInt("Radius") : 52;
        generation = tag.getInt("Generation");
        cookTime = tag.getInt("CookTime");
        hp = tag.contains("Hp") ? tag.getInt("Hp") : 100;
        maxHp = tag.contains("MaxHp") ? tag.getInt("MaxHp") : 100;
        activated = tag.getBoolean("Activated");
        powerLevel = tag.getInt("PowerLevel");
        powerLevelTimer = tag.getInt("PowerLevelTimer");
        nextAttackTime = tag.getLong("NextAttackTime");
        lastWorldTime = tag.getLong("LastWorldTime");
        continuousAttack = tag.getBoolean("ContinuousAttack");
    }

    public void debugStartInvasion(int startWave) throws WaveSpawnerException {
        beginInvasion(Math.max(1, startWave));
        LogHelper.info("Debug start invasion at wave {} @ {}", currentWave, worldPosition);
    }

    public void debugStartContinuous() {
        beginContinuous();
        LogHelper.info("Debug start continuous @ {}", worldPosition);
    }

    /**
     * Test helper: ensure continuous mode and schedule the next attack after {@code delayTicks}
     * (game time). Used by VoxPilot so mode=3 can be reached without waiting minDays.
     */
    public void debugStartContinuousAttackSoon(int delayTicks) {
        if (mode != NexusMode.CONTINUOUS && mode != NexusMode.CONTINUOUS_ATTACK) {
            beginContinuous();
        }
        if (level == null || level.isClientSide) {
            return;
        }
        int delay = Math.max(1, delayTicks);
        nextAttackTime = level.getGameTime() + delay;
        nightLoomWarned = false;
        lastWorldTime = level.getGameTime();
        setChanged();
        LogHelper.info("Debug continuous attack soon in {} ticks (nextAttack={}) @ {}",
                delay, nextAttackTime, worldPosition);
    }

    /** Test helper (B-41): set continuous powerLevel for difficulty = 1 + power/4500. */
    public void debugSetPowerLevel(int power) {
        this.powerLevel = Math.max(0, power);
        setChanged();
        LogHelper.info("Debug powerLevel={} (diff~{}) @ {}", powerLevel,
                1.0F + powerLevel / 4500.0F, worldPosition);
    }

    /** P2 / VoxPilot: consume Strong Catalyst path and start invasion at wave 10. */
    public void debugActivateStrongCatalyst() {
        items.set(SLOT_INPUT, new ItemStack(ItemRegistry.CATALYST_STRONG.get()));
        beginInvasion(CatalystLogic.invasionStartWave(true));
        items.set(SLOT_INPUT, ItemStack.EMPTY);
        LogHelper.info("Debug strong catalyst activation at wave {} @ {}", currentWave, worldPosition);
    }

    public void emergencyStop() {
        mode = NexusMode.IDLE;
        activated = false;
        activationTimer = 0;
        continuousAttack = false;
        nightLoomWarned = false;
        if (powerLevel < 0) {
            powerLevel = 0;
        }
        if (waveSpawner != null) {
            waveSpawner.stop();
        }
        NexusTracker.setActiveNexus(null);
        setChanged();
        NexusTracker.syncStatus(this);
        LogHelper.info("Emergency stop @ {}", worldPosition);
    }

    public boolean setSpawnRadius(int radius) {
        if (activated && (mode == NexusMode.INVASION || mode == NexusMode.CONTINUOUS_ATTACK)) {
            return false;
        }
        if (radius < 32 || radius > 128) {
            return false;
        }
        this.spawnRadius = radius;
        if (waveSpawner != null) {
            waveSpawner.setSpawnRadius(radius);
        }
        setChanged();
        return true;
    }

    public String debugStatus() {
        return String.format(
                "Nexus@%s mode=%d activated=%s wave=%d level=%d kills=%d radius=%d hp=%d/%d gen=%d power=%d nextAttack=%d contAtk=%s",
                worldPosition, mode, activated, currentWave, nexusLevel, nexusKills,
                spawnRadius, hp, maxHp, generation, powerLevel, nextAttackTime, continuousAttack);
    }
}
