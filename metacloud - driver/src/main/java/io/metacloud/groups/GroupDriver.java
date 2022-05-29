package io.metacloud.groups;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeProperties;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.nodes.in.NodeHaltServicePacket;
import io.metacloud.network.packets.nodes.in.NodeLaunchServicePacket;
import io.metacloud.network.packets.services.in.ProxyServiceRemoveServicePacket;
import io.metacloud.queue.bin.QueueContainer;
import io.metacloud.queue.bin.QueueStatement;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.services.processes.bin.CloudServiceType;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;

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
            GroupConfiguration group = getGroupByService(service);


        ServiceRest rest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + serviceConfig.getCommunication().getManagerHostAddress() + ":" + serviceConfig.getCommunication().getRestApiPort()+
                "/" + serviceConfig.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);



        CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + serviceConfig.getCommunication().getManagerHostAddress() + ":" + serviceConfig.getCommunication().getRestApiPort()+
                "/" + serviceConfig.getCommunication().getRestApiAuthKey()+ "/statistics", CloudStatisticsConfig.class);

        stats.setRunningServices(stats.getRunningServices()-1);
        Driver.getInstance().getRestDriver().getRestServer(serviceConfig.getCommunication().getRestApiPort()).updateContent("statistics", stats);

        if (group.getProperties().getNode().equalsIgnoreCase("InternalNode")) {
            rest.getServices().forEach(liveService -> {
                if (liveService.getServiceName().equalsIgnoreCase(service)) {
                    Driver.getInstance().getServiceDriver().usedPorts.remove(liveService.getSelectedPort());
                     Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.STOPPING, service));
                    selecdedService = liveService;
                }
            });

            if (selecdedService == null)
                return;

            rest.getServices().remove(selecdedService);
            ArrayList<Integer> updatedIDs = new ArrayList<>();

            rest.getUsedIDs().forEach(integer -> {
                if (integer != Integer.valueOf(service.replace(getGroupByService(service).getName() + serviceConfig.getGeneral().getServerSplitter(), ""))) {
                    updatedIDs.add(integer);
                }
            });
            rest.getUsedIDs().clear();

            updatedIDs.forEach(integer -> {
                rest.getUsedIDs().add(integer);
            });
            deployOnRest(group.getName(), rest);

            Driver.getInstance().getConnectionDriver().removeServiceChannel(service);
            if (Driver.getInstance().getGroupDriver().getGroupByService(service).getMode() != GroupType.PROXY){
                Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel1 -> {
                    ProxyServiceRemoveServicePacket removeServicePacket = new ProxyServiceRemoveServicePacket();
                    removeServicePacket.setService(service);
                    channel1.sendPacket(removeServicePacket);
                });
            }
        }else {

            NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);

            if (Driver.getInstance().getGroupDriver().getGroupByService(service).getMode() != GroupType.PROXY){
                Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel1 -> {
                    ProxyServiceRemoveServicePacket removeServicePacket = new ProxyServiceRemoveServicePacket();
                    removeServicePacket.setService(service);
                    channel1.sendPacket(removeServicePacket);
                });
            }
            Driver.getInstance().getConnectionDriver().removeServiceChannel(service);
            rest.getServices().forEach(liveService -> {
                if (liveService.getServiceName().equalsIgnoreCase(service)){

                    Channel channel = Driver.getInstance().getConnectionDriver().getNodeChannel(group.getProperties().getNode());
                    NodeHaltServicePacket packet = new NodeHaltServicePacket();
                    packet.setService(service);
                    channel.sendPacket(packet);
                    selecdedService = liveService;
                    configuration.getNodes().forEach(nodeProperties -> {
                        if (nodeProperties.getNodeName().equals(liveService.getNode())){
                            if (nodeProperties.getNodeHost().equalsIgnoreCase(serviceConfig.getCommunication().getManagerHostAddress())){
                                Driver.getInstance().getServiceDriver().usedPorts.remove(liveService.getSelectedPort());
                            }
                        }
                    });

                }
            });
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+service+"§7 is now stopping");

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

              CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                      "/" + service.getCommunication().getRestApiAuthKey()+ "/statistics", CloudStatisticsConfig.class);

              stats.setRunningServices(stats.getRunningServices()+1);
              Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("statistics", stats);
              String node = group.getProperties().getNode();
              if (node.equalsIgnoreCase("InternalNode")){
                  for (int i = 0; i != count; i++) {
                      int id = getNextFreeID(groupName);
                      ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                              "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupName, ServiceRest.class);
                      LiveService liveService = new LiveService();
                      liveService.setServiceName(groupName + service.getGeneral().getServerSplitter() + id);
                      liveService.setServiceState(CloudServiceState.STARTING);

                      liveService.setCurrentCloudPlayers(0);
                      liveService.setStartTime(System.currentTimeMillis());
                      liveService.setHostAddress(service.getCommunication().getManagerHostAddress());
                      liveService.setGroupConfiguration(group);
                      liveService.setNode(node);

                      if (group.getStaticServices()){
                          liveService.setServiceType(CloudServiceType.STATIC);
                      }else {
                          liveService.setServiceType(CloudServiceType.DYNAMIC);
                      }

                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+groupName+service.getGeneral().getServerSplitter()+id+"§7 has been §badded§7 to the queue.");
                      if (group.getMode() == GroupType.PROXY){
                          int port = Driver.getInstance().getServiceDriver().getFreePort(true);
                          liveService.setSelectedPort(port);
                          Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING, groupName+service.getGeneral().getServerSplitter()+id, port, group, service.getCommunication().getNetworkingPort(), service.getCommunication().getRestApiPort(), service.getCommunication().getManagerHostAddress(), service.getCommunication().getNodeAuthKey(), service.getCommunication().getRestApiAuthKey()));
                      }else {
                          int port = Driver.getInstance().getServiceDriver().getFreePort(false);
                          liveService.setSelectedPort(port);
                          Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING, groupName+service.getGeneral().getServerSplitter()+id, port, group, service.getCommunication().getNetworkingPort(), service.getCommunication().getRestApiPort(), service.getCommunication().getManagerHostAddress(), service.getCommunication().getNodeAuthKey(), service.getCommunication().getRestApiAuthKey()));
                      }
                      serviceRest.getServices().add(liveService);

                      deployOnRest(group.getName(), serviceRest);
                  }
              }else {
                  if (Driver.getInstance().getConnectionDriver().getNodeChannel(node) != null){
                      Channel channel = Driver.getInstance().getConnectionDriver().getNodeChannel(node);
                      for (int i = 0; i != count-1; i++) {
                          int id = getNextFreeID(groupName);
                          Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+groupName+service.getGeneral().getServerSplitter()+id+"§7 has been §badded§7 to the queue.");

                          ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                                  "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupName, ServiceRest.class);

                          NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
                          Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+groupName + service.getGeneral().getServerSplitter() + id+"§7 is now being §bprepared...");

                          NodeLaunchServicePacket launchServicePacket = new NodeLaunchServicePacket();
                          LiveService liveService = new LiveService();
                          for (int j = 0; j != configuration.getNodes().size(); j++) {
                              NodeProperties properties = configuration.getNodes().get(j);
                              if (properties.getNodeName().equalsIgnoreCase(node)){
                                  liveService.setHostAddress(properties.getNodeHost());
                                  if (properties.getNodeHost().equalsIgnoreCase("127.0.0.1") || properties.getNodeHost().equalsIgnoreCase(service.getCommunication().getManagerHostAddress())){
                                      launchServicePacket.setSameAddress(true);
                                      if (group.getMode() == GroupType.PROXY)
                                          launchServicePacket.setPort(Driver.getInstance().getServiceDriver().getFreePort(true));
                                      else
                                          launchServicePacket.setPort(Driver.getInstance().getServiceDriver().getFreePort(false));
                                  }
                              }
                          }


                          launchServicePacket.setGroup(group.getName());

                          liveService.setNode(node);
                          liveService.setStartTime(System.currentTimeMillis());
                          liveService.setServiceName(groupName + service.getGeneral().getServerSplitter() + id);
                          liveService.setServiceState(CloudServiceState.STARTING);
                          liveService.setGroupConfiguration(group);
                          liveService.setCurrentCloudPlayers(0);
                          if (group.getStaticServices()){
                              liveService.setServiceType(CloudServiceType.STATIC);
                          }else {
                              liveService.setServiceType(CloudServiceType.DYNAMIC);
                          }
                          serviceRest.getServices().add(liveService);
                          launchServicePacket.setServiceCount(id);
                          channel.sendPacket(launchServicePacket);
                          deployOnRest(group.getName(), serviceRest);
                      }
                  }
              }
          }

    }

    public void shutdownNode(String node){
        ArrayList<GroupConfiguration> groups = getGroupsFromNode(node);
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        groups.forEach(groupConfiguration -> {
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + groupConfiguration.getName(), ServiceRest.class);

            serviceRest.getUsedIDs().clear();
            serviceRest.getServices().clear();
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("livegroup-" + groupConfiguration.getName(), serviceRest);
        });

    }


    public GroupConfiguration getGroupByService(String service){
        ArrayList<GroupConfiguration> configurations = getGroups();
        for (int i = 0; i !=configurations.size() ; i++) {
            GroupConfiguration configuration =  configurations.get(i);
            if (service.startsWith(configuration.getName())){
                i = configurations.size();
                return configuration;
            }
        }
        return null;
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
