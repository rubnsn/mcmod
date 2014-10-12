package ruby.bamboo.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemNinjaHelm extends ItemNinjaArmor {

    public ItemNinjaHelm(int slot) {
        super(slot);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.isInWater() && !world.isRemote) {
            for (int i = 0; i < 7; i++) {
                if (world.isAirBlock(player.getPlayerCoordinates().posX, player.getPlayerCoordinates().posY + i, player.getPlayerCoordinates().posZ)) {
                    player.setAir(300);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void playerDeadEvent(LivingDeathEvent lde) {
        if (lde.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) lde.entity;
            if (!player.worldObj.isRemote && isEqNinjaArmor(player, HELM) && isEqNinjaArmor(player, CHEST) && isEqNinjaArmor(player, LEG) && isEqNinjaArmor(player, BOOTS)) {
                player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 5.0F, true);
            }
        }
    }

    @SubscribeEvent
    public void playerAtackEvent(AttackEntityEvent ae) {
        if (isEqNinjaArmor(ae.entityPlayer, HELM)) {
            int[] butPotionIds = new int[] { Potion.poison.getId(), Potion.moveSlowdown.getId(), Potion.blindness.getId(), Potion.weakness.getId(), Potion.harm.getId() };
            int[] goodPotionIds = new int[] { Potion.damageBoost.getId(), Potion.moveSpeed.getId(), Potion.invisibility.getId(), Potion.regeneration.getId() };
            if (!ae.entityPlayer.worldObj.isRemote && ae.target instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) ae.target;
                ItemStack is = ae.entityPlayer.getEquipmentInSlot(0);
                if (is != null && is.getItem() instanceof ItemSword && entity.worldObj.rand.nextFloat() < 0.25F) {
                    if (!this.isEqNinjaArmor(ae.entityPlayer, CHEST) && entity.worldObj.rand.nextFloat() < 0.1F) {
                        entity.addPotionEffect(new PotionEffect(goodPotionIds[entity.worldObj.rand.nextInt(goodPotionIds.length)], 100, 10));
                        return;
                    }
                    entity.addPotionEffect(new PotionEffect(butPotionIds[entity.worldObj.rand.nextInt(butPotionIds.length)], 100, 20));
                }
            }
        }
    }
}
