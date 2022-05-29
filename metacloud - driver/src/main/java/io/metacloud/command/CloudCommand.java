package io.metacloud.command;

import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import lombok.var;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public abstract class CloudCommand{
    private String command;
    private String[] aliases;
    private String description;
    private String permission;

    public CloudCommand() {
        final var annotation = getClass().getAnnotation(CommandInfo.class);

        this.command = annotation.command();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
    }


    public abstract boolean performCommand(CloudCommand command, Logger logger, String[] args);
    public abstract ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args);

    public void sendHelp(Logger logger) {

    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAliases() {
        ArrayList<String> resuls = new ArrayList<>();
        for (String al : aliases){
            resuls.add(al);
        }
        return resuls;
    }

}
