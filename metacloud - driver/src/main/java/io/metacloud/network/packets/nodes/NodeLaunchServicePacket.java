package io.metacloud.network.packets.nodes;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeLaunchServicePacket extends Packet {

    private String group;
    private Integer serviceID;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("group", group);
        buffer.write("serviceID", serviceID);
    }

    @Override
    public void read(IBuffer buffer) {
        this.group = buffer.read("group", String.class);
        this.serviceID = buffer.read("serviceID", Integer.class);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getServiceCount() {
        return serviceID;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceID = serviceCount;
    }
}
