package io.metacloud.module.config;

import io.metacloud.configuration.IConfig;

public class Configuration implements IConfig {


        private ConnectionType connectionType;

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
