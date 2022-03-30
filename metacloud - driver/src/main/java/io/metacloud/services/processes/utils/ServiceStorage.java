package io.metacloud.services.processes.utils;

import io.metacloud.configuration.configs.group.GroupConfiguration;

public class ServiceStorage {


    private GroupConfiguration groupConfiguration;
    private String serviceName;
    private Integer selectedPort;
    private String authRestAPIKey;
    private String authNetworkingKey;
    private Integer networkingPort;
    private Integer restAPIPort;
    private String managerAddress;

    public ServiceStorage() {}

    public String getManagerAddress() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress = managerAddress;
    }

    public Integer getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(Integer selectedPort) {
        this.selectedPort = selectedPort;
    }


    public String getAuthRestAPIKey() {
        return authRestAPIKey;
    }

    public void setAuthRestAPIKey(String authRestAPIKey) {
        this.authRestAPIKey = authRestAPIKey;
    }

    public String getAuthNetworkingKey() {
        return authNetworkingKey;
    }

    public void setAuthNetworkingKey(String authNetworkingKey) {
        this.authNetworkingKey = authNetworkingKey;
    }

    public Integer getNetworkingPort() {
        return networkingPort;
    }

    public void setNetworkingPort(Integer networkingPort) {
        this.networkingPort = networkingPort;
    }

    public Integer getRestAPIPort() {
        return restAPIPort;
    }

    public void setRestAPIPort(Integer restAPIPort) {
        this.restAPIPort = restAPIPort;
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
