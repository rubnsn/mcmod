package ruby.bamboo.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import ruby.bamboo.BambooInit;

public class WorldGenSakura extends WorldGenAbstractTree {
    int type;

    public WorldGenSakura(boolean par1) {
        this(par1, 15);
    }

    public WorldGenSakura(boolean par1, int dmg) {
        super(par1);
        type = dmg;
    }

    @Override
    public boolean generate(World world, Random random, int posX, int posY, int posZ) {
        int l;
        boolean flag;

        l = random.nextInt(3) + 4;
        flag = true;

        if (posY < 1 || posY + l + 1 > 128) {
            return false;
        }

        for (int i1 = posY; i1 <= posY + 1 + l; i1++) {
            byte byte0 = 1;

            if (i1 == posY) {
                byte0 = 0;
            }

            if (i1 >= (posY + 1 + l) - 2) {
                byte0 = 2;
            }

            for (int i2 = posX - byte0; i2 <= posX + byte0 && flag; i2++) {
                for (int l2 = posZ - byte0; l2 <= posZ + byte0 && flag; l2++) {
                    if (i1 >= 0) {
                        world.getClass();

                        if (i1 < 128) {
                            Block j3 = world.getBlock(i2, i1, l2);

                            if (j3 != Blocks.air && j3 != BambooInit.sakuraleavs) {
                                flag = false;
                            }

                            continue;
                        }
                    }

                    flag = false;
                }
            }
        }

        if (!flag) {
            return false;
        }

        Block j1 = world.getBlock(posX, posY - 1, posZ);

        if (!(j1 == Blocks.grass || j1 == Blocks.dirt)) {
            if (posY >= 128 - l - 1) {
                return false;
            }
        }

        world.setBlock(posX, posY - 1, posZ, Blocks.dirt, 0, 3);

        for (int k1 = (posY - 3) + l; k1 <= posY + l; k1++) {
            int j2 = k1 - (posY + l);
            int i3 = 1 - j2 / 2;

            for (int k3 = posX - i3; k3 <= posX + i3; k3++) {
                int l3 = k3 - posX;

                for (int i4 = posZ - i3; i4 <= posZ + i3; i4++) {
                    int j4 = i4 - posZ;

                    if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && !world.getBlock(k3, k1, i4).isOpaqueCube()) {
                        world.setBlock(k3, k1, i4, BambooInit.sakuraleavs, type, 3);
                    }
                }
            }
        }

        for (int l1 = 0; l1 < l; l1++) {
            Block k2 = world.getBlock(posX, posY + l1, posZ);

            if (k2 == Blocks.air || k2 == BambooInit.sakuraleavs) {
                world.setBlock(posX, posY + l1, posZ, BambooInit.sakuralog, 0, 3);
            }
        }

        return true;
    }
}
