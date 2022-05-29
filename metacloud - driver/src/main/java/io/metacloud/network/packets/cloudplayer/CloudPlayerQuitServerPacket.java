package io.metacloud.network.packets.cloudplayer;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class CloudPlayerQuitServerPacket extends Packet {


    private String service;
    private String uuid;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("service", this.service);
        buffer.write("uuid", this.uuid);
    }

    @Override
    public void read(IBuffer buffer) {
        this.service = buffer.read("service", String.class);
        this.uuid = buffer.read("uuid", String.class);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
