package io.metacloud.module.config;

import io.metacloud.configuration.IConfig;

public class Configuration implements IConfig {


        private ConnectionType connectionType;
        private int connectionPort;


    public int getConnectionPort() {
        return connectionPort;
    }

    public void setConnectionPort(int connectionPort) {
        this.connectionPort = connectionPort;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
