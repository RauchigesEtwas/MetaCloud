package io.metacloud.groups;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GroupDriver {


    public GroupDriver() {}


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
