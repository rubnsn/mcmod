package ruby.bamboo.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class ItemKatana extends ItemSword
{
    private float weaponDamage;
    private short tick = 0;
    public ItemKatana(int par1)
    {
        super(par1, EnumToolMaterial.IRON);
        this.setMaxDamage(150);
        weaponDamage = 1;
        this.setCreativeTab(BambooCore.tabBamboo);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par2EntityLivingBase), getDamageVsEntity(par2EntityLivingBase));
        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }

    @Override
    public Multimap func_111205_h()
    {
        Multimap multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(field_111210_e, "Weapon modifier", 1, 0));
        return multimap;
    }

    public float getDamageVsEntity(Entity par1Entity)
    {
        float dmg = 4;

        if (par1Entity == null)
        {
            return dmg;
        }

        float dmgRate = 1.5F;

        //敵の種類による攻撃力変動補正
        if (par1Entity instanceof EntityZombie || par1Entity instanceof EntityCreeper || par1Entity instanceof EntitySpider)
        {
            dmgRate = 2F;
        }
        else if (par1Entity instanceof EntityAnimal)
        {
            dmgRate = 2F;
        }
        else if (par1Entity instanceof EntitySkeleton)
        {
            dmgRate = 0.5F;
        }
        else if (par1Entity instanceof EntitySlime)
        {
            dmgRate = 0;
        }
        else if (par1Entity instanceof EntityBlaze)
        {
            dmgRate = 0.7F;
        }
        else if (par1Entity instanceof EntityGolem)
        {
            dmgRate = 0.1F;
        }

        //小数点切り上げ
        dmg = dmg * dmgRate + 1;

        //無敵時間チェック
        if (!par1Entity.worldObj.isRemote && par1Entity instanceof EntityLivingBase)
        {
            if ((float)((EntityLivingBase)par1Entity).hurtResistantTime <= ((EntityLivingBase)par1Entity).maxHurtResistantTime)
            {
                float entityHp = ((EntityLivingBase) par1Entity).func_110143_aJ() - dmg;

                if (entityHp <= 0 && entityHp > -dmg)
                {
                    //ボーナスドロップ
                    boolean isRareLottery = itemRand.nextInt(500) == 0;

                    if (par1Entity instanceof EntityZombie)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(leather), 0.0F);
                        }

                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(bone, itemRand.nextInt(2) + 1), 0.0F);
                        }

                        if (isRareLottery)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 2), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntitySkeleton)
                    {
                        if (isRareLottery && ((EntitySkeleton) par1Entity).getSkeletonType() == 1)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 1), 0.0F);
                        }
                        else if (isRareLottery && ((EntitySkeleton) par1Entity).getSkeletonType() == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 0), 0.0F);
                        }

                        if (isRareLottery)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 0), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityCreeper)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.gunpowder, 2), 0.0F);
                        }

                        if (itemRand.nextInt(100) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.tnt), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(BambooInit.firecrackerIID, 1, 0), 0.0F);
                        }

                        if (itemRand.nextInt(20) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(BambooInit.firecrackerIID, 1, 0), 0.0F);
                        }

                        if (itemRand.nextInt(200) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(BambooInit.firecrackerIID, 1, 0), 0.0F);
                        }

                        if (isRareLottery)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 4), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntitySpider)
                    {
                        if (itemRand.nextInt(50) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.web), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityGhast)
                    {
                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.fireballCharge, itemRand.nextInt(2) + 1), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.tnt), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.glowstone), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityEnderman)
                    {
                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.plantRed), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.plantYellow), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.mushroomBrown), 0.0F);
                        }

                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.mushroomRed), 0.0F);
                        }

                        if (itemRand.nextInt(20) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(eyeOfEnder), 0.0F);
                        }

                        if (itemRand.nextInt(20) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(BambooInit.bambooIID, 1, 0), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntitySilverfish)
                    {
                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(paper, itemRand.nextInt(2) + 1), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityBlaze)
                    {
                        if (itemRand.nextInt(10) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(fireballCharge), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityPig)
                    {
                        if (itemRand.nextInt(4) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(leather, itemRand.nextInt(2) + 1), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntitySheep)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(silk, itemRand.nextInt(2) + 1), 0.0F);
                        }

                        if (itemRand.nextInt(10) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.tallGrass, 1, 1), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityBat)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(appleRed), 0.0F);
                        }

                        if (itemRand.nextInt(100) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(appleGold), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityWitch)
                    {
                        if (itemRand.nextInt(2) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.waterlily), 0.0F);
                        }

                        if (isRareLottery)
                        {
                            par1Entity.entityDropItem(new ItemStack(Item.skull.itemID, 1, 3), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityChicken)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(seeds, itemRand.nextInt(2) + 1), 0.0F);
                        }

                        if (itemRand.nextInt(4) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(leather, itemRand.nextInt(2) + 1), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityCow)
                    {
                        if (itemRand.nextInt(5) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(Block.tallGrass, 1, 1), 0.0F);
                        }
                    }
                    else if (par1Entity instanceof EntityWolf)
                    {
                        if (!((EntityWolf) par1Entity).isTamed())
                        {
                            if (itemRand.nextInt(5) == 0)
                            {
                                par1Entity.entityDropItem(new ItemStack(bone), 0.0F);
                            }

                            if (itemRand.nextInt(5) == 0)
                            {
                                par1Entity.entityDropItem(new ItemStack(beefRaw), 0.0F);
                            }

                            if (itemRand.nextInt(5) == 0)
                            {
                                par1Entity.entityDropItem(new ItemStack(porkRaw), 0.0F);
                            }

                            if (itemRand.nextInt(5) == 0)
                            {
                                par1Entity.entityDropItem(new ItemStack(chickenRaw), 0.0F);
                            }

                            if (itemRand.nextInt(5) == 0)
                            {
                                par1Entity.entityDropItem(new ItemStack(feather), 0.0F);
                            }
                        }
                    }
                    else if (par1Entity instanceof EntityOcelot)
                    {
                        if (itemRand.nextInt(3) == 0)
                        {
                            par1Entity.entityDropItem(new ItemStack(fishRaw, itemRand.nextInt(2) + 1), 0.0F);
                        }
                    }
                }
            }
        }

        return dmg;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("katana");
    }
}
