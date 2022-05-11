package io.metacloud.servers.listener;

import io.metacloud.Driver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.global.Motd;
import io.metacloud.servers.SyncProxyService;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.function.Consumer;

public class MotdListener implements Listener {


    @EventHandler
    public void handelPings(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Players players = ping.getPlayers();

        GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SyncProxyService.getInstance().configuration.getNetworkProperty().getManagerAddress()+ ":" + SyncProxyService.getInstance().configuration.getNetworkProperty().getRestAPIPort() + "/"
                + SyncProxyService.getInstance().configuration.getNetworkProperty().getAuthRestAPIKey() + "/group-" +SyncProxyService.getInstance().configuration.getGroupname(), GroupConfiguration.class);
        CloudStatisticsConfig cloudPlayerConfig = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SyncProxyService.getInstance().configuration.getNetworkProperty().getManagerAddress() + ":" +  SyncProxyService.getInstance().configuration.getNetworkProperty().getRestAPIPort()+
                "/" + SyncProxyService.getInstance().configuration.getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);

        if (group.getMaintenance()){
            Motd motd = SyncProxyService.getInstance().config.getMaintenancen().get(SyncProxyService.getInstance().pingCount);



            if (!motd.getPlayerinfos().isEmpty()){

                String array[] = new String[motd.getPlayerinfos().size()];
                for(int j =0;j<motd.getPlayerinfos().size();j++){
                    array[j] = motd.getPlayerinfos().get(j);
                }
                ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[array.length];
                for (int i = 0; i < playerInfo.length; i++) {
                    playerInfo[i] = new ServerPing.PlayerInfo(
                            Driver.getInstance().getStorageDriver().base64ToUTF8(array[i]).replace("&", "§"),
                            UUID.randomUUID().toString());
                }

               ping.setPlayers( new ServerPing.Players(group.getMaxOnlinePlayers(), cloudPlayerConfig.getPlayers().size(), playerInfo));
            }else{
                ping.setPlayers( new ServerPing.Players(group.getMaxOnlinePlayers(), cloudPlayerConfig.getPlayers().size(), null));
            }


            if (motd.getProtocol() != null) {
                ping.setVersion(new ServerPing.Protocol(Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getProtocol()).replace("&", "§").replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size()).replace("%max_players%", ""+group.getMaxOnlinePlayers()), ping.getVersion().getProtocol() -1));
            }else{
                ping.setVersion(new ServerPing.Protocol("§7" + cloudPlayerConfig.getPlayers().size() + "/" + group.getMaxOnlinePlayers(), ping.getVersion().getProtocol() -1));
            }

            ping.setDescription( Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getFirstline())
                    .replace("&", "§")
                    .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                    .replace("%proxy_group%", SyncProxyService.getInstance().configuration.getGroupname())
                    .replace("%proxy_version%", group.getProperties().getVersion().toString()
                            .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                            .replace("%max_players%", ""+group.getMaxOnlinePlayers())) +
                    "\n" +
                    Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getSecondline())
                            .replace("&", "§")
                            .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                            .replace("%proxy_group%", SyncProxyService.getInstance().configuration.getGroupname())
                            .replace("%proxy_version%", group.getProperties().getVersion().toString())
                            .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                            .replace("%max_players%", ""+group.getMaxOnlinePlayers()));

            event.setResponse(ping);

        }else {
            Motd motd = SyncProxyService.getInstance().config.getDefaults().get(SyncProxyService.getInstance().pingCount);

            if (!motd.getPlayerinfos().isEmpty()){

                String array[] = new String[motd.getPlayerinfos().size()];
                for(int j =0;j<motd.getPlayerinfos().size();j++){
                    array[j] = motd.getPlayerinfos().get(j);
                }
                ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[array.length];
                for (int i = 0; i < playerInfo.length; i++) {
                    playerInfo[i] = new ServerPing.PlayerInfo(
                            Driver.getInstance().getStorageDriver().base64ToUTF8(array[i]).replace("&", "§"),
                            UUID.randomUUID().toString());
                }

                ping.setPlayers( new ServerPing.Players(group.getMaxOnlinePlayers(), cloudPlayerConfig.getPlayers().size(), playerInfo));
            }else{
                ping.setPlayers( new ServerPing.Players(group.getMaxOnlinePlayers(), cloudPlayerConfig.getPlayers().size(), null));
            }


            if (motd.getProtocol() != null) {
                ping.setVersion(new ServerPing.Protocol(Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getProtocol()).replace("&", "§"), ping.getVersion().getProtocol() -1));
            }else{
                ping.setVersion(new ServerPing.Protocol("§7" + cloudPlayerConfig.getPlayers().size() + "/" + group.getMaxOnlinePlayers(), ping.getVersion().getProtocol() -1));
            }

            ping.setDescription( Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getFirstline())
                    .replace("&", "§")
                    .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                    .replace("%proxy_group%", SyncProxyService.getInstance().configuration.getGroupname())
                    .replace("%proxy_version%", group.getProperties().getVersion().toString()
                            .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                            .replace("%max_players%", ""+group.getMaxOnlinePlayers())) +
                    "\n" +
                    Driver.getInstance().getStorageDriver().base64ToUTF8(motd.getSecondline())
                            .replace("&", "§")
                            .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                            .replace("%proxy_group%", SyncProxyService.getInstance().configuration.getGroupname())
                            .replace("%proxy_version%", group.getProperties().getVersion().toString())
                            .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                            .replace("%max_players%", ""+group.getMaxOnlinePlayers()));

            event.setResponse(ping);

        }

    }

}
