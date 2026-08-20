/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.util.DamageSource
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombiePigman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class EntityAISprinpigman
extends EntityAIBase {
    private EntityIMLiving theEntity;
    private int updateTimer;
    private int timer;
    private boolean isExecuting;
    private boolean isSprinting;
    private boolean isInWindup;
    private int missingTarget;
    private double lastX;
    private double lastY;
    private double lastZ;

    public EntityAISprinpigman(EntityIMLiving entity) {
        this.theEntity = entity;
        this.updateTimer = 0;
        this.timer = 0;
        this.isExecuting = true;
        this.isSprinting = false;
        this.isInWindup = false;
        this.missingTarget = 0;
    }

    public boolean func_75250_a() {
        if (--this.updateTimer <= 0) {
            this.updateTimer = 20;
            if (this.theEntity.func_70638_az() != null && this.theEntity.func_70685_l((Entity)this.theEntity.func_70638_az()) || this.isSprinting) {
                return true;
            }
            this.isExecuting = false;
            return false;
        }
        return this.isExecuting;
    }

    public void func_75249_e() {
        this.isExecuting = true;
        this.timer = 60;
    }

    public void func_75246_d() {
        if (this.isSprinting) {
            EntityLivingBase target = this.theEntity.func_70638_az();
            if (!this.theEntity.func_70051_ag() || target == null || this.missingTarget > 0 && ++this.missingTarget > 20) {
                this.endSprint();
                return;
            }
            double dZ = target.field_70161_v - this.theEntity.field_70161_v;
            double dX = target.field_70165_t - this.theEntity.field_70165_t;
            double dAngle = (Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0 - (double)this.theEntity.field_70177_z) % 360.0;
            if (dAngle > 60.0) {
                this.theEntity.setTurnRate(2.0f);
                this.missingTarget = 1;
            }
            if (this.theEntity.func_70092_e(this.lastX, this.lastY, this.lastZ) < 9.0E-4) {
                this.crash();
                return;
            }
            this.lastX = this.theEntity.field_70165_t;
            this.lastY = this.theEntity.field_70163_u;
            this.lastZ = this.theEntity.field_70161_v;
        }
        if (--this.timer <= 0) {
            if (!this.isInWindup) {
                if (!this.isSprinting) {
                    this.startSprint();
                } else {
                    this.endSprint();
                }
            } else {
                this.sprint();
            }
        }
    }

    protected void startSprint() {
        EntityIMZombiePigman pigman = (EntityIMZombiePigman)this.theEntity;
        pigman.updateAnimation(true);
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target == null || target.field_70121_D.field_72338_b - this.theEntity.field_70163_u >= 1.0) {
            return;
        }
        double dZ = target.field_70161_v - this.theEntity.field_70161_v;
        double dX = target.field_70165_t - this.theEntity.field_70165_t;
        double dAngle = (Math.atan2(dZ, dX) * 180.0 / Math.PI - 90.0 - (double)this.theEntity.field_70177_z) % 360.0;
        if (dAngle < 10.0) {
            this.isInWindup = true;
            this.timer = 20;
            this.theEntity.setMoveSpeedStat(0.0f);
        } else {
            this.timer = 10;
        }
    }

    protected void sprint() {
        this.isInWindup = false;
        this.isSprinting = true;
        this.missingTarget = 0;
        this.timer = 35;
        this.theEntity.resetMoveSpeed();
        this.theEntity.setMoveSpeedStat(this.theEntity.getMoveSpeedStat() * 2.3f);
        this.theEntity.func_70031_b(true);
        this.theEntity.setTurnRate(4.9f);
        this.theEntity.field_70724_aR = 0;
    }

    protected void endSprint() {
        EntityIMZombiePigman pigman = (EntityIMZombiePigman)this.theEntity;
        pigman.updateAnimation(true);
        this.isSprinting = false;
        this.timer = 180;
        this.theEntity.resetMoveSpeed();
        this.theEntity.setTurnRate(30.0f);
        this.theEntity.func_70031_b(false);
    }

    protected void crash() {
        this.theEntity.stunEntity(40);
        this.theEntity.func_70097_a(DamageSource.field_76377_j, 5.0f);
        this.theEntity.field_70170_p.func_72956_a((Entity)this.theEntity, "random.explode", 1.0f, 0.6f);
        this.endSprint();
    }
}

