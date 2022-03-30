package io.metacloud.bungeecord.networking;

import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.services.ServiceSendCommandPacket;
import io.metacloud.protocol.Packet;
import net.md_5.bungee.api.ProxyServer;

public class ManagerListener extends PacketListener{


    @PacketProvideHandler
    public void handelCommand(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();

        if (readPacket instanceof ServiceSendCommandPacket){
            ServiceSendCommandPacket packet = new ServiceSendCommandPacket();
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), packet.getCommand());
        }
    }

}
