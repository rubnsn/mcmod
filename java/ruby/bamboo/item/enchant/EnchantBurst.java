package ruby.bamboo.item.enchant;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EnchantBurst extends EnchantBase {

    public EnchantBurst(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        createExplosion(world, entity, posX, posY, posZ, enchantLvl * 2F);
    }

    private void createExplosion(World world, EntityLivingBase entity, int posX, int posY, int posZ, float f) {
        Explosion e = new Explosion(world, entity, posX, posY, posZ, f) {
            private int field_77289_h = 16;
            private World worldObj;

            public Explosion setWorld(World world) {
                this.worldObj = world;
                return this;
            }

            @Override
            public void doExplosionA() {
                float f = this.explosionSize;
                HashSet hashset = new HashSet();
                int i;
                int j;
                int k;
                double d5;
                double d6;
                double d7;

                for (i = 0; i < this.field_77289_h; ++i) {
                    for (j = 0; j < this.field_77289_h; ++j) {
                        for (k = 0; k < this.field_77289_h; ++k) {
                            if (i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1) {
                                double d0 = (double) ((float) i / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d1 = (double) ((float) j / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d2 = (double) ((float) k / ((float) this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 /= d3;
                                d1 /= d3;
                                d2 /= d3;
                                float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                                d5 = this.explosionX;
                                d6 = this.explosionY;
                                d7 = this.explosionZ;

                                for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                                    int j1 = MathHelper.floor_double(d5);
                                    int k1 = MathHelper.floor_double(d6);
                                    int l1 = MathHelper.floor_double(d7);
                                    Block block = this.worldObj.getBlock(j1, k1, l1);

                                    if (block.getMaterial() != Material.air) {
                                        float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.worldObj, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, worldObj, j1, k1, l1, explosionX, explosionY, explosionZ);
                                        f1 -= (f3 + 0.3F) * f2;
                                    }

                                    if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.worldObj, j1, k1, l1, block, f1))) {
                                        hashset.add(new ChunkPosition(j1, k1, l1));
                                    }

                                    d5 += d0 * (double) f2;
                                    d6 += d1 * (double) f2;
                                    d7 += d2 * (double) f2;
                                }
                            }
                        }
                    }
                }

                this.affectedBlockPositions.addAll(hashset);
            }
        }.setWorld(world);
        e.isSmoking = true;
        e.doExplosionA();
        e.doExplosionB(true);
    }
}
