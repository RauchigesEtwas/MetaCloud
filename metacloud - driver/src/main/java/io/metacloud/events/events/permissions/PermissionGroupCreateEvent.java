package io.metacloud.events.events.permissions;

import io.metacloud.events.bin.IEventStack;

public class PermissionGroupCreateEvent extends IEventStack {

    private String name;

    public PermissionGroupCreateEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
