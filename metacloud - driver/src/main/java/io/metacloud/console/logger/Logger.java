package io.metacloud.console.logger;

import io.metacloud.Driver;
import io.metacloud.console.logger.enums.CloudColor;
import io.metacloud.console.logger.enums.MSGType;
import jline.console.ConsoleReader;
import lombok.SneakyThrows;
import org.fusesource.jansi.Ansi;

import java.io.*;
import java.text.SimpleDateFormat;

public class Logger {

    public ConsoleReader consoleReader;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd.MM HH:mm:ss" );
    private File logs;

    @SneakyThrows
    public Logger() {
        try {
            this.consoleReader = new ConsoleReader(System.in, System.out);
        } catch (IOException ignored) { }

        new File("./local/logs/").mkdirs();

        logs = new File("./local/logs/latest.log");
        if (logs.exists()){
            logs.delete();
        }
        logs.createNewFile();

        this.consoleReader.setExpandEvents(false);
    }

    public void log(MSGType type,boolean useCommand, String message){

        switch (type){
            case MESSAGETYPE_INFO:
                printLine(useCommand, "§3INFO", message, null);
                break;
            case MESSAGETYPE_WARN:
                printLine(useCommand, "§eWARN", message, null);
                break;
            case MESSAGETYPE_ERROR:
                printLine(useCommand, "§cERROR", message, null);
                break;
            case MESSAGETYPE_NETWORK:
                printLine(useCommand, "§3NETWORK", message, null);
                break;
            case MESSAGETYPE_SETUP:
                printLine(useCommand, "§3SETUP", message, null);
                break;
            case MESSAGETYPE_EMPTY:
                printLine(useCommand, null, message, null);
                break;
            case MESSAGETYPE_COMMAND:
                printLine(useCommand, "§3COMMAND", message, null);
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
                    latestLog(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + getColoredString(message + CloudColor.RESET.getAnsiCode()));
                    consoleReader.drawLine();
                    consoleReader.flush();

                } catch (IOException exception) {

                }
            }else{
                try {
                    consoleReader.println(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + getColoredString("§7[§f" + simpleDateFormat.format(System.currentTimeMillis()) +"§7] "+ prefix + "§7: §r" + CloudColor.RESET.getAnsiCode() + message + CloudColor.RESET.getAnsiCode()));
                    latestLog("[" + simpleDateFormat.format(System.currentTimeMillis()) +"] "+ prefix + ": " + CloudColor.RESET.getAnsiCode() + message + CloudColor.RESET.getAnsiCode());
                    consoleReader.drawLine();
                    consoleReader.flush();
                } catch (IOException exception) {

                }
            }
            if(!usedcommand){
                if(print != null){
                    String coloredPromp = getColoredString("§bMetaCloud§f@"+ Driver.getInstance().getCloudStorage().getVersion()+" §7» §7");
                    consoleReader.setPrompt(getColoredString(coloredPromp));
                    consoleReader.resetPromptLine(getColoredString(coloredPromp), print, print.length());
                }else{
                    String coloredPromp = getColoredString("§bMetaCloud§f@"+ Driver.getInstance().getCloudStorage().getVersion()+" §7» §7");
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


    @SneakyThrows
    private void latestLog(String line){
        Writer printWriter= new BufferedWriter(new FileWriter(this.logs));
        printWriter.write(line.replace("§", "&"));
        printWriter.write("\n");
        printWriter.flush();
        printWriter.close();

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
