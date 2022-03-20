package io.metacloud.groups;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.network.packets.nodes.NodeLaunchServicePacket;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GroupDriver {


    private Map<String, ArrayList<Integer>> usedID;

    public GroupDriver() {
        usedID = new HashMap<>();
    }


    public void create(GroupConfiguration configuration){
        if (getGroup(configuration.getName()) == null){
         new ConfigDriver("./local/groups/" + configuration.getName() + ".json").save(configuration);
         deployOnRest(configuration.getName());
        }
    }

    public void delete(String group){
        if (new File("./local/groups/" + group + ".json").exists()){
            new File("./local/groups/" + group + ".json").delete();
        }
    }


    public void update(GroupConfiguration configuration){
        new ConfigDriver("./local/groups/" + configuration.getName() + ".json").save(configuration);
        deployOnRest(configuration.getName());
    }


    public void deployOnRest(String group){
       GroupConfiguration configuration = getGroup(group);
        if (configuration != null){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if(Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).exitsContent("group-" + group)){
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                        .updateContent("group-" + group,  configuration);
            }else{
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                        .addContent("group-" + group, "./local/groups/" + group + ".json", configuration);
            }

        }
    }


    public void launchService(String groupName, Integer count){
        GroupConfiguration group = getGroup(groupName);
        if (group != null){
            String node = group.getProperties().getNode();
            if (node.equalsIgnoreCase("InternalNode")){
                for (int i = 0; i != count; i++) {

                }
            }else {
                if (Driver.getInstance().getConnectionDriver().getNodeChannel(node) != null){
                    Channel channel = Driver.getInstance().getConnectionDriver().getNodeChannel(node);

                    for (int i = 0; i != count; i++) {
                        NodeLaunchServicePacket launchServicePacket = new NodeLaunchServicePacket();
                        launchServicePacket.setGroup(group.getName());
                        launchServicePacket.setServiceCount(getNextFreeID(group.getName()));
                        channel.sendPacket(launchServicePacket);
                    }
                }
            }
        }
    }

    private Integer getNextFreeID(String groupname){
        int id = 0;
        int maxService = getGroup(groupname).getMaxOnlineServers();
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupname, ServiceRest.class);
        if (maxService == -1){
            maxService = 900000;
        }
        for (int i = 1; i != maxService; i++ ){
            if (!serviceRest.getUsedIDs().contains(i)){
                int returns = i;
                serviceRest.getUsedIDs().add(returns);
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("livegroup-" + groupname, serviceRest);
                i = maxService;
                return returns;

            }
        }

        return id;
    }

    public GroupConfiguration getGroup(String name){
        ArrayList<GroupConfiguration> configurations =       getGroups();
        for (int i = 0; i !=configurations.size() ; i++) {
           GroupConfiguration configuration =  configurations.get(i);
           if (configuration.getName().equalsIgnoreCase(name)){
               i = configurations.size();
               return configuration;
           }
        }
        return null;
    }

    public ArrayList<GroupConfiguration> getGroupsFromNode(String node) {
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        ArrayList<GroupConfiguration> modules = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".json")[0];
            GroupConfiguration configuration = (GroupConfiguration) new ConfigDriver("./local/groups/" + group + ".json").read(GroupConfiguration.class);
            if (configuration.getProperties().getNode().equalsIgnoreCase(node)){
                modules.add(configuration);
            }
        }
        return modules;
    }


    public ArrayList<GroupConfiguration> getGroups() {
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        ArrayList<GroupConfiguration> modules = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".json")[0];
            GroupConfiguration configuration = (GroupConfiguration) new ConfigDriver("./local/groups/" + group + ".json").read(GroupConfiguration.class);
            modules.add(configuration);
        }
        return modules;
    }
}
