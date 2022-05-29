package io.metacloud.node.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.node.MetaNode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@CommandInfo(command = "stop", description = "shutdown the full CloudSystem", aliases = {"end", "shutdown", "exit"})
public class EndCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {


        if (!Driver.getInstance().getStorageDriver().isShutdown()){
            Driver.getInstance().getStorageDriver().setShutdown(true);

            logger.log(MSGType.MESSAGETYPE_COMMAND,  "please enter the command again for §bconfirmation§7, you have §b15 seconds §7to do so");


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Driver.getInstance().getStorageDriver().setShutdown(false);
                }
            }, 1000*15);

        }else{
            MetaNode.shutdown();
        }


        return false;
    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        return null;
    }
}
