package io.metacloud.permission.module;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.modules.interfaces.IModule;
import io.metacloud.permission.global.configs.Configuration;
import io.metacloud.permission.global.configs.Group;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class PermissionModule implements IModule {


    private static PermissionModule instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!new File("./modules/permissions/config.json").exists()){
            new File("./modules/permissions/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setCloudplayers(new ArrayList<>());
            configuration.setGroups(new ArrayList<>());
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
        }
        Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        ArrayList<Group> groups = new ArrayList<>();

        configuration.groups.forEach(group -> {
            Group update = new Group();
            update.setPermissions(group.getPermissions());
            update.setDefault(group.getDefault());
            update.setInherit(group.getInherit());
            update.setName(group.getName());
            update.setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(group.getPrefix()));
            update.setSuffix(Driver.getInstance().getStorageDriver().utf8ToUBase64(group.getSuffix()));
            groups.add(update);
        });
        configuration.setGroups(groups);

        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("module_permissions", "./modules/permissions/config.json",configuration);

        pushToRest();

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }


    public static PermissionModule getInstance() {
        return instance;
    }


    public void pushToRest(){

        Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        ArrayList<Group> groups = new ArrayList<>();

        configuration.groups.forEach(group -> {
            Group update = new Group();
            update.setPermissions(group.getPermissions());
            update.setDefault(group.getDefault());
            update.setInherit(group.getInherit());
            update.setName(group.getName());
            update.setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(group.getPrefix()));
            update.setSuffix(Driver.getInstance().getStorageDriver().utf8ToUBase64(group.getSuffix()));
            groups.add(update);
        });
        configuration.setGroups(groups);

        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("module_permissions", configuration);

    }
}
