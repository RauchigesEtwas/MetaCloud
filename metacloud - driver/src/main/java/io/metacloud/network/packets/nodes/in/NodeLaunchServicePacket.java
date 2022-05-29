package io.metacloud.network.packets.nodes.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeLaunchServicePacket extends Packet {

    private String group;
    private Integer serviceID;
    private boolean sameAddress;
    private Integer port;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("group", group);
        buffer.write("serviceID", serviceID);
        buffer.write("sameAddress", sameAddress);
        buffer.write("port", port);
    }

    @Override
    public void read(IBuffer buffer) {
        this.group = buffer.read("group", String.class);
        this.serviceID = buffer.read("serviceID", Integer.class);
        this.sameAddress = buffer.read("sameAddress", Boolean.class);
        this.port = buffer.read("port", Integer.class);
    }


    public boolean isSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(boolean sameAddress) {
        this.sameAddress = sameAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
