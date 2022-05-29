package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.events.events.permissions.*;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.permissions.*;
import io.metacloud.protocol.Packet;

public class PermissionHandlerListener extends PacketListener {


    @PacketProvideHandler
    public void handelPermissions(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof CloudPlayerCreatePacket){
            CloudPlayerCreatePacket packet = (CloudPlayerCreatePacket)readPacket;
            Driver.getInstance().getEventDriver().executeEvent(new PermissionCloudPlayerCreateEvent(packet.getUuid(), packet.getName()));
        }

        if (readPacket instanceof CloudPlayerUpdatePacket){
            CloudPlayerUpdatePacket packet = (CloudPlayerUpdatePacket)readPacket;
            Driver.getInstance().getEventDriver().executeEvent(new PermissionCloudPlayerUpdateEvent(packet.getUuid(), packet.getName(), packet.getPermissions(), packet.getGroups()));
        }

        if (readPacket instanceof GroupCreatePacket){
            GroupCreatePacket packet = (GroupCreatePacket)readPacket;
            Driver.getInstance().getEventDriver().executeEvent(new PermissionGroupCreateEvent(packet.getName()));
        }

        if (readPacket instanceof GroupDeletePacket){
            GroupDeletePacket packet = (GroupDeletePacket)readPacket;
            Driver.getInstance().getEventDriver().executeEvent(new PermissionGroupDeleteEvent(packet.getName()));
        }


        if (readPacket instanceof GroupUpdatePacket){
            GroupUpdatePacket packet = (GroupUpdatePacket)readPacket;
            Driver.getInstance().getEventDriver().executeEvent(new PermissionGroupUpdate(packet.getName(), packet.getPermissions(), packet.getDefault(), packet.getInherit()));
        }
    }

}
