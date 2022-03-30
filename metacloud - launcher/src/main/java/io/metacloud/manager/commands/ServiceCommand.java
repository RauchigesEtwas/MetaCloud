package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.util.function.Consumer;

@CommandInfo(command = "service", description = "manage all services, start and stop them", aliases = {"serv", "srv"})
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
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "the action is now executed...");
                    Driver.getInstance().getGroupDriver().launchService(group, count);
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
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
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "the action is now executed...");
                    Driver.getInstance().getGroupDriver().shutdownService(args[1]);
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified service §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("gstop")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "the action is now executed...");

                    ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                            "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group, ServiceRest.class);
                    serviceRest.getServices().forEach(liveService -> {
                        Driver.getInstance().getGroupDriver().shutdownService(liveService.getServiceName());
                    });
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("list")){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            Driver.getInstance().getGroupDriver().getGroups().forEach(group -> {
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + group.getName(), ServiceRest.class);

                logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> Groupname: §b" + group.getName());

                serviceRest.getServices().forEach(liveService -> {
                    if (liveService.getServiceState() == CloudServiceState.STARTING || liveService.getServiceState() == CloudServiceState.STOPPING){
                        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§e"+liveService.getServiceState()+"§7)");
                    }  if (liveService.getServiceState() == CloudServiceState.LOBBY){
                        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§a"+liveService.getServiceState()+"§7)");
                    }  if (liveService.getServiceState() == CloudServiceState.INGAME){
                        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "service: §b"+liveService.getServiceName() + "§7 | Players: §b" + liveService.getCurrentCloudPlayers() + " players§7 (§e"+liveService.getServiceState()+"§7)");
                    }
                });
            });

        }else{
            sendHelp(logger);
        }


        return false;
    }


    private void sendHelp(Logger logger){

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bservice launch §7<§bgroup§7> §7<§bcount§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bservice stop §7<§bservice§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bservice gstop §7<§bgroup§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bservice list");
    }
}
