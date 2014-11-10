package mmm.littleMaidMob.net;

import mmm.littleMaidMob.Statics;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.mode.IFF;
import mmm.littleMaidMob.sound.EnumSound;
import mmm.util.MMM_Helper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler implements
        IMessageHandler<MessageBytes, MessageBytes> {
    private static SimpleNetworkWrapper net;

    public static void init() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel("lmmentitypacket");
        net.registerMessage(NetworkHandler.class, MessageBytes.class, 0, Side.CLIENT);
    }

    public static void sendToAll(byte[] message) {
        MessageBytes mes = new MessageBytes(message);
        net.sendToAll(mes);
    }

    //戻り値はローカルからのレスポンス
    @Override
    public MessageBytes onMessage(MessageBytes message, MessageContext ctx) {
        // クライアント側の特殊パケット受信動作
        byte lmode = message.data[0];
        int leid = 0;
        EntityLittleMaidBase lemaid = null;
        if ((lmode & 0x80) != 0) {
            leid = MMM_Helper.getInt(message.data, 1);
            lemaid = getLittleMaid(message.data, 1, MMM_Helper.mc.theWorld);
            if (lemaid == null)
                return null;
        }
        littleMaidMob.Debug(String.format("LMM|Upd Clt Call[%2x:%d].", lmode, leid));

        switch (lmode) {
        case Statics.LMN_Client_SwingArm:
            // 腕振り
            byte larm = message.data[5];
            EnumSound lsound = EnumSound.getEnumSound(MMM_Helper.getInt(message.data, 6));
            lemaid.swingController.setSwinging(larm, lsound);
            //          mod_LMM_littleMaidMob.Debug(String.format("SwingSound:%s", lsound.name()));
            break;

        case Statics.LMN_Client_SetIFFValue:
            // IFFの設定値を受信
            int lval = message.data[1];
            int lindex = MMM_Helper.getInt(message.data, 2);
            String lname = (String) IFF.DefaultIFF.keySet().toArray()[lindex];
            littleMaidMob.Debug("setIFF-CL %s(%d)=%d", lname, lindex, lval);
            IFF.setIFFValue(null, lname, lval);
            break;

        case Statics.LMN_Client_PlaySound:
            // 音声再生
            EnumSound lsound9 = EnumSound.getEnumSound(MMM_Helper.getInt(message.data, 5));
            lemaid.playLittleMaidSound(lsound9, true);
            littleMaidMob.Debug(String.format("playSound:%s", lsound9.name()));
            break;

        }
        return null;
    }

    public static void sendToAllEClient(Entity entity, byte[] lba) {
        MMM_Helper.setInt(lba, 1, entity.getEntityId());
        net.sendToDimension(new MessageBytes(lba), entity.dimension);
    }

    public static EntityLittleMaidBase getLittleMaid(byte[] pData, int pIndex, World pWorld) {
        Entity lentity = MMM_Helper.getEntity(pData, pIndex, pWorld);
        if (lentity instanceof EntityLittleMaidBase) {
            return (EntityLittleMaidBase) lentity;
        } else {
            return null;
        }
    }
}
