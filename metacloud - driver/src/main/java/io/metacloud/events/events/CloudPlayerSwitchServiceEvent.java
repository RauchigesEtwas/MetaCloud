package io.metacloud.events.events;

import io.metacloud.events.bin.IEventStack;

public class CloudPlayerSwitchServiceEvent extends IEventStack {

    private String playerName, newService;

    public CloudPlayerSwitchServiceEvent(String playerName, String newService) {
        this.playerName = playerName;
        this.newService = newService;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNewService() {
        return newService;
    }
}
