package io.metacloud.apidriver.service;

public class CloudService {

    private String serviceName;
    private String groupName;

    public CloudService(String serviceName, String groupName) {
        this.serviceName = serviceName;
        this.groupName = groupName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getGroupName() {
        return groupName;
    }
}
