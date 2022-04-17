package io.metacloud.groups;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.nodes.NodeHaltServicePacket;
import io.metacloud.network.packets.nodes.NodeLaunchServicePacket;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.services.processes.bin.CloudServiceType;
import io.metacloud.services.processes.utils.ServiceStorage;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GroupDriver {


    private Map<String, ArrayList<Integer>> usedID;
    private   LiveService selecdedService;

    public GroupDriver() {
        usedID = new HashMap<>();
    }


    public void create(GroupConfiguration configuration){
        if (getGroup(configuration.getName()) == null){
         new ConfigDriver("./local/groups/" + configuration.getName() + ".json").save(configuration);
         deployOnRest(configuration.getName(), null);
        }
    }

    public void delete(String group){
        if (new File("./local/groups/" + group + ".json").exists()){
            new File("./local/groups/" + group + ".json").delete();
        }
    }


    public void update(GroupConfiguration configuration){
        new ConfigDriver("./local/groups/" + configuration.getName() + ".json").save(configuration);
        deployOnRest(configuration.getName(), null);
    }


    public void deployOnRest(String group, ServiceRest serviceRest){
       GroupConfiguration configuration = getGroup(group);
        if (configuration != null){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if(Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).exitsContent("group-" + group)){
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                        .updateContent("group-" + group,  configuration);

                if (!Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).exitsContent("livegroup-" + group)){

                    Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                            .addContent("livegroup-" + group,  new ServiceRest());
                }else{
                    if (serviceRest != null){
                        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                                .updateContent("livegroup-" + group,  serviceRest);

                    }
                }

            }else{
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                        .addContent("group-" + group, "./local/groups/" + group + ".json", configuration);
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort())
                        .addContent("livegroup-" + group,  new ServiceRest());
            }

        }
    }


    public void shutdownService(String service){

            ServiceConfiguration serviceConfig = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            GroupConfiguration group = getGroup(service.split(serviceConfig.getGeneral().getServerSplitter())[0]);

            ServiceRest rest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + serviceConfig.getCommunication().getManagerHostAddress() + ":" + serviceConfig.getCommunication().getRestApiPort()+
                    "/" + serviceConfig.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);


            if (group.getProperties().getNode().equalsIgnoreCase("InternalNode")){
                rest.getServices().forEach(liveService -> {
                    if (liveService.getServiceName().equalsIgnoreCase(service)){
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+service+"§7 is stopped on the §b" + group.getProperties().getNode());
                        Driver.getInstance().getServiceDriver().haltService(service);
                        selecdedService = liveService;
                    }
                });
                rest.getServices().remove(selecdedService);
                ArrayList<Integer> updatedIDs = new ArrayList<>();

                rest.getUsedIDs().forEach(integer -> {
                    if (integer != Integer.valueOf(service.split(serviceConfig.getGeneral().getServerSplitter())[1])){
                        updatedIDs.add(integer);
                    }
                });
                rest.getUsedIDs().clear();

                updatedIDs.forEach(integer -> {
                    rest.getUsedIDs().add(integer);
                });
                deployOnRest(group.getName(), rest);
            }else{
                rest.getServices().forEach(liveService -> {
                    if (liveService.getServiceName().equalsIgnoreCase(service)){

                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+service+"§7 is stopped on the §b" + group.getProperties().getNode());
                        Channel channel = Driver.getInstance().getConnectionDriver().getNodeChannel(group.getProperties().getNode());
                        NodeHaltServicePacket packet = new NodeHaltServicePacket();
                        packet.setService(service);
                        channel.sendPacket(packet);
                        selecdedService = liveService;
                    }
                });

                ArrayList<Integer> updatedIDs = new ArrayList<>();

                rest.getUsedIDs().forEach(integer -> {
                    if (integer != Integer.valueOf(service.split(serviceConfig.getGeneral().getServerSplitter())[1])){
                        updatedIDs.add(integer);
                    }
                });
                rest.getUsedIDs().clear();

                updatedIDs.forEach(integer -> {
                    rest.getUsedIDs().add(integer);
                });
                rest.getServices().remove(selecdedService);
                deployOnRest(group.getName(), rest);
            }



    }


    public void launchService(String groupName, Integer count){

          ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
          GroupConfiguration group = getGroup(groupName);
          if (group != null){
              String node = group.getProperties().getNode();
              if (node.equalsIgnoreCase("InternalNode")){
                  for (int i = 0; i != count; i++) {
                      int id = getNextFreeID(groupName);
                      Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+groupName+service.getGeneral().getServerSplitter()+id+"§7 is now §aprepared §7[node: §b"+node+"§7, §7version:§b "+group.getProperties().getVersion()+"§7]");
                      ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                              "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupName, ServiceRest.class);
                      LiveService liveService = new LiveService();
                      liveService.setServiceName(groupName + service.getGeneral().getServerSplitter() + id);
                      liveService.setServiceState(CloudServiceState.STARTING);
                      liveService.setCurrentCloudPlayers(0);
                      liveService.setStartTime(System.currentTimeMillis());
                      liveService.setNode(node);

                      if (group.getStaticServices()){
                          liveService.setServiceType(CloudServiceType.STATIC);
                      }else {
                          liveService.setServiceType(CloudServiceType.DYNAMIC);
                      }
                      serviceRest.getServices().add(liveService);
                      serviceRest.getUsedIDs().add(id);
                      deployOnRest(group.getName(), serviceRest);
                      ServiceStorage storage = new ServiceStorage();
                      storage.setServiceName(groupName + service.getGeneral().getServerSplitter() + id);
                      storage.setGroupConfiguration(group);
                      storage.setNetworkingPort(service.getCommunication().getNetworkingPort());
                      storage.setRestAPIPort(service.getCommunication().getRestApiPort());

                      storage.setManagerAddress(service.getCommunication().getManagerHostAddress());
                      storage.setAuthNetworkingKey(service.getCommunication().getNodeAuthKey());
                      storage.setAuthRestAPIKey(service.getCommunication().getRestApiAuthKey());
                      if (group.getMode() == GroupType.PROXY){
                          storage.setSelectedPort(Driver.getInstance().getServiceDriver().getFreePort(true));
                      }else {
                          storage.setSelectedPort(Driver.getInstance().getServiceDriver().getFreePort(false));
                      }

                      Driver.getInstance().getServiceDriver().launchService(storage);
                  }
              }else {
                  if (Driver.getInstance().getConnectionDriver().getNodeChannel(node) != null){
                      Channel channel = Driver.getInstance().getConnectionDriver().getNodeChannel(node);

                      for (int i = 0; i != count; i++) {
                          ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                                  "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupName, ServiceRest.class);


                          NodeLaunchServicePacket launchServicePacket = new NodeLaunchServicePacket();
                          launchServicePacket.setGroup(group.getName());
                          int id = getNextFreeID(groupName);
                          LiveService liveService = new LiveService();
                          liveService.setNode(node);
                          liveService.setStartTime(System.currentTimeMillis());
                          liveService.setServiceName(groupName + service.getGeneral().getServerSplitter() + id);
                          liveService.setServiceState(CloudServiceState.STARTING);
                          liveService.setCurrentCloudPlayers(0);
                          if (group.getStaticServices()){
                              liveService.setServiceType(CloudServiceType.STATIC);
                          }else {
                              liveService.setServiceType(CloudServiceType.DYNAMIC);
                          }
                          serviceRest.getServices().add(liveService);
                          launchServicePacket.setServiceCount(id);
                          channel.sendPacket(launchServicePacket);
                          Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+groupName+service.getGeneral().getServerSplitter()+id+"§7 is now §aprepared §7[node: §b"+node+"§7, §7version:§b "+group.getProperties().getVersion()+"§7]");
                          serviceRest.getUsedIDs().add(id);
                          deployOnRest(group.getName(), serviceRest);
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
