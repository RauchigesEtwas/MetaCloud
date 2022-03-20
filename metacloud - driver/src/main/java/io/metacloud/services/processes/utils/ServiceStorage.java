package io.metacloud.services.processes.utils;

import io.metacloud.configuration.configs.group.GroupConfiguration;

public class ServiceStorage {


    private GroupConfiguration groupConfiguration;
    private String serviceName;
    private Integer selectedPort;

    public ServiceStorage() {}


    public Integer getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(Integer selectedPort) {
        this.selectedPort = selectedPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public GroupConfiguration getGroupConfiguration() {
        return groupConfiguration;
    }

    public void setGroupConfiguration(GroupConfiguration groupConfiguration) {
        this.groupConfiguration = groupConfiguration;
    }
}
