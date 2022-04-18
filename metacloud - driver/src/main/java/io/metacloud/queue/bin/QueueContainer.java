package io.metacloud.queue.bin;

import io.metacloud.configuration.configs.group.GroupConfiguration;

public class QueueContainer {
    
    private QueueStatement queueStatement;
    private String serviceName;
    private int port;
    private GroupConfiguration groupConfiguration;
    private Integer networkingPort;
    private Integer restPort;
    private String managerAddress;
    private String networkAuthKey;
    private String restAuthKey;

    public QueueContainer(QueueStatement queueStatement, String serviceName, int port, GroupConfiguration groupConfiguration, Integer networkingPort, Integer restPort, String managerAddress, String networkAuthKey, String restAuthKey) {
        this.queueStatement = queueStatement;
        this.serviceName = serviceName;
        this.port = port;
        this.groupConfiguration = groupConfiguration;
        this.networkingPort = networkingPort;
        this.restPort = restPort;
        this.managerAddress = managerAddress;
        this.networkAuthKey = networkAuthKey;
        this.restAuthKey = restAuthKey;
    }

    public QueueContainer(QueueStatement queueStatement, String serviceName) {
        this.queueStatement = queueStatement;
        this.serviceName = serviceName;
        this.port = 0;
        this.groupConfiguration = null;
    }


    public QueueStatement getQueueStatement() {
        return queueStatement;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getPort() {
        return port;
    }

    public GroupConfiguration getGroupConfiguration() {
        return groupConfiguration;
    }

    public Integer getNetworkingPort() {
        return networkingPort;
    }

    public Integer getRestPort() {
        return restPort;
    }

    public String getManagerAddress() {
        return managerAddress;
    }

    public String getNetworkAuthKey() {
        return networkAuthKey;
    }

    public String getRestAuthKey() {
        return restAuthKey;
    }
}
