package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.util.Timer;
import java.util.TimerTask;

@CommandInfo(command = "stop", description = "shutdown the full CloudSystem", aliases = {"end", "shutdown", "exit"})
public class EndCommand extends CloudCommand {



    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {


        if (!Driver.getInstance().getCloudStorage().isShutdown()){
            Driver.getInstance().getCloudStorage().setShutdown(true);

            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "please enter the command again for §bconfirmation§7, you have §b15 seconds §7to do so");


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Driver.getInstance().getCloudStorage().setShutdown(false);
                }
            }, 1000*15);

        }else{
            System.exit(0);
        }

        return false;
    }
}
