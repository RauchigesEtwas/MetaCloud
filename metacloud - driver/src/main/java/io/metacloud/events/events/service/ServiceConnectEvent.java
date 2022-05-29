package io.metacloud.events.events.service;

import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.events.bin.IEventStack;

public class ServiceConnectEvent extends IEventStack {

    private String serviceName;
    private Integer usedPort;
    private String host;
    private GroupConfiguration group;

    public ServiceConnectEvent(String serviceName, Integer usedPort, GroupConfiguration group, String host) {
        this.serviceName = serviceName;
        this.usedPort = usedPort;
        this.group = group;
        this.host =host;
    }

    public String getHost() {
        return host;
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
