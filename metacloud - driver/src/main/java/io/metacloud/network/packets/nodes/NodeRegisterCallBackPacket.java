package io.metacloud.network.packets.nodes;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeRegisterCallBackPacket extends Packet {

    private boolean connectionAccept;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("connectionaccept", this.connectionAccept);
    }

    @Override
    public void read(IBuffer buffer) {
        this.connectionAccept = buffer.read("connectionaccept", Boolean.class);
    }

    public boolean isConnectionAccept() {
        return connectionAccept;
    }

    public void setConnectionAccept(boolean connectionAccept) {
        this.connectionAccept = connectionAccept;
    }
}
