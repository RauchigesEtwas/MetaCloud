package io.metacloud.network.packets.permissions;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupUpdatePacket extends Packet {

    private String name;
    private Map<String, Boolean> permissions;
    private Boolean isDefault;
    private List<String> inherit;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("name", name);
        buffer.write("permissions", permissions);
        buffer.write("isDefault", isDefault);
        buffer.write("inherit", inherit);
    }

    @Override
    public void read(IBuffer buffer) {
        name = buffer.read("name", String.class);
        isDefault = buffer.read("isDefault", Boolean.class);
        inherit = buffer.readList("inherit", String.class);
        permissions = buffer.readMap("permissions", String.class, Boolean.class);
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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public List<String> getInherit() {
        return inherit;
    }

    public void setInherit(List<String> inherit) {
        this.inherit = inherit;
    }
}
