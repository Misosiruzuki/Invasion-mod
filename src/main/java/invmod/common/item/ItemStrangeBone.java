/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 */
package invmod.common.item;

import invmod.common.entity.EntityIMWolf;
import invmod.common.item.ItemIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class ItemStrangeBone
extends ItemIM {
    public ItemStrangeBone() {
        this.func_77655_b("strangeBone");
    }

    public int getDamage(ItemStack stack) {
        return 0;
    }

    public boolean func_111207_a(ItemStack itemStack, EntityPlayer player, EntityLivingBase targetEntity) {
        if (!targetEntity.field_70170_p.field_72995_K && targetEntity instanceof EntityWolf && !(targetEntity instanceof EntityIMWolf)) {
            EntityWolf wolf = (EntityWolf)targetEntity;
            if (wolf.func_70909_n()) {
                TileEntityNexus nexus = null;
                int x = MathHelper.func_76128_c((double)wolf.field_70165_t);
                int y = MathHelper.func_76128_c((double)wolf.field_70163_u);
                int z = MathHelper.func_76128_c((double)wolf.field_70161_v);
                for (int i = -7; i < 8; ++i) {
                    block1: for (int j = -4; j < 5; ++j) {
                        for (int k = -7; k < 8; ++k) {
                            if (wolf.field_70170_p.func_147439_a(x + i, y + j, z + k) != mod_Invasion.blockNexus) continue;
                            nexus = (TileEntityNexus)wolf.field_70170_p.func_147438_o(x + i, y + j, z + k);
                            continue block1;
                        }
                    }
                }
                if (nexus != null) {
                    EntityIMWolf newWolf = new EntityIMWolf(wolf, nexus);
                    wolf.field_70170_p.func_72838_d((Entity)newWolf);
                    wolf.func_70106_y();
                    --itemStack.field_77994_a;
                } else {
                    player.func_145747_a((IChatComponent)new ChatComponentText("The wolf doesn't like this strange bone."));
                }
            }
            return true;
        }
        return false;
    }
}

