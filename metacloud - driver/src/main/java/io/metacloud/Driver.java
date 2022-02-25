package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import lombok.Getter;
import lombok.Setter;

public class Driver {

    private static Driver instance;
    private  CloudStorageDriver cloudStorage;
    private ConsoleDriver consoleDriver;

    public Driver(){
        instance = this;
        cloudStorage = new CloudStorageDriver();
    }


    public void setConsoleDriver(ConsoleDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
    }

    public static Driver getInstance() {
        return instance;
    }

    public CloudStorageDriver getCloudStorage() {
        return cloudStorage;
    }

    public ConsoleDriver getConsoleDriver() {
        return consoleDriver;
    }
}
