package io.metacloud.network.packets.permissions;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class CloudPlayerCreatePacket extends Packet {

    private String uuid;
    private String name;

    @Override
    public void write(IBuffer buffer) {

        buffer.write("uuid", uuid);
        buffer.write("name", name);
    }

    @Override
    public void read(IBuffer buffer) {
        uuid = buffer.read("uuid", String.class);
        name = buffer.read("name", String.class);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
