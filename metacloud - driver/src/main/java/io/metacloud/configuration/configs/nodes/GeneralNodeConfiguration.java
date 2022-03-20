package io.metacloud.configuration.configs.nodes;

import io.metacloud.configuration.IConfig;

public class GeneralNodeConfiguration implements IConfig {

    private String nodeName;
    private String managerHostAddress;
    private Integer networkCommunicationPort;
    private String networkAuthKey;
    private String restAPIAuthKey;
    private Integer restAPICommunicationPort;


    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getManagerHostAddress() {
        return managerHostAddress;
    }

    public void setManagerHostAddress(String managerHostAddress) {
        this.managerHostAddress = managerHostAddress;
    }

    public Integer getNetworkCommunicationPort() {
        return networkCommunicationPort;
    }

    public void setNetworkCommunicationPort(Integer networkCommunicationPort) {
        this.networkCommunicationPort = networkCommunicationPort;
    }

    public String getNetworkAuthKey() {
        return networkAuthKey;
    }

    public void setNetworkAuthKey(String networkAuthKey) {
        this.networkAuthKey = networkAuthKey;
    }

    public String getRestAPIAuthKey() {
        return restAPIAuthKey;
    }

    public void setRestAPIAuthKey(String restAPIAuthKey) {
        this.restAPIAuthKey = restAPIAuthKey;
    }

    public Integer getRestAPICommunicationPort() {
        return restAPICommunicationPort;
    }

    public void setRestAPICommunicationPort(Integer restAPICommunicationPort) {
        this.restAPICommunicationPort = restAPICommunicationPort;
    }
}
