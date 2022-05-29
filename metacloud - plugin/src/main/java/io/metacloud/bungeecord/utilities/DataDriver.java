package io.metacloud.bungeecord.utilities;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataDriver {
    private final ArrayList<String > registeredGroups;
    public Boolean hasGotStartNewByFullService;
    public HashMap<UUID, String> lastConnectedServer;

    public DataDriver() {
        lastConnectedServer = new HashMap<>();
        hasGotStartNewByFullService = false;
        registeredGroups = new ArrayList<>();
    }

    public ArrayList<String> getRegisteredGroups() {
        return registeredGroups;
    }
    public String getGroupByService(String service) {
        ArrayList<String> configurations = registeredGroups;
        for (int i = 0; i != configurations.size(); i++) {
            if (service.startsWith(configurations.get(i))) {
                return configurations.get(i);
            }
        }
        return null;
    }
}
