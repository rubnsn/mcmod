package ruby.bamboo.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import ruby.bamboo.BambooCore;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKatana extends ItemSword {
    private final float weaponDamage;
    private final short tick = 0;
    
    //crafting event用
    private static boolean repair;

    public ItemKatana() {
        super(Item.ToolMaterial.IRON);
        this.setMaxDamage(150);
        weaponDamage = 0;
        this.setContainerItem(this);
    }
    
    /**
     * 刀の無限増殖対策をここに追加
     * @author defeatedcrow 2015.4.12
     */
    @SubscribeEvent
	public void onCraftingEvent(PlayerEvent.ItemCraftedEvent event) {
        repair = (event.crafting != null && event.crafting.getItem() instanceof ItemKatana);
    }
    
    @Override
    public boolean hasContainerItem()
    {
    	return !repair;
    }

    @Override
    public float func_150931_i() {
        return 0;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        itemStack.setItemDamage(itemStack.getItemDamage() + 1);
        return !repair ? itemStack : (ItemStack)null;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), getDamageVsEntity(entity));
        if (!entity.worldObj.isRemote) {
            if (entity instanceof EntityLivingBase) {
                if (entity.hurtResistantTime == ((EntityLivingBase) entity).maxHurtResistantTime) {
                    if (((EntityLivingBase) entity).getHealth() <= 0) {
                        ItemStack drop = KatanaDropManager.getRandomDrop(EntityList.getClassFromID(EntityList.getEntityID(entity)));
                        if (drop != null) {
                            entity.entityDropItem(drop, 0.0F);
                        }
                    }
                }
                /**
                 * hitEntityメソッドが呼ばれなくなっていそうなのでここで呼ぶことにした
                 * @author defeatedcrow 2015.4.12
                 */
                this.hitEntity(stack, player, (EntityLivingBase)entity);
            }
        }
        return true;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 4, 0));
        return multimap;
    }

    public float getDamageVsEntity(Entity par1Entity) {
        float dmg = 4;

        if (par1Entity == null) {
            return dmg;
        }

        float dmgRate = 1.5F;

        // 敵の種類による攻撃力変動補正
        if (par1Entity instanceof EntityZombie || par1Entity instanceof EntityCreeper || par1Entity instanceof EntitySpider) {
            dmgRate = 2F;
        } else if (par1Entity instanceof EntityAnimal) {
            dmgRate = 2F;
        } else if (par1Entity instanceof EntitySkeleton) {
            dmgRate = 0.5F;
        } else if (par1Entity instanceof EntitySlime) {
            dmgRate = 0;
        } else if (par1Entity instanceof EntityBlaze) {
            dmgRate = 0.7F;
        } else if (par1Entity instanceof EntityGolem) {
            dmgRate = 0.1F;
        }

        // 小数点切り上げ
        dmg = dmg * dmgRate + 1;
        return dmg;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "katana");
    }
}
