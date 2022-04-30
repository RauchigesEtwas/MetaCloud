package io.metacloud.bungeecord.serverhelper;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

public class ServerDriver {

    public  boolean serverExists(String name) {
        return getServerInfo(name) != null;
    }

    public  ServerInfo getServerInfo(String name) {
        return getServers().get(name);
    }

    public  void addServer(ServerInfo serverInfo) {
        if (serverExists(serverInfo.getName())) {
            return;
        }

        getServers().put(serverInfo.getName(), serverInfo);
        ServerConfig.addToConfig(serverInfo);
    }

    public  void removeServer(String name) {
        if (!serverExists(name)) {
            return;
        }

        getServers().remove(name);
        ServerConfig.removeFromConfig(name);
    }

    public  Map<String, ServerInfo> getServers() {
        return ProxyServer.getInstance().getServers();
    }

}
