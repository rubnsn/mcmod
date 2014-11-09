package mmm.littleMaidMob.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import com.mojang.authlib.GameProfile;

public class EntityLittleMaidAvatar extends FakePlayer {
    private EntityLittleMaidBase owner;

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
}
