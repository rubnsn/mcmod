package mmm.littleMaidMob.net;

import net.minecraft.entity.Entity;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler implements
        IMessageHandler<ServerToClientMessage, ClientToServerMessage> {
    private static SimpleNetworkWrapper net;

    public static void init() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel("a");
    }

    public static void sendToAll(byte[] message) {
        ServerToClientMessage mes = new ServerToClientMessage();
        mes.message = message;
        net.sendToAll(mes);
    }

    //引数はローカルからのメッセージ
    @Override
    public ClientToServerMessage onMessage(ServerToClientMessage message, MessageContext ctx) {
        return null;
    }

    public static void sendToAllEClient(Entity entity, byte[] lba) {
    }
}
