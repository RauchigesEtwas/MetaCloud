package io.metacloud.webservice.restconfigs.nodesetup;

public class NodeSetupCommunication {

    private String managerHostAddress;
    private Integer networkCommunicationPort;
    private String networkAuthKey;
    private String restAPIAuthKey;
    private Integer restAPICommunicationPort;

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
