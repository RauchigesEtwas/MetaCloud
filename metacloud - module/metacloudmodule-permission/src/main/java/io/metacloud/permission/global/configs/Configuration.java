package io.metacloud.permission.global.configs;

import io.metacloud.configuration.IConfig;

import java.util.ArrayList;

public class Configuration implements IConfig {

    public ArrayList<Group> groups;
    public ArrayList<CloudPlayers> cloudplayers;

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<CloudPlayers> getCloudPlayers() {
        return cloudplayers;
    }

    public void setCloudplayers(ArrayList<CloudPlayers> cloudplayers) {
        this.cloudplayers = cloudplayers;
    }
}
