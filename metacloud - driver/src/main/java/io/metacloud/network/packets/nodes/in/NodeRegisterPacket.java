package io.metacloud.network.packets.nodes.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeRegisterPacket extends Packet {

    private String  nodeName;
    private String authKey;

    @Override
    public void write(IBuffer buffer) {
            buffer.write("nodename", this.nodeName);
            buffer.write("authkey", this.authKey);
    }

    @Override
    public void read(IBuffer buffer) {
        this.nodeName = buffer.read("nodename", String.class);
        this.authKey = buffer.read("authkey", String.class);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
