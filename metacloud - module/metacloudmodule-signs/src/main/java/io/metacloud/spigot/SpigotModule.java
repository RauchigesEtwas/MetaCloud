package io.metacloud.spigot;

import io.metacloud.Driver;
import io.metacloud.configs.LocationConfiguration;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.spigot.listener.SignAddListener;
import io.metacloud.spigot.utils.CloudSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotModule extends JavaPlugin {

    private static SpigotModule instance;
    private CloudServiceConfiguration configuration;
    public NetworkClientDriver networkClientDriver;
    public SignDriver signDriver;

    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);
        networkClientDriver = new NetworkClientDriver();
        networkClientDriver.bind(this.configuration.getNetworkProperty().getManagerAddress(), this.configuration.getNetworkProperty().getNetworkingPort()).run();
        signDriver = new SignDriver();
        Bukkit.getPluginManager().registerEvents(new SignAddListener(), this);

        LocationConfiguration locs = (LocationConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress()+ ":" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort() + "/"
                + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/module-signs-locations", LocationConfiguration.class);

        locs.getSigns().forEach(location -> {
            this.signDriver.registerSign(new CloudSign(location.getSignUUID(), new Location(Bukkit.getWorld(location.getLocationWorld()), location.getLocationPosX(), location.getLocationPosY(), location.getLocationPosZ()), location.getGroupName(), null, null, 0));
        });

    }

    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }

    public static SpigotModule getInstance() {
        return instance;
    }
}
