package io.metacloud.configuration.configs.service;

import io.metacloud.configuration.IConfig;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;

public class CloudServiceConfiguration implements IConfig {

    private String servicename;
    private String groupname;
    private ServiceNetworkProperty networkProperty;

    public CloudServiceConfiguration() {}


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }


    public ServiceNetworkProperty getNetworkProperty() {
        return networkProperty;
    }

    public void setNetworkProperty(ServiceNetworkProperty networkProperty) {
        this.networkProperty = networkProperty;
    }
}
