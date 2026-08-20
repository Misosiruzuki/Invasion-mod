/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  com.google.common.io.ByteArrayDataOutput
 *  cpw.mods.fml.common.registry.IEntityAdditionalSpawnData
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIMBolt
extends Entity
implements IEntityAdditionalSpawnData {
    private int age = 0;
    private int ticksToRender;
    private long timeCreated = this.lastVertexUpdate = System.currentTimeMillis();
    private double[][] vertices = new double[3][0];
    private long lastVertexUpdate;
    private float yaw;
    private float pitch;
    private double distance;
    private float widthVariance = 6.0f;
    private float vecX;
    private float vecY;
    private float vecZ;
    private int soundMade;

    public EntityIMBolt(World world) {
        super(world);
        this.field_70158_ak = true;
    }

    public EntityIMBolt(World world, double x, double y, double z) {
        this(world);
        this.func_70107_b(x, y, z);
    }

    public EntityIMBolt(World world, double x, double y, double z, double x2, double y2, double z2, int ticksToRender, int soundMade) {
        this(world, x, y, z);
        this.vecX = (float)(x2 - x);
        this.vecY = (float)(y2 - y);
        this.vecZ = (float)(z2 - z);
        this.ticksToRender = ticksToRender;
        this.soundMade = soundMade;
        this.setHeading(this.vecX, this.vecY, this.vecZ);
        this.doVertexUpdate();
    }

    public void writeSpawnData(ByteArrayDataOutput data) {
        data.writeShort((int)((short)this.ticksToRender));
        data.writeFloat((float)this.field_70165_t);
        data.writeFloat((float)this.field_70163_u);
        data.writeFloat((float)this.field_70161_v);
        data.writeFloat(this.vecX);
        data.writeFloat(this.vecY);
        data.writeFloat(this.vecZ);
        data.writeByte((int)((byte)this.soundMade));
    }

    public void readSpawnData(ByteArrayDataInput data) {
        this.ticksToRender = data.readShort();
        this.func_70107_b(data.readFloat(), data.readFloat(), data.readFloat());
        this.setHeading(data.readFloat(), data.readFloat(), data.readFloat());
        this.soundMade = data.readByte();
        this.doVertexUpdate();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        ++this.age;
        if (this.age == 1 && this.soundMade == 1) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:zap" + (this.field_70146_Z.nextInt(2) + Integer.valueOf(1)), 1.0f, 1.0f);
        }
        if (this.age > this.ticksToRender) {
            this.func_70106_y();
        }
    }

    public double[][] getVertices() {
        long time = System.currentTimeMillis();
        if (time - this.timeCreated > (long)(this.ticksToRender * 50)) {
            return null;
        }
        if (time - this.lastVertexUpdate >= 75L) {
            this.doVertexUpdate();
            while (this.lastVertexUpdate + 50L <= time) {
                this.lastVertexUpdate += 50L;
            }
        }
        return this.vertices;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void func_70103_a(byte byte0) {
        if (byte0 == 0) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:zap" + (this.field_70146_Z.nextInt(2) + Integer.valueOf(1)), 1.0f, 1.0f);
        } else if (byte0 == 1 || byte0 != 2) {
            // empty if block
        }
    }

    public void func_70088_a() {
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
    }

    private void setHeading(float x, float y, float z) {
        float xzSq = x * x + z * z;
        this.yaw = (float)(Math.atan2(x, z) * 180.0 / Math.PI) + 90.0f;
        this.pitch = (float)(Math.atan2(MathHelper.func_76133_a((double)xzSq), y) * 180.0 / Math.PI);
        this.distance = Math.sqrt(xzSq + y * y);
    }

    private void doVertexUpdate() {
        this.field_70170_p.field_72984_F.func_76320_a("IMBolt");
        this.widthVariance = 10.0f / (float)Math.log10(this.distance + 1.0);
        int numberOfVertexes = 60;
        if (numberOfVertexes != this.vertices[0].length) {
            this.vertices[0] = new double[numberOfVertexes];
            this.vertices[1] = new double[numberOfVertexes];
            this.vertices[2] = new double[numberOfVertexes];
        }
        for (int vertex = 0; vertex < numberOfVertexes; ++vertex) {
            this.vertices[1][vertex] = (double)vertex * this.distance / (double)(numberOfVertexes - 1);
        }
        this.createSegment(0, numberOfVertexes - 1);
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    private void createSegment(int begin, int end) {
        int points = end + 1 - begin;
        if (points <= 4) {
            if (points == 3) {
                this.createVertex(begin, begin + 1, end);
            } else {
                this.createVertex(begin, begin + 1, end);
                this.createVertex(begin, begin + 2, end);
            }
            return;
        }
        int midPoint = begin + points / 2;
        this.createVertex(begin, midPoint, end);
        this.createSegment(begin, midPoint);
        this.createSegment(midPoint, end);
    }

    private void createVertex(int begin, int mid, int end) {
        double difference = this.vertices[0][end] - this.vertices[0][begin];
        double yDiffToMid = this.vertices[1][mid] - this.vertices[1][begin];
        double yRatio = yDiffToMid / (this.vertices[1][end] - this.vertices[1][begin]);
        this.vertices[0][mid] = this.vertices[0][begin] + difference * yRatio + ((double)this.field_70170_p.field_73012_v.nextFloat() - 0.5) * yDiffToMid * (double)this.widthVariance;
        difference = this.vertices[2][end] - this.vertices[2][begin];
        this.vertices[2][mid] = this.vertices[2][begin] + difference * yRatio + ((double)this.field_70170_p.field_73012_v.nextFloat() - 0.5) * yDiffToMid * (double)this.widthVariance;
    }

    public void writeSpawnData(ByteBuf buffer) {
    }

    public void readSpawnData(ByteBuf additionalData) {
    }
}

