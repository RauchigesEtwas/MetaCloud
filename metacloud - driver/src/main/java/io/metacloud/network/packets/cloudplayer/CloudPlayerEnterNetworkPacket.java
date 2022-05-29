package io.metacloud.network.packets.cloudplayer;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class CloudPlayerEnterNetworkPacket extends Packet {

    private String playerName;
    private String uuid;
    private String currentProxy;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("uuid", uuid);
        buffer.write("playerName", playerName);
        buffer.write("currentProxy", currentProxy);
    }

    @Override
    public void read(IBuffer buffer) {
        uuid = buffer.read("uuid", String.class);
        playerName = buffer.read("playerName", String.class);
        currentProxy = buffer.read("currentProxy", String.class);
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

    public String getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }
}
