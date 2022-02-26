package io.metacloud.console;

import io.metacloud.Driver;
import io.metacloud.command.CommandDriver;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.console.setup.CloudMainSetup;
import jline.console.completer.Completer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class ConsoleDriver extends  Thread implements Serializable {


    private CommandDriver commandDriver;
    private Logger logger;

    @SneakyThrows
    public ConsoleDriver(){
        this.logger = new Logger();
        this.commandDriver = new CommandDriver();
        setDaemon(true);
        setPriority(Thread.MIN_PRIORITY);
        start();

    }

    @SneakyThrows
    @Override
    public void run() {
        String line;
        while (!isInterrupted() && isAlive()){
            if(this.commandDriver != null){
                String coloredPromp = getLogger().getColoredString("§bMetaCloud §7» §7");

                while ((line = this.getLogger().getConsoleReader().readLine(coloredPromp)) != null) {
                    getLogger().getConsoleReader().setPrompt("");
                    getLogger().getConsoleReader().resetPromptLine("", "", 0);
                    if (Driver.getInstance().getCloudStorage().isCloudSetup()){
                        if (Driver.getInstance().getCloudStorage().getSetupType().equalsIgnoreCase("MAIN_SETUP")){
                            new CloudMainSetup(getLogger().consoleReader, line);
                        }
                    }else {
                     if (!line.trim().isEmpty()) {
                        this.getCommandDriver().executeCommand(line);
                    }else if (line.isEmpty()){
                        getLogger().log(MSGType.MESSAGETYPE_INFO, true, "the command was not found please type \"help\" to get help");

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
            this.getLogger().log(MSGType.MESSAGETYPE_ERROR,false, exception.getMessage());
        }
    }

    public CommandDriver getCommandDriver() {
        return commandDriver;
    }

    public Logger getLogger() {
        return logger;
    }
}
