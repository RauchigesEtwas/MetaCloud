package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;

@CommandInfo(command = "clear", description = "clean the howl console", aliases = {"cls"})
public class ClearCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        Driver.getInstance().getConsoleDriver().clearScreen();

        return false;
    }
}
