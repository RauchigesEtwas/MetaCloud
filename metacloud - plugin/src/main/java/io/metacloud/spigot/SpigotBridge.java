package io.metacloud.spigot;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.network.packets.services.out.ServiceRegisterPacket;
import io.metacloud.network.packets.services.out.ServiceUnregisterPacket;
import io.metacloud.spigot.listener.CloudPlayerHandlerListener;
import io.metacloud.spigot.networking.ManagerListener;
import io.metacloud.spigot.utilitis.DataDriver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class SpigotBridge extends JavaPlugin {

    private static  SpigotBridge instance;
    public NetworkClientDriver networkClientDriver;
    public CloudServiceConfiguration configuration;
    public DataDriver dataDriver;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        dataDriver = new DataDriver();
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);
        networkClientDriver = new NetworkClientDriver();

        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();

        NetworkingBootStrap.packetListenerHandler.registerListener(new ManagerListener());
        ServiceRegisterPacket packet = new ServiceRegisterPacket();
        packet.setServiceName(configuration.getServicename());
        Bukkit.getPluginManager().registerEvents(new CloudPlayerHandlerListener(), this);
        playerChecker();
        NetworkingBootStrap.client.sendPacket(packet);
    }

    @Override
    public void onDisable() {
        ServiceUnregisterPacket ServiceUnregisterPacket = new ServiceUnregisterPacket();

        ServiceUnregisterPacket.setService(configuration.getServicename());

        NetworkingBootStrap.client.sendPacket(ServiceUnregisterPacket);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        instance = null;

    }

    private void playerChecker(){}


    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }

    public static SpigotBridge getInstance() {
        return instance;
    }
}
