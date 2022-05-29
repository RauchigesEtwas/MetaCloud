package io.metacloud.webservice.restconfigs.statistics;

import io.metacloud.webservice.bin.IRestConfig;

import java.util.ArrayList;

public class CloudStatisticsConfig implements IRestConfig {

    private ArrayList<String> players;
    private Integer runningServices;

    public CloudStatisticsConfig() {
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public Integer getRunningServices() {
        return runningServices;
    }

    public void setRunningServices(Integer runningServices) {
        this.runningServices = runningServices;
    }
}
