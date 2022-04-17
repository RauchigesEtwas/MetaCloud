package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;

import java.util.ArrayList;

@CommandInfo(command = "clear", description = "clean the howl console", aliases = {"cls"})
public class ClearCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        Driver.getInstance().getConsoleDriver().clearConsole();

        return false;
    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        return null;
    }
}
