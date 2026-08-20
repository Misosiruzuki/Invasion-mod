/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package invmod.common.item;

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMGiantBird;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.EntityIMSkeleton;
import invmod.common.entity.EntityIMSpider;
import invmod.common.entity.EntityIMThrower;
import invmod.common.entity.EntityIMZombie;
import invmod.common.item.ItemIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebugWand
extends ItemIM {
    private TileEntityNexus nexus;

    public ItemDebugWand() {
        this.func_77656_e(0);
        this.func_77655_b("debugWand");
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        EntityIMMob entity;
        if (world.field_72995_K) {
            return false;
        }
        Block block = world.func_147439_a(x, y, z);
        if (block == mod_Invasion.blockNexus) {
            this.nexus = (TileEntityNexus)world.func_147438_o(x, y, z);
            return true;
        }
        EntityIMGiantBird bird = new EntityIMGiantBird(world);
        bird.func_70107_b(x, y + 1, z);
        EntityZombie zombie2 = new EntityZombie(world);
        zombie2.func_70107_b((double)x, (double)(y + 1), (double)z);
        EntityWolf wolf = new EntityWolf(world);
        wolf.func_70107_b((double)x, (double)(y + 1), (double)z);
        world.func_72838_d((Entity)wolf);
        EntityIMPigEngy entity1 = new EntityIMPigEngy(world);
        entity1.func_70107_b(x, y + 1, z);
        EntityIMZombie zombie = new EntityIMZombie(world, this.nexus);
        zombie.setTexture(0);
        zombie.setFlavour(0);
        zombie.setTier(1);
        zombie.func_70107_b(x, y + 1, z);
        if (this.nexus != null) {
            entity = new EntityIMPigEngy(world, this.nexus);
            entity.func_70107_b(x, y + 1, z);
            zombie = new EntityIMZombie(world, this.nexus);
            zombie.setTexture(0);
            zombie.setFlavour(0);
            zombie.setTier(2);
            zombie.func_70107_b(x, y + 1, z);
            EntityIMThrower thrower = new EntityIMThrower(world, this.nexus);
            thrower.func_70107_b(x, y + 1, z);
            EntityIMCreeper creep = new EntityIMCreeper(world, this.nexus);
            creep.func_70107_b(x, y + 1, z);
            EntityIMSpider spider = new EntityIMSpider(world, this.nexus);
            spider.setTexture(0);
            spider.setFlavour(0);
            spider.setTier(2);
            spider.func_70107_b(x, y + 1, z);
            EntityIMSkeleton skeleton = new EntityIMSkeleton(world, this.nexus);
            skeleton.func_70107_b(x, y + 1, z);
        }
        entity = new EntityIMSpider(world, this.nexus);
        ((EntityIMSpider)entity).setTexture(0);
        ((EntityIMSpider)entity).setFlavour(1);
        ((EntityIMSpider)entity).setTier(2);
        entity.func_70107_b(x, y + 1, z);
        EntityIMCreeper creep = new EntityIMCreeper(world);
        creep.func_70107_b(150.5, 64.0, 271.5);
        return true;
    }

    public boolean hitEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase targetEntity) {
        if (targetEntity instanceof EntityWolf) {
            EntityWolf wolf = (EntityWolf)targetEntity;
            if (player != null) {
                wolf.func_152115_b(player.getDisplayName());
            }
            return true;
        }
        return false;
    }

    public void addCreativeItems(ArrayList itemList) {
    }
}

