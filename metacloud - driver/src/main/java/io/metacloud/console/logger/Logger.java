package io.metacloud.console.logger;

import io.metacloud.Driver;
import io.metacloud.console.logger.enums.CloudColor;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.console.logger.logs.SimpleLatestLog;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.utils.InfoCmp;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Logger {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd.MM HH:mm:ss" );
    private Queue<LoggerQueueInput> loggerQueue;
    public SimpleLatestLog logs;

    @SneakyThrows
    public Logger() {
        logs = new SimpleLatestLog();
        loggerQueue = new LinkedList<>();
        printLine();
        System.setOut(new PrintStream(new LoggerOutputStream(this, MSGType.MESSAGETYPE_INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(this, MSGType.MESSAGETYPE_ERROR), true));
    }

    public void log(MSGType type, String message){

        switch (type){
            case MESSAGETYPE_INFO:
                this.loggerQueue.add(new LoggerQueueInput("§3INFO", message));
                break;
            case MESSAGETYPE_WARN:
                this.loggerQueue.add(new LoggerQueueInput("§eWARN", message));
                break;
            case MESSAGETYPE_ERROR:
                this.loggerQueue.add(new LoggerQueueInput("§cERROR", message));
                break;
            case MESSAGETYPE_NETWORK:
                this.loggerQueue.add(new LoggerQueueInput("§3NETWORK", message));
                break;
            case MESSAGETYPE_SETUP:
                this.loggerQueue.add(new LoggerQueueInput("§3SETUP", message));
                break;
            case MESSAGETYPE_SUCCESS:
                this.loggerQueue.add(new LoggerQueueInput("§aSUCCESS", message));
                break;
            case MESSAGETYPE_EMPTY:
                this.loggerQueue.add(new LoggerQueueInput(null, message));
                break;
            case MESSAGETYPE_COMMAND:
                this.loggerQueue.add(new LoggerQueueInput("§3COMMAND", message));

                break;
            case MESSAGETYPE_NETWORK_FAIL:
                this.loggerQueue.add(new LoggerQueueInput("§cNETWORK", message));

                break;
            case MESSAGETYPE_MODULES:
                this.loggerQueue.add(new LoggerQueueInput("§3MODULES", message));

                break;
        }

    }





    private void printLine() {

        new Thread(() -> {

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!loggerQueue.isEmpty()){
                        LoggerQueueInput input = loggerQueue.poll();
                        try {

                            if(input.getPrefix() == null){
                                try {
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.CLEAR);
                                    Driver.getInstance().getConsoleDriver().getLineReader().getTerminal().writer().println(getColoredString(input.getMessage()  + CloudColor.RESET.getAnsiCode()));
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.REDRAW_LINE);
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.REDISPLAY);
                                    Driver.getInstance().getConsoleDriver().getLineReader().getTerminal().writer().flush();
                                }catch (Exception ignored){}

                                logs.log(getColoredString(input.getMessage() + CloudColor.RESET.getAnsiCode()));
                                logs.saveLogs();
                            }else{

                                try {
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.CLEAR);
                                    Driver.getInstance().getConsoleDriver().getLineReader().getTerminal().writer().println( getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] "+ input.getPrefix() + "§7: §r" + CloudColor.RESET.getAnsiCode() + input.getMessage()  + CloudColor.RESET.getAnsiCode()));
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.REDRAW_LINE);
                                    Driver.getInstance().getConsoleDriver().getLineReader().callWidget(LineReader.REDISPLAY);
                                    Driver.getInstance().getConsoleDriver().getLineReader().getTerminal().writer().flush();

                                }catch (Exception ignored){}
                                logs.log( getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] "+ input.getPrefix() + "§7: §r" + CloudColor.RESET.getAnsiCode() + input.getMessage()  + CloudColor.RESET.getAnsiCode()));
                                logs.saveLogs();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000, 1);

        }).start();
    }









    /**
     * Color string string.
     *
     * @param text the text
     * @return the string
     */
    public String getColoredString(String text) {

        for (CloudColor consoleColour : CloudColor.values()) {
            text = text.replace('§' + "" + consoleColour.getIndex(), consoleColour.getAnsiCode());
        }

        return text;
    }
}
