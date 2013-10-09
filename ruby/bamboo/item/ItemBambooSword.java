package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemBambooSword extends ItemSword {
    private int weaponDamage;

    public ItemBambooSword(int par1) {
        super(par1, EnumToolMaterial.WOOD);
        this.setMaxDamage(200);
        weaponDamage = 3;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving) {
        if (!par2World.isRemote) {
            int exp = (int) Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6);

            if (exp >= 1) {
                exp = exp > 50 ? 50 : exp;
                par2World.spawnEntityInWorld(new EntityXPOrb(par2World, par4 + 0.5, par5 + 0.5, par6 + 0.5, exp));
            }
        }

        return super.onBlockDestroyed(par1ItemStack, par2World, par3, par4, par5, par6, par7EntityLiving);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par2EntityLivingBase), this.getDamageVsEntity(par2EntityLivingBase));
        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }

    public float getDamageVsEntity(Entity par1Entity) {
        float dmg = 4;

        if (par1Entity == null) {
            return dmg;
        }

        float dmgRate = 1F;

        // 敵の種類による攻撃力変動補正
        if (par1Entity instanceof EntityZombie) {
            dmgRate = 1.3F;
        } else if (par1Entity instanceof EntityAnimal) {
            dmgRate = 2F;
        } else if (par1Entity instanceof EntitySkeleton) {
            dmgRate = 2F;
        } else if (par1Entity instanceof EntityBlaze) {
            dmgRate = 2F;
        } else if (par1Entity instanceof EntityGolem) {
            dmgRate = 3F;
        }

        // 小数点切り上げ
        dmg = (int) Math.ceil(dmg * dmgRate + itemRand.nextFloat());

        // 無敵時間チェック
        if (!par1Entity.worldObj.isRemote && par1Entity instanceof EntityLivingBase) {
            if ((float) ((EntityLivingBase) par1Entity).hurtResistantTime <= ((EntityLivingBase) par1Entity).maxHurtResistantTime) {
                if (par1Entity instanceof EntityZombie) {
                    if (par1Entity instanceof EntityPigZombie) {
                        if (itemRand.nextInt(10) == 0) {
                            ItemStack is = reduceEquipment((EntityLiving) par1Entity);

                            if (is != null) {
                                par1Entity.entityDropItem(is, 0.5F);
                            }
                        }
                    } else {
                        if (itemRand.nextBoolean()) {
                            ItemStack is = reduceEquipment((EntityLiving) par1Entity);

                            if (is != null) {
                                par1Entity.entityDropItem(is, 0.5F);
                            }
                        }
                    }
                } else if (par1Entity instanceof EntitySkeleton) {
                    if (itemRand.nextInt(10) == 0) {
                        par1Entity.entityDropItem(new ItemStack(Item.dyePowder, 1, 15), 0.5F);
                    }

                    if (itemRand.nextBoolean()) {
                        ItemStack is = reduceEquipment((EntityLiving) par1Entity);

                        if (is != null) {
                            par1Entity.entityDropItem(is, 0.5F);
                        }
                    }
                } else if (par1Entity instanceof EntityCreeper) {
                    if (itemRand.nextInt(10) == 0) {
                        par1Entity.entityDropItem(new ItemStack(Item.gunpowder, 1), 0.5F);
                    }
                } else if (par1Entity instanceof EntityEnderman) {
                    ItemStack is = reduceEquipment((EntityLiving) par1Entity);

                    if (is != null) {
                        par1Entity.entityDropItem(is, 0.5F);
                    }
                } else if (par1Entity instanceof EntityBlaze) {
                    if (itemRand.nextInt(10) == 0) {
                        par1Entity.entityDropItem(new ItemStack(blazePowder), 0.0F);
                    }
                }
            }

            dmg += ((EntityLiving) par1Entity).getTotalArmorValue() / 2;
        }

        return dmg;
    }

    private ItemStack reduceEquipment(EntityLiving e) {
        ItemStack result = null;

        if (e instanceof EntityEnderman) {
            if (((EntityEnderman) e).getCarried() != 0) {
                result = new ItemStack(((EntityEnderman) e).getCarried(), 1, ((EntityEnderman) e).getCarryingData());
                ((EntityEnderman) e).setCarried(0);
                ((EntityEnderman) e).setCarryingData(0);
            }
        } else {
            for (int i = 0; i < e.getLastActiveItems().length; i++) {
                if (e.getLastActiveItems()[i] != null) {
                    if (e instanceof EntitySkeleton && e.getLastActiveItems()[i].getItem() instanceof ItemBow) {
                        continue;
                    }

                    result = e.getLastActiveItems()[i];
                    e.setCanPickUpLoot(false);
                    e.getLastActiveItems()[i] = null;
                    break;
                }
            }

            if (result != null && (result.getItem() instanceof ItemSword || result.getItem() instanceof ItemArmor)) {
                float dmg = 0;

                while ((dmg += 0.09F) < 0.99F && itemRand.nextBoolean())
                    ;

                dmg = 1 - dmg;
                result.setItemDamage(Math.abs((int) (result.getItem().getMaxDamage() * dmg)));
            }
        }

        return result;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("bamboosword");
    }
}
