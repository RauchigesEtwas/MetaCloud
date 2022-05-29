package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.apidriver.out.*;
import io.metacloud.protocol.Packet;

import java.util.function.Consumer;

public class ApiHandleListener extends PacketListener {


    @PacketProvideHandler
    public void handleDataPackets(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ApiSendCloudPlayerDataPacket){
            ApiSendCloudPlayerDataPacket packet = (ApiSendCloudPlayerDataPacket) readPacket;
            if (packet.getChosen().equalsIgnoreCase("SEND_ACTIONBAR") || packet.getChosen().equalsIgnoreCase("SEND_TITLE") ){
                Driver.getInstance().getConnectionDriver().getAllNonProxiedChannel().forEach(channel -> {
                    channel.sendPacket(packet);
                });
            }else {
                Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel -> {
                    channel.sendPacket(packet);
                });
            }
        }
    }


    @PacketProvideHandler
    public void handleMessage(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ApiSendMessage){
            ApiSendMessage packet = (ApiSendMessage)readPacket;
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.valueOf(packet.getType()), packet.getMessage());
        }
        if (readPacket instanceof ApiSendCommand){
            ApiSendCommand packet = (ApiSendCommand) readPacket;
            Driver.getInstance().getConsoleDriver().getCommandDriver().executeCommand(packet.getCommand());

        }
        if (readPacket instanceof ApiStartServicePacket){
            ApiStartServicePacket packet = (ApiStartServicePacket) readPacket;

            if (Driver.getInstance().getGroupDriver().getGroup(packet.getGroup()) != null){
                Driver.getInstance().getGroupDriver().launchService(packet.getGroup(), packet.getCount());
            }
        }

        if (readPacket instanceof ApiStopServicePacket){
            ApiStopServicePacket packet = (ApiStopServicePacket) readPacket;
            Driver.getInstance().getGroupDriver().shutdownService(packet.getService());
        }
    }
}
