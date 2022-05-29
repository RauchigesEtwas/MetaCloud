package io.metacloud.spigot.utils;

public class ServerStorage {

    private String motd;
    private Integer players;
    private Integer maxplayers;

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Integer getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(Integer maxplayers) {
        this.maxplayers = maxplayers;
    }
}
