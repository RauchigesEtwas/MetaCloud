package io.metacloud.configuration.configs.group;

import io.metacloud.configuration.IConfig;

public class GroupConfiguration implements IConfig {


    private String name;
    private GroupType mode;
    private Integer memory;
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

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
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

    public class GroupProperties{
        private Integer percentForNewServerAutomatically;
        private GroupVersion serviceVersion;
        private String template;
        private String node;

        public Integer getPercentForNewServerAutomatically() {
            return percentForNewServerAutomatically;
        }

        public void setPercentForNewServerAutomatically(Integer percentForNewServerAutomatically) {
            this.percentForNewServerAutomatically = percentForNewServerAutomatically;
        }

        public GroupVersion getServiceVersion() {
            return serviceVersion;
        }

        public void setServiceVersion(GroupVersion serviceVersion) {
            this.serviceVersion = serviceVersion;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }
    }

}
