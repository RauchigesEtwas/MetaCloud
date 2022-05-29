package io.metacloud.network.packets.services.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ServiceUnregisterPacket extends Packet {

    private String service;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("service", service);
    }

    @Override
    public void read(IBuffer buffer) {
        service = buffer.read("service", String.class);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
