package io.metacloud.network.packets.nodes.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeLaunchServiceCallBackPacket extends Packet {

    private String serviceName;
    private Integer selecedPort;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("servicename", this.serviceName);
        buffer.write("selecedport", this.selecedPort);
    }

    @Override
    public void read(IBuffer buffer) {
        this.serviceName = buffer.read("servicename", String.class);
        this.selecedPort = buffer.read("selecedport", Integer.class);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getSelecedPort() {
        return selecedPort;
    }

    public void setSelecedPort(Integer selecedPort) {
        this.selecedPort = selecedPort;
    }
}
