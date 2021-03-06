package io.metacloud.console.data;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import lombok.var;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ConsoleCompleter implements Completer {

    private boolean exeists;

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        final var input = parsedLine.line();
        List<String> suggestions = null;
        if (input.isEmpty() || input.equalsIgnoreCase("")){
            final var result = new LinkedList<String>();
            Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                result.add(command.getCommand());

                command.getAliases().forEach(s -> {
                    result.add(s);
                });
            });
            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);
        }else if (!canBeFinde(input)){
            final var result = new LinkedList<String>();
            Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                result.add(command.getCommand());

                command.getAliases().forEach(s -> {
                    result.add(s);
                });
            });
            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);
        }else {
            var arguments = input.split(" ");
            final var consoleInput = Driver.getInstance().getConsoleDriver().getInputs().peek();
            if (input.isEmpty() || input.indexOf(' ') == -1) {
                if (consoleInput == null) {
                    final var result = new LinkedList<String>();
                    final var toTest = arguments[arguments.length - 1];

                    Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                        if (command.getCommand() != null && (toTest.trim().isEmpty() || command.getCommand().toLowerCase().contains(toTest.toLowerCase()))) {

                            result.add(command.getCommand());
                        }

                        command.getAliases().forEach(s -> {
                            if (s.toLowerCase().contains(toTest.toLowerCase())){
                                result.add(s);
                            }
                        });
                    });

                    if (result.isEmpty() && !Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().isEmpty()) {
                        Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                            result.add(command.getCommand());

                            command.getAliases().forEach(s -> {
                                result.add(s);
                            });
                        });


                    }

                    suggestions = result;

                }else{
                    suggestions = consoleInput.tabCompletes();
                }
            }else{
                if (consoleInput != null) return;
                final var command = Driver.getInstance().getConsoleDriver().getCommandDriver().getCommand(arguments[0]);
                final var result = new LinkedList<String>();

                if(command == null){
                    Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command1 -> {
                        result.add(command1.getCommand());

                        command1.getAliases().forEach(s -> {
                            result.add(s);
                        });
                    });
                }else{
                    if (command.tabComplete(consoleInput, arguments) != null){
                        command.tabComplete(consoleInput, Driver.getInstance().getStorageDriver().dropFirstString(arguments)).forEach(s -> {
                            result.add(s);
                        });
                        suggestions = result;
                    }
                }
                if (suggestions == null || suggestions.isEmpty()) return;


                }
            suggestions.stream().map(Candidate::new).forEach(list::add);

        }
    }


    private boolean canBeFinde(String line){
        ArrayList<String> commandsAndAliases = new ArrayList<>();
        exeists = false;

        if (line.contains(" ")){
            return true;
        }

        Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
            commandsAndAliases.add(command.getCommand());

            command.getAliases().forEach(s -> {
                commandsAndAliases.add(s);
            });
        });

        commandsAndAliases.forEach(command -> {
            if (command.startsWith(line) || command.equals(line)){
                exeists = true;
            }
        });


        return exeists;
    }
}
