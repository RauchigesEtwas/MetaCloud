package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.services.ServiceSendCommandPacket;
import io.metacloud.queue.bin.QueueContainer;
import io.metacloud.queue.bin.QueueStatement;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.util.ArrayList;

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
                int count = Integer.parseInt(args[2]);
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
                    for (int i = 0; i != count; i++){
                        Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING, group));
                    }
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("stop")){
            if (args.length == 2){
                ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                String group = args[1].split(service.getGeneral().getServerSplitter())[0];
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
                    Thread execuet = new Thread(() -> {
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");

                        ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                                "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group, ServiceRest.class);
                        serviceRest.getServices().forEach(liveService -> {
                            Driver.getInstance().getGroupDriver().shutdownService(liveService.getServiceName());
                        });
                    });
                    execuet.setPriority(Thread.MIN_PRIORITY);
                    execuet.run();
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
                for (int i = 3; i < args.length; i++) {
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
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
                    GroupConfiguration group = Driver.getInstance().getGroupDriver().getGroup(servicename.split(service.getGeneral().getServerSplitter())[0]);
                    if (Driver.getInstance().getConnectionDriver().isServiceRegistered(servicename)){
                        Channel channel = Driver.getInstance().getConnectionDriver().getServiceChannel(servicename);

                        ServiceSendCommandPacket ServiceSendCommandPacket = new ServiceSendCommandPacket();
                        ServiceSendCommandPacket.setCommand(msg);
                        channel.sendPacket(ServiceSendCommandPacket);

                    }else{
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service is not ready to do that ");
                    }

                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }
        }else {
            sendHelp(logger);
        }


        return false;
    }


    private void sendHelp(Logger logger){

        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice launch §7<§bgroup§7> §7<§bcount§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice stop §7<§bservice§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice gstop §7<§bgroup§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bservice execute §7<§bservice§7> §7<§bcommand§7>");
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
        }else {
            results.add("launch");
            results.add("stop");
            results.add("gstop");
            results.add("execute");
            results.add("info");
            results.add("list");
        }


        return results;
    }
}
