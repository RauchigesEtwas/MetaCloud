package io.metacloud.apidriver.bootsrap;

import io.metacloud.apidriver.CloudAPI;
import io.metacloud.apidriver.utilitis.DataDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotBootstrap extends JavaPlugin {


    public static CloudServiceConfiguration configuration;
    public static NetworkClientDriver networkClientDriver;

    @Override
    public void onEnable() {
        new CloudAPI();
        DataDriver.isBungeeSide = false;
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);


        networkClientDriver = new NetworkClientDriver();
        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();

    }
}
