package io.metacloud.spigot.listener;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.bungeecord.BungeeBridge;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.network.packets.cloudplayer.CloudPlayerEnterServerPacket;
import io.metacloud.network.packets.cloudplayer.CloudPlayerQuitServerPacket;
import io.metacloud.spigot.SpigotBridge;
import io.metacloud.webservice.restconfigs.cloudpalyer.CloudPlayerConfig;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CloudPlayerHandlerListener implements Listener {


    @EventHandler
    public void playerDisconnect(PlayerQuitEvent event){
        CloudPlayerQuitServerPacket packet = new CloudPlayerQuitServerPacket();
        packet.setService(SpigotBridge.getInstance().getConfiguration().getServicename());
        packet.setUuid(event.getPlayer().getUniqueId().toString());
        NetworkingBootStrap.client.sendPacket(packet);


    }



    @EventHandler
    public void loginEvent(PlayerLoginEvent event){
        String addres = event.getRealAddress().getHostAddress();
        ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
        CloudStatisticsConfig cloudPlayerConfig = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +  SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + SpigotBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);

        if (!cloudservice.getCommunication().getWhitelistAddresses().contains(addres) || !cloudPlayerConfig.getPlayers().contains(event.getPlayer().getName())){
            event.disallow(null, Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getOnlyProxyJoinKickMessage()).replace("&", "§"));
        }

    }

    @EventHandler
    public void playerConnect(PlayerJoinEvent event){
        CloudPlayerEnterServerPacket packet = new CloudPlayerEnterServerPacket();
        packet.setUuid(event.getPlayer().getUniqueId().toString());
        packet.setService(SpigotBridge.getInstance().getConfiguration().getServicename());
        packet.setPlayername(event.getPlayer().getName());
        NetworkingBootStrap.client.sendPacket(packet);
    }
}
