package io.metacloud.apidriver.bootsrap;

import io.metacloud.apidriver.CloudAPI;
import io.metacloud.apidriver.utilitis.DataDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin {


    public static CloudServiceConfiguration configuration;
    public static NetworkClientDriver networkClientDriver;
    @Override
    public void onEnable() {
        new CloudAPI();
        DataDriver.isBungeeSide = true;
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);

        networkClientDriver = new NetworkClientDriver();
        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();

    }
}
