package io.metacloud.events.events.permissions;

import io.metacloud.events.bin.IEventStack;

import java.util.List;
import java.util.Map;

public class PermissionCloudPlayerUpdateEvent extends IEventStack {

    private String uuid;
    private String name;
    private Map<String, Boolean> permissions;
    private Map<String, Boolean> group;


    public PermissionCloudPlayerUpdateEvent(String uuid, String name, Map<String, Boolean> permissions, Map<String, Boolean> group) {
        this.uuid = uuid;
        this.name = name;
        this.permissions = permissions;
        this.group = group;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }


    public Map<String, Boolean> getPermissions() {
        return permissions;
    }



    public Map<String, Boolean> getGroup() {
        return group;
    }


}
