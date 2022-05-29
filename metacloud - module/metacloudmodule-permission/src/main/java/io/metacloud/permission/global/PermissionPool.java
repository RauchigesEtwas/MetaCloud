package io.metacloud.permission.global;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.network.packets.permissions.CloudPlayerCreatePacket;
import io.metacloud.network.packets.permissions.GroupCreatePacket;
import io.metacloud.network.packets.permissions.GroupDeletePacket;
import io.metacloud.permission.bungee.PermissionProxy;
import io.metacloud.permission.global.configs.Configuration;
import io.metacloud.permission.global.configs.Group;
import io.metacloud.permission.spigot.PermissionSpigot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PermissionPool {

    public Group getPermissionGroup(String group){

        Group groupData = new Group();
        if (!new File("./service.json").exists()){
            if (Settings.isProxied){
                Configuration permissionPool = (Configuration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" +  PermissionProxy.getInstance().getConfiguration().getNetworkProperty().getManagerAddress()+ ":" +  PermissionProxy.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/"
                        + PermissionProxy.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/module_permissions", Configuration.class);
                for (int i = 0; i != permissionPool.getGroups().size() ; i++) {
                    Group g = permissionPool.getGroups().get(i);
                    if (g.getName().equals(group)){
                        groupData.setName(g.getName());
                        groupData.setPermissions(g.getPermissions());
                        groupData.setPrefix(g.getPrefix());
                        groupData.setSuffix(g.getSuffix());
                        groupData.setInherit(g.getInherit());
                        groupData.setDefault(g.getDefault());
                    }
                }
            }else {
                Configuration permissionPool = (Configuration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" +  PermissionSpigot.getInstance().getConfiguration().getNetworkProperty().getManagerAddress()+ ":" +  PermissionSpigot.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/"
                        + PermissionSpigot.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/module_permissions", Configuration.class);

                for (int i = 0; i != permissionPool.getGroups().size() ; i++) {
                    Group g = permissionPool.getGroups().get(i);
                    if (g.getName().equals(group)){
                        groupData.setName(g.getName());
                        groupData.setPermissions(g.getPermissions());
                        groupData.setPrefix(g.getPrefix());
                        groupData.setSuffix(g.getSuffix());
                        groupData.setInherit(g.getInherit());
                        groupData.setDefault(g.getDefault());
                    }
                }

            }
        }else {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
            for (int i = 0; i != configuration.getGroups().size() ; i++) {
                Group g = configuration.getGroups().get(i);
                if (g.getName().equals(group)){
                    groupData.setName(g.getName());
                    groupData.setPermissions(g.getPermissions());
                    groupData.setPrefix(g.getPrefix());
                    groupData.setSuffix(g.getSuffix());
                    groupData.setInherit(g.getInherit());
                    groupData.setDefault(g.getDefault());
                }
            }


        }

        return groupData;
    }


    public void createPermissionGroup(String group, boolean defaults){
        if (!new File("./service.json").exists()){

            GroupCreatePacket packet = new GroupCreatePacket();
            packet.setName(group);
            packet.setDefault(defaults);
            NetworkingBootStrap.client.sendPacket(packet);

        }else {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
            Group groupData = new Group();
            groupData.setDefault(defaults);
            groupData.setName(group);
            groupData.setPrefix("§b" + group + "§8 - §7");
            groupData.setSuffix("");
            groupData.setInherit(new ArrayList<>());
            groupData.setPermissions(new HashMap<>());
            configuration.groups.add(groupData);
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
        }
    }

    public void deletePermissionGroup(String group){

        if (!new File("./service.json").exists()){

            GroupDeletePacket packet = new GroupDeletePacket();
            packet.setName(group);
            NetworkingBootStrap.client.sendPacket(packet);

        }else {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
            for (int i = 0; i != configuration.getGroups().size() ; i++) {
                Group g = configuration.getGroups().get(i);
                if (g.getName().equals(group)){
                    configuration.groups.remove(i);
                }
            }

            new ConfigDriver("./modules/permissions/config.json").save(configuration);
        }
    }

    public void updatePermissionGroup(String group, Group groupData){

    }


    public void getPermissionPlayer(String player){
    }


    public void createPermissionPlayer(String player){

    }

    public void deletePermissionPlayer(String player){

    }

    public void updatePermissionPlayer(String player){

    }

}
