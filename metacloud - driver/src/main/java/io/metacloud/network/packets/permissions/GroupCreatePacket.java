package io.metacloud.network.packets.permissions;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class GroupCreatePacket extends Packet {

    private String name;
    private Boolean isDefault;

    @Override
    public void write(IBuffer buffer) {

        buffer.write("name", name);
        buffer.write("isDefault", isDefault);
    }

    @Override
    public void read(IBuffer buffer) {
        name = buffer.read("name", String.class);
        isDefault = buffer.read("isDefault", Boolean.class);
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
