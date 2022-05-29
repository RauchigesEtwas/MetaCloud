package io.metacloud.permission.bungee;

import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import net.md_5.bungee.api.plugin.Plugin;

public class PermissionProxy extends Plugin {


    private static PermissionProxy instance;
    private CloudServiceConfiguration configuration;

    @Override
    public void onEnable() {

        instance = this;
    }


    public static PermissionProxy getInstance() {
        return instance;
    }

    public CloudServiceConfiguration getConfiguration() {
        return configuration;
    }
}
