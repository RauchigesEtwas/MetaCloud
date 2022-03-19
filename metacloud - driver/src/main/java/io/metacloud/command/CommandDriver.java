package io.metacloud.command;

import io.metacloud.Driver;
import io.metacloud.console.logger.enums.MSGType;
import lombok.SneakyThrows;

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


    @SneakyThrows
    public void executeCommand(String line){
        Driver.getInstance().getConsoleDriver().getLogger().getConsoleReader().resetPromptLine("", "", 0);
        Driver.getInstance().getConsoleDriver().getLogger().getConsoleReader().setPrompt("");

        CloudCommand command = getCommand(line.split(" ")[0]);
        String[] args = Driver.getInstance().getStorageDriver().dropFirstString(line.split(" "));
        if(command != null){
            if(Driver.getInstance().getConsoleDriver().isAlive()){
                command.performCommand(command, Driver.getInstance().getConsoleDriver().getLogger(), args);
                Driver.getInstance().getConsoleDriver().getLogger().getConsoleReader().resetPromptLine("", "", 0);
                Driver.getInstance().getConsoleDriver().getLogger().getConsoleReader().setPrompt("");
            }
        }else {
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, true, "the command was not found please type \"help\" to get help");
        }
    }


    public CloudCommand getCommand(String name){
        for (CloudCommand command : getCommands()){
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
