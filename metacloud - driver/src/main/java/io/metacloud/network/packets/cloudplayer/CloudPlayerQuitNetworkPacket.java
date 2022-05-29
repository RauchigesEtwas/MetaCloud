package io.metacloud.network.packets.cloudplayer;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class CloudPlayerQuitNetworkPacket extends Packet {
    private String uuid;
    private String playerName;
    private String service;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("uuid", uuid);
        buffer.write("playerName", playerName);
        buffer.write("service", service);
    }

    @Override
    public void read(IBuffer buffer) {
        uuid = buffer.read("uuid", String.class);
        playerName = buffer.read("playerName", String.class);
        service = buffer.read("service", String.class);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

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
}
