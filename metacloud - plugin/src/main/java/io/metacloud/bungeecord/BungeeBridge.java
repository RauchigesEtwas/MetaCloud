package io.metacloud.bungeecord;

import io.metacloud.NetworkingBootStrap;
import io.metacloud.bungeecord.networking.ManagerListener;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.network.packets.services.ServiceRegisterPacket;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBridge extends Plugin {

    private static BungeeBridge instance;
    private NetworkClientDriver networkClientDriver;
    private CloudServiceConfiguration configuration;

    @Override
    public void onEnable() {
        instance = this;
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


    public static BungeeBridge getInstance() {
        return instance;
    }
}
