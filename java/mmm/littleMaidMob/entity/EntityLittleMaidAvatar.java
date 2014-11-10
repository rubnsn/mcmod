package mmm.littleMaidMob.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import com.mojang.authlib.GameProfile;

public class EntityLittleMaidAvatar extends FakePlayer {
    private EntityLittleMaidBase owner;
    private final ItemStack[] previousEquipment = new ItemStack[5];

    public EntityLittleMaidAvatar(WorldServer world, GameProfile name) {
        super(world, name);
    }

    public void setOwner(EntityLittleMaidBase maid) {
        this.owner = maid;
    }

    @Override
    public void onItemPickup(Entity entity, int p_71001_2_) {
        if (owner == null) {
            return;
        }
        if (!entity.isDead && !this.worldObj.isRemote) {
            EntityTracker entitytracker = ((WorldServer) this.worldObj).getEntityTracker();

            if (entity instanceof EntityItem) {
                entitytracker.func_151247_a(entity, new S0DPacketCollectItem(entity.getEntityId(), this.owner.getEntityId()));
            }

            if (entity instanceof EntityArrow) {
                entitytracker.func_151247_a(entity, new S0DPacketCollectItem(entity.getEntityId(), this.owner.getEntityId()));
            }

            if (entity instanceof EntityXPOrb) {
                entitytracker.func_151247_a(entity, new S0DPacketCollectItem(entity.getEntityId(), this.owner.getEntityId()));
            }
        }
    }

    @Override
    public void damageEntity(DamageSource par1DamageSource, float par2) {
        super.damageEntity(par1DamageSource, par2);
    }

    @Override
    public void onUpdate() {
        //装備の同期とAttributeによる能力変更
        for (int j = 0; j < 5; ++j) {
            ItemStack itemstack = this.previousEquipment[j];
            ItemStack itemstack1 = this.getEquipmentInSlot(j);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                ((WorldServer) this.worldObj).getEntityTracker().func_151247_a(this, new S04PacketEntityEquipment(this.getEntityId(), j, itemstack1));

                if (itemstack != null) {
                    this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers());
                }

                if (itemstack1 != null) {
                    this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers());
                }

                this.previousEquipment[j] = itemstack1 == null ? null : itemstack1.copy();
            }
        }
    }
}
