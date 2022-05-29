package io.metacloud.network.packets.services.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ServiceRegisterPacket extends Packet {

    private String serviceName;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("servicename", this.serviceName);
    }

    @Override
    public void read(IBuffer buffer) {
        this.serviceName = buffer.read("servicename", String.class);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
