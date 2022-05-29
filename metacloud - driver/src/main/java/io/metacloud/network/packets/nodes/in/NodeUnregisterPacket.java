package io.metacloud.network.packets.nodes.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeUnregisterPacket extends Packet {

    private String  nodeName;
    @Override
    public void write(IBuffer buffer) {
        buffer.write("nodename", this.nodeName);

    }

    @Override
    public void read(IBuffer buffer) {
        this.nodeName = buffer.read("nodename", String.class);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
