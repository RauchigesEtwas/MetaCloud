package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import io.metacloud.groups.GroupDriver;
import io.metacloud.webservice.RestDriver;
import lombok.Getter;
import lombok.Setter;

public class Driver {

    private static Driver instance;
    private  StorageDriver storageDriver;
    private ConsoleDriver consoleDriver;
    private RestDriver restDriver;
    private GroupDriver groupDriver;

    public Driver(){
        instance = this;
        groupDriver = new GroupDriver();
        restDriver = new RestDriver();
        storageDriver = new StorageDriver();
    }


    public GroupDriver getGroupDriver() {
        return groupDriver;
    }

    public void setConsoleDriver(ConsoleDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
    }

    public RestDriver getRestDriver() { return restDriver; }

    public static Driver getInstance() {
        return instance;
    }

    public StorageDriver getStorageDriver() {
        return storageDriver;
    }

    public ConsoleDriver getConsoleDriver() {
        return consoleDriver;
    }
}
