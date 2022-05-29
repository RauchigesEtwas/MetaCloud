package io.metacloud.bungeecord.listener;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.bungeecord.BungeeBridge;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.network.packets.cloudplayer.CloudPlayerEnterNetworkPacket;
import io.metacloud.network.packets.cloudplayer.CloudPlayerQuitNetworkPacket;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.xml.ws.Service;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class CloudEventHandler implements Listener {

   public boolean isOnWhitelist;
   public boolean bypassPermsMaintenance;
   public boolean bypassPermsFullJoin;
   public ArrayList<String> fullJoinNetwork;
   public ArrayList<String> maintenanceJoin;

    public CloudEventHandler() {
        maintenanceJoin = new ArrayList<>();
        fullJoinNetwork = new ArrayList<>();
    }

    @EventHandler(priority = -128)
    public void handelCloudDisconnectCloudPlayer(PlayerDisconnectEvent event){
        if (BungeeBridge.getInstance().getHandlerDriver().getPlayersFirstJoin().contains(event.getPlayer().getUniqueId())){
            BungeeBridge.getInstance().getHandlerDriver().getPlayersFirstJoin().remove(event.getPlayer().getUniqueId());

        }

        CloudPlayerQuitNetworkPacket packet = new CloudPlayerQuitNetworkPacket();
        packet.setUuid(event.getPlayer().getUniqueId().toString());
        packet.setPlayerName(event.getPlayer().getName());
        packet.setService(BungeeBridge.getInstance().getConfiguration().getServicename());
        NetworkingBootStrap.client.sendPacket(packet);
        if (fullJoinNetwork.contains(event.getPlayer().getName())){
            fullJoinNetwork.remove(event.getPlayer().getName());
        }    if (maintenanceJoin.contains(event.getPlayer().getName())){
            maintenanceJoin.remove(event.getPlayer().getName());
        }

        if (  BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer().getUniqueId())!= null){
            BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = - 127)
    public void handelCloudKickCloudPlayer(ServerKickEvent event){
        ProxiedPlayer player = event.getPlayer();
        String kickedServer = event.getKickedFrom().getName();
        ArrayList<String> premiumLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingPermissionLobby(player);
        ArrayList<String> freeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingLobby(player);


        for (int i = 0; i != premiumLobby.size() ; i++) {
            if (premiumLobby.get(i).equalsIgnoreCase(kickedServer)){
                premiumLobby.remove(i);
            }
        }
        for (int i = 0; i != freeLobby.size() ; i++) {
            if (freeLobby.get(i).equalsIgnoreCase(kickedServer)){
                freeLobby.remove(i);
            }
        }

        if (premiumLobby.isEmpty() && freeLobby.isEmpty()){
            event.setCancelled(false);
            event.setCancelServer(null);
        }else if (premiumLobby.size() != 0){
            String info = premiumLobby.get(new Random().nextInt(premiumLobby.size()));

            event.setCancelServer(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
            event.setCancelled(true);
        }else {
            String info = freeLobby.get(new Random().nextInt(freeLobby.size()));

            event.setCancelServer(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
            event.setCancelled(true);
        }
    }

    public void HandelPermissionAble(final PostLoginEvent event){
        if (event.getPlayer().hasPermission("cloud.bypass.maintenance")){
            maintenanceJoin.add(event.getPlayer().getName());
        }
        if (event.getPlayer().hasPermission("cloud.bypass.fullnetwork")){
            fullJoinNetwork.add(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = - 128)
    public void handelCloudLoginCloudPlayer(final LoginEvent event){

        ArrayList<String> primeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingPermissionLobby(ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId()));
        ArrayList<String > freeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingLobby(ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId()));
        ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig( "http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
        GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/group-" + BungeeBridge.getInstance().getConfiguration().getGroupname(), GroupConfiguration.class);
        ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ BungeeBridge.getInstance().getConfiguration().getGroupname(), ServiceRest.class);

        CloudStatisticsConfig cloudPlayerConfig = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +  BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);
        CloudPlayerEnterNetworkPacket packet = new CloudPlayerEnterNetworkPacket();
        packet.setPlayerName(event.getConnection().getName());
        packet.setUuid(event.getConnection().getUniqueId().toString());
        packet.setCurrentProxy(BungeeBridge.getInstance().getConfiguration().getServicename());
        NetworkingBootStrap.client.sendPacket(packet);



        isOnWhitelist = false;
        bypassPermsMaintenance = false;
        bypassPermsFullJoin = false;

        cloudservice.getGeneral().getWhitelist().forEach(s -> {
            if (event.getConnection().getName().equalsIgnoreCase(s)){
                isOnWhitelist = true;
            }
        });
        this.maintenanceJoin.forEach(s -> {
            if (event.getConnection().getName().equalsIgnoreCase(s)){
                bypassPermsMaintenance = true;
            }
        });

        this.fullJoinNetwork.forEach(s -> {
            if (event.getConnection().getName().equalsIgnoreCase(s)){
                bypassPermsFullJoin = true;
            }
        });
        if (group.getMaintenance() && !isOnWhitelist && !bypassPermsMaintenance){
            CloudPlayerQuitNetworkPacket packet2 = new CloudPlayerQuitNetworkPacket();
            packet2.setUuid(event.getConnection().getUniqueId().toString());
            packet2.setPlayerName(event.getConnection().getName());
            packet2.setService(BungeeBridge.getInstance().getConfiguration().getServicename());
            NetworkingBootStrap.client.sendPacket(packet2);
            event.setCancelReason(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getMaintenanceKickMessage()).replace("&", "§"));
            event.setCancelled(true);
        }else if (cloudPlayerConfig.getPlayers().size() >= group.getMaxOnlinePlayers() && !bypassPermsFullJoin){
            CloudPlayerQuitNetworkPacket packet2 = new CloudPlayerQuitNetworkPacket();
            packet2.setUuid(event.getConnection().getUniqueId().toString());
            packet2.setPlayerName(event.getConnection().getName());
            packet2.setService(BungeeBridge.getInstance().getConfiguration().getServicename());
            NetworkingBootStrap.client.sendPacket(packet2);
            event.setCancelReason(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getFullNetworkKickMessage()).replace("&", "§"));
            event.setCancelled(true);
        }else  if (primeLobby.isEmpty() && freeLobby.isEmpty()){
            CloudPlayerQuitNetworkPacket packet2 = new CloudPlayerQuitNetworkPacket();
            packet2.setUuid(event.getConnection().getUniqueId().toString());
            packet2.setPlayerName(event.getConnection().getName());
            packet2.setService(BungeeBridge.getInstance().getConfiguration().getServicename());
            NetworkingBootStrap.client.sendPacket(packet2);
            event.setCancelReason(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getNoFallbackKickMessage()).replace("&", "§"));
            event.setCancelled(true);
        }else {
            event.setCancelled(false);
        }

    }

    @EventHandler
    public void handle(ServerSwitchEvent event){
        if (BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer().getUniqueId()) != null){
            BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
            BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), event.getPlayer().getServer().getInfo().getName());
        }
    }

    @EventHandler(priority = - 127)
    public void handle(final ServerConnectEvent event) {
        if (event.getPlayer().getServer() == null) {

            ArrayList<String> primeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingPermissionLobby(event.getPlayer());
            ArrayList<String > freeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingLobby(event.getPlayer());
            if (!primeLobby.isEmpty()){
                if (BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer().getUniqueId()) == null){
                    String info = primeLobby.get(new Random().nextInt(primeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }else{
                    if (primeLobby.contains(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()))){
                        primeLobby.remove(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()));
                        String info = primeLobby.get(new Random().nextInt(primeLobby.size()));
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                        event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    }else {
                        String info = primeLobby.get(new Random().nextInt(primeLobby.size()));
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                        event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    }
                }
            }else {


                if (BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer().getUniqueId()) == null){
                    String info = freeLobby.get(new Random().nextInt(freeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }else{
                    if (freeLobby.contains(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()))){
                        freeLobby.remove(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()));
                        String info = freeLobby.get(new Random().nextInt(freeLobby.size()));
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                        event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    }else {
                        String info = freeLobby.get(new Random().nextInt(freeLobby.size()));


                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                        BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                        event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    }
                }
            }
        }if (event.getTarget().getName().equalsIgnoreCase("lobby")){
            ArrayList<String> primeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingPermissionLobby(event.getPlayer());
            ArrayList<String > freeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingLobby(event.getPlayer());
            if (!primeLobby.isEmpty()){
                if (primeLobby.contains(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()))){
                    primeLobby.remove(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()));
                    String info = primeLobby.get(new Random().nextInt(primeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }else {
                    String info = primeLobby.get(new Random().nextInt(primeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }
            }else {
                if (freeLobby.contains(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()))){
                    freeLobby.remove(BungeeBridge.getInstance().getDataDriver().lastConnectedServer.get(event.getPlayer()));
                    String info = freeLobby.get(new Random().nextInt(freeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }else {
                    String info = freeLobby.get(new Random().nextInt(freeLobby.size()));
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.remove(event.getPlayer().getUniqueId());
                    BungeeBridge.getInstance().getDataDriver().lastConnectedServer.put(event.getPlayer().getUniqueId(), info);
                    event.setTarget(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                }
            }

        }
    }



    @EventHandler
    public void handelCloudPluginMessage(PluginMessageEvent event){
        if (event.getTag().equals("MC|BSign") || event.getTag().equals("MC|BEdit"))
            event.setCancelled(true);
    }


}
