package io.metacloud.console;

import io.metacloud.Driver;
import io.metacloud.command.CommandDriver;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import lombok.SneakyThrows;

import java.io.IOException;

public class ConsoleDriver extends  Thread{


    private CommandDriver commandDriver;
    private Logger logger;

    public ConsoleDriver(){
        this.logger = new Logger();
        this.commandDriver = new CommandDriver();
        this.setDaemon(true);
        this.start();

    }

    @SneakyThrows
    @Override
    public void run() {
        while (!isInterrupted() && isAlive()){
            if(this.commandDriver != null){
                String line;
                String coloredPromp = getLogger().getColoredString("§bMetaCloud §7» §7");
                while ((line = this.getLogger().getConsoleReader().readLine(coloredPromp)) != null) {
                    if (Driver.getInstance().getCloudStorage().isCloudSetup()){

                    }else {
                        if (!line.trim().isEmpty()) {
                        this.getCommandDriver().executeCommand(line);
                    }else {
                        getLogger().sendMessage(MSGType.MESSAGETYPE_INFO, false, "the command was not found please type \"help\" to get help");
                        getLogger().getConsoleReader().setPrompt("");
                        getLogger().getConsoleReader().resetPromptLine("", "", 0);

                    }
                    }
                }

            }
        }
    }

    public void clearScreen(){
        try {
            this.getLogger().getConsoleReader().clearScreen();
        } catch (IOException exception) {
            this.getLogger().sendMessage(MSGType.MESSAGETYPE_ERROR,false, exception.getMessage());
        }
    }

    public CommandDriver getCommandDriver() {
        return commandDriver;
    }

    public Logger getLogger() {
        return logger;
    }
}
