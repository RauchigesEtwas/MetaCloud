package io.metacloud.network.packets.permissions;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class GroupDeletePacket extends Packet {
    private String name;

    @Override
    public void write(IBuffer buffer) {

        buffer.write("name", name);
    }

    @Override
    public void read(IBuffer buffer) {
        name = buffer.read("name", String.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
