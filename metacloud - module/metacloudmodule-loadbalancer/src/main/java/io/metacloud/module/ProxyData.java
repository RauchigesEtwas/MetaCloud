package io.metacloud.module;

import io.metacloud.module.utils.subgates.SubGate;

public class ProxyData {

    private Integer connectedPlayers;
    private String serviceName;
    private String host;
    private Integer port;
    private SubGate subGate;

    public ProxyData(Integer connectedPlayers, String serviceName, String host, Integer port, SubGate subGate) {
        this.connectedPlayers = connectedPlayers;
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.subGate = subGate;
    }

    public void setConnectedPlayers(Integer connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public Integer getConnectedPlayers() {
        return connectedPlayers;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public SubGate getSubGate() {
        return subGate;
    }
}
