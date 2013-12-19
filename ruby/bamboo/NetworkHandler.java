package ruby.bamboo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import ruby.bamboo.item.ItemSoulMiller;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class NetworkHandler implements IPacketHandler, IConnectionHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("bamboo")) {
            try {
                Object[] temp = (Object[]) new ObjectInputStream(new ByteArrayInputStream(packet.data)).readObject();
                int i = 0;

                for (Object obj : temp) {
                    BambooCore.getConf().getClass().getFields()[i++].set(BambooCore.getConf(), obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            BambooCore.getConf().reSetExRecipes();
        } else if (packet.channel.equals("bamboo2")) {
            if (packet.data[0] == 0) {
                if (player instanceof EntityClientPlayerMP) {
                    ((EntityClientPlayerMP) player).addChatMessage("[bamboo] I hate offline mode server");
                }
            }
        }else if(packet.channel.equals("soulMiller")){
            try {
                ItemSoulMiller.createClientDummyEntity(new CompressedStreamTools().decompress(packet.data),(EntityClientPlayerMP)player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
        try {
            Object[] temp = new Object[BambooCore.getConf().getClass().getFields().length];
            int i = 0;

            for (Field field : Config.class.getFields()) {
                temp[i++] = field.get(BambooCore.getConf());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(temp);
            manager.addToSendQueue(new Packet250CustomPayload("bamboo", baos.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }/*
          * 
          * if(FMLCommonHandler.instance().getMinecraftServerInstance().
          * isDedicatedServer()){ manager.addToSendQueue(new
          * Packet250CustomPayload("bamboo2", new byte[]{(byte)
          * (FMLCommonHandler
          * .instance().getMinecraftServerInstance().isServerInOnlineMode
          * ()?1:0)})); }
          */
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
        return "";
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
    }

    @Override
    public void connectionClosed(INetworkManager manager) {
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
    }
}
