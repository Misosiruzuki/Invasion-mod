package com.whammich.invasion.nexus;

import com.whammich.invasion.registry.BlockEntityRegistry;
import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.util.LogHelper;
import com.whammich.invasion.wave.IMWaveSpawner;
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

/**
 * Nexus block entity — holds invasion state and drives waves (group 5).
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

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private int activationTimer;
    private int currentWave;
    private int spawnRadius = 52;
    private int nexusLevel = 1;
    private int nexusKills;
    private int generation;
    private int cookTime;
    private int maxHp = 100;
    private int hp = 100;
    private int mode;
    private boolean activated;

    private IMWaveSpawner waveSpawner;
    private int waveRestTimer;

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
        nexus.tickCook();
        nexus.tickWaves();
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
        if (mode == 1) {
            activationTimer--;
            if (activationTimer <= 0) {
                mode = 2;
                currentWave = 1;
                waveRestTimer = 0;
                try {
                    spawner().beginNextWave(currentWave);
                } catch (WaveSpawnerException e) {
                    LogHelper.warn("Failed to start wave: {}", e.getMessage());
                    mode = 0;
                    activated = false;
                }
                setChanged();
            }
            return;
        }
        if (mode != 2) {
            return;
        }
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
                    spawner().beginNextWave(currentWave);
                    setChanged();
                }
            }
        } catch (WaveSpawnerException e) {
            LogHelper.warn("Wave error: {}", e.getMessage());
        }
    }

    private void tickCook() {
        ItemStack input = items.get(SLOT_INPUT);
        ItemStack output = items.get(SLOT_OUTPUT);
        if (input.isEmpty()) {
            cookTime = 0;
            return;
        }
        boolean canCook = input.is(ItemRegistry.CATALYST_MIXTURE_STABLE.get())
                && (output.isEmpty() || (output.is(ItemRegistry.NEXUS_CATALYST_STABLE.get()) && output.getCount() < output.getMaxStackSize()));
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

    public void tryActivate(Player player) {
        if (activated || mode != 0) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (held.is(ItemRegistry.NEXUS_CATALYST_STABLE.get())
                || held.is(ItemRegistry.NEXUS_CATALYST_UNSTABLE.get())) {
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            mode = 1;
            activationTimer = 60;
            activated = true;
            setChanged();
            if (level != null && !level.isClientSide) {
                player.displayClientMessage(Component.translatable("block.invasion.nexus.activating"), true);
            }
        }
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    public void attackNexus(int damage) {
        hp = Math.max(0, hp - damage);
        setChanged();
        if (hp <= 0 && level != null && !level.isClientSide) {
            mode = 0;
            activated = false;
        }
    }

    @Override
    public void registerMobDied() {
        nexusKills++;
        generation++;
        setChanged();
    }

    @Override
    public boolean isActivating() {
        return mode == 1;
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
        for (ItemStack s : items) {
            if (!s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
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
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
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
    }
}
