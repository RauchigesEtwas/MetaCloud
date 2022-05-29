package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.events.events.signs.CloudSignAddEvent;
import io.metacloud.events.events.signs.CloudSignRemoveEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.cloudsign.SignCreatePacket;
import io.metacloud.network.packets.cloudsign.SignDeletePacket;
import io.metacloud.protocol.Packet;

public class SignHandlerListener  extends PacketListener {

    @PacketProvideHandler
    public void handelServiceRegister(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof SignCreatePacket){
            SignCreatePacket packet = (SignCreatePacket) readPacket;
            CloudSignAddEvent cloudSignAddEvent = new CloudSignAddEvent();

            cloudSignAddEvent.setSignUUID(packet.getSignUUID());
            cloudSignAddEvent.setGroupName(packet.getGroupName());
            cloudSignAddEvent.setLocationPosX(packet.getLocationPosX());
            cloudSignAddEvent.setLocationPosY(packet.getLocationPosY());
            cloudSignAddEvent.setLocationPosZ(packet.getLocationPosZ());
            cloudSignAddEvent.setLocationWorld(packet.getLocationWorld());

            Driver.getInstance().getEventDriver().executeEvent(cloudSignAddEvent);
        }      if (readPacket instanceof SignDeletePacket){
            SignDeletePacket packet = (SignDeletePacket) readPacket;
            CloudSignRemoveEvent cloudSignRemoveEvent = new CloudSignRemoveEvent();

            cloudSignRemoveEvent.setSignuuid(packet.getSignuuid());

            Driver.getInstance().getEventDriver().executeEvent(cloudSignRemoveEvent);
        }
    }
}
