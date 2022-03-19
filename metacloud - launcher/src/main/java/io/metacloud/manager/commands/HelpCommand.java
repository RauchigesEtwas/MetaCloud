package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;


@CommandInfo(command = "help", description = "here you get all commands", aliases = {"ask", "?", "commands"})
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
            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §b" + command1.getCommand() + " §7- Aliases: [§b" +aliases+"§7] ~ §f" + command1.getDescription());
        });

        return false;
    }

}
