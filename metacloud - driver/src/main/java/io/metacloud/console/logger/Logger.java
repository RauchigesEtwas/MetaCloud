package io.metacloud.console.logger;

import io.metacloud.Driver;
import io.metacloud.console.logger.enums.CloudColor;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.console.logger.logs.SimpleLatestLog;
import lombok.SneakyThrows;
import org.jline.utils.InfoCmp;

import java.io.PrintStream;
import java.text.SimpleDateFormat;

public class Logger {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd.MM HH:mm:ss" );
    public SimpleLatestLog logs;

    @SneakyThrows
    public Logger() {
        logs = new SimpleLatestLog();
        System.setOut(new PrintStream(new LoggerOutputStream(this, MSGType.MESSAGETYPE_INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(this, MSGType.MESSAGETYPE_ERROR), true));
    }

    public void log(MSGType type, String message){

        switch (type){
            case MESSAGETYPE_INFO:
                printLine("§3INFO", message);
                break;
            case MESSAGETYPE_WARN:
                printLine("§eWARN", message);
                break;
            case MESSAGETYPE_ERROR:
                printLine("§cERROR", message);
                break;
            case MESSAGETYPE_NETWORK:
                printLine("§3NETWORK", message);
                break;
            case MESSAGETYPE_SETUP:
                printLine("§3SETUP", message);
                break;
            case MESSAGETYPE_SUCCESS:
                printLine("§aSUCCESS", message);
                break;
            case MESSAGETYPE_EMPTY:
                printLine(null, message);
                break;
            case MESSAGETYPE_COMMAND:
                printLine("§3COMMAND", message);
                break;
            case MESSAGETYPE_NETWORK_FAIL:
                printLine("§cNETWORK", message);
                break;
            case MESSAGETYPE_MODULES:
                printLine("§3MODULES", message);
                break;
        }

    }


    private void printLine(String prefix, String message) {
        try {

            if(prefix == null){
                Driver.getInstance().getConsoleDriver().getTerminal().puts(InfoCmp.Capability.carriage_return);
               Driver.getInstance().getConsoleDriver().getTerminal().writer().println(getColoredString(message + CloudColor.RESET.getAnsiCode()));
               Driver.getInstance().getConsoleDriver().getTerminal().flush();
                Driver.getInstance().getConsoleDriver().redraw();
                this.logs.log(getColoredString(message + CloudColor.RESET.getAnsiCode()));
                this.logs.saveLogs();
            }else{
                Driver.getInstance().getConsoleDriver().getTerminal().puts(InfoCmp.Capability.carriage_return);
                Driver.getInstance().getConsoleDriver().getTerminal().writer().println( getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] "+ prefix + "§7: §r" + CloudColor.RESET.getAnsiCode() + message + CloudColor.RESET.getAnsiCode()));
                Driver.getInstance().getConsoleDriver().getTerminal().flush();
                Driver.getInstance().getConsoleDriver().redraw();

                this.logs.log( getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] "+ prefix + "§7: §r" + CloudColor.RESET.getAnsiCode() + message + CloudColor.RESET.getAnsiCode()));
                this.logs.saveLogs();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
