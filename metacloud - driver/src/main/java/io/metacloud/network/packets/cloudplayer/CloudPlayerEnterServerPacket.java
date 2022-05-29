package io.metacloud.network.packets.cloudplayer;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class CloudPlayerEnterServerPacket extends Packet {

    private String uuid;
    private String playername;
    private String service;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("uuid", uuid);
        buffer.write("playername", playername);
        buffer.write("service", service);
    }

    @Override
    public void read(IBuffer buffer) {
        uuid = buffer.read("uuid", String.class);
        service = buffer.read("service", String.class);
        playername = buffer.read("playername", String.class);
    }


    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
