package io.metacloud.console;

import io.metacloud.command.CommandDriver;
import io.metacloud.console.data.ConsoleCompleter;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public final class ConsoleDriver {
    private final Logger logger;
    private Thread consoleReadingThread;
    private final Terminal terminal;
    private final CommandDriver commandDriver;
    private final LineReader lineReader;
    private final Queue<ConsoleStorageLine> inputs;


    public CommandDriver getCommandDriver() {
        return commandDriver;
    }

    public Logger getLogger() {
        return logger;
    }

    public ConsoleDriver() throws IOException {

        System.setProperty("library.jansi.version", "MetaCloud");

        this.logger = new Logger();
        this.commandDriver = new CommandDriver();
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .streams(System.in, System.out)
                .encoding(StandardCharsets.UTF_8)
                .build();

        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .variable(LineReader.BELL_STYLE, "off")
                .completer(new ConsoleCompleter())
                .build();


        this.inputs = new LinkedList<>();
    }


    public void start() {
        this.consoleReadingThread = new ConsolReading(this.logger, this);
        this.consoleReadingThread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        this.consoleReadingThread.start();
    }

    public void clearConsole() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redraw();
    }

    public void redraw() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }


    public void setCommandHistory(List<String> history) {
        try {
            this.lineReader.getHistory().purge();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (history != null) {
            for (String s : history) {
                this.lineReader.getHistory().add(s);
            }
        }
    }


    public Terminal getTerminal() {
        return this.terminal;
    }

    public LineReader getLineReader() {
        return this.lineReader;
    }

    public void addInput(final Consumer<String> input, final List<String> tabCompletions) {
        this.inputs.add(new ConsoleStorageLine(input, tabCompletions));
    }

    public Queue<ConsoleStorageLine> getInputs() {
        return this.inputs;
    }
}
