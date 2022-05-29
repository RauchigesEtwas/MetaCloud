package io.metacloud.events.events.permissions;

import io.metacloud.events.bin.IEventStack;

public class PermissionCloudPlayerCreateEvent extends IEventStack {

    private String uuid;
    private String name;


    public PermissionCloudPlayerCreateEvent(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }


    public String getName() {
        return name;
    }


}
