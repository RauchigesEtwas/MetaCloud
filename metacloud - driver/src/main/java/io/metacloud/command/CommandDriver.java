package io.metacloud.command;

import java.util.HashSet;
import java.util.Set;

public class CommandDriver {

    private Set<CloudCommand> commands;

    public CommandDriver() {
        this.commands = new HashSet<>();
    }

    public Set<CloudCommand> getCommands() {
        return commands;
    }

    public void registerCommand(CloudCommand command){
        this.commands.add(command);
    }

    public void unregisterCommand(CloudCommand command){
        this.commands.remove(command);
    }



    public void executeCommand(String line){

    }


    public CloudCommand getCommand(String name){
        for (CloudCommand command : this.commands){
            if(command.getCommand().equalsIgnoreCase(name)){
                return command;
            }
            if (command.getAliases().contains(name)){
                return command;
            }
        }
        return null;
    }
}
