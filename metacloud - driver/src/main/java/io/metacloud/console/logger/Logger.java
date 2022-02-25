package io.metacloud.console.logger;

import io.metacloud.console.logger.enums.CloudColor;
import io.metacloud.console.logger.enums.MSGType;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Logger {

    public ConsoleReader consoleReader;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "HH:mm:ss" );

    public Logger() {
        try {
            this.consoleReader = new ConsoleReader(System.in, System.out);
        } catch (IOException ignored) { }
        this.consoleReader.setExpandEvents(false);
    }

    public void sendMessage(MSGType type,boolean useCommand, String message){

        switch (type){
            case MESSAGETYPE_INFO:
                printLine(useCommand, "INFO", message, null);
                break;
            case MESSAGETYPE_WARN:
                printLine(useCommand, "WARN", message, null);
                break;
            case MESSAGETYPE_ERROR:
                printLine(useCommand, "ERROR", message, null);
                break;
            case MESSAGETYPE_NETWORK:
                printLine(useCommand, "NETWORK", message, null);
                break;
            case MESSAGETYPE_SETUP:
                printLine(useCommand, "SETUP", message, null);
                break;
            case MESSAGETYPE_EMPTY:
                printLine(useCommand, null, message, null);
                break;
        }

    }


    private void printLine(Boolean usedcommand, String prefix, String message, String print) {
        try {
            String inline = "";

            if(!usedcommand){
                inline = consoleReader.getCursorBuffer().toString();
                consoleReader.setPrompt("");
                consoleReader.resetPromptLine("", "", 0);
            }
            if(prefix == null){
                try {
                    consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + getColoredString(message + CloudColor.RESET.getAnsiCode()));
                    consoleReader.drawLine();
                    consoleReader.flush();

                } catch (IOException exception) {

                }
            }else{
                try {
                    consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] §b"+ prefix + "§7: §r" + CloudColor.RESET.getAnsiCode() + message + CloudColor.RESET.getAnsiCode()));

                    consoleReader.drawLine();
                    consoleReader.flush();
                } catch (IOException exception) {

                }
            }
            if(!usedcommand){

                if(print != null){
                    String coloredPromp = getColoredString("§bMetaCloud §7» §7");
                    consoleReader.setPrompt(getColoredString(coloredPromp));
                    consoleReader.resetPromptLine(getColoredString(coloredPromp), print, print.length());
                }else{
                    String coloredPromp = getColoredString("§bMetaCloud §7» §7");
                    consoleReader.setPrompt(getColoredString(coloredPromp));
                    consoleReader.resetPromptLine(getColoredString(coloredPromp), inline, inline.length());
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }




    /**
     * Read line string.
     *
     * @return the string
     */
    public String readLine() {
        try {
            return this.consoleReader.readLine();
        } catch (IOException ex) {
            return "null";
        }
    }



    /**
     * Gets console reader.
     *
     * @return the console reader
     */
    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
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
