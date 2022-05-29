package io.metacloud.node.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.util.ArrayList;

@CommandInfo(command = "screen", description = "see all screens")
public class ScreenCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {


        if (args.length == 0){
            sendHelp(logger);
        }else if (args[0].equalsIgnoreCase("toggle")){
            if (args.length == 2){
                String service = args[1];
                if (Driver.getInstance().getServiceDriver().getService(service) != null){
                    logger.log(MSGType.MESSAGETYPE_INFO, "the screen of the service is toggled");
                    Driver.getInstance().getServiceDriver().getService(service).toggleScreen();
                }else {
                    logger.log(MSGType.MESSAGETYPE_INFO, "the task was not found in the cloud");
                }
            }else {
                sendHelp(logger);
            }
        }else {
         sendHelp(logger);
        }
        return false;
    }


    @Override
    public void sendHelp(Logger logger) {
        logger.log(MSGType.MESSAGETYPE_INFO, "> §bscreen toggle §7<§bservice§7> ");
    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 0){
            results.add("toggle");
        }else if (args[0].equalsIgnoreCase("toggle")){
            if (args.length == 1){
               Driver.getInstance().getServiceDriver().getRunningProcesses().forEach(cloudService -> {
                   results.add(cloudService.getStorage().getServiceName());
               });
            }
        }else {
            results.add("toggle");
        }

        return results;
    }
}
