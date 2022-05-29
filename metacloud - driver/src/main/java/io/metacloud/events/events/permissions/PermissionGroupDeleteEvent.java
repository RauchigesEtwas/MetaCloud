package io.metacloud.events.events.permissions;

import io.metacloud.events.bin.IEventStack;

public class PermissionGroupDeleteEvent extends IEventStack {

    private String name;

    public PermissionGroupDeleteEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
