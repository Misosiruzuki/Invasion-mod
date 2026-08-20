/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package invmod.common;

import net.minecraft.entity.Entity;

public interface SparrowAPI {
    public boolean isStupidToAttack();

    public boolean doNotVaporize();

    public boolean isPredator();

    public boolean isHostile();

    public boolean isPeaceful();

    public boolean isPrey();

    public boolean isNeutral();

    public boolean isUnkillable();

    public boolean isThreatTo(Entity var1);

    public boolean isFriendOf(Entity var1);

    public boolean isNPC();

    public int isPet();

    public Entity getPetOwner();

    public String getName();

    public Entity getAttackingTarget();

    public float getSize();

    public String getSpecies();

    public int getTier();

    public int getGender();

    public String customStringAndResponse(String var1);

    public String getSimplyID();
}

