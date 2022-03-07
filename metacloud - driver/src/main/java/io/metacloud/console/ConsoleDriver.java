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
        label29: while ( isAlive()){
            try {
                while (true) {
                    if ((line = this.getLogger().getConsoleReader().readLine(((Driver.getInstance() == null) ? "" : ((getLogger().getColoredString("§bMetaCloud§f@"+Driver.getInstance().getCloudStorage().getVersion()+" §7=» §7") == null) ? "" : getLogger().getColoredString("§3MetaCloud§f@"+Driver.getInstance().getCloudStorage().getVersion()+" §7=» §7"))))) != null){
                        getLogger().getConsoleReader().setPrompt("");
                        getLogger().getConsoleReader().resetPromptLine("", "", 0);
                        if (Driver.getInstance().getCloudStorage().isCloudSetup()) {
                            getLogger().getConsoleReader().setPrompt("");
                            if (Driver.getInstance().getCloudStorage().getSetupType().equalsIgnoreCase("MAIN_SETUP")) {
                                new CloudMainSetup(getLogger().consoleReader, line);
                            }
                            continue;
                        }
                        if (!line.trim().isEmpty()) {
                            getLogger().getConsoleReader().setPrompt("");
                            this.getCommandDriver().executeCommand(line);
                            continue;
                        }
                        continue label29;
                    }
                    continue label29;
                }
            }catch (IOException exception){

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
