package io.metacloud.command;

import java.util.ArrayList;

public abstract class CloudCommand {
    private String command;
    private String[] aliases;
    private String description;
    private String permission;

    public CloudCommand(String command, String description, String permission, String... aliases) {
        this.command = command;
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
    }


    public abstract boolean executeCommand(CloudCommand command, String[] args);

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
