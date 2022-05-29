package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupProperties;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.configuration.configs.group.GroupVersions;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.nodes.in.NodeSyncTemplatePacket;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Consumer;


@CommandInfo(command = "group",
        description = "manage all the groups of the cloud, you can create them for example",
        aliases = {"template", "g"})
public class GroupCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        if (args.length == 0){
            sendHelp(logger);

        }else if (args[0].equalsIgnoreCase("create")){
            if (args.length == 7){

                String groupName = args[1];
                String modeType = args[2];
                String dynamicMemory = args[3];
                String staticService = args[4];
                String version = args[5];
                String node = args[6];

                if (Driver.getInstance().getGroupDriver().getGroup(groupName) != null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "A group already §bexists §7under this name");
                    return false;
                }

                if(dynamicMemory.matches("[0-9]+")){
                    GroupConfiguration configuration = new GroupConfiguration();
                    if (modeType.equalsIgnoreCase("LOBBY")){
                        configuration.setName(groupName);
                        configuration.setDynamicMemory(Integer.valueOf(dynamicMemory));
                        configuration.setMaintenance(true);
                        if (staticService.equalsIgnoreCase("true")){
                            configuration.setStaticServices(true);
                        }else {
                            configuration.setStaticServices(false);
                        }
                        configuration.setMaxOnlinePlayers(50);
                        configuration.setMode(GroupType.LOBBY);
                        configuration.setMaxOnlineServers(-1);
                        configuration.setMinOnlineServers(1);
                        configuration.setMinOnlineServers(1);
                        GroupProperties properties = new GroupProperties();
                        properties.setTemplate("/local/templates/" + groupName + "/" );
                        properties.setNode(node);
                        properties.setPercentForNewServerAutomatically(80);
                        if (Driver.getInstance().getStorageDriver().versionCheck(version.toUpperCase(), "SERVER")){
                            properties.setVersion(GroupVersions.valueOf(version.toUpperCase()));
                            configuration.setProperties(properties);
                            Driver.getInstance().getGroupDriver().create(configuration);

                            Driver.getInstance().getGroupDriver().launchService(groupName, 1);

                            logger.log(MSGType.MESSAGETYPE_SUCCESS,  "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");
                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "version: §bPAPERSPIGOT_1_8_8, PAPERSPIGOT_1_9_4, PAPERSPIGOT_1_10_2, PAPERSPIGOT_1_11_2, PAPERSPIGOT_1_12_2, PAPERSPIGOT_1_13_2, PAPERSPIGOT_1_14_4, PAPERSPIGOT_1_15_2, PAPERSPIGOT_1_16_5, PAPERSPIGOT_1_17_1, PAPERSPIGOT_1_18_1");

                        }

                    }else if (modeType.equalsIgnoreCase("PROXY")){
                        configuration.setName(groupName);
                        configuration.setDynamicMemory(Integer.valueOf(dynamicMemory));
                        configuration.setMaintenance(true);
                        if (staticService.equalsIgnoreCase("true")){
                            configuration.setStaticServices(true);
                        }else {
                            configuration.setStaticServices(false);
                        }
                        configuration.setMaxOnlinePlayers(50);
                        configuration.setMode(GroupType.PROXY);
                        configuration.setMaxOnlineServers(-1);
                        configuration.setMinOnlineServers(1);
                        configuration.setMinOnlineServers(1);
                        GroupProperties properties = new GroupProperties();
                        properties.setTemplate("/local/templates/" + groupName + "/" );
                        properties.setNode(node);
                        properties.setPercentForNewServerAutomatically(80);
                        if (Driver.getInstance().getStorageDriver().versionCheck(version.toUpperCase(), "PROXY")){
                            properties.setVersion(GroupVersions.valueOf(version.toUpperCase()));
                            configuration.setProperties(properties);
                            Driver.getInstance().getGroupDriver().create(configuration);

                            Driver.getInstance().getGroupDriver().launchService(groupName, 1);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS,  "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");
                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "version: §bBUNGEECORD_LATEST, WATERFALL_LATEST");

                        }
                    }else{
                        configuration.setName(groupName);
                        configuration.setDynamicMemory(Integer.valueOf(dynamicMemory));
                        configuration.setMaintenance(true);
                        if (staticService.equalsIgnoreCase("true")){
                            configuration.setStaticServices(true);
                        }else {
                            configuration.setStaticServices(false);
                        }
                        configuration.setMaxOnlinePlayers(50);
                        configuration.setMode(GroupType.GAME);
                        configuration.setMaxOnlineServers(-1);
                        configuration.setMinOnlineServers(1);
                        configuration.setMinOnlineServers(1);
                        GroupProperties properties = new GroupProperties();
                        properties.setTemplate("/local/templates/" + groupName + "/" );
                        properties.setNode(node);
                        properties.setPercentForNewServerAutomatically(80);
                        if (Driver.getInstance().getStorageDriver().versionCheck(version.toUpperCase(), "SERVER")){
                            properties.setVersion(GroupVersions.valueOf(version.toUpperCase()));
                            configuration.setProperties(properties);

                            Driver.getInstance().getGroupDriver().create(configuration);


                            Driver.getInstance().getGroupDriver().launchService(groupName, 1);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS,  "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");


                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "version: §bPAPERSPIGOT_1_8_8, PAPERSPIGOT_1_9_4, PAPERSPIGOT_1_10_2, PAPERSPIGOT_1_11_2, PAPERSPIGOT_1_12_2, PAPERSPIGOT_1_13_2, PAPERSPIGOT_1_14_4, PAPERSPIGOT_1_15_2, PAPERSPIGOT_1_16_5, PAPERSPIGOT_1_17_1, PAPERSPIGOT_1_18_1");

                        }

                    }


                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "the memory and the dynamicmemory must be a number!");
                }

            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("delete")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the group was§b successfully§7 deleted, all services are now §bstopped");
                    Driver.getInstance().getGroupDriver().delete(group);

                    //TODO: shuting Down all Running Server
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else{
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("list")){
            Driver.getInstance().getGroupDriver().getGroups().forEach(configuration -> {
                logger.log(MSGType.MESSAGETYPE_COMMAND,  " > §b" + configuration.getName());
            });
        }else if (args[0].equalsIgnoreCase("info")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "Group name: §b" + configuration.getName());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "Group mode: §b" + configuration.getMode());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "dynamicMemory: §b" + configuration.getDynamicMemory() + "MB");
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "Maintenance: §b" + configuration.getMaintenance());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "staticServices: §b" + configuration.getStaticServices());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "permission: §b" + configuration.getPermission());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "minOnlineServers: §b" + configuration.getMinOnlineServers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "maxOnlineServers: §b" + configuration.getMaxOnlineServers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "maxOnlinePlayers: §b" + configuration.getMaxOnlinePlayers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "properties.percentForNewServerAutomatically: §b" + configuration.getProperties().getPercentForNewServerAutomatically());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "properties.version: §b" + configuration.getProperties().getVersion());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "properties.template: §b" + configuration.getProperties().getTemplate());
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "properties.node: §b" + configuration.getProperties().getNode());
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("deploy")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    Driver.getInstance().getGroupDriver().deployOnRest(group, null);
                    logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the server was §bsuccessfully§7 updated to the§b RestAPI");
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("edit")){
            if (args.length == 4){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    if (args[2].equalsIgnoreCase("setMaintenance")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].equalsIgnoreCase("true")){
                            configuration.setMaintenance(true);
                        }else {
                            configuration.setMaintenance(false);
                        }
                        Driver.getInstance().getGroupDriver().update(configuration);
                        logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the data has been §bupdated§7 and has been §bsynchronised");
                    }else if (args[2].equalsIgnoreCase("setStaticServices")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].equalsIgnoreCase("true")){
                            configuration.setStaticServices(true);
                        }else {
                            configuration.setStaticServices(false);
                        }
                        Driver.getInstance().getGroupDriver().update(configuration);
                        logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the data has been §bupdated§7 and has been §bsynchronised");
                    }else if (args[2].equalsIgnoreCase("setMaxOnlinePlayers")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].matches("[0-9]+")){
                            configuration.setMaxOnlinePlayers(Integer.valueOf(args[3]));
                            Driver.getInstance().getGroupDriver().update(configuration);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the data has been §bupdated§7 and has been §bsynchronised");
                        }else {
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "please enter a number as value");
                        }
                    }else if (args[2].equalsIgnoreCase("setMinOnlineServers")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].matches("[0-9]+")){
                            configuration.setMaxOnlinePlayers(Integer.valueOf(args[3]));
                            Driver.getInstance().getGroupDriver().update(configuration);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the data has been §bupdated§7 and has been §bsynchronised");
                        }else {
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "please enter a number as value");
                        }
                    }else {
                        sendHelp(logger);
                    }
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified group §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("synctemplate")) {
            if (args.length == 2){
                GroupConfiguration group = Driver.getInstance().getGroupDriver().getGroupByService(args[1]);
                if (group == null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                    return false;
                }else{
                    if (group.getProperties().getNode().equalsIgnoreCase("InternalNode")){
                        if (new File("./live/" + group.getName()+"/" + args[1]+ "/").exists()){
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "the template was §bupdated§7, everything was copied from §b" + args[1]);
                            try {
                                FileUtils.deleteDirectory(new File(group.getProperties().getTemplate()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            new File(group.getProperties().getTemplate()).mkdirs();
                            try {
                                FileUtils.copyDirectory(new File("./live/" + group.getName()+"/" + args[1]+ "/"), new File("." + group.getProperties().getTemplate()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified service §bcould not be found§7 in the Cloud ");
                            return false;
                        }
                    }else{
                        NodeSyncTemplatePacket packet = new NodeSyncTemplatePacket();
                        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the template was §bupdated§7, everything was copied from §b" + args[1]);

                        packet.setServiceName(args[1]);
                        packet.setGroupName(group.getName());
                        packet.setTemplatePath(group.getProperties().getTemplate());
                        Driver.getInstance().getConnectionDriver().getNodeChannel(group.getProperties().getNode()).sendPacket(packet);
                    }
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

        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup create §7<§bname§7> §7<§bLOBBY/PROXY/GAME§7> §7<§bmemory(in mb)§7> §7<§bstatic§7> §7<§bversion§7> §7<§bnode§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup edit §7<§bname§7> §7<§bsetMaintenance/setStaticServices/setMaxOnlinePlayers/setMinOnlineServers§7> §7<§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup delete §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup deploy §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup info §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup synctemplate §7<§bservice§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bgroup list");

    }
    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 0){
            results.add("create");
            results.add("edit");
            results.add("delete");
            results.add("deploy");
            results.add("info");
            results.add("synctemplate");
            results.add("list");
            return results;
        }

        if (args[0].equalsIgnoreCase("create")){

            if (args.length == 2){
                results.add("LOBBY");
                results.add("PROXY");
                results.add("GAME");
            }else if (args.length == 4){
                results.add("true");
                results.add("false");
            }else if (args.length == 5){
                for (GroupVersions version : GroupVersions.values()){
                    if (args[1].equalsIgnoreCase("PROXY")){
                        if (version == GroupVersions.BUNGEECORD_LATEST || version == GroupVersions.WATERFALL_LATEST){
                            results.add(version.toString());
                        }
                    }else {
                        if (version == GroupVersions.BUNGEECORD_LATEST || version == GroupVersions.WATERFALL_LATEST){

                        }else{
                            results.add(version.toString());
                        }
                    }
                }
            }else if (args.length == 6){
                NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
                configuration.getNodes().forEach(properties -> {
                    results.add(properties.getNodeName());
                });
            }

        }else   if (args[0].equalsIgnoreCase("edit")){

            if (args.length == 1){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }else if (args.length == 2){
                results.add("setMaintenance");
                results.add("setStaticServices");
                results.add("setMaxOnlinePlayers");
                results.add("setMinOnlineServers");
            }
        }else  if (args[0].equalsIgnoreCase("delete")){
            if (args.length != 2){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }
        }else if (args[0].equalsIgnoreCase("deploy")){
            if (args.length != 2){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }
        }else if (args[0].equalsIgnoreCase("info")){
            if (args.length != 2){
                Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                    results.add(groupConfiguration.getName());
                });
            }
        }else if (args[0].equalsIgnoreCase("list")){
        }else if (args[0].equalsIgnoreCase("synctemplate")) {
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
        }else {
            results.add("create");
            results.add("edit");
            results.add("delete");
            results.add("deploy");
            results.add("info");
            results.add("synctemplate");
            results.add("list");
        }


        return results;
    }

}
