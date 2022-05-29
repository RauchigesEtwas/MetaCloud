package io.metacloud.bungeecord.utilities;

import io.metacloud.Driver;
import io.metacloud.bungeecord.BungeeBridge;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HandlerDriver {

    private final HashMap<String, ServerInfo> lobbyServerPool;
    private final ArrayList<UUID> playersFirstJoin;


    public HandlerDriver() {
        lobbyServerPool = new HashMap<>();
        playersFirstJoin = new ArrayList<>();
    }

    public ArrayList<UUID> getPlayersFirstJoin() {
        return playersFirstJoin;
    }

    public void registerService(LiveService service){
        lobbyServerPool.put(service.getServiceName(), BungeeBridge.getInstance().getServerDriver().getServerInfo(service.getServiceName()));
    }


    public void unregisterService(String service){
        lobbyServerPool.remove(service);
    }


    public ArrayList<String> getMatchingPermissionLobby(ProxiedPlayer player){

        ArrayList<String> services = new ArrayList<>();

        for (String service : this.lobbyServerPool.keySet()){
            String parsedGroup = BungeeBridge.getInstance().getDataDriver().getGroupByService(service);

            GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/group-"+parsedGroup, GroupConfiguration.class);

            if (group.getPermission() != null  && !group.getPermission().equals("") && player.hasPermission(group.getPermission())){
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                        "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ parsedGroup, ServiceRest.class);

                for (int i = 0; i != serviceRest.getServices().size(); i++){
                    LiveService liveService = serviceRest.getServices().get(i);
                    if (liveService.getServiceState() == CloudServiceState.LOBBY && group.getMode() == GroupType.LOBBY && this.lobbyServerPool.containsKey(liveService.getServiceName()) && !group.getMaintenance()){
                        services.add(service);
                    }
                }
            }
        }
        return services;
    }


    public ArrayList<String> getMatchingLobby(ProxiedPlayer ignoredPlayer){

        ArrayList<String> services = new ArrayList<>();

        for (String service : this.lobbyServerPool.keySet()){
            String parsedGroup = BungeeBridge.getInstance().getDataDriver().getGroupByService(service);

            GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/group-"+parsedGroup, GroupConfiguration.class);

            if (group.getPermission() == null  || group.getPermission().equals("")){
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                        "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ parsedGroup, ServiceRest.class);

                for (int i = 0; i != serviceRest.getServices().size(); i++){
                    LiveService liveService = serviceRest.getServices().get(i);
                    if (liveService.getServiceState() == CloudServiceState.LOBBY && group.getMode() == GroupType.LOBBY && this.lobbyServerPool.containsKey(liveService.getServiceName()) && !group.getMaintenance()){
                        services.add(service);
                    }
                }
            }
        }
        return services;
    }
}
