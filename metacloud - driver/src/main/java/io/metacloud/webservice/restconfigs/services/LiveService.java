package io.metacloud.webservice.restconfigs.services;

import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.services.processes.bin.CloudServiceType;

public class LiveService {


    private CloudServiceState serviceState;
    private CloudServiceType serviceType;
    private Integer currentCloudPlayers;

    public LiveService() {}

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
