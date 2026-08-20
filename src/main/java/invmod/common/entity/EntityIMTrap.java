/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.mod_Invasion;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIMTrap
extends Entity {
    public static final int TRAP_DEFAULT = 0;
    public static final int TRAP_RIFT = 1;
    public static final int TRAP_FIRE = 2;
    private static final int ARM_TIME = 60;
    private static final int META_CHANGED = 29;
    private static final int META_TYPE = 30;
    private static final int META_EMPTY = 31;
    private int trapType;
    private int ticks;
    private boolean isEmpty;
    private byte metaChanged;
    private boolean fromLoaded;

    public EntityIMTrap(World world) {
        super(world);
        this.func_70105_a(0.5f, 0.28f);
        this.field_70129_M = 0.0f;
        this.ticks = 0;
        this.isEmpty = false;
        this.field_70178_ae = true;
        this.trapType = 0;
        this.metaChanged = world.field_72995_K ? (byte)1 : 0;
        this.field_70180_af.func_75682_a(29, (Object)this.metaChanged);
        this.field_70180_af.func_75682_a(30, (Object)this.trapType);
        this.field_70180_af.func_75682_a(31, (Object)((byte)(!this.isEmpty ? 1 : 0)));
    }

    public EntityIMTrap(World world, double x, double y, double z) {
        this(world, x, y, z, 0);
    }

    public EntityIMTrap(World world, double x, double y, double z, int trapType) {
        this(world);
        this.trapType = trapType;
        this.field_70180_af.func_75692_b(30, (Object)trapType);
        this.func_70012_b(x, y, z, 0.0f, 0.0f);
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        ++this.ticks;
        if (this.field_70170_p.field_72995_K) {
            if (this.metaChanged != this.field_70180_af.func_75683_a(29) || this.ticks % 20 == 0) {
                this.metaChanged = this.field_70180_af.func_75683_a(29);
                this.trapType = this.field_70180_af.func_75679_c(30);
                boolean wasEmpty = this.isEmpty;
                boolean bl = this.isEmpty = this.field_70180_af.func_75683_a(31) == 0;
                if (this.isEmpty && !wasEmpty && this.trapType == 1) {
                    this.doRiftParticles();
                }
            }
            return;
        }
        if (!this.isValidPlacement()) {
            EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(mod_Invasion.itemIMTrap, 1, 0));
            entityitem.field_145804_b = 10;
            this.field_70170_p.func_72838_d((Entity)entityitem);
            this.func_70106_y();
        }
        if (this.field_70170_p.field_72995_K || !this.isEmpty && this.ticks < 60) {
            return;
        }
        List entities = this.field_70170_p.func_72872_a(EntityLivingBase.class, this.field_70121_D);
        if (entities.size() > 0 && !this.isEmpty) {
            for (EntityLivingBase entity : entities) {
                if (!this.trapEffect(entity)) continue;
                this.setEmpty();
                return;
            }
        }
    }

    public boolean trapEffect(EntityLivingBase triggerEntity) {
        if (this.trapType == 0) {
            triggerEntity.func_70097_a(DamageSource.field_76377_j, 4.0f);
        } else if (this.trapType == 1) {
            triggerEntity.func_70097_a(DamageSource.field_76376_m, triggerEntity instanceof EntityPlayer ? 12.0f : 38.0f);
            List entities = this.field_70170_p.func_72839_b((Entity)this, this.field_70121_D.func_72314_b((double)1.9f, 1.0, (double)1.9f));
            for (Entity entity : entities) {
                entity.func_70097_a(DamageSource.field_76376_m, 8.0f);
                if (!(entity instanceof EntityIMLiving)) continue;
                ((EntityIMLiving)entity).stunEntity(60);
            }
            this.field_70170_p.func_72956_a((Entity)this, "random.break", 1.5f, 1.0f * (this.field_70146_Z.nextFloat() * 0.25f + 0.55f));
        } else if (this.trapType == 2) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:fireball1", 1.5f, 1.15f / (this.field_70146_Z.nextFloat() * 0.3f + 1.0f));
            this.doFireball(1.1f, 8);
        }
        return true;
    }

    public void func_70100_b_(EntityPlayer entityPlayer) {
        if (!this.field_70170_p.field_72995_K && this.ticks > 30 && this.isEmpty && entityPlayer.field_71071_by.func_70441_a(new ItemStack(mod_Invasion.itemIMTrap, 1, 0))) {
            this.field_70170_p.func_72956_a((Entity)this, "random.pop", 0.2f, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityPlayer.func_71001_a((Entity)this, 1);
            this.func_70106_y();
        }
    }

    public boolean func_130002_c(EntityPlayer entityPlayer) {
        if (this.field_70170_p.field_72995_K || this.isEmpty) {
            return false;
        }
        ItemStack curItem = entityPlayer.field_71071_by.func_70448_g();
        if (curItem != null && curItem.func_77973_b() == mod_Invasion.itemProbe && curItem.func_77960_j() >= 1) {
            EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, new ItemStack(mod_Invasion.itemIMTrap, 1, this.trapType));
            entityitem.field_145804_b = 5;
            this.field_70170_p.func_72838_d((Entity)entityitem);
            this.func_70106_y();
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public int getTrapType() {
        return this.trapType;
    }

    public boolean isValidPlacement() {
        return this.field_70170_p.func_147445_c(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)this.field_70163_u) - 1, MathHelper.func_76128_c((double)this.field_70161_v), true) && this.field_70170_p.func_72872_a(EntityIMTrap.class, this.field_70121_D).size() < 2;
    }

    public boolean func_70067_L() {
        return true;
    }

    public void func_70088_a() {
    }

    public float func_70053_R() {
        return 0.0f;
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        this.isEmpty = nbttagcompound.func_74767_n("isEmpty");
        this.trapType = nbttagcompound.func_74762_e("type");
        this.field_70180_af.func_75692_b(31, (Object)((byte)(!this.isEmpty ? 1 : 0)));
        this.field_70180_af.func_75692_b(30, (Object)this.trapType);
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74757_a("isEmpty", this.isEmpty);
        nbttagcompound.func_74768_a("type", this.trapType);
    }

    private void setEmpty() {
        this.isEmpty = true;
        this.ticks = 0;
        this.field_70180_af.func_75692_b(31, (Object)((byte)(!this.isEmpty ? 1 : 0)));
        this.field_70180_af.func_75692_b(29, (Object)((byte)(this.field_70180_af.func_75683_a(29) == 0 ? 1 : 0)));
    }

    private void doFireball(float size, int initialDamage) {
        int x = MathHelper.func_76128_c((double)this.field_70165_t);
        int y = MathHelper.func_76128_c((double)this.field_70163_u);
        int z = MathHelper.func_76128_c((double)this.field_70161_v);
        int min = 0 - (int)size;
        int max = 0 + (int)size;
        for (int i = min; i <= max; ++i) {
            for (int j = min; j <= max; ++j) {
                for (int k = min; k <= max; ++k) {
                    if (this.field_70170_p.func_147439_a(x + i, y + j, z + k) != Blocks.field_150350_a && !this.field_70170_p.func_147439_a(x + i, y + j, z + k).func_149688_o().func_76217_h()) continue;
                    this.field_70170_p.func_147449_b(x + i, y + j, z + k, (Block)Blocks.field_150480_ab);
                }
            }
        }
        List entities = this.field_70170_p.func_72839_b((Entity)this, this.field_70121_D.func_72314_b((double)size, (double)size, (double)size));
        for (Entity entity : entities) {
            entity.func_70015_d(8);
            entity.func_70097_a(DamageSource.field_76370_b, (float)initialDamage);
        }
    }

    private void doRiftParticles() {
        for (int i = 0; i < 300; ++i) {
            float x = this.field_70146_Z.nextFloat() * 6.0f - 3.0f;
            float z = this.field_70146_Z.nextFloat() * 6.0f - 3.0f;
            this.field_70170_p.func_72869_a("portal", this.field_70165_t + (double)x, this.field_70163_u + 2.0, this.field_70161_v + (double)z, (double)(-x / 3.0f), -2.0, (double)(-z / 3.0f));
        }
    }
}

