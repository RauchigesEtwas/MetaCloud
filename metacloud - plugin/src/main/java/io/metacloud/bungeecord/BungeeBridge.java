package io.metacloud.bungeecord;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.bungeecord.commands.HubCommand;
import io.metacloud.bungeecord.listener.CloudEventHandler;
import io.metacloud.bungeecord.networking.ManagerListener;
import io.metacloud.bungeecord.utilities.DataDriver;
import io.metacloud.bungeecord.utilities.HandlerDriver;
import io.metacloud.bungeecord.utilities.serverhelper.ServerDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.network.packets.cloudplayer.CloudPlayerQuitNetworkPacket;
import io.metacloud.network.packets.services.ServiceStartNewInstancePacket;
import io.metacloud.network.packets.services.out.ServiceRegisterPacket;
import io.metacloud.network.packets.services.out.ServiceUnregisterPacket;
import io.metacloud.network.packets.services.out.ServiceUpdatePacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class BungeeBridge extends Plugin {

    private static BungeeBridge instance;
    private NetworkClientDriver networkClientDriver;
    private CloudServiceConfiguration configuration;
    private HandlerDriver handlerDriver;
    private ServerDriver serverDriver;
    private DataDriver dataDriver;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        handlerDriver = new HandlerDriver();
        serverDriver = new ServerDriver();
        dataDriver = new DataDriver();
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);
        networkClientDriver = new NetworkClientDriver();
        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new CloudEventHandler());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("hub"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("lobby"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("l"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("leave"));
         playerChecker();
        NetworkingBootStrap.packetListenerHandler.registerListener(new ManagerListener());
        ServiceRegisterPacket packet = new ServiceRegisterPacket();
        packet.setServiceName(configuration.getServicename());
        NetworkingBootStrap.client.sendPacket(packet);


    }

    @Override
    public void onDisable() {
        instance = null;


        ServiceUnregisterPacket ServiceUnregisterPacket = new ServiceUnregisterPacket();

        ServiceUnregisterPacket.setService(configuration.getServicename());
        NetworkingBootStrap.client.sendPacket(ServiceUnregisterPacket);;


        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {

            CloudPlayerQuitNetworkPacket packet = new CloudPlayerQuitNetworkPacket();
            packet.setUuid(proxiedPlayer.getUniqueId().toString());
            packet.setPlayerName(proxiedPlayer.getName());
            packet.setService(BungeeBridge.getInstance().getConfiguration().getServicename());
            NetworkingBootStrap.client.sendPacket(packet);


            proxiedPlayer.disconnect("cloudserver-shutdown");
        });


    }



    private void playerChecker(){
        GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/group-"+configuration.getGroupname(), GroupConfiguration.class);

    }

    public HandlerDriver getHandlerDriver() {
        return handlerDriver;
    }

    public ServerDriver getServerDriver() {
        return serverDriver;
    }

    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }


    public DataDriver getDataDriver() {
        return dataDriver;
    }

    public static BungeeBridge getInstance() {
        return instance;
    }
}
