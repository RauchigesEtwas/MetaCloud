package io.metacloud.events.events.service;

import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.events.bin.IEventStack;

public class ServiceRemoveEvent extends IEventStack {

    private String serviceName;
    private GroupConfiguration group;

    public ServiceRemoveEvent(String serviceName, GroupConfiguration group) {
        this.serviceName = serviceName;
        this.group = group;
    }

    public String getServiceName() {
        return serviceName;
    }

    public GroupConfiguration getGroup() {
        return group;
    }
}
