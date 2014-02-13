package ruby.bamboo;


public class NetworkHandler {
/*    @Override
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
        }
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
    }*/
}
