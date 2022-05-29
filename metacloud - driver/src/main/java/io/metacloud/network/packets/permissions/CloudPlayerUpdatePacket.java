package io.metacloud.network.packets.permissions;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudPlayerUpdatePacket extends Packet {

    private String uuid;
    private String name;
    private Map<String, Boolean> permissions;
    private Map<String, Boolean> groups;

    @Override
    public void write(IBuffer buffer) {
            buffer.write("uuid", uuid);
            buffer.write("name", name);
            buffer.write("permissions", permissions);
            buffer.write("groups", groups);
    }

    @Override
    public void read(IBuffer buffer) {
        uuid = buffer.read("uuid", String.class);
        name = buffer.read("name", String.class);
        permissions = buffer.readMap("permissions", String.class, Boolean.class);
        groups = buffer.readMap("groups", String.class, Boolean.class);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }
}
