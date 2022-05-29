package io.metacloud.webservice.restconfigs.cloudpalyer;

import io.metacloud.webservice.bin.IRestConfig;

import java.util.HashMap;

public class CloudPlayerConfig implements IRestConfig {
    
    
    private String playerName;
    private String uuid;
    private String currentServer;
    private String currentProxy;
    private HashMap<String, Object> modules;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public String getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

    public HashMap<String, Object> getModules() {
        return modules;
    }

    public void setModules(HashMap<String, Object> modules) {
        this.modules = modules;
    }
}
