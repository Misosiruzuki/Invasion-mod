/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 */
package invmod.common.util.spawneggs;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CustomTags {
    public static NBTTagCompound poweredCreeper() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74774_a("powered", (byte)1);
        return tag;
    }

    public static NBTTagCompound IMZombie_T1() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 0);
        tag.func_74768_a("tier", 1);
        return tag;
    }

    public static NBTTagCompound IMZombie_T2() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 0);
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMZombie_T2_tar() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 2);
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMZombie_T3() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 0);
        tag.func_74768_a("tier", 3);
        return tag;
    }

    public static NBTTagCompound IMSpider_T1_baby() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 1);
        tag.func_74768_a("tier", 1);
        return tag;
    }

    public static NBTTagCompound IMSpider_T2() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 0);
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMSpider_T2_mother() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 1);
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMThrower_T2() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMZombiePigman_T1() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 1);
        tag.func_74768_a("tier", 1);
        return tag;
    }

    public static NBTTagCompound IMZombiePigman_T2() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 1);
        tag.func_74768_a("tier", 2);
        return tag;
    }

    public static NBTTagCompound IMZombiePigman_T3() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("flavour", 1);
        tag.func_74768_a("tier", 3);
        return tag;
    }

    public static NBTTagCompound witherSkeleton() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74774_a("SkeletonType", (byte)1);
        NBTTagList list = new NBTTagList();
        NBTTagCompound swordItem = CustomTags.createItemTag((byte)1, (short)0, (short)272);
        list.func_74742_a((NBTBase)swordItem);
        for (int i = 0; i < 4; ++i) {
            list.func_74742_a((NBTBase)new NBTTagCompound());
        }
        tag.func_74782_a("Equipment", (NBTBase)list);
        return tag;
    }

    public static NBTTagCompound villagerZombie() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74774_a("IsVillager", (byte)1);
        return tag;
    }

    public static NBTTagCompound babyZombie() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74774_a("IsBaby", (byte)1);
        return tag;
    }

    public static NBTTagCompound horseType(int type) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74768_a("Type", type);
        return tag;
    }

    public static NBTTagCompound createItemTag(byte count, short damage, short id) {
        NBTTagCompound item = new NBTTagCompound();
        item.func_74774_a("Count", count);
        item.func_74777_a("Damage", damage);
        item.func_74777_a("id", id);
        return item;
    }

    public static NBTTagCompound getEntityTag(String entityID) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74778_a("id", entityID);
        return tag;
    }

    public static NBTTagCompound ridingTag(NBTTagCompound ridden) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.func_74782_a("Riding", (NBTBase)ridden);
        return tag;
    }

    public static NBTTagCompound spiderJockey(boolean wither) {
        NBTTagCompound skele = wither ? CustomTags.witherSkeleton() : new NBTTagCompound();
        skele.func_74782_a("Riding", (NBTBase)CustomTags.getEntityTag("Spider"));
        return skele;
    }

    public static NBTTagCompound chickenJockey(boolean villager) {
        NBTTagCompound zomb = CustomTags.babyZombie();
        if (villager) {
            zomb.func_74774_a("IsVillager", (byte)1);
        }
        zomb.func_74782_a("Riding", (NBTBase)CustomTags.getEntityTag("Chicken"));
        return zomb;
    }
}

