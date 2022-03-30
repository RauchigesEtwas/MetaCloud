package io.metacloud.configuration.configs.service;

public class ServiceNetworkProperty {

    private String authNetworkingKey;
    private String authRestAPIKey;
    private Integer restAPIPort;
    private Integer networkingPort;
    private String managerAddress;

    public String getAuthNetworkingKey() {
        return authNetworkingKey;
    }

    public void setAuthNetworkingKey(String authNetworkingKey) {
        this.authNetworkingKey = authNetworkingKey;
    }

    public String getAuthRestAPIKey() {
        return authRestAPIKey;
    }

    public void setAuthRestAPIKey(String authRestAPIKey) {
        this.authRestAPIKey = authRestAPIKey;
    }

    public Integer getRestAPIPort() {
        return restAPIPort;
    }

    public void setRestAPIPort(Integer restAPIPort) {
        this.restAPIPort = restAPIPort;
    }

    public Integer getNetworkingPort() {
        return networkingPort;
    }

    public void setNetworkingPort(Integer networkingPort) {
        this.networkingPort = networkingPort;
    }

    public String getManagerAddress() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress = managerAddress;
    }
}
