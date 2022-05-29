package io.metacloud.network.packets.nodes.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeHaltServicePacket extends Packet {


    private String serviceID;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("serviceID", serviceID);
    }

    @Override
    public void read(IBuffer buffer) {
        this.serviceID = buffer.read("serviceID", String.class);
    }

    public String getService() {
        return serviceID;
    }

    public void setService(String service) {
        this.serviceID = service;
    }
}
