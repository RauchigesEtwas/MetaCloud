package io.metacloud.console.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsoleStorageLine {

    public List<String> tabCompletes;
    public Consumer<String> inputs;

    public ConsoleStorageLine(Consumer<String> inputs, List<String> tabCompletes) {
        this.tabCompletes = tabCompletes;
        this.inputs = inputs;
    }

    public Consumer<String> inputs() {
        return inputs;
    }

    public List<String> tabCompletes() {
        return tabCompletes;
    }
}
