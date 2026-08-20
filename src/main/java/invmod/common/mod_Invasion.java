/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLCommonHandler
 *  cpw.mods.fml.common.Mod
 *  cpw.mods.fml.common.Mod$EventHandler
 *  cpw.mods.fml.common.SidedProxy
 *  cpw.mods.fml.common.event.FMLInitializationEvent
 *  cpw.mods.fml.common.event.FMLInterModComms
 *  cpw.mods.fml.common.event.FMLPostInitializationEvent
 *  cpw.mods.fml.common.event.FMLPreInitializationEvent
 *  cpw.mods.fml.common.event.FMLServerStartingEvent
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  cpw.mods.fml.common.gameevent.PlayerEvent$PlayerLoggedInEvent
 *  cpw.mods.fml.common.network.IGuiHandler
 *  cpw.mods.fml.common.network.NetworkRegistry
 *  cpw.mods.fml.common.registry.EntityRegistry
 *  cpw.mods.fml.common.registry.GameRegistry
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDispenser
 *  net.minecraft.command.CommandHandler
 *  net.minecraft.command.ICommand
 *  net.minecraft.command.ICommandManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.monster.EntitySkeleton
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.World
 *  net.minecraft.world.biome.BiomeGenBase
 *  net.minecraftforge.common.MinecraftForge
 */
package invmod.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import invmod.common.ConfigInvasion;
import invmod.common.GuiHandler;
import invmod.common.InvasionCommand;
import invmod.common.ProxyCommon;
import invmod.common.creativetab.CreativeTabInvmod;
import invmod.common.entity.EntityIMBird;
import invmod.common.entity.EntityIMBolt;
import invmod.common.entity.EntityIMBoulder;
import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMEgg;
import invmod.common.entity.EntityIMGiantBird;
import invmod.common.entity.EntityIMImp;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.EntityIMPrimedTNT;
import invmod.common.entity.EntityIMSkeleton;
import invmod.common.entity.EntityIMSpawnProxy;
import invmod.common.entity.EntityIMSpider;
import invmod.common.entity.EntityIMThrower;
import invmod.common.entity.EntityIMTrap;
import invmod.common.entity.EntityIMWolf;
import invmod.common.entity.EntityIMZombie;
import invmod.common.entity.EntityIMZombiePigman;
import invmod.common.item.ItemCatalystMixture;
import invmod.common.item.ItemDampingAgent;
import invmod.common.item.ItemDebugWand;
import invmod.common.item.ItemEngyHammer;
import invmod.common.item.ItemInfusedSword;
import invmod.common.item.ItemNexusCatalyst;
import invmod.common.item.ItemPhaseCrystal;
import invmod.common.item.ItemProbe;
import invmod.common.item.ItemRiftFlux;
import invmod.common.item.ItemSearingBow;
import invmod.common.item.ItemSmallRemnants;
import invmod.common.item.ItemStableCatalystMixture;
import invmod.common.item.ItemStableNexusCatalyst;
import invmod.common.item.ItemStrangeBone;
import invmod.common.item.ItemStrongCatalyst;
import invmod.common.item.ItemStrongDampingAgent;
import invmod.common.item.ItemTrap;
import invmod.common.nexus.BlockNexus;
import invmod.common.nexus.IEntityIMPattern;
import invmod.common.nexus.IMWaveBuilder;
import invmod.common.nexus.MobBuilder;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.ISelect;
import invmod.common.util.RandomSelectionPool;
import invmod.common.util.ThreadGetData;
import invmod.common.util.Version;
import invmod.common.util.VersionChecker;
import invmod.common.util.spawneggs.CustomTags;
import invmod.common.util.spawneggs.DispenserBehaviorSpawnEgg;
import invmod.common.util.spawneggs.ItemSpawnEgg;
import invmod.common.util.spawneggs.SpawnEggInfo;
import invmod.common.util.spawneggs.SpawnEggRegistry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="mod_Invasion", name="Invasion", version="1.1.5")
public class mod_Invasion {
    @SidedProxy(clientSide="invmod.client.ProxyClient", serverSide="invmod.common.ProxyCommon")
    public static ProxyCommon proxy;
    public static String recentNews;
    public static Version versionNumber;
    public static String latestVersionNumber;
    public static GuiHandler guiHandler;
    public static ConfigInvasion configInvasion;
    private static File configFile;
    private static boolean runFlag;
    private static long timer;
    private static long clientElapsed;
    private static long serverElapsed;
    private static boolean serverRunFlag;
    private static int killTimer;
    private static boolean loginFlag;
    private static HashMap<String, Long> deathList;
    private static MobBuilder defaultMobBuilder;
    private static BufferedWriter logOut;
    private static ISelect<IEntityIMPattern> nightSpawnPool1;
    private static TileEntityNexus focusNexus;
    private static TileEntityNexus activeNexus;
    private static boolean isInvasionActive;
    public static final byte PACKET_SFX = 0;
    public static final byte PACKET_INV_MOB_SPAWN = 2;
    public static int entityId;
    public static final String modid = "mod_Invasion";
    private static final boolean DEBUG_CONFIG = false;
    private static final int DEFAULT_GUI_ID_NEXUS = 76;
    private static final boolean DEFAULT_CRAFT_ITEMS_ENABLED = true;
    private static final boolean DEFAULT_NIGHT_SPAWNS_ENABLED = false;
    private static final int DEFAULT_MIN_CONT_MODE_DAYS = 2;
    private static final int DEFAULT_MAX_CONT_MODE_DAYS = 3;
    private static final int DEFAULT_NIGHT_MOB_SIGHT_RANGE = 20;
    private static final int DEFAULT_NIGHT_MOB_SENSE_RANGE = 8;
    private static final int DEFAULT_NIGHT_MOB_SPAWN_CHANCE = 30;
    private static final int DEFAULT_NIGHT_MOB_MAX_GROUP_SIZE = 3;
    private static final int DEFAULT_NIGHT_MOB_LIMIT_OVERRIDE = 70;
    private static final float DEFAULT_NIGHT_MOB_STATS_SCALING = 1.0f;
    private static final boolean DEFAULT_NIGHT_MOBS_BURN = true;
    public static final String[] DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS;
    public static final float[] DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS;
    private static boolean alreadyNotified;
    private static boolean updateNotifications;
    private static boolean destructedBlocksDrop;
    private static boolean craftItemsEnabled;
    private static boolean debugMode;
    private static int guiIdNexus;
    private static int minContinuousModeDays;
    private static int maxContinuousModeDays;
    private static boolean nightSpawnsEnabled;
    private static int nightMobSightRange;
    private static int nightMobSenseRange;
    private static int nightMobSpawnChance;
    private static int nightMobMaxGroupSize;
    private static int maxNightMobs;
    private static float nightMobStatsScaling;
    private static boolean nightMobsBurnInDay;
    private static boolean enableLog;
    public static HashMap<String, Integer> mobHealthNightspawn;
    public static HashMap<String, Integer> mobHealthInvasion;
    public static CreativeTabInvmod tabInvmod;
    public static BlockNexus blockNexus;
    public static Item itemPhaseCrystal;
    public static Item itemRiftFlux;
    public static Item itemSmallRemnants;
    public static Item itemNexusCatalyst;
    public static Item itemInfusedSword;
    public static Item itemIMTrap;
    public static Item itemSearingBow;
    public static Item itemCatalystMixture;
    public static Item itemStableCatalystMixture;
    public static Item itemStableNexusCatalyst;
    public static Item itemDampingAgent;
    public static Item itemStrongDampingAgent;
    public static Item itemStrangeBone;
    public static Item itemProbe;
    public static Item itemStrongCatalyst;
    public static Item itemEngyHammer;
    public static Item itemDebugWand;
    public static ItemSpawnEgg itemSpawnEgg;
    public static mod_Invasion instance;

    public mod_Invasion() {
        instance = this;
        runFlag = true;
        serverRunFlag = true;
        loginFlag = false;
        timer = 0L;
        clientElapsed = 0L;
        guiHandler = new GuiHandler();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File logFile = proxy.getFile("/logs/invasion_log.log");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logOut = new BufferedWriter(new FileWriter(logFile));
        }
        catch (Exception e) {
            logOut = null;
            mod_Invasion.log("Couldn't write to logfile");
            mod_Invasion.log(e.getMessage());
        }
        configFile = proxy.getFile("/config/invasion_config.cfg");
        alreadyNotified = false;
        configInvasion = new ConfigInvasion();
        configInvasion.loadConfig(configFile);
        enableLog = configInvasion.getPropertyValueBoolean("enable-log-file", false);
        destructedBlocksDrop = configInvasion.getPropertyValueBoolean("destructed-blocks-drop", true);
        updateNotifications = configInvasion.getPropertyValueBoolean("update-messages-enabled", true);
        craftItemsEnabled = configInvasion.getPropertyValueBoolean("craft-items-enabled", true);
        debugMode = configInvasion.getPropertyValueBoolean("debug", false);
        guiIdNexus = configInvasion.getPropertyValueInt("guiID-Nexus", 76);
        minContinuousModeDays = configInvasion.getPropertyValueInt("min-days-to-attack", 2);
        maxContinuousModeDays = configInvasion.getPropertyValueInt("max-days-to-attack", 3);
        this.nightSpawnConfig();
        this.loadHealthConfig();
        HashMap<Integer, Float> strengthOverrides = new HashMap<Integer, Float>();
        for (int i = 1; i < 4096; ++i) {
            float strength;
            String property = configInvasion.getProperty("block" + i + "-strength", "null");
            if (property == "null" || !((strength = Float.parseFloat(property)) > 0.0f)) continue;
            strengthOverrides.put(i, Float.valueOf(strength));
            EntityIMLiving.putBlockStrength(Block.func_149729_e((int)i), strength);
            float pathCost = 1.0f + strength * 0.4f;
            EntityIMLiving.putBlockCost(Block.func_149729_e((int)i), pathCost);
        }
        configInvasion.saveConfig(configFile, strengthOverrides, false);
        this.loadCreativeTabs();
        this.loadBlocks();
        this.loadItems();
        this.loadEntities();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)instance, (IGuiHandler)guiHandler);
        new ThreadGetData();
        FMLCommonHandler.instance().bus().register((Object)this);
        MinecraftForge.EVENT_BUS.register((Object)this);
        FMLInterModComms.sendMessage((String)"Waila", (String)"register", (String)"invmod.common.util.IMWailaProvider.callbackRegister");
        if (craftItemsEnabled) {
            this.addRecipes();
        }
        if (nightSpawnsEnabled) {
            BiomeGenBase[] biomes = new BiomeGenBase[]{BiomeGenBase.field_76772_c, BiomeGenBase.field_76770_e, BiomeGenBase.field_76767_f, BiomeGenBase.field_76768_g, BiomeGenBase.field_76780_h, BiomeGenBase.field_76785_t, BiomeGenBase.field_76784_u, BiomeGenBase.field_76783_v, BiomeGenBase.field_76782_w, BiomeGenBase.field_76792_x};
            EntityRegistry.addSpawn(EntityIMSpawnProxy.class, (int)nightMobSpawnChance, (int)1, (int)1, (EnumCreatureType)EnumCreatureType.monster, (BiomeGenBase[])biomes);
            EntityRegistry.addSpawn(EntityZombie.class, (int)1, (int)1, (int)1, (EnumCreatureType)EnumCreatureType.monster, (BiomeGenBase[])biomes);
            EntityRegistry.addSpawn(EntitySpider.class, (int)1, (int)1, (int)1, (EnumCreatureType)EnumCreatureType.monster, (BiomeGenBase[])biomes);
            EntityRegistry.addSpawn(EntitySkeleton.class, (int)1, (int)1, (int)1, (EnumCreatureType)EnumCreatureType.monster, (BiomeGenBase[])biomes);
        }
        if (maxNightMobs != 70) {
            try {
                Class<EnumCreatureType> c = EnumCreatureType.class;
                T[] consts = c.getEnumConstants();
                Class<?> sub = consts[0].getClass();
                Field field = sub.getDeclaredField("maxNumberOfCreature");
                field.setAccessible(true);
                field.set(EnumCreatureType.monster, maxNightMobs);
            }
            catch (Exception e) {
                mod_Invasion.log(e.getMessage());
            }
        }
    }

    @Mod.EventHandler
    public void postInitialise(FMLPostInitializationEvent evt) {
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().func_71187_D();
        if (commandManager instanceof CommandHandler) {
            ((CommandHandler)commandManager).func_71560_a((ICommand)new InvasionCommand());
        }
    }

    @SubscribeEvent
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (mod_Invasion.getUpdateNotifications() && !alreadyNotified) {
                alreadyNotified = true;
                VersionChecker.checkForUpdates((EntityPlayerMP)event.player);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void loadHealthConfig() {
        mobHealthInvasion.put("IMCreeper-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMCreeper-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMVulture-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMVulture-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMImp-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMImp-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMPigManEngineer-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMPigManEngineer-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMSkeleton-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMSkeleton-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMSpider-T1-Spider-invasionSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T1-Spider-invasionSpawn-health", 18));
        mobHealthInvasion.put("IMSpider-T1-Baby-Spider-invasionSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T1-Baby-Spider-invasionSpawn-health", 3));
        mobHealthInvasion.put("IMSpider-T2-Jumping-Spider-invasionSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T2-Jumping-Spider-invasionSpawn-health", 18));
        mobHealthInvasion.put("IMSpider-T2-Mother-Spider-invasionSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T2-Mother-Spider-invasionSpawn-health", 23));
        mobHealthInvasion.put("IMThrower-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMThrower-T1-invasionSpawn-health", 50));
        mobHealthInvasion.put("IMThrower-T2-invasionSpawn-health", configInvasion.getPropertyValueInt("IMThrower-T2-invasionSpawn-health", 70));
        mobHealthInvasion.put("IMZombie-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMZombie-T2-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T2-invasionSpawn-health", 30));
        mobHealthInvasion.put("IMZombie-T3-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T3-invasionSpawn-health", 65));
        mobHealthInvasion.put("IMZombiePigman-T1-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T1-invasionSpawn-health", 20));
        mobHealthInvasion.put("IMZombiePigman-T2-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T2-invasionSpawn-health", 30));
        mobHealthInvasion.put("IMZombiePigman-T3-invasionSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T3-invasionSpawn-health", 65));
        mobHealthNightspawn.put("IMCreeper-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMCreeper-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMVulture-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMVulture-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMImp-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMImp-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMPigManEngineer-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMPigManEngineer-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMSkeleton-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMSkeleton-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMSpider-T1-Spider-nightSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T1-Spider-nightSpawn-health", 18));
        mobHealthNightspawn.put("IMSpider-T1-Baby-Spider-nightSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T1-Baby-Spider-nightSpawn-health", 3));
        mobHealthNightspawn.put("IMSpider-T2-Jumping-Spider-nightSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T2-Jumping-Spider-nightSpawn-health", 18));
        mobHealthNightspawn.put("IMSpider-T2-Mother-Spider-nightSpawn-health", configInvasion.getPropertyValueInt("IMSpider-T2-Mother-Spider-nightSpawn-health", 23));
        mobHealthNightspawn.put("IMThrower-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMThrower-T1-nightSpawn-health", 50));
        mobHealthNightspawn.put("IMThrower-T2-nightSpawn-health", configInvasion.getPropertyValueInt("IMThrower-T2-nightSpawn-health", 70));
        mobHealthNightspawn.put("IMZombie-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMZombie-T2-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T2-nightSpawn-health", 30));
        mobHealthNightspawn.put("IMZombie-T3-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombie-T3-nightSpawn-health", 65));
        mobHealthNightspawn.put("IMZombiePigman-T1-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T1-nightSpawn-health", 20));
        mobHealthNightspawn.put("IMZombiePigman-T2-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T2-nightSpawn-health", 30));
        mobHealthNightspawn.put("IMZombiePigman-T3-nightSpawn-health", configInvasion.getPropertyValueInt("IMZombiePigman-T3-nightSpawn-health", 65));
    }

    protected void loadCreativeTabs() {
        tabInvmod = new CreativeTabInvmod();
    }

    protected void loadBlocks() {
        blockNexus = new BlockNexus();
        GameRegistry.registerBlock((Block)blockNexus, (String)blockNexus.func_149739_a().substring(5));
        GameRegistry.registerTileEntity(TileEntityNexus.class, (String)"Nexus");
    }

    protected void loadItems() {
        itemPhaseCrystal = new ItemPhaseCrystal();
        itemRiftFlux = new ItemRiftFlux();
        itemSmallRemnants = new ItemSmallRemnants();
        itemNexusCatalyst = new ItemNexusCatalyst();
        itemInfusedSword = new ItemInfusedSword();
        itemSearingBow = new ItemSearingBow();
        itemCatalystMixture = new ItemCatalystMixture();
        itemStableCatalystMixture = new ItemStableCatalystMixture();
        itemStableNexusCatalyst = new ItemStableNexusCatalyst();
        itemDampingAgent = new ItemDampingAgent();
        itemStrongDampingAgent = new ItemStrongDampingAgent();
        itemStrangeBone = new ItemStrangeBone();
        itemStrongCatalyst = new ItemStrongCatalyst();
        itemEngyHammer = new ItemEngyHammer();
        itemProbe = new ItemProbe();
        itemIMTrap = new ItemTrap();
        itemSpawnEgg = new ItemSpawnEgg();
        GameRegistry.registerItem((Item)itemPhaseCrystal, (String)itemPhaseCrystal.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemRiftFlux, (String)itemRiftFlux.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemSmallRemnants, (String)itemSmallRemnants.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemNexusCatalyst, (String)itemNexusCatalyst.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemInfusedSword, (String)itemInfusedSword.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemSearingBow, (String)itemSearingBow.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemCatalystMixture, (String)itemCatalystMixture.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemStableCatalystMixture, (String)itemStableCatalystMixture.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemStableNexusCatalyst, (String)itemStableNexusCatalyst.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemDampingAgent, (String)itemDampingAgent.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemStrongDampingAgent, (String)itemStrongDampingAgent.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemStrangeBone, (String)itemStrangeBone.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemStrongCatalyst, (String)itemStrongCatalyst.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemEngyHammer, (String)itemEngyHammer.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemProbe, (String)itemProbe.func_77658_a().substring(5));
        GameRegistry.registerItem((Item)itemIMTrap, (String)itemIMTrap.func_77658_a().substring(5));
        if (debugMode) {
            itemDebugWand = new ItemDebugWand();
            GameRegistry.registerItem((Item)itemDebugWand, (String)itemDebugWand.func_77658_a().substring(5));
        } else {
            itemDebugWand = null;
        }
    }

    protected void loadEntities() {
        EntityRegistry.registerModEntity(EntityIMZombie.class, (String)"IMZombie", (int)5, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMSkeleton.class, (String)"IMSkeleton", (int)6, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMSpider.class, (String)"IMSpider", (int)7, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMPigEngy.class, (String)"IMPigEngy", (int)8, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMWolf.class, (String)"IMWolf", (int)9, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMEgg.class, (String)"IMEgg", (int)10, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMCreeper.class, (String)"IMCreeper", (int)11, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMImp.class, (String)"IMImp", (int)12, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMZombiePigman.class, (String)"IMZombiePigman", (int)13, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMThrower.class, (String)"IMThrower", (int)14, (Object)this, (int)128, (int)1, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMBoulder.class, (String)"IMBoulder", (int)1, (Object)this, (int)36, (int)4, (boolean)true);
        EntityRegistry.registerModEntity(EntityIMBolt.class, (String)"IMBolt", (int)2, (Object)this, (int)36, (int)5, (boolean)false);
        EntityRegistry.registerModEntity(EntityIMTrap.class, (String)"IMTrap", (int)3, (Object)this, (int)36, (int)5, (boolean)false);
        EntityRegistry.registerModEntity(EntityIMPrimedTNT.class, (String)"IMPrimedTNT", (int)4, (Object)this, (int)36, (int)4, (boolean)true);
        if (debugMode) {
            EntityRegistry.registerModEntity(EntityIMBird.class, (String)"IMBird", (int)15, (Object)this, (int)128, (int)1, (boolean)true);
            EntityRegistry.registerModEntity(EntityIMGiantBird.class, (String)"IMGiantBird", (int)16, (Object)this, (int)128, (int)1, (boolean)true);
        }
        GameRegistry.registerItem((Item)itemSpawnEgg, (String)itemSpawnEgg.func_77658_a());
        BlockDispenser.field_149943_a.func_82595_a((Object)itemSpawnEgg, (Object)new DispenserBehaviorSpawnEgg());
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(1, "mod_Invasion.IMZombie", "Zombie T1", CustomTags.IMZombie_T1(), 7042367, 2628362));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(2, "mod_Invasion.IMZombie", "Zombie T2", CustomTags.IMZombie_T2(), 4814131, 0x7C7C7C));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(3, "mod_Invasion.IMZombie", "Tar Zombie T2", CustomTags.IMZombie_T2_tar(), 3818021, 1645587));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(4, "mod_Invasion.IMZombie", "Zombie Brute T3", CustomTags.IMZombie_T3(), 5792070, 1984057));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(5, "mod_Invasion.IMSkeleton", "Skeleton T1", new NBTTagCompound(), 0x9B9B9B, 0x797979));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(6, "mod_Invasion.IMSpider", "Spider T1", new NBTTagCompound(), 5261886, 10752540));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(7, "mod_Invasion.IMSpider", "Spider T1 Baby", CustomTags.IMSpider_T1_baby(), 5261886, 10752540));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(8, "mod_Invasion.IMSpider", "Spider T2 Jumper", CustomTags.IMSpider_T2(), 4473191, 656168));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(9, "mod_Invasion.IMSpider", "Spider T2 Mother", CustomTags.IMSpider_T2_mother(), 4473191, 656168));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(10, "mod_Invasion.IMCreeper", "Creeper T1", new NBTTagCompound(), 2330399, 0xA5AAA6));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(11, "mod_Invasion.IMPigEngy", "Pigman Engineer T1", new NBTTagCompound(), 15505045, 0x420000));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(12, "mod_Invasion.IMThrower", "Thrower T1", new NBTTagCompound(), 5529399, 1912126));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(13, "mod_Invasion.IMThrower", "Thrower T2", CustomTags.IMThrower_T2(), 87046164, 6498312));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(14, "mod_Invasion.IMImp", "Imp T1", new NBTTagCompound(), 11796755, 0xFF0000));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(15, "mod_Invasion.IMZombiePigman", "Zombie Pigman T1", CustomTags.IMZombiePigman_T1(), 15437457, 4810031));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(16, "mod_Invasion.IMZombiePigman", "Zombie Pigman T2", CustomTags.IMZombiePigman_T2(), 15437457, 4810031));
        SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(17, "mod_Invasion.IMZombiePigman", "Zombie Pigman T3", CustomTags.IMZombiePigman_T3(), 15437457, 4810031));
        if (debugMode) {
            SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(18, "mod_Invasion.IMGiantBird", "Vulture T1", new NBTTagCompound(), 0x2B2B2B, 15367900));
        }
        proxy.preloadTexture("/mods/invmod/textures/zombie_old.png");
        proxy.preloadTexture("/mods/invmod/textures/zombieT1a.png");
        proxy.preloadTexture("/mods/invmod/textures/zombieT2.png");
        proxy.preloadTexture("/mods/invmod/textures/zombieT2a.png");
        proxy.preloadTexture("/mods/invmod/textures/zombietar.png");
        proxy.preloadTexture("/mods/invmod/textures/zombieT3.png");
        proxy.preloadTexture("/mods/invmod/textures/spiderT2.png");
        proxy.preloadTexture("/mods/invmod/textures/spiderT2b.png");
        proxy.preloadTexture("/mods/invmod/textures/throwerT1.png");
        proxy.preloadTexture("/mods/invmod/textures/throwerT2.png");
        proxy.preloadTexture("/mods/invmod/textures/pigengT1.png");
        proxy.preloadTexture("/mods/invmod/textures/nexusgui.png");
        proxy.preloadTexture("/mods/invmod/textures/boulder.png");
        proxy.preloadTexture("/mods/invmod/textures/trap.png");
        proxy.preloadTexture("/mods/invmod/textures/testmodel.png");
        proxy.preloadTexture("/mods/invmod/textures/burrower.png");
        proxy.preloadTexture("/mods/invmod/textures/spideregg.png");
        proxy.preloadTexture("/mods/invmod/textures/imp.png");
        proxy.preloadTexture("/mods/invmod/textures/vulture.png");
        proxy.loadAnimations();
        proxy.registerEntityRenderers();
    }

    protected void addRecipes() {
        GameRegistry.addRecipe((ItemStack)new ItemStack((Block)blockNexus, 1), (Object[])new Object[]{" X ", "#D#", " # ", Character.valueOf('X'), itemPhaseCrystal, Character.valueOf('#'), Items.field_151137_ax, Character.valueOf('D'), Blocks.field_150343_Z});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemPhaseCrystal, 1), (Object[])new Object[]{" X ", "#D#", " X ", Character.valueOf('X'), new ItemStack(Items.field_151100_aR, 1, 4), Character.valueOf('#'), Items.field_151137_ax, Character.valueOf('D'), Items.field_151045_i});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemPhaseCrystal, 1), (Object[])new Object[]{" X ", "#D#", " X ", Character.valueOf('X'), Items.field_151137_ax, Character.valueOf('#'), new ItemStack(Items.field_151100_aR, 1, 4), Character.valueOf('D'), Items.field_151045_i});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemRiftFlux, 1), (Object[])new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), new ItemStack(itemSmallRemnants, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemInfusedSword, 1), (Object[])new Object[]{"X  ", "X# ", "X  ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1), Character.valueOf('#'), new ItemStack(Items.field_151048_u, 1, Short.MAX_VALUE)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemCatalystMixture, 1), (Object[])new Object[]{"   ", "D#H", " X ", Character.valueOf('X'), Items.field_151054_z, Character.valueOf('#'), Items.field_151137_ax, Character.valueOf('D'), Items.field_151103_aS, Character.valueOf('H'), Items.field_151078_bh});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemCatalystMixture, 1), (Object[])new Object[]{"   ", "H#D", " X ", Character.valueOf('X'), Items.field_151054_z, Character.valueOf('#'), Items.field_151137_ax, Character.valueOf('D'), Items.field_151103_aS, Character.valueOf('H'), Items.field_151078_bh});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemStableCatalystMixture, 1), (Object[])new Object[]{"   ", "D#D", " X ", Character.valueOf('X'), Items.field_151054_z, Character.valueOf('#'), Items.field_151044_h, Character.valueOf('D'), Items.field_151103_aS, Character.valueOf('H'), Items.field_151078_bh});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemDampingAgent, 1), (Object[])new Object[]{"   ", "#X#", "   ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1), Character.valueOf('#'), new ItemStack(Items.field_151100_aR, 1, 4)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemStrongDampingAgent, 1), (Object[])new Object[]{" X ", " X ", " X ", Character.valueOf('X'), itemDampingAgent});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemStrongDampingAgent, 1), (Object[])new Object[]{"   ", "XXX", "   ", Character.valueOf('X'), itemDampingAgent});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemStrangeBone, 1), (Object[])new Object[]{"   ", "X#X", "   ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1), Character.valueOf('#'), Items.field_151103_aS});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemSearingBow, 1), (Object[])new Object[]{"XXX", "X# ", "X  ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1), Character.valueOf('#'), new ItemStack((Item)Items.field_151031_f, 1, Short.MAX_VALUE)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151016_H, 16), (Object[])new Object[]{" X ", " X ", " X ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151016_H, 16), (Object[])new Object[]{"   ", "XXX", "   ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151045_i, 1), (Object[])new Object[]{" X ", "X X", " X ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151042_j, 4), (Object[])new Object[]{"   ", " X ", "   ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151137_ax, 24), (Object[])new Object[]{"   ", "X X", "   ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(Items.field_151100_aR, 12, 4), (Object[])new Object[]{" X ", "   ", " X ", Character.valueOf('X'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemIMTrap, 1, 0), (Object[])new Object[]{" X ", "X#X", " X ", Character.valueOf('X'), Items.field_151042_j, Character.valueOf('#'), new ItemStack(itemRiftFlux, 1)});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemIMTrap, 1, 2), (Object[])new Object[]{"   ", " # ", " X ", Character.valueOf('X'), new ItemStack(itemIMTrap, 1, 0), Character.valueOf('#'), Items.field_151129_at});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemProbe, 1, 0), (Object[])new Object[]{" X ", "XX ", "XX ", Character.valueOf('X'), Items.field_151042_j});
        GameRegistry.addRecipe((ItemStack)new ItemStack(itemProbe, 1, 1), (Object[])new Object[]{" D ", " # ", " X ", Character.valueOf('X'), Items.field_151055_y, Character.valueOf('#'), itemPhaseCrystal, Character.valueOf('D'), new ItemStack(itemProbe, 1, 0)});
        GameRegistry.addSmelting((Item)itemCatalystMixture, (ItemStack)new ItemStack(itemNexusCatalyst), (float)1.0f);
        GameRegistry.addSmelting((Item)itemStableCatalystMixture, (ItemStack)new ItemStack(itemStableNexusCatalyst), (float)1.0f);
    }

    protected void nightSpawnConfig() {
        nightSpawnsEnabled = configInvasion.getPropertyValueBoolean("night-spawns-enabled", false);
        nightMobSightRange = configInvasion.getPropertyValueInt("night-mob-sight-range", 20);
        nightMobSenseRange = configInvasion.getPropertyValueInt("night-mob-sense-range", 12);
        nightMobSpawnChance = configInvasion.getPropertyValueInt("night-mob-spawn-chance", 30);
        nightMobMaxGroupSize = configInvasion.getPropertyValueInt("night-mob-max-group-size", 3);
        maxNightMobs = configInvasion.getPropertyValueInt("mob-limit-override", 70);
        nightMobsBurnInDay = configInvasion.getPropertyValueBoolean("night-mobs-burn-in-day", true);
        String[] pool1Patterns = new String[DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length];
        float[] pool1Weights = new float[DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS.length];
        RandomSelectionPool<IEntityIMPattern> mobPool = new RandomSelectionPool<IEntityIMPattern>();
        nightSpawnPool1 = mobPool;
        if (DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length == DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS.length) {
            for (int i = 0; i < DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length; ++i) {
                pool1Patterns[i] = configInvasion.getPropertyValueString("nm-spawnpool1-slot" + (1 + i), DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS[i]);
                pool1Weights[i] = configInvasion.getPropertyValueFloat("nm-spawnpool1-slot" + (1 + i) + "-weight", DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS[i]);
                if (IMWaveBuilder.isPatternNameValid(pool1Patterns[i])) {
                    mod_Invasion.log("Added entry for pattern 1 slot " + (i + 1));
                    mobPool.addEntry(IMWaveBuilder.getPattern(pool1Patterns[i]), pool1Weights[i]);
                    continue;
                }
                mod_Invasion.log("Pattern 1 slot " + (i + 1) + " in config not recognised. Proceeding as blank.");
                configInvasion.setProperty("nm-spawnpool1-slot" + (1 + i), "none");
            }
        } else {
            mod_Invasion.log("Mob pattern table element mismatch. Ensure each slot has a probability weight");
        }
    }

    public static void addToDeathList(String username, long timeStamp) {
        deathList.put(username, timeStamp);
    }

    public String toString() {
        return modid;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void finalize() throws Throwable {
        try {
            if (logOut != null) {
                logOut.close();
            }
        }
        catch (Exception e) {
            logOut = null;
            mod_Invasion.log("Error closing invasion log file");
        }
        finally {
            super.finalize();
        }
    }

    public static boolean isInvasionActive() {
        return isInvasionActive;
    }

    public static boolean tryGetInvasionPermission(TileEntityNexus nexus) {
        if (nexus == activeNexus) {
            return true;
        }
        if (nexus != null) {
            activeNexus = nexus;
            isInvasionActive = true;
            return true;
        }
        String s = "Nexus entity invalid";
        mod_Invasion.log(s);
        return false;
    }

    public static void setInvasionEnded(TileEntityNexus nexus) {
        if (activeNexus == nexus) {
            isInvasionActive = false;
        }
    }

    public static void setNexusUnloaded(TileEntityNexus nexus) {
        if (activeNexus == nexus) {
            nexus = null;
            isInvasionActive = false;
        }
    }

    public static void setNexusClicked(TileEntityNexus nexus) {
        focusNexus = nexus;
    }

    public static TileEntityNexus getActiveNexus() {
        return activeNexus;
    }

    public static TileEntityNexus getFocusNexus() {
        return focusNexus;
    }

    public static Entity[] getNightMobSpawns1(World world) {
        ISelect<IEntityIMPattern> mobPool = mod_Invasion.getMobSpawnPool();
        int numberOfMobs = world.field_73012_v.nextInt(nightMobMaxGroupSize) + 1;
        Entity[] entities = new Entity[numberOfMobs];
        for (int i = 0; i < numberOfMobs; ++i) {
            EntityIMLiving mob = mod_Invasion.getMobBuilder().createMobFromConstruct(mobPool.selectNext().generateEntityConstruct(), world, null);
            mob.setEntityIndependent();
            mob.setAggroRange(mod_Invasion.getNightMobSightRange());
            mob.setSenseRange(mod_Invasion.getNightMobSenseRange());
            mob.setBurnsInDay(mod_Invasion.getNightMobsBurnInDay());
            entities[i] = mob;
        }
        return entities;
    }

    public static MobBuilder getMobBuilder() {
        return defaultMobBuilder;
    }

    public static ISelect<IEntityIMPattern> getMobSpawnPool() {
        return nightSpawnPool1;
    }

    public static int getMinContinuousModeDays() {
        return minContinuousModeDays;
    }

    public static int getMaxContinuousModeDays() {
        return maxContinuousModeDays;
    }

    public static int getNightMobSightRange() {
        return nightMobSightRange;
    }

    public static int getNightMobSenseRange() {
        return nightMobSenseRange;
    }

    public static boolean getNightMobsBurnInDay() {
        return nightMobsBurnInDay;
    }

    public static ItemStack getRenderHammerItem() {
        return new ItemStack(itemEngyHammer, 1);
    }

    public static int getGuiIdNexus() {
        return guiIdNexus;
    }

    public static mod_Invasion getLoadedInstance() {
        return instance;
    }

    public static void broadcastToAll(String message) {
        FMLCommonHandler.instance().getMinecraftServerInstance().func_71203_ab().func_148539_a((IChatComponent)new ChatComponentText(message));
    }

    public static void sendMessageToPlayers(HashMap<String, Long> hashMap, String message) {
        if (hashMap != null) {
            for (Map.Entry<String, Long> entry : hashMap.entrySet()) {
                mod_Invasion.sendMessageToPlayer(FMLCommonHandler.instance().getMinecraftServerInstance().func_71203_ab().func_152612_a(entry.getKey()), message);
            }
        }
    }

    public static void sendMessageToPlayer(EntityPlayerMP player, String message) {
        if (player != null) {
            player.func_146105_b((IChatComponent)new ChatComponentText(message));
        }
    }

    public static void log(String s) {
        if (enableLog) {
            if (s == null) {
                return;
            }
            try {
                if (logOut != null) {
                    logOut.write(s);
                    logOut.newLine();
                    logOut.flush();
                } else {
                    System.out.println(s);
                }
            }
            catch (IOException e) {
                System.out.println("Couldn't write to invasion log file");
                System.out.println(s);
            }
        }
    }

    public static boolean isDebug() {
        return debugMode;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getMobHealth(EntityIMLiving mob) {
        int health = 0;
        if (mob.isNexusBound()) {
            if (mobHealthInvasion.get(mob.toString() + "-invasionSpawn-health") == null) return 20;
            return mobHealthInvasion.get(mob.toString() + "-invasionSpawn-health");
        }
        if (mobHealthNightspawn.get(mob.toString() + "-nightSpawn-health") == null) return 20;
        return mobHealthNightspawn.get(mob.toString() + "-nightSpawn-health");
    }

    public static boolean getUpdateNotifications() {
        return updateNotifications;
    }

    public static Version getVersionNumber() {
        return versionNumber;
    }

    public static String getLatestVersionNumber() {
        return latestVersionNumber;
    }

    public static String getRecentNews() {
        return recentNews;
    }

    public static boolean getDestructedBlocksDrop() {
        return destructedBlocksDrop;
    }

    static {
        versionNumber = new Version(1, 1, 5);
        deathList = new HashMap();
        defaultMobBuilder = new MobBuilder();
        isInvasionActive = false;
        entityId = 250;
        DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS = new String[]{"zombie_t1_any", "zombie_t2_any_basic", "zombie_t2_plain", "zombie_t2_tar", "zombie_t2_pigman", "zombie_t3_any", "zombiePigman_t1_any", "zombiePigman_t2_any", "zombiePigman_t3_any", "spider_t1_any", "spider_t2_any", "pigengy_t1_any", "skeleton_t1_any", "thrower_t1", "thrower_t2", "creeper_t1_basic", "imp_t1"};
        DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS = new float[]{1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        mobHealthNightspawn = new HashMap();
        mobHealthInvasion = new HashMap();
    }
}

