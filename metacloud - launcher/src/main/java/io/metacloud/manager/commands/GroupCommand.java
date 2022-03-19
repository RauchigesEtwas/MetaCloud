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

        }
        return false;
    }


    private void sendHelp(Logger logger){

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup create ");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup delete §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup set maintenance §7<§bname§7> <§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup set maxPlayerCount §7<§bname§7> <§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup set minServiceCount §7<§bname§7> <§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup set staticService §7<§bname§7> <§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup set runningNode §7<§bname§7> <§bvalue§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup list ");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup updateToRest §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bgroup info §7<§bname§7>");
    }

}
