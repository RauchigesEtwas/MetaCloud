package io.metacloud.networking;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class SignDeletePacket extends Packet {

    private String signuuid;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("signuuid", signuuid);
    }

    @Override
    public void read(IBuffer buffer) {
        signuuid = buffer.read("signuuid", String.class);
    }

    public String getSignuuid() {
        return signuuid;
    }

    public void setSignuuid(String signuuid) {
        this.signuuid = signuuid;
    }
}
