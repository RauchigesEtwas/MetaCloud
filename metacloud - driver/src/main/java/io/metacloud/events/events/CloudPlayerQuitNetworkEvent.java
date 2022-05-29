package io.metacloud.events.events;

import io.metacloud.events.bin.IEventStack;

public class CloudPlayerQuitNetworkEvent extends IEventStack {


    private String playerName, uuid, service;

    public CloudPlayerQuitNetworkEvent(String playerName, String uuid, String service) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.service = service;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getService() {
        return service;
    }
}
