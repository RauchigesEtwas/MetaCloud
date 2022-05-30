package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.services.in.ProxyServiceRemoveServicePacket;
import io.metacloud.network.packets.services.in.ServiceSendCommandPacket;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

@CommandInfo(command = "service", description = "manage all services, start and stop them", aliases = {"serv"})
public class ServiceCommand extends CloudCommand {


    Boolean exists;

    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {


        if (args.length == 0){
            sendHelp(logger);
        }else if (args[0].equalsIgnoreCase("launch")){
            if (args.length == 3){
                String group = args[1];
                if(args[2].matches("[0-9]+")){
                    int count = Integer.parseInt(args[2]);
                    if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
                        Driver.getInstance().getGroupDriver().launchService(group, count);
                    }else {
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                    }
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the last value§b must be §7a §bnumber");
                }

            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("stop")){
            if (args.length == 2){
                ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                String group = Driver.getInstance().getGroupDriver().getGroupByService(args[1]).getName();
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group, ServiceRest.class);
                exists = false;
                serviceRest.getServices().forEach(live -> {
                    if (live.getServiceName().equals(args[1])){
                        exists = true;
                    }
                });

                if (exists){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
                    Driver.getInstance().getGroupDriver().shutdownService(args[1]);
                    if (Driver.getInstance().getGroupDriver().getGroupByService(args[1]).getMode() != GroupType.PROXY){
                        Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel1 -> {
                            ProxyServiceRemoveServicePacket removeServicePacket = new ProxyServiceRemoveServicePacket();
                            removeServicePacket.setService(args[1]);
                            channel1.sendPacket(removeServicePacket);
                        });
                    }


                    if (serviceRest.getServices().size() < Driver.getInstance().getGroupDriver().getGroupByService(args[1]).getMinOnlineServers()){
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {

                                Driver.getInstance().getGroupDriver().launchService(group, 1);
                            }
                        }, 2*1000);
                    }
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("gstop")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");

                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group, ServiceRest.class);
                    serviceRest.getServices().forEach(liveService -> {
                        Driver.getInstance().getGroupDriver().shutdownService(liveService.getServiceName());
                    });


                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Driver.getInstance().getGroupDriver().launchService(group, Driver.getInstance().getGroupDriver().getGroup(group).getMinOnlineServers());
                        }
                    }, 2*1000);


                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("list")){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            Thread thread = new Thread(() -> {
                Driver.getInstance().getGroupDriver().getGroups().forEach(group -> {
                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "> Groupname: §b" + group.getName());
                    serviceRest.getServices().forEach(liveService -> {
                        if (liveService.getServiceState() == CloudServiceState.STARTING || liveService.getServiceState() == CloudServiceState.STOPPING){
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§e"+liveService.getServiceState()+"§7)");
                        }  if (liveService.getServiceState() == CloudServiceState.LOBBY){
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§a"+liveService.getServiceState()+"§7)");
                        }  if (liveService.getServiceState() == CloudServiceState.INGAME){
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§e"+liveService.getServiceState()+"§7)");
                        }
                    });
                });
            });
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.run();

        }else if (args[0].equalsIgnoreCase("execute")) {
            if (args.length >= 3){
                String msg = "";
                String servicename = args[1];

                if (Driver.getInstance().getGroupDriver().getGroupByService(servicename) == null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                    return false;
                }

                for (int i = 2; i < args.length; i++) {
                    msg = msg + args[i] + " ";
                }

                ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + servicename.split(service.getGeneral().getServerSplitter())[0], ServiceRest.class);
                exists = false;
                serviceRest.getServices().forEach(live -> {
                    if (live.getServiceName().equals(args[1])){
                        exists = true;
                    }
                });

                if (exists){
                     if (Driver.getInstance().getConnectionDriver().isServiceRegistered(servicename)){
                         ServiceSendCommandPacket ServiceSendCommandPacket = new ServiceSendCommandPacket();
                         ServiceSendCommandPacket.setCommand(msg);
                         Channel channel = Driver.getInstance().getConnectionDriver().getServiceChannel(servicename);
                         channel.sendPacket(ServiceSendCommandPacket);
                         logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
                    }else{
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service is not ready to do that ");
                    }

                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2){
                ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                String group = Driver.getInstance().getGroupDriver().getGroupByService(args[1]).getName();
                if (Driver.getInstance().getGroupDriver().getGroup(group) == null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                    return false;
                }
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group, ServiceRest.class);
                exists = false;
                serviceRest.getServices().forEach(live -> {
                    if (live.getServiceName().equals(args[1])){
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Service Name: §b" + live.getServiceName());
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Service Tyoe: §b" + live.getServiceType());
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Service State: §b" + live.getServiceState());
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Running Node: §b" + live.getNode());
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Current CloudPlayers: §b" + live.getCurrentCloudPlayers());
                        logger.log(MSGType.MESSAGETYPE_COMMAND, "selected Port: §b" + live.getSelectedPort());


                        long finalTime =  (System.currentTimeMillis() - live.getStartTime());
                        long millis = finalTime;
                        long secs = millis / 1000;
                        long mins = secs / 60;
                        exists = true;

                        logger.log(MSGType.MESSAGETYPE_COMMAND, "Uptime: §b"+mins+" minute(s) "+(secs-(mins*60))+" second(s)");
                    }
                });

                if (!exists){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bis not ready§7 to do that ");
                }
            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("whitelist")) {
            if (args.length == 3){
                String chose = args[1];
                String target = args[2];
                ServiceConfiguration service1 = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                if (chose.equalsIgnoreCase("add")){
                    if (!service1.getGeneral().getWhitelist().contains(target)){



                        service1.getGeneral().getWhitelist().add(target);


                        new ConfigDriver("./service.json").save(service1);

                        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                        service.getMessages().setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getPrefix()));
                        service.getMessages().setMaintenanceGroupMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceGroupMessage()));
                        service.getMessages().setMaintenanceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceKickMessage()));
                        service.getMessages().setNoFallbackKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getNoFallbackKickMessage()));
                        service.getMessages().setFullNetworkKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullNetworkKickMessage()));
                        service.getMessages().setFullServiceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullServiceKickMessage()));
                        service.getMessages().setOnlyProxyJoinKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getOnlyProxyJoinKickMessage()));
                        service.getMessages().setHubCommandNoFallbackFound(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandNoFallbackFound()));
                        service.getMessages().setHubCommandAlreadyOnFallBack(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandAlreadyOnFallBack()));
                        service.getMessages().setHubCommandSendToAnFallback(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandSendToAnFallback()));
                        service.getMessages().setServiceStartingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStartingNotification()));
                        service.getMessages().setServiceConnectedToProxyNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceConnectedToProxyNotification()));
                        service.getMessages().setServiceStoppingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStoppingNotification()));

                        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("service", service);

                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the player was added to the whitelist");
                    }else {
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the player is already on the whitelist");
                    }
                }else if (chose.equalsIgnoreCase("remove")){
                    if (service1.getGeneral().getWhitelist().contains(target)){
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the player was deleted from the whitelist");



                        service1.getGeneral().getWhitelist().remove(target);


                        new ConfigDriver("./service.json").save(service1);

                        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

                        service.getMessages().setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getPrefix()));
                        service.getMessages().setMaintenanceGroupMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceGroupMessage()));
                        service.getMessages().setMaintenanceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceKickMessage()));
                        service.getMessages().setNoFallbackKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getNoFallbackKickMessage()));
                        service.getMessages().setFullNetworkKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullNetworkKickMessage()));
                        service.getMessages().setFullServiceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullServiceKickMessage()));
                        service.getMessages().setOnlyProxyJoinKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getOnlyProxyJoinKickMessage()));
                        service.getMessages().setHubCommandNoFallbackFound(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandNoFallbackFound()));
                        service.getMessages().setHubCommandAlreadyOnFallBack(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandAlreadyOnFallBack()));
                        service.getMessages().setHubCommandSendToAnFallback(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandSendToAnFallback()));
                        service.getMessages().setServiceStartingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStartingNotification()));
                        service.getMessages().setServiceConnectedToProxyNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceConnectedToProxyNotification()));
                        service.getMessages().setServiceStoppingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStoppingNotification()));

                        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("service", service);


                    }else {
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the player is not on the whitelist");
                    }
                }else {
                        sendHelp(logger);

                }
            } else if (args.length == 2){
                if (args[1].equalsIgnoreCase("list")){
                    ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

                    service.getGeneral().getWhitelist().forEach(string -> {
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §b" + string);
                    });
                }else{

                    sendHelp(logger);
                }
            }
        }else {
            sendHelp(logger);
        }


        return false;
    }

    @Override
    public void sendHelp(Logger logger) {
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice launch §7<§bgroup§7> §7<§bcount§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice stop §7<§bservice§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice gstop §7<§bgroup§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice execute §7<§bservice§7> §7<§bcommand§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice info §7<§bservice§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice whitelist §7<§badd/remove/list§7> §7<§bplayername§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice list");

    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 0){
            results.add("launch");
            results.add("stop");
            results.add("gstop");
            results.add("execute");
            results.add("info");
            results.add("list");
            results.add("whitelist");
            return results;
        }

        if (args[0].equalsIgnoreCase("launch")){
            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }
        }else if (args[0].equalsIgnoreCase("stop")){

            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(group -> {
                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);
                    serviceRest.getServices().forEach(liveService -> {
                        results.add(liveService.getServiceName());
                    });
                });
            }


        }else if (args[0].equalsIgnoreCase("info")){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(group -> {
                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);
                    serviceRest.getServices().forEach(liveService -> {
                        results.add(liveService.getServiceName());
                    });
                });
            }

        }else if (args[0].equalsIgnoreCase("gstop")){
            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }
        }else if (args[0].equalsIgnoreCase("execute")){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(group -> {
                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);
                    serviceRest.getServices().forEach(liveService -> {
                        results.add(liveService.getServiceName());
                    });
                });
            }

        }else if (args[0].equalsIgnoreCase("list")){
        }else if (args[0].equalsIgnoreCase("whitelist")){
            if (args.length == 1){
                results.add("add");
                results.add("remove");
                results.add("list");
            }
        }else {
            results.add("launch");
            results.add("stop");
            results.add("gstop");
            results.add("execute");
            results.add("info");
            results.add("list");
            results.add("whitelist");
        }


        return results;
    }
}
