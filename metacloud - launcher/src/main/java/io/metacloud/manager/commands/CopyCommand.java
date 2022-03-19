package io.metacloud.manager.commands;

import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;

@CommandInfo(command = "copy",  description = "copy an Service into an Template", aliases = {"cp"})
public class CopyCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {
        return false;
    }
}
