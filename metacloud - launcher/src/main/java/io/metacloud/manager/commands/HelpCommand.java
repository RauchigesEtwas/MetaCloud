package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;


@CommandInfo(command = "help", description = "here you get all commands", aliases = {"hilfe", "?"})
public class HelpCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command1 -> {
            String aliases = "";

            if(command1.getAliases().isEmpty()){

            }else if (command1.getAliases().size() == 1){
                aliases = command1.getAliases().get(0);
            }else {
                for (int i = 0; i !=   command1.getAliases().size(); i++){
                    if(i == 0){
                        aliases =   command1.getAliases().get(i);
                    }else{
                        aliases = aliases + "§7, §b" +   command1.getAliases().get(i);
                    }
                }
            }
            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> " + command1.getCommand() + " §7- Aliases: [§b" +aliases+"§7] ~ §f" + command1.getDescription());
        });

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "threads: §b" + Runtime.getRuntime().availableProcessors());
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "memory: §b" +  (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / (1024 * 1024) + "MB §7/ §b" +  Runtime.getRuntime().totalMemory() / (1024 * 1024) + "MB");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "operating system: §b" + System.getProperty("os.name"));
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "our support: §bhttps://discord.gg/4kKEcaP9WC");

        return false;
    }

}
