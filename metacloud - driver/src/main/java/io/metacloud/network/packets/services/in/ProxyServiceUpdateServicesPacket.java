package io.metacloud.network.packets.services.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.util.List;

public class ProxyServiceUpdateServicesPacket  extends Packet {


    private List<String> groups;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("groups", groups);
    }

    @Override
    public void read(IBuffer buffer) {
        groups = buffer.readList("groups", String.class);
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
