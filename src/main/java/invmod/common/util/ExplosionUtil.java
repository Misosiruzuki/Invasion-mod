/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.ChunkPosition
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package invmod.common.util;

import invmod.common.mod_Invasion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionUtil {
    public static void doExplosionB(World world, Explosion explosion, boolean p_77279_1_) {
        Block block;
        int k;
        int j;
        int i;
        world.func_72908_a(explosion.field_77284_b, explosion.field_77285_c, explosion.field_77282_d, "random.explode", 4.0f, (1.0f + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.2f) * 0.7f);
        if (explosion.field_77280_f >= 2.0f && explosion.field_82755_b) {
            world.func_72869_a("hugeexplosion", explosion.field_77284_b, explosion.field_77285_c, explosion.field_77282_d, 1.0, 0.0, 0.0);
        } else {
            world.func_72869_a("largeexplode", explosion.field_77284_b, explosion.field_77285_c, explosion.field_77282_d, 1.0, 0.0, 0.0);
        }
        if (explosion.field_82755_b) {
            for (ChunkPosition chunkposition : explosion.field_77281_g) {
                i = chunkposition.field_151329_a;
                j = chunkposition.field_151327_b;
                k = chunkposition.field_151328_c;
                block = world.func_147439_a(i, j, k);
                if (p_77279_1_) {
                    double d0 = (float)i + world.field_73012_v.nextFloat();
                    double d1 = (float)j + world.field_73012_v.nextFloat();
                    double d2 = (float)k + world.field_73012_v.nextFloat();
                    double d3 = d0 - explosion.field_77284_b;
                    double d4 = d1 - explosion.field_77285_c;
                    double d5 = d2 - explosion.field_77282_d;
                    double d6 = MathHelper.func_76133_a((double)(d3 * d3 + d4 * d4 + d5 * d5));
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5 / (d6 / (double)explosion.field_77280_f + 0.1);
                    world.func_72869_a("explode", (d0 + explosion.field_77284_b * 1.0) / 2.0, (d1 + explosion.field_77285_c * 1.0) / 2.0, (d2 + explosion.field_77282_d * 1.0) / 2.0, d3 *= (d7 *= (double)(world.field_73012_v.nextFloat() * world.field_73012_v.nextFloat() + 0.3f)), d4 *= d7, d5 *= d7);
                    world.func_72869_a("smoke", d0, d1, d2, d3, d4, d5);
                }
                if (world.field_72995_K || block.func_149688_o() == Material.field_151579_a) continue;
                if (block.func_149659_a(explosion)) {
                    if (mod_Invasion.getDestructedBlocksDrop()) {
                        block.func_149690_a(world, i, j, k, world.func_72805_g(i, j, k), 1.0f / explosion.field_77280_f, 0);
                    } else {
                        block.func_149690_a(world, i, j, k, world.func_72805_g(i, j, k), 0.0f, 0);
                    }
                }
                block.onBlockExploded(world, i, j, k, explosion);
            }
        }
        if (explosion.field_77286_a) {
            for (ChunkPosition chunkposition : explosion.field_77281_g) {
                i = chunkposition.field_151329_a;
                j = chunkposition.field_151327_b;
                k = chunkposition.field_151328_c;
                block = world.func_147439_a(i, j, k);
                Block block1 = world.func_147439_a(i, j - 1, k);
                if (block.func_149688_o() != Material.field_151579_a || !block1.func_149730_j() || world.field_73012_v.nextInt(3) != 0) continue;
                world.func_147449_b(i, j, k, (Block)Blocks.field_150480_ab);
            }
        }
    }
}

