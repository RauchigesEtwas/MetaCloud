package io.metacloud.network.packets.nodes.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class NodeSyncTemplatePacket extends Packet {

    private String serviceName;
    private String groupName;
    private String templatePath;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("servicename", this.serviceName);
        buffer.write("groupname", this.groupName);
        buffer.write("templatepath", this.templatePath);
    }

    @Override
    public void read(IBuffer buffer) {
        this.serviceName = buffer.read("servicename", String.class);
        this.groupName = buffer.read("groupname", String.class);
        this.templatePath = buffer.read("templatepath", String.class);
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
