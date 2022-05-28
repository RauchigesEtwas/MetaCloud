package io.metacloud.events.events;

import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.events.bin.IEventStack;

public class ServiceAddEvent extends IEventStack {

    private String serviceName;
    private Integer usedPort;
    private GroupConfiguration group;

    public ServiceAddEvent(String serviceName, Integer usedPort, GroupConfiguration group) {
        this.serviceName = serviceName;
        this.usedPort = usedPort;
        this.group = group;
    }

    public Integer getUsedPort() {
        return usedPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public GroupConfiguration getGroup() {
        return group;
    }

}
