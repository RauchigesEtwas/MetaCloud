package io.metacloud.console.logger;

public class LoggerQueueInput {

    private String prefix;
    private String message;

    public LoggerQueueInput(String prefix, String message) {
        this.prefix = prefix;
        this.message = message;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage() {
        return message;
    }
}
