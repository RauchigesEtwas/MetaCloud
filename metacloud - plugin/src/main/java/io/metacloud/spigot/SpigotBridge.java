package io.metacloud.spigot;

import io.metacloud.Driver;
import io.metacloud.NetworkClient;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.network.packets.services.ServiceRegisterPacket;
import io.metacloud.spigot.networking.ManagerListener;
import org.bukkit.plugin.java.JavaPlugin;


public class SpigotBridge extends JavaPlugin {

    private static  SpigotBridge instance;
    public NetworkClientDriver networkClientDriver;
    public CloudServiceConfiguration configuration;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);
        networkClientDriver = new NetworkClientDriver();
        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();
        ServiceRegisterPacket packet = new ServiceRegisterPacket();
        packet.setServiceName(configuration.getServicename());
        NetworkingBootStrap.client.sendPacket(packet);
        NetworkingBootStrap.packetListenerHandler.registerListener(new ManagerListener());

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }

    public static SpigotBridge getInstance() {
        return instance;
    }
}
