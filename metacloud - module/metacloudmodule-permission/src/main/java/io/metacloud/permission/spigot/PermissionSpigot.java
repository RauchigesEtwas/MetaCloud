package io.metacloud.permission.spigot;

import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.permission.global.configs.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionSpigot extends JavaPlugin {


    private static PermissionSpigot instance;
    private CloudServiceConfiguration configuration;

    @Override
    public void onEnable() {

        instance = this;
    }


    public static PermissionSpigot getInstance() {
        return instance;
    }

    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }
}
