package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import io.metacloud.webservice.RestDriver;
import lombok.Getter;
import lombok.Setter;

public class Driver {

    private static Driver instance;
    private  CloudStorageDriver cloudStorage;
    private ConsoleDriver consoleDriver;
    private RestDriver restDriver;

    public Driver(){
        instance = this;
        restDriver = new RestDriver();
        cloudStorage = new CloudStorageDriver();
    }


    public void setConsoleDriver(ConsoleDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
    }

    public RestDriver getRestDriver() { return restDriver; }

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
