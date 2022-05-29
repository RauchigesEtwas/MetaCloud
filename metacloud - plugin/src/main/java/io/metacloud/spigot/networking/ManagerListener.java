package io.metacloud.spigot.networking;

import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.apidriver.out.ApiSendCloudPlayerDataPacket;
import io.metacloud.network.packets.services.in.ServiceSendCommandPacket;
import io.metacloud.protocol.Packet;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ManagerListener extends PacketListener {



    @PacketProvideHandler
    public void handleCommands(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();

        Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> player.sendMessage("test"));

        if (readPacket instanceof ServiceSendCommandPacket){
            ServiceSendCommandPacket packet = (ServiceSendCommandPacket)readPacket;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), packet.getCommand());
        }

        if (readPacket instanceof ApiSendCloudPlayerDataPacket){
            ApiSendCloudPlayerDataPacket packet = (ApiSendCloudPlayerDataPacket)readPacket;
            if (packet.getChosen().equalsIgnoreCase("SEND_TITLE")){
                Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> {
                    if (player.getName().equalsIgnoreCase(player.getName())){
                        player.sendTitle(packet.getTitle(), packet.getSubTitle(), packet.getFadeIn(), packet.getStay(), packet.getFadeOut());
                    }
                });
            }

            if (packet.getChosen().equalsIgnoreCase("SEND_ACTIONBAR")){
                Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> {
                    if (player.getName().equalsIgnoreCase(player.getName())){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(packet.getMessage()));
                    }
                });
            }
        }

    }


}
