/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.ICrafting
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 */
package invmod.common.nexus;

import invmod.common.nexus.SlotOutput;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerNexus
extends Container {
    private TileEntityNexus nexus;
    private int activationTimer = 0;
    private int currentWave = 0;
    private int nexusLevel = 0;
    private int nexusKills = 0;
    private int spawnRadius = 0;
    private int generation = 0;
    private int powerLevel = 0;
    private int cookTime = 0;
    private int mode = 0;

    public ContainerNexus(InventoryPlayer inventoryplayer, TileEntityNexus tileEntityNexus) {
        this.nexus = tileEntityNexus;
        this.func_75146_a(new Slot((IInventory)tileEntityNexus, 0, 32, 33));
        this.func_75146_a(new SlotOutput(tileEntityNexus, 1, 102, 33));
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.func_75146_a(new Slot((IInventory)inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot((IInventory)inventoryplayer, j, 8 + j * 18, 142));
        }
    }

    public void func_75142_b() {
        super.func_75142_b();
        for (int i = 0; i < this.field_75149_d.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.field_75149_d.get(i);
            if (this.activationTimer != this.nexus.getActivationTimer()) {
                icrafting.func_71112_a((Container)this, 0, this.nexus.getActivationTimer());
            }
            if (this.mode != this.nexus.getMode()) {
                icrafting.func_71112_a((Container)this, 1, this.nexus.getMode());
            }
            if (this.currentWave != this.nexus.getCurrentWave()) {
                icrafting.func_71112_a((Container)this, 2, this.nexus.getCurrentWave());
            }
            if (this.nexusLevel != this.nexus.getNexusLevel()) {
                icrafting.func_71112_a((Container)this, 3, this.nexus.getNexusLevel());
            }
            if (this.nexusKills != this.nexus.getNexusKills()) {
                icrafting.func_71112_a((Container)this, 4, this.nexus.getNexusKills());
            }
            if (this.spawnRadius != this.nexus.getSpawnRadius()) {
                icrafting.func_71112_a((Container)this, 5, this.nexus.getSpawnRadius());
            }
            if (this.generation != this.nexus.getGeneration()) {
                icrafting.func_71112_a((Container)this, 6, this.nexus.getGeneration());
            }
            if (this.generation != this.nexus.getNexusPowerLevel()) {
                icrafting.func_71112_a((Container)this, 7, this.nexus.getNexusPowerLevel());
            }
            if (this.generation == this.nexus.getCookTime()) continue;
            icrafting.func_71112_a((Container)this, 9, this.nexus.getCookTime());
        }
        this.activationTimer = this.nexus.getActivationTimer();
        this.mode = this.nexus.getMode();
        this.currentWave = this.nexus.getCurrentWave();
        this.nexusLevel = this.nexus.getNexusLevel();
        this.nexusKills = this.nexus.getNexusKills();
        this.spawnRadius = this.nexus.getSpawnRadius();
        this.generation = this.nexus.getGeneration();
        this.powerLevel = this.nexus.getNexusPowerLevel();
        this.cookTime = this.nexus.getCookTime();
    }

    public void func_75137_b(int i, int j) {
        if (i == 0) {
            this.nexus.setActivationTimer(j);
        } else if (i == 1) {
            this.nexus.setMode(j);
        } else if (i == 2) {
            this.nexus.setWave(j);
        } else if (i == 3) {
            this.nexus.setNexusLevel(j);
        } else if (i == 4) {
            this.nexus.setNexusKills(j);
        } else if (i == 5) {
            this.nexus.setSpawnRadius(j);
        } else if (i == 6) {
            this.nexus.setGeneration(j);
        } else if (i == 7) {
            this.nexus.setNexusPowerLevel(j);
        } else if (i == 8) {
            this.nexus.setCookTime(j);
        }
    }

    public boolean func_75145_c(EntityPlayer entityplayer) {
        return this.nexus.func_70300_a(entityplayer);
    }

    public ItemStack func_82846_b(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.field_75151_b.get(i);
        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();
            itemstack = itemstack1.func_77946_l();
            if (i == 1 ? !this.func_75135_a(itemstack1, 2, 38, true) : (i >= 2 && i < 29 ? !this.func_75135_a(itemstack1, 29, 38, false) : (i >= 29 && i < 38 ? !this.func_75135_a(itemstack1, 2, 29, false) : !this.func_75135_a(itemstack1, 2, 38, false)))) {
                return null;
            }
            if (itemstack1.field_77994_a == 0) {
                slot.func_75215_d(null);
            } else {
                slot.func_75218_e();
            }
            if (itemstack1.field_77994_a != itemstack.field_77994_a) {
                slot.func_82870_a(player, itemstack1);
            } else {
                return null;
            }
        }
        return itemstack;
    }
}

