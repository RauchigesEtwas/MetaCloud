package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupProperties;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.configuration.configs.group.GroupVersions;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

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
            if (args.length == 8){

                String groupName = args[1];
                String modeType = args[2];
                String memory = args[3];
                String dynamicMemory = args[4];
                String staticService = args[5];
                String version = args[6];
                String node = args[7];

                if (Driver.getInstance().getGroupDriver().getGroup(groupName) != null){
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "A group already §bexists §7under this name");
                    return false;
                }

                if(memory.matches("[0-9]+") && dynamicMemory.matches("[0-9]+")){
                    GroupConfiguration configuration = new GroupConfiguration();
                    if (modeType.equalsIgnoreCase("LOBBY")){
                        configuration.setName(groupName);
                        configuration.setMemory(Integer.valueOf(memory));
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


                            logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");
                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "version: §bPAPERSPIGOT_1_8_8, PAPERSPIGOT_1_9_4, PAPERSPIGOT_1_10_2, PAPERSPIGOT_1_11_2, PAPERSPIGOT_1_12_2, PAPERSPIGOT_1_13_2, PAPERSPIGOT_1_14_4, PAPERSPIGOT_1_15_2, PAPERSPIGOT_1_16_5, PAPERSPIGOT_1_17_1, PAPERSPIGOT_1_18_1");

                        }

                    }else if (modeType.equalsIgnoreCase("PROXY")){
                        configuration.setName(groupName);
                        configuration.setMemory(Integer.valueOf(memory));
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


                            logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");
                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "version: §bBUNGEECORD_LATEST, WATERFALL_LATEST");

                        }
                    }else{
                        configuration.setName(groupName);
                        configuration.setMemory(Integer.valueOf(memory));
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


                            logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "The group has been §bsuccessfully§7 created, all servers will be §bstarted§7 shortly.");


                        }else{
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "You have the following version that you can use");
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "version: §bPAPERSPIGOT_1_8_8, PAPERSPIGOT_1_9_4, PAPERSPIGOT_1_10_2, PAPERSPIGOT_1_11_2, PAPERSPIGOT_1_12_2, PAPERSPIGOT_1_13_2, PAPERSPIGOT_1_14_4, PAPERSPIGOT_1_15_2, PAPERSPIGOT_1_16_5, PAPERSPIGOT_1_17_1, PAPERSPIGOT_1_18_1");

                        }

                    }


                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "the memory and the dynamicmemory must be a number!");
                }

            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("delete")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the group was§b successfully§7 deleted, all services are now §bstopped");
                    Driver.getInstance().getGroupDriver().delete(group);

                    //TODO: shuting Down all Running Server
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else{
                sendHelp(logger);
            }

        }else if (args[0].equalsIgnoreCase("list")){
            Driver.getInstance().getGroupDriver().getGroups().forEach(configuration -> {
                logger.log(MSGType.MESSAGETYPE_COMMAND, true, " > §b" + configuration.getName());
            });
        }else if (args[0].equalsIgnoreCase("info")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Group name: §b" + configuration.getName());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Group mode: §b" + configuration.getMode());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Memory: §b" + configuration.getMemory());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "dynamicMemory: §b" + configuration.getDynamicMemory());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Maintenance: §b" + configuration.getMaintenance());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "staticServices: §b" + configuration.getStaticServices());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "permission: §b" + configuration.getPermission());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "minOnlineServers: §b" + configuration.getMinOnlineServers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "maxOnlineServers: §b" + configuration.getMaxOnlineServers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "maxOnlinePlayers: §b" + configuration.getMaxOnlinePlayers());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "properties.percentForNewServerAutomatically: §b" + configuration.getProperties().getPercentForNewServerAutomatically());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "properties.version: §b" + configuration.getProperties().getVersion());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "properties.template: §b" + configuration.getProperties().getTemplate());
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "properties.node: §b" + configuration.getProperties().getNode());
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
                }
            }else {
                sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("deploy")){
            if (args.length == 2){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().getGroup(group) != null){
                    Driver.getInstance().getGroupDriver().deployOnRest(group, null);
                    logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the server was §bsuccessfully§7 updated to the§b RestAPI");
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
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
                        logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the data has been §bupdated§7 and has been §bsynchronised");
                    }else if (args[2].equalsIgnoreCase("setStaticServices")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].equalsIgnoreCase("true")){
                            configuration.setStaticServices(true);
                        }else {
                            configuration.setStaticServices(false);
                        }
                        Driver.getInstance().getGroupDriver().update(configuration);
                        logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the data has been §bupdated§7 and has been §bsynchronised");
                    }else if (args[2].equalsIgnoreCase("setMaxOnlinePlayers")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].matches("[0-9]+")){
                            configuration.setMaxOnlinePlayers(Integer.valueOf(args[3]));
                            Driver.getInstance().getGroupDriver().update(configuration);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the data has been §bupdated§7 and has been §bsynchronised");
                        }else {
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "please enter a number as value");
                        }
                    }else if (args[2].equalsIgnoreCase("setMinOnlineServers")){
                        GroupConfiguration configuration = Driver.getInstance().getGroupDriver().getGroup(group);
                        if (args[3].matches("[0-9]+")){
                            configuration.setMaxOnlinePlayers(Integer.valueOf(args[3]));
                            Driver.getInstance().getGroupDriver().update(configuration);
                            logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the data has been §bupdated§7 and has been §bsynchronised");
                        }else {
                            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "please enter a number as value");
                        }
                    }else {
                        sendHelp(logger);
                    }
                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified group §bcould not be found§7 in the Cloud ");
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

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup create §7<§bname§7> §7<§bLOBBY/PROXY/GAME§7> §7<§bmemory§7> §7<§bdynamicMemory§7> §7<§bstatic§7> §7<§bversion§7> §7<§bnode§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup edit §7<§bname§7> §7<§bsetMaintenance/setStaticServices/setMaxOnlinePlayers/setMinOnlineServers§7> §7<§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup delete §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup deploy §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup info §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup list");

    }

}
