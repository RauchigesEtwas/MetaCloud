package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;


@CommandInfo(command = "group",
        description = "manage all the groups of the cloud, you can create them for example",
        aliases = {"template", "g"})
public class GroupCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        if (args.length == 0){
            sendHelp(logger);
        }if (args.length == 1){
            if (args[0].equalsIgnoreCase("list")){
                logger.log(MSGType.MESSAGETYPE_COMMAND, true, "the following groups were found");
                Driver.getInstance().getCloudStorage().getGroups().forEach(s -> {
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §f" + s);
                });
            }else if (args[0].equalsIgnoreCase("create")) {

            }else {
                sendHelp(logger);
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("delete")) {

            }else {
                sendHelp(logger);
            }
        }else if (args.length == 3){
            if (args[0].equalsIgnoreCase("setmaintenance")) {

            }else  if (args[0].equalsIgnoreCase("setminonline")) {

            }else {
                sendHelp(logger);
            }
        }else{
            sendHelp(logger);
        }

        return false;
    }


    private void sendHelp(Logger logger){
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> group create §7- §fcreate a new group in the cloud");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> group delete <§bgroup§7> §7- §fdelete an existing group in the cloud");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> group list §7- §fsee all groups from the cloud");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> group setmaintenance <§bgroup§7> <§btrue§7/§bfalse§7> §7- §fchange the maintenance status of the group");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> group setminonline <§bgroup§7> <§binteger§7> §7- §fset the online services");
    }

}
