package io.metacloud.network.packets.services;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ServiceStartNewInstancePacket extends Packet {

    private String group;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("group", group);

    }

    @Override
    public void read(IBuffer buffer) {
        group = buffer.read("group", String.class);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
