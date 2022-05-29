package io.metacloud.servers.listener;

import io.metacloud.Driver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.global.Tablist;
import io.metacloud.servers.SyncProxyService;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TablistListener implements Listener {

    @EventHandler
    public void handelServerConnection(ServerConnectedEvent event){
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            sendTab(player);
        });

    }

    @EventHandler
    public void login(PostLoginEvent event){
        ProxyServer.getInstance().getScheduler().schedule(SyncProxyService.getInstance(), new Runnable() {
            @Override
            public void run() {
                sendTab(event.getPlayer());
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    @EventHandler
    public void handelServerConnection(ServerSwitchEvent event){
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            sendTab(player);
        });
    }



    private static void sendTab(ProxiedPlayer player){
        CloudStatisticsConfig cloudPlayerConfig = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SyncProxyService.getInstance().configuration.getNetworkProperty().getManagerAddress() + ":" +  SyncProxyService.getInstance().configuration.getNetworkProperty().getRestAPIPort()+
                "/" + SyncProxyService.getInstance().configuration.getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);
        GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SyncProxyService.getInstance().configuration.getNetworkProperty().getManagerAddress()+ ":" + SyncProxyService.getInstance().configuration.getNetworkProperty().getRestAPIPort() + "/"
                + SyncProxyService.getInstance().configuration.getNetworkProperty().getAuthRestAPIKey() + "/group-" +SyncProxyService.getInstance().configuration.getGroupname(), GroupConfiguration.class);


        Tablist tablist = SyncProxyService.getInstance().config.getTablist().get(SyncProxyService.getInstance().tabCount);

        player.setTabHeader(TextComponent.fromLegacyText(Driver.getInstance().getStorageDriver().base64ToUTF8(tablist.getHeader())
                .replace("&", "§")
                        .replace("%service_name%", player.getServer().getInfo().getName())
                        .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                        .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                        .replace("%max_players%", ""+group.getMaxOnlinePlayers())
                        .replace("%player_ping%", String.valueOf(player.getPing()))
                        .replace("%player_name%", player.getName())
                        .replace("%proxy_group_name%", SyncProxyService.getInstance().configuration.getGroupname())
                        .replace("%service_group_name%", group.getName()))
                ,
                TextComponent.fromLegacyText(Driver.getInstance().getStorageDriver().base64ToUTF8(tablist.getFooter())
                        .replace("&", "§")
                        .replace("%service_name%", player.getServer().getInfo().getName())
                        .replace("%proxy_name%", SyncProxyService.getInstance().configuration.getServicename())
                        .replace("%online_players%", ""+cloudPlayerConfig.getPlayers().size())
                        .replace("%max_players%", ""+group.getMaxOnlinePlayers())
                        .replace("%player_ping%", String.valueOf(player.getPing()))
                        .replace("%player_name%", player.getName())
                        .replace("%proxy_group_name%", SyncProxyService.getInstance().configuration.getGroupname())
                        .replace("%service_group_name%", group.getName())));
    }

}
