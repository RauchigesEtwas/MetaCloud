package io.metacloud.spigot.networking;

import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.services.ServiceSendCommandPacket;
import io.metacloud.protocol.Packet;
import org.bukkit.Bukkit;

public class ManagerListener extends PacketListener {


    @PacketProvideHandler
    public void handelCommand(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ServiceSendCommandPacket){
            ServiceSendCommandPacket packet = new ServiceSendCommandPacket();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), packet.getCommand());
        }
    }
}
