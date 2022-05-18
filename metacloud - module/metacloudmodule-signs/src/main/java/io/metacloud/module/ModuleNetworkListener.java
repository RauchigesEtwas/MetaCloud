package io.metacloud.module;

import io.metacloud.configs.Location;
import io.metacloud.configs.LocationConfiguration;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.cloudsign.SignCreatePacket;
import io.metacloud.network.packets.cloudsign.SignDeletePacket;
import io.metacloud.protocol.Packet;

public class ModuleNetworkListener extends PacketListener {

    @PacketProvideHandler
    public void handelSignadd(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof SignCreatePacket){
            SignCreatePacket packet = (SignCreatePacket) readPacket;
            LocationConfiguration locs = (LocationConfiguration) new ConfigDriver("./modules/signs/locations.json").read(LocationConfiguration.class);
            Location location = new Location();
            location.setSignUUID(packet.getSignUUID());
            location.setGroupName(packet.getGroupName());
            location.setLocationPosX(packet.getLocationPosX());
            location.setLocationPosY(packet.getLocationPosY());
            location.setLocationPosZ(packet.getLocationPosZ());
            location.setLocationWorld(packet.getLocationWorld());
            locs.getSigns().add(location);
            new ConfigDriver("./modules/signs/locations.json").save(locs);
            SignsModule.getInstance().pushToRest();
        }

        if (readPacket instanceof SignDeletePacket){
            SignDeletePacket packet = (SignDeletePacket) readPacket;
            LocationConfiguration locs = (LocationConfiguration) new ConfigDriver("./modules/signs/locations.json").read(LocationConfiguration.class);
            locs.getSigns().forEach(location -> {
                if (!location.getSignUUID().equals(packet.getSignuuid()))return;
                locs.getSigns().remove(location);
            });
            new ConfigDriver("./modules/signs/locations.json").save(locs);
            SignsModule.getInstance().pushToRest();

        }
    }

}
