package io.metacloud.console;

import io.metacloud.Driver;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.CloudColor;
import io.metacloud.console.setup.CloudMainSetup;
import lombok.var;
import org.jline.reader.LineReader;

public final class ConsolReading extends Thread{

    private final String consolePrompt;
    private final ConsoleDriver consoleDriver;
    private final LineReader lineReader;

    public ConsolReading(Logger logger, ConsoleDriver consoleDriver) {
        this.consolePrompt = logger.getColoredString("§bMetaCloud§f@"+ Driver.getInstance().getStorageDriver().getVersion()+" §7» §7");
        this.consoleDriver = consoleDriver;
        this.lineReader = this.consoleDriver.getLineReader();;
    }


    @Override
    public void run() {
        while (!this.isInterrupted()) {
            final var line = this.lineReader.readLine(consolePrompt);
            if (line != null && !line.isEmpty()) {
                final var input = this.consoleDriver.getInputs().poll();

                if(Driver.getInstance().getStorageDriver().isCloudSetup()){
                    if (Driver.getInstance().getStorageDriver().getSetupType().equalsIgnoreCase("MAIN_SETUP")) {
                        new CloudMainSetup(line);
                    }
                } else if (input != null) {
                    input.inputs().accept(line);
                } else {
                    this.consoleDriver.getCommandDriver().executeCommand(line);
                }
            }
        }
    }
}
