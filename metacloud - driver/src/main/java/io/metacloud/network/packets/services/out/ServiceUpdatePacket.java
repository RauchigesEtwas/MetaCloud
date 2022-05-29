package io.metacloud.network.packets.services.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ServiceUpdatePacket extends Packet {

    private String service;
    private Integer players;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("service", service);
        buffer.write("players", players);
    }

    @Override
    public void read(IBuffer buffer) {
        service = buffer.read("service", String.class);
        players = buffer.read("players", Integer.class);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }
}
