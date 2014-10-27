package mmm.littleMaidMob.net;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/** サーバーからクライアント用 */
public class ServerToClientMessage implements IMessage {
    public byte[] message;

    @Override
    public void fromBytes(ByteBuf buf) {
        buf.writeBytes(message);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.readBytes(message);
    }

}
