package io.metacloud.console.logger.enums;

import org.fusesource.jansi.Ansi;

/*
 * Projectname: VapeCloud
 * Created AT: 21.12.2021/15:06
 * Created by Robin B. (RauchigesEtwas)
 */

public enum CloudColor {

    /**
     * Reset color.
     */
    RESET( 'r',  Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT).boldOff().toString()),
    /**
     * White color.
     */
    WHITE('f', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString()),
    /**
     * Black color.
     */
    BLACK( '0', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    /**
     * Red color.
     */
    RED( 'c', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    /**
     * Yellow color.
     */
    YELLOW( 'e', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    /**
     * Blue color.
     */
    BLUE('9', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    /**
     * Green color.
     */
    GREEN( 'a', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    /**
     * Purple color.
     */
    PURPLE( '5', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
    /**
     * Orange color.
     */
    ORANGE( '6', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString()),
    /**
     * Gray color.
     */
    GRAY( '7', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
    /**
     * Dark red color.
     */
    DARK_RED('4', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
    /**
     * Dark gray color.
     */
    DARK_GRAY( '8', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
    /**
     * Dark blue color.
     */
    DARK_BLUE( '1', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
    /**
     * Dark green color.
     */
    DARK_GREEN( '2', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
    /**
     * Light blue color.
     */
    LIGHT_BLUE( 'b', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    /**
     * Cyan color.
     */
    CYAN( '3', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
    private final String ansiCode;

    private final char index;

    CloudColor(char index, String ansiCode) {
        this.index = index;
        this.ansiCode = ansiCode;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public char getIndex() {
        return index;
    }

    /**
     * Gets ansi code.
     *
     * @return the ansi code
     */
    public String getAnsiCode() {
        return ansiCode;
    }

    /**
     * Strip color string.
     *
     * @param input the input
     * @return the string
     */
    public static String stripColor(String input) {
        return input.replaceAll("\u001B\\[[;\\d]*m", "");
    }




}