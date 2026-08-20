/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLCommonHandler
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S35PacketUpdateTileEntity
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.DamageSource
 *  net.minecraft.world.World
 */
package invmod.common.nexus;

import cpw.mods.fml.common.FMLCommonHandler;
import invmod.common.entity.EntityIMBolt;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMWolf;
import invmod.common.entity.ai.AttackerAI;
import invmod.common.mod_Invasion;
import invmod.common.nexus.IMWaveBuilder;
import invmod.common.nexus.IMWaveSpawner;
import invmod.common.nexus.INexusAccess;
import invmod.common.nexus.Wave;
import invmod.common.nexus.WaveSpawnerException;
import invmod.common.util.ComparatorEntityDistance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class TileEntityNexus
extends TileEntity
implements INexusAccess,
IInventory {
    private static final long BIND_EXPIRE_TIME = 300000L;
    private IMWaveSpawner waveSpawner;
    private IMWaveBuilder waveBuilder;
    private ItemStack[] nexusItemStacks;
    private AxisAlignedBB boundingBoxToRadius;
    private HashMap<String, Long> boundPlayers;
    private List<EntityIMLiving> mobList;
    private AttackerAI attackerAI;
    private int activationTimer;
    private int currentWave;
    private int spawnRadius;
    private int nexusLevel;
    private int nexusKills;
    private int generation;
    private int cookTime;
    private int maxHp;
    private int hp;
    private int lastHp;
    private int mode;
    private int powerLevel;
    private int lastPowerLevel;
    private int powerLevelTimer;
    private int mobsLeftInWave;
    private int lastMobsLeftInWave;
    private int mobsToKillInWave;
    private int nextAttackTime;
    private int daysToAttack;
    private long lastWorldTime;
    private int zapTimer;
    private int errorState;
    private int tickCount;
    private int cleanupTimer;
    private long spawnerElapsedRestore;
    private long timer;
    private long waveDelayTimer;
    private long waveDelay;
    private boolean continuousAttack;
    private boolean mobsSorted;
    private boolean resumedFromNBT;
    private boolean activated;

    public TileEntityNexus() {
        this(null);
    }

    public TileEntityNexus(World world) {
        this.field_145850_b = world;
        this.spawnRadius = 52;
        this.waveSpawner = new IMWaveSpawner(this, this.spawnRadius);
        this.waveBuilder = new IMWaveBuilder();
        this.nexusItemStacks = new ItemStack[2];
        this.boundingBoxToRadius = AxisAlignedBB.func_72330_a((double)this.field_145851_c, (double)this.field_145848_d, (double)this.field_145849_e, (double)this.field_145851_c, (double)this.field_145848_d, (double)this.field_145849_e);
        this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), (double)(this.field_145848_d - (this.spawnRadius + 40)), (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), (double)(this.field_145848_d + (this.spawnRadius + 40)), (double)(this.field_145849_e + (this.spawnRadius + 10)));
        this.boundPlayers = new HashMap();
        this.mobList = new ArrayList<EntityIMLiving>();
        this.attackerAI = new AttackerAI(this);
        this.activationTimer = 0;
        this.cookTime = 0;
        this.currentWave = 0;
        this.nexusLevel = 1;
        this.nexusKills = 0;
        this.generation = 0;
        this.lastHp = 100;
        this.hp = 100;
        this.maxHp = 100;
        this.mode = 0;
        this.powerLevelTimer = 0;
        this.powerLevel = 0;
        this.lastPowerLevel = 0;
        this.mobsLeftInWave = 0;
        this.nextAttackTime = 0;
        this.daysToAttack = 0;
        this.lastWorldTime = 0L;
        this.errorState = 0;
        this.tickCount = 0;
        this.timer = 0L;
        this.zapTimer = 0;
        this.cleanupTimer = 0;
        this.waveDelayTimer = -1L;
        this.waveDelay = 0L;
        this.continuousAttack = false;
        this.mobsSorted = false;
        this.resumedFromNBT = false;
        this.activated = false;
    }

    public void func_145845_h() {
        if (this.field_145850_b.field_72995_K) {
            return;
        }
        this.updateStatus();
        this.updateAI();
        if (this.mode == 1 || this.mode == 2 || this.mode == 3) {
            if (this.resumedFromNBT) {
                this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), 0.0, (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), 127.0, (double)(this.field_145849_e + (this.spawnRadius + 10)));
                if (this.mode == 2 && this.continuousAttack) {
                    if (this.resumeSpawnerContinuous()) {
                        this.mobsLeftInWave = this.lastMobsLeftInWave += this.acquireEntities();
                        mod_Invasion.log("mobsLeftInWave: " + this.mobsLeftInWave);
                        mod_Invasion.log("mobsToKillInWave: " + this.mobsToKillInWave);
                    }
                } else {
                    this.resumeSpawnerInvasion();
                    this.acquireEntities();
                }
                this.attackerAI.onResume();
                this.resumedFromNBT = false;
            }
            try {
                ++this.tickCount;
                if (this.tickCount == 60) {
                    this.tickCount -= 60;
                    this.bindPlayers();
                    this.updateMobList();
                }
                if (this.mode == 1 || this.mode == 3) {
                    this.doInvasion(50);
                } else if (this.mode == 2) {
                    this.doContinuous(50);
                }
            }
            catch (WaveSpawnerException e) {
                mod_Invasion.log(e.getMessage());
                e.printStackTrace();
                this.stop();
            }
        }
        if (this.cleanupTimer++ > 40) {
            this.cleanupTimer = 0;
            if (this.field_145850_b.func_147439_a(this.field_145851_c, this.field_145848_d, this.field_145849_e) != mod_Invasion.blockNexus) {
                mod_Invasion.setInvasionEnded(this);
                this.stop();
                this.func_145843_s();
                mod_Invasion.log("Stranded nexus entity trying to delete itself...");
            }
        }
    }

    public void emergencyStop() {
        mod_Invasion.log("Nexus manually stopped by command");
        this.stop();
        this.killAllMobs();
    }

    public void debugStatus() {
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Current Time: " + this.field_145850_b.func_72820_D());
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Time to next: " + this.nextAttackTime);
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Days to attack: " + this.daysToAttack);
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mobs left: " + this.mobsLeftInWave);
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mode: " + this.mode);
    }

    public void debugStartInvaion(int startWave) {
        mod_Invasion.tryGetInvasionPermission(this);
        this.startInvasion(startWave);
        this.activated = true;
    }

    public void createBolt(int x, int y, int z, int t) {
        EntityIMBolt bolt = new EntityIMBolt(this.field_145850_b, (double)this.field_145851_c + 0.5, (double)this.field_145848_d + 0.5, (double)this.field_145849_e + 0.5, (double)x + 0.5, (double)y + 0.5, (double)z + 0.5, t, 1);
        this.field_145850_b.func_72838_d((Entity)bolt);
    }

    public boolean setSpawnRadius(int radius) {
        if (!this.waveSpawner.isActive() && radius > 8) {
            this.spawnRadius = radius;
            this.waveSpawner.setRadius(radius);
            this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), 0.0, (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), 127.0, (double)(this.field_145849_e + (this.spawnRadius + 10)));
            return true;
        }
        return false;
    }

    @Override
    public void attackNexus(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.hp = 0;
            if (this.mode == 1) {
                this.theEnd();
            }
        }
        while (this.hp + 5 <= this.lastHp) {
            mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus at " + (this.lastHp - 5) + " hp");
            this.lastHp -= 5;
        }
    }

    @Override
    public void registerMobDied() {
        ++this.nexusKills;
        --this.mobsLeftInWave;
        if (this.mobsLeftInWave <= 0) {
            if (this.lastMobsLeftInWave > 0) {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stable again!");
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Unleashing tapped energy...");
                this.lastMobsLeftInWave = this.mobsLeftInWave;
            }
            return;
        }
        while ((float)this.mobsLeftInWave + (float)this.mobsToKillInWave * 0.1f <= (float)this.lastMobsLeftInWave) {
            mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stabilised to " + (100 - 100 * this.mobsLeftInWave / this.mobsToKillInWave) + "%");
            this.lastMobsLeftInWave = (int)((float)this.lastMobsLeftInWave - (float)this.mobsToKillInWave * 0.1f);
        }
    }

    public void registerMobClose() {
    }

    @Override
    public boolean isActivating() {
        return this.activationTimer > 0 && this.activationTimer < 400;
    }

    @Override
    public int getMode() {
        return this.mode;
    }

    @Override
    public int getActivationTimer() {
        return this.activationTimer;
    }

    @Override
    public int getSpawnRadius() {
        return this.spawnRadius;
    }

    @Override
    public int getNexusKills() {
        return this.nexusKills;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    public int getNexusLevel() {
        return this.nexusLevel;
    }

    public int getPowerLevel() {
        return this.powerLevel;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public int getNexusID() {
        return -1;
    }

    @Override
    public int getXCoord() {
        return this.field_145851_c;
    }

    @Override
    public int getYCoord() {
        return this.field_145848_d;
    }

    @Override
    public int getZCoord() {
        return this.field_145849_e;
    }

    @Override
    public World getWorld() {
        return this.field_145850_b;
    }

    @Override
    public List<EntityIMLiving> getMobList() {
        return this.mobList;
    }

    public int getActivationProgressScaled(int i) {
        return this.activationTimer * i / 400;
    }

    public int getGenerationProgressScaled(int i) {
        return this.generation * i / 3000;
    }

    public int getCookProgressScaled(int i) {
        return this.cookTime * i / 1200;
    }

    public int getNexusPowerLevel() {
        return this.powerLevel;
    }

    @Override
    public int getCurrentWave() {
        return this.currentWave;
    }

    public int func_70302_i_() {
        return this.nexusItemStacks.length;
    }

    public int func_70297_j_() {
        return 64;
    }

    public boolean isInvNameLocalized() {
        return false;
    }

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.nexusItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.field_77994_a > this.func_70297_j_()) {
            itemstack.field_77994_a = this.func_70297_j_();
        }
    }

    public ItemStack func_70301_a(int i) {
        return this.nexusItemStacks[i];
    }

    public ItemStack func_70298_a(int i, int j) {
        if (this.nexusItemStacks[i] != null) {
            if (this.nexusItemStacks[i].field_77994_a <= j) {
                ItemStack itemstack = this.nexusItemStacks[i];
                this.nexusItemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = this.nexusItemStacks[i].func_77979_a(j);
            if (this.nexusItemStacks[i].field_77994_a == 0) {
                this.nexusItemStacks[i] = null;
            }
            return itemstack1;
        }
        return null;
    }

    public boolean func_70300_a(EntityPlayer entityplayer) {
        return true;
    }

    public ItemStack func_70304_b(int i) {
        return null;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        int i;
        mod_Invasion.log("Restoring TileEntityNexus from NBT");
        super.func_145839_a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Items", 0);
        this.nexusItemStacks = new ItemStack[this.func_70302_i_()];
        for (i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            byte byte0 = nbttagcompound1.func_74771_c("Slot");
            if (byte0 < 0 || byte0 >= this.nexusItemStacks.length) continue;
            this.nexusItemStacks[byte0] = ItemStack.func_77949_a((NBTTagCompound)nbttagcompound1);
        }
        nbttaglist = nbttagcompound.func_150295_c("boundPlayers", 0);
        for (i = 0; i < nbttaglist.func_74745_c(); ++i) {
            this.boundPlayers.put(nbttaglist.func_150305_b(i).func_74779_i("name"), System.currentTimeMillis());
            mod_Invasion.log("Added bound player: " + nbttaglist.func_150305_b(i).func_74779_i("name"));
        }
        this.activationTimer = nbttagcompound.func_74765_d("activationTimer");
        this.mode = nbttagcompound.func_74762_e("mode");
        this.currentWave = nbttagcompound.func_74765_d("currentWave");
        this.spawnRadius = nbttagcompound.func_74765_d("spawnRadius");
        this.nexusLevel = nbttagcompound.func_74765_d("nexusLevel");
        this.hp = nbttagcompound.func_74765_d("hp");
        this.nexusKills = nbttagcompound.func_74762_e("nexusKills");
        this.generation = nbttagcompound.func_74765_d("generation");
        this.powerLevel = nbttagcompound.func_74762_e("powerLevel");
        this.lastPowerLevel = nbttagcompound.func_74762_e("lastPowerLevel");
        this.nextAttackTime = nbttagcompound.func_74762_e("nextAttackTime");
        this.daysToAttack = nbttagcompound.func_74762_e("daysToAttack");
        this.continuousAttack = nbttagcompound.func_74767_n("continuousAttack");
        this.activated = nbttagcompound.func_74767_n("activated");
        this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), (double)(this.field_145848_d - (this.spawnRadius + 40)), (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), (double)(this.field_145848_d + (this.spawnRadius + 40)), (double)(this.field_145849_e + (this.spawnRadius + 10)));
        mod_Invasion.log("activationTimer = " + this.activationTimer);
        mod_Invasion.log("mode = " + this.mode);
        mod_Invasion.log("currentWave = " + this.currentWave);
        mod_Invasion.log("spawnRadius = " + this.spawnRadius);
        mod_Invasion.log("nexusLevel = " + this.nexusLevel);
        mod_Invasion.log("hp = " + this.hp);
        mod_Invasion.log("nexusKills = " + this.nexusKills);
        mod_Invasion.log("powerLevel = " + this.powerLevel);
        mod_Invasion.log("lastPowerLevel = " + this.lastPowerLevel);
        mod_Invasion.log("nextAttackTime = " + this.nextAttackTime);
        this.waveSpawner.setRadius(this.spawnRadius);
        if (this.mode == 1 || this.mode == 3 || this.mode == 2 && this.continuousAttack) {
            mod_Invasion.log("Nexus is active; flagging for restore");
            this.resumedFromNBT = true;
            this.spawnerElapsedRestore = nbttagcompound.func_74763_f("spawnerElapsed");
            mod_Invasion.log("spawnerElapsed = " + this.spawnerElapsedRestore);
        }
        this.attackerAI.readFromNBT(nbttagcompound);
    }

    public void func_145841_b(NBTTagCompound nbttagcompound) {
        if (this.mode != 0) {
            mod_Invasion.setNexusUnloaded(this);
        }
        super.func_145841_b(nbttagcompound);
        nbttagcompound.func_74777_a("activationTimer", (short)this.activationTimer);
        nbttagcompound.func_74777_a("currentWave", (short)this.currentWave);
        nbttagcompound.func_74777_a("spawnRadius", (short)this.spawnRadius);
        nbttagcompound.func_74777_a("nexusLevel", (short)this.nexusLevel);
        nbttagcompound.func_74777_a("hp", (short)this.hp);
        nbttagcompound.func_74768_a("nexusKills", this.nexusKills);
        nbttagcompound.func_74777_a("generation", (short)this.generation);
        nbttagcompound.func_74772_a("spawnerElapsed", this.waveSpawner.getElapsedTime());
        nbttagcompound.func_74768_a("mode", this.mode);
        nbttagcompound.func_74768_a("powerLevel", this.powerLevel);
        nbttagcompound.func_74768_a("lastPowerLevel", this.lastPowerLevel);
        nbttagcompound.func_74768_a("nextAttackTime", this.nextAttackTime);
        nbttagcompound.func_74768_a("daysToAttack", this.daysToAttack);
        nbttagcompound.func_74757_a("continuousAttack", this.continuousAttack);
        nbttagcompound.func_74757_a("activated", this.activated);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.nexusItemStacks.length; ++i) {
            if (this.nexusItemStacks[i] == null) continue;
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.nexusItemStacks[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a((NBTBase)nbttagcompound1);
        }
        nbttagcompound.func_74782_a("Items", (NBTBase)nbttaglist);
        NBTTagList nbttaglist2 = new NBTTagList();
        for (Map.Entry<String, Long> entry : this.boundPlayers.entrySet()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74778_a("name", entry.getKey());
            nbttaglist2.func_74742_a((NBTBase)nbttagcompound1);
        }
        nbttagcompound.func_74782_a("boundPlayers", (NBTBase)nbttaglist2);
        this.attackerAI.writeToNBT(nbttagcompound);
    }

    @Override
    public void askForRespawn(EntityIMLiving entity) {
        mod_Invasion.log("Stuck entity asking for respawn: " + entity.toString() + "  " + entity.field_70165_t + ", " + entity.field_70163_u + ", " + entity.field_70161_v);
        this.waveSpawner.askForRespawn(entity);
    }

    @Override
    public AttackerAI getAttackerAI() {
        return this.attackerAI;
    }

    protected void setActivationTimer(int i) {
        this.activationTimer = i;
    }

    protected void setNexusLevel(int i) {
        this.nexusLevel = i;
    }

    protected void setNexusKills(int i) {
        this.nexusKills = i;
    }

    protected void setGeneration(int i) {
        this.generation = i;
    }

    protected void setNexusPowerLevel(int i) {
        this.powerLevel = i;
    }

    protected void setCookTime(int i) {
        this.cookTime = i;
    }

    protected void setWave(int wave) {
        this.currentWave = wave;
    }

    private void startInvasion(int startWave) {
        this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), (double)(this.field_145848_d - (this.spawnRadius + 40)), (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), (double)(this.field_145848_d + (this.spawnRadius + 40)), (double)(this.field_145849_e + (this.spawnRadius + 10)));
        if (this.mode == 2 && this.continuousAttack) {
            mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Can't activate nexus when already under attack!");
            return;
        }
        if (this.mode == 0 || this.mode == 2) {
            if (this.waveSpawner.isReady()) {
                try {
                    this.currentWave = startWave;
                    this.waveSpawner.beginNextWave(this.currentWave);
                    if (this.mode == 0) {
                        this.setMode(1);
                    } else {
                        this.setMode(3);
                    }
                    this.bindPlayers();
                    this.hp = this.maxHp;
                    this.lastHp = this.maxHp;
                    this.waveDelayTimer = -1L;
                    this.timer = System.currentTimeMillis();
                    mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The first wave is coming soon!");
                    this.playSoundForBoundPlayers("invmod:rumble");
                }
                catch (WaveSpawnerException e) {
                    this.stop();
                    mod_Invasion.log(e.getMessage());
                    mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), e.getMessage());
                }
            } else {
                mod_Invasion.log("Wave spawner not in ready state");
            }
        } else {
            mod_Invasion.log("Tried to activate nexus while already active");
        }
    }

    private void startContinuousPlay() {
        this.boundingBoxToRadius.func_72324_b((double)(this.field_145851_c - (this.spawnRadius + 10)), 0.0, (double)(this.field_145849_e - (this.spawnRadius + 10)), (double)(this.field_145851_c + (this.spawnRadius + 10)), 127.0, (double)(this.field_145849_e + (this.spawnRadius + 10)));
        if (this.mode == 4 && this.waveSpawner.isReady() && mod_Invasion.tryGetInvasionPermission(this)) {
            this.setMode(2);
            this.hp = this.maxHp;
            this.lastHp = this.maxHp;
            this.lastPowerLevel = this.powerLevel;
            this.lastWorldTime = this.field_145850_b.func_72820_D();
            this.nextAttackTime = (int)(this.lastWorldTime / 24000L * 24000L) + 14000;
            if (this.lastWorldTime % 24000L > 12000L && this.lastWorldTime % 24000L < 16000L) {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
            } else {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus activated and stable");
            }
        } else {
            mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Couldn't activate nexus");
        }
    }

    private void doInvasion(int elapsed) throws WaveSpawnerException {
        if (this.waveSpawner.isActive()) {
            if (this.hp <= 0) {
                this.theEnd();
            } else {
                this.generateFlux(1);
                if (this.waveSpawner.isWaveComplete()) {
                    if (this.waveDelayTimer == -1L) {
                        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " almost complete!");
                        this.playSoundForBoundPlayers("invmod:chime1");
                        this.waveDelayTimer = 0L;
                        this.waveDelay = this.waveSpawner.getWaveRestTime();
                    } else {
                        this.waveDelayTimer += (long)elapsed;
                        if (this.waveDelayTimer > this.waveDelay) {
                            ++this.currentWave;
                            mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " about to begin");
                            this.waveSpawner.beginNextWave(this.currentWave);
                            this.waveDelayTimer = -1L;
                            this.playSoundForBoundPlayers("invmod:rumble1");
                            if (this.currentWave > this.nexusLevel) {
                                this.nexusLevel = this.currentWave;
                            }
                        }
                    }
                } else {
                    this.waveSpawner.spawn(elapsed);
                }
            }
        }
    }

    private void playSoundForBoundPlayers(String sound) {
        HashMap<String, Long> boundPlayers = this.getBoundPlayers();
        if (boundPlayers != null) {
            for (Map.Entry<String, Long> entry : boundPlayers.entrySet()) {
                try {
                    EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().func_71203_ab().func_152612_a(entry.getKey());
                    if (player == null) continue;
                    player.func_130014_f_().func_72956_a((Entity)player, sound, 1.0f, 1.0f);
                }
                catch (Exception name) {
                    System.out.println("Problem while trying to play sound at player.");
                }
            }
        }
    }

    private void doContinuous(int elapsed) {
        this.powerLevelTimer += elapsed;
        if (this.powerLevelTimer > 2200) {
            this.powerLevelTimer -= 2200;
            this.generateFlux(5 + (int)((float)(5 * this.powerLevel) / 1550.0f));
            if (this.nexusItemStacks[0] == null || this.nexusItemStacks[0].func_77973_b() != mod_Invasion.itemDampingAgent) {
                ++this.powerLevel;
            }
        }
        if (this.nexusItemStacks[0] != null && this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStrongDampingAgent && this.powerLevel >= 0 && !this.continuousAttack) {
            --this.powerLevel;
            if (this.powerLevel < 0) {
                this.stop();
            }
        }
        if (!this.continuousAttack) {
            long currentTime = this.field_145850_b.func_72820_D();
            int timeOfDay = (int)(this.lastWorldTime % 24000L);
            if (timeOfDay < 12000 && currentTime % 24000L >= 12000L && currentTime + 12000L > (long)this.nextAttackTime) {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
            }
            if (this.lastWorldTime > currentTime) {
                this.nextAttackTime = (int)((long)this.nextAttackTime - (this.lastWorldTime - currentTime));
            }
            this.lastWorldTime = currentTime;
            if (this.lastWorldTime >= (long)this.nextAttackTime) {
                float difficulty = 1.0f + (float)(this.powerLevel / 4500);
                float tierLevel = 1.0f + (float)(this.powerLevel / 4500);
                int timeSeconds = 240;
                try {
                    Wave wave = this.waveBuilder.generateWave(difficulty, tierLevel, timeSeconds);
                    this.lastMobsLeftInWave = this.mobsToKillInWave = (int)((float)wave.getTotalMobAmount() * 0.8f);
                    this.mobsLeftInWave = this.mobsToKillInWave;
                    this.waveSpawner.beginNextWave(wave);
                    this.continuousAttack = true;
                    int days = mod_Invasion.getMinContinuousModeDays() + this.field_145850_b.field_73012_v.nextInt(1 + mod_Invasion.getMaxContinuousModeDays() - mod_Invasion.getMinContinuousModeDays());
                    this.nextAttackTime = (int)(currentTime / 24000L * 24000L) + 14000 + days * 24000;
                    this.lastHp = 100;
                    this.hp = 100;
                    this.zapTimer = 0;
                    this.waveDelayTimer = -1L;
                    mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Forces are destabilising the nexus!");
                    this.playSoundForBoundPlayers("invmod:rumble");
                }
                catch (WaveSpawnerException e) {
                    mod_Invasion.log(e.getMessage());
                    e.printStackTrace();
                    this.stop();
                }
            }
        } else if (this.hp <= 0) {
            this.continuousAttack = false;
            this.continuousNexusHurt();
        } else if (this.waveSpawner.isWaveComplete()) {
            if (this.waveDelayTimer == -1L) {
                this.waveDelayTimer = 0L;
                this.waveDelay = this.waveSpawner.getWaveRestTime();
            } else {
                this.waveDelayTimer += (long)elapsed;
                if (this.waveDelayTimer > this.waveDelay && this.zapTimer < -200) {
                    this.waveDelayTimer = -1L;
                    this.continuousAttack = false;
                    this.waveSpawner.stop();
                    this.hp = 100;
                    this.lastHp = 100;
                    this.lastPowerLevel = this.powerLevel;
                }
            }
            --this.zapTimer;
            if (this.mobsLeftInWave <= 0 && this.zapTimer <= 0 && this.zapEnemy(1)) {
                this.zapEnemy(0);
                this.zapTimer = 23;
            }
        } else {
            try {
                this.waveSpawner.spawn(elapsed);
            }
            catch (WaveSpawnerException e) {
                mod_Invasion.log(e.getMessage());
                e.printStackTrace();
                this.stop();
            }
        }
    }

    private void updateStatus() {
        if (this.nexusItemStacks[0] != null) {
            if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemIMTrap && this.nexusItemStacks[0].func_77960_j() == 0) {
                if (this.cookTime < 1200) {
                    this.cookTime = this.mode == 0 ? ++this.cookTime : (this.cookTime += 9);
                }
                if (this.cookTime >= 1200) {
                    if (this.nexusItemStacks[1] == null) {
                        this.nexusItemStacks[1] = new ItemStack(mod_Invasion.itemIMTrap, 1, 1);
                        if (--this.nexusItemStacks[0].field_77994_a <= 0) {
                            this.nexusItemStacks[0] = null;
                        }
                        this.cookTime = 0;
                    } else if (this.nexusItemStacks[1].func_77973_b() == mod_Invasion.itemIMTrap && this.nexusItemStacks[1].func_77960_j() == 1) {
                        ++this.nexusItemStacks[1].field_77994_a;
                        if (--this.nexusItemStacks[0].field_77994_a <= 0) {
                            this.nexusItemStacks[0] = null;
                        }
                        this.cookTime = 0;
                    }
                }
            } else if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemRiftFlux && this.nexusItemStacks[0].func_77960_j() == 1) {
                if (this.cookTime < 1200 && this.nexusLevel >= 10) {
                    this.cookTime += 5;
                }
                if (this.cookTime >= 1200 && this.nexusItemStacks[1] == null) {
                    this.nexusItemStacks[1] = new ItemStack(mod_Invasion.itemStrongCatalyst, 1);
                    if (--this.nexusItemStacks[0].field_77994_a <= 0) {
                        this.nexusItemStacks[0] = null;
                    }
                    this.cookTime = 0;
                }
            }
        } else {
            this.cookTime = 0;
        }
        if (this.activationTimer >= 400) {
            this.activationTimer = 0;
            if (mod_Invasion.tryGetInvasionPermission(this) && this.nexusItemStacks[0] != null) {
                if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemNexusCatalyst) {
                    --this.nexusItemStacks[0].field_77994_a;
                    if (this.nexusItemStacks[0].field_77994_a == 0) {
                        this.nexusItemStacks[0] = null;
                    }
                    this.activated = true;
                    this.startInvasion(1);
                } else if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStrongCatalyst) {
                    --this.nexusItemStacks[0].field_77994_a;
                    if (this.nexusItemStacks[0].field_77994_a == 0) {
                        this.nexusItemStacks[0] = null;
                    }
                    this.activated = true;
                    this.startInvasion(10);
                } else if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStableNexusCatalyst) {
                    --this.nexusItemStacks[0].field_77994_a;
                    if (this.nexusItemStacks[0].field_77994_a == 0) {
                        this.nexusItemStacks[0] = null;
                    }
                    this.activated = true;
                    this.startContinuousPlay();
                }
            }
        } else if (this.mode == 0 || this.mode == 4) {
            if (this.nexusItemStacks[0] != null) {
                if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemNexusCatalyst || this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStrongCatalyst) {
                    ++this.activationTimer;
                    this.mode = 0;
                } else if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStableNexusCatalyst) {
                    ++this.activationTimer;
                    this.mode = 4;
                }
            } else {
                this.activationTimer = 0;
            }
        } else if (this.mode == 2) {
            if (this.nexusItemStacks[0] != null) {
                if (this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemNexusCatalyst || this.nexusItemStacks[0].func_77973_b() == mod_Invasion.itemStrongCatalyst) {
                    ++this.activationTimer;
                }
            } else {
                this.activationTimer = 0;
            }
        }
    }

    private void generateFlux(int increment) {
        this.generation += increment;
        if (this.generation >= 3000) {
            if (this.nexusItemStacks[1] == null) {
                this.nexusItemStacks[1] = new ItemStack(mod_Invasion.itemRiftFlux, 1);
                this.generation -= 3000;
            } else if (this.nexusItemStacks[1].func_77973_b() == mod_Invasion.itemRiftFlux) {
                ++this.nexusItemStacks[1].field_77994_a;
                this.generation -= 3000;
            }
        }
    }

    private void stop() {
        if (this.mode == 3) {
            this.setMode(2);
            int days = mod_Invasion.getMinContinuousModeDays() + this.field_145850_b.field_73012_v.nextInt(1 + mod_Invasion.getMaxContinuousModeDays() - mod_Invasion.getMinContinuousModeDays());
            this.nextAttackTime = (int)(this.field_145850_b.func_72820_D() / 24000L * 24000L) + 14000 + days * 24000;
        } else {
            this.setMode(0);
        }
        this.waveSpawner.stop();
        mod_Invasion.setInvasionEnded(this);
        this.activationTimer = 0;
        this.currentWave = 0;
        this.errorState = 0;
        this.activated = false;
    }

    private void bindPlayers() {
        List players = this.field_145850_b.func_72872_a(EntityPlayer.class, this.boundingBoxToRadius);
        for (EntityPlayer entityPlayer : players) {
            long time = System.currentTimeMillis();
            if (!this.boundPlayers.containsKey(entityPlayer.getDisplayName())) {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), entityPlayer.getDisplayName() + (entityPlayer.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s") + " life is now bound to the nexus");
            } else if (time - this.boundPlayers.get(entityPlayer.getDisplayName()) > 300000L) {
                mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), entityPlayer.getDisplayName() + (entityPlayer.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s") + " life is now bound to the nexus");
            }
            this.boundPlayers.put(entityPlayer.getDisplayName(), time);
        }
    }

    private void updateMobList() {
        this.mobList = this.field_145850_b.func_72872_a(EntityIMLiving.class, this.boundingBoxToRadius);
        this.mobsSorted = false;
    }

    protected void setMode(int i) {
        this.mode = i;
        if (this.mode == 0) {
            this.setActive(false);
        } else {
            this.setActive(true);
        }
    }

    private void setActive(boolean flag) {
        if (this.field_145850_b != null) {
            int meta = this.field_145850_b.func_72805_g(this.field_145851_c, this.field_145848_d, this.field_145849_e);
            if (flag) {
                this.field_145850_b.func_72921_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, (meta & 4) == 0 ? meta + 4 : meta, 3);
            } else {
                this.field_145850_b.func_72921_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, (meta & 4) == 4 ? meta - 4 : meta, 3);
            }
        }
    }

    private int acquireEntities() {
        AxisAlignedBB bb = this.boundingBoxToRadius.func_72314_b(10.0, 128.0, 10.0);
        List entities = this.field_145850_b.func_72872_a(EntityIMLiving.class, bb);
        for (EntityIMLiving entity : entities) {
            entity.acquiredByNexus(this);
        }
        mod_Invasion.log("Acquired " + entities.size() + " entities after state restore");
        return entities.size();
    }

    private void theEnd() {
        if (!this.field_145850_b.field_72995_K) {
            mod_Invasion.sendMessageToPlayers(this.boundPlayers, "The nexus is destroyed!");
            this.stop();
            long time = System.currentTimeMillis();
            for (Map.Entry<String, Long> entry : this.boundPlayers.entrySet()) {
                EntityPlayer player;
                if (time - entry.getValue() >= 300000L || (player = this.field_145850_b.func_72924_a(entry.getKey())) == null) continue;
                player.func_70097_a(DamageSource.field_76376_m, 500.0f);
                this.playSoundForBoundPlayers("random.explode");
            }
            this.boundPlayers.clear();
            this.killAllMobs();
        }
    }

    private void continuousNexusHurt() {
        mod_Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus severely damaged!");
        this.playSoundForBoundPlayers("random.explode");
        this.killAllMobs();
        this.waveSpawner.stop();
        this.lastPowerLevel = this.powerLevel = (int)((float)(this.powerLevel - (this.powerLevel - this.lastPowerLevel)) * 0.7f);
        if (this.powerLevel < 0) {
            this.powerLevel = 0;
            this.stop();
        }
    }

    private void killAllMobs() {
        List mobs = this.field_145850_b.func_72872_a(EntityIMLiving.class, this.boundingBoxToRadius);
        for (EntityIMLiving mob : mobs) {
            mob.func_70097_a(DamageSource.field_76376_m, 500.0f);
        }
        List wolves = this.field_145850_b.func_72872_a(EntityIMWolf.class, this.boundingBoxToRadius);
        for (EntityIMWolf wolf : wolves) {
            wolf.func_70097_a(DamageSource.field_76376_m, 500.0f);
        }
    }

    private boolean zapEnemy(int sfx) {
        if (this.mobList.size() > 0) {
            if (!this.mobsSorted) {
                Collections.sort(this.mobList, new ComparatorEntityDistance(this.field_145851_c, this.field_145848_d, this.field_145849_e));
            }
            EntityIMLiving mob = this.mobList.remove(this.mobList.size() - 1);
            mob.func_70097_a(DamageSource.field_76376_m, 500.0f);
            EntityIMBolt bolt = new EntityIMBolt(this.field_145850_b, (double)this.field_145851_c + 0.5, (double)this.field_145848_d + 0.5, (double)this.field_145849_e + 0.5, mob.field_70165_t, mob.field_70163_u, mob.field_70161_v, 15, sfx);
            this.field_145850_b.func_72838_d((Entity)bolt);
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean resumeSpawnerContinuous() {
        try {
            mod_Invasion.tryGetInvasionPermission(this);
            float difficulty = 1.0f + (float)(this.powerLevel / 4500);
            float tierLevel = 1.0f + (float)(this.powerLevel / 4500);
            int timeSeconds = 240;
            Wave wave = this.waveBuilder.generateWave(difficulty, tierLevel, timeSeconds);
            this.mobsToKillInWave = (int)((float)wave.getTotalMobAmount() * 0.8f);
            mod_Invasion.log("Original mobs to kill: " + this.mobsToKillInWave);
            this.mobsLeftInWave = this.lastMobsLeftInWave = this.mobsToKillInWave - this.waveSpawner.resumeFromState(wave, this.spawnerElapsedRestore, this.spawnRadius);
            boolean bl = true;
            return bl;
        }
        catch (WaveSpawnerException e) {
            mod_Invasion.log("Error resuming spawner:" + e.getMessage());
            this.waveSpawner.stop();
            boolean bl = false;
            return bl;
        }
        finally {
            mod_Invasion.setInvasionEnded(this);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean resumeSpawnerInvasion() {
        try {
            mod_Invasion.tryGetInvasionPermission(this);
            this.waveSpawner.resumeFromState(this.currentWave, this.spawnerElapsedRestore, this.spawnRadius);
            boolean bl = true;
            return bl;
        }
        catch (WaveSpawnerException e) {
            mod_Invasion.log("Error resuming spawner:" + e.getMessage());
            this.waveSpawner.stop();
            boolean bl = false;
            return bl;
        }
        finally {
            mod_Invasion.setInvasionEnded(this);
        }
    }

    private void playSoundTo() {
    }

    private void updateAI() {
        this.attackerAI.update();
    }

    public String func_145825_b() {
        return "Nexus";
    }

    public boolean func_145818_k_() {
        return false;
    }

    public void func_70295_k_() {
    }

    public void func_70305_f() {
    }

    @Override
    public HashMap<String, Long> getBoundPlayers() {
        return this.boundPlayers;
    }

    public boolean isActive() {
        return this.activated;
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.func_145839_a(pkt.func_148857_g());
        this.field_145850_b.func_147471_g(this.field_145851_c, this.field_145848_d, this.field_145849_e);
    }

    public Packet func_145844_m() {
        NBTTagCompound tag = new NBTTagCompound();
        this.func_145841_b(tag);
        return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 0, tag);
    }
}

