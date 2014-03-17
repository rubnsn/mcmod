package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.Config;
import ruby.bamboo.dimension.TelepoterBamboo;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDimensionCreater extends ItemSword {
    private float weaponDamage;

    public ItemDimensionCreater() {
        this(ToolMaterial.EMERALD);
    }

    public ItemDimensionCreater(ToolMaterial p_i45356_1_) {
        super(p_i45356_1_);
        this.setMaxDamage(3600);
        this.weaponDamage = 1F;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
        if (!par2World.isRemote) {
            int chargeFrame = this.getMaxItemUseDuration(par1ItemStack) - par4;
            if (chargeFrame > 10) {
                int newDim = par3EntityPlayer.dimension == 0 ? Config.dimensionId : 0;
                if (par3EntityPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) par3EntityPlayer).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) par3EntityPlayer, newDim, new TelepoterBamboo(((EntityPlayerMP) par3EntityPlayer).mcServer.worldServerForDimension(newDim)));
                }
            }
        }
    }

    @Override
    public float func_150931_i() {
        return this.weaponDamage;
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = HashMultimap.create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double) this.weaponDamage, 0));
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "dimensioncreater");
    }
}
