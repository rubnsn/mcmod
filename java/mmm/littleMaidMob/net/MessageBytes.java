package mmm.littleMaidMob.net;

import io.netty.buffer.ByteBuf;
import scala.actors.threadpool.Arrays;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/** サーバーからクライアント用 */
public class MessageBytes implements IMessage {
    public byte[] data;

    public MessageBytes() {
    }

    public MessageBytes(byte[] message) {
        this.data = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        //先頭にチャンネル識別用が入っているため。
        byte[] array = buf.array();
        data = Arrays.copyOfRange(array, 1, array.length);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBytes(data);
    }

}
