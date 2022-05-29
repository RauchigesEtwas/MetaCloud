package io.metacloud.configuration.configs.group;

import io.metacloud.configuration.IConfig;

public class GroupConfiguration implements IConfig {


    private String name;
    private GroupType mode;
    private Integer dynamicMemory;
    private Boolean maintenance;
    private Boolean staticServices;
    private String permission;
    private Integer minOnlineServers;
    private Integer maxOnlineServers;
    private Integer maxOnlinePlayers;
    private GroupProperties properties;


    public GroupConfiguration() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getMode() {
        return mode;
    }

    public void setMode(GroupType mode) {
        this.mode = mode;
    }

    public Integer getDynamicMemory() {
        return dynamicMemory;
    }

    public void setDynamicMemory(Integer dynamicMemory) {
        this.dynamicMemory = dynamicMemory;
    }

    public Boolean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    public Boolean getStaticServices() {
        return staticServices;
    }

    public void setStaticServices(Boolean staticServices) {
        this.staticServices = staticServices;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getMinOnlineServers() {
        return minOnlineServers;
    }

    public void setMinOnlineServers(Integer minOnlineServers) {
        this.minOnlineServers = minOnlineServers;
    }

    public Integer getMaxOnlineServers() {
        return maxOnlineServers;
    }

    public void setMaxOnlineServers(Integer maxOnlineServers) {
        this.maxOnlineServers = maxOnlineServers;
    }

    public Integer getMaxOnlinePlayers() {
        return maxOnlinePlayers;
    }

    public void setMaxOnlinePlayers(Integer maxOnlinePlayers) {
        this.maxOnlinePlayers = maxOnlinePlayers;
    }

    public GroupProperties getProperties() {
        return properties;
    }

    public void setProperties(GroupProperties properties) {
        this.properties = properties;
    }


}
