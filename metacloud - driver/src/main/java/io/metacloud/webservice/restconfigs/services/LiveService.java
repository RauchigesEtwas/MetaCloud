package io.metacloud.webservice.restconfigs.services;

import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.services.processes.bin.CloudServiceType;

public class LiveService {


    private CloudServiceState serviceState;
    private CloudServiceType serviceType;
    private GroupConfiguration groupConfiguration;
    private String serviceName;
    private Integer selectedPort;
    private String node;
    private String hostAddress;
    private Integer currentCloudPlayers;
    private Long startTime;

    public LiveService() {}

    public String getHostAddress() {
        return hostAddress;
    }

    public GroupConfiguration getGroupConfiguration() {
        return groupConfiguration;
    }

    public void setGroupConfiguration(GroupConfiguration groupConfiguration) {
        this.groupConfiguration = groupConfiguration;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Integer getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(Integer selectedPort) {
        this.selectedPort = selectedPort;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public CloudServiceState getServiceState() {
        return serviceState;
    }

    public void setServiceState(CloudServiceState serviceState) {
        this.serviceState = serviceState;
    }

    public CloudServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(CloudServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getCurrentCloudPlayers() {
        return currentCloudPlayers;
    }

    public void setCurrentCloudPlayers(Integer currentCloudPlayers) {
        this.currentCloudPlayers = currentCloudPlayers;
    }
}
