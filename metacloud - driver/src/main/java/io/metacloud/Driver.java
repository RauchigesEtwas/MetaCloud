package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import io.metacloud.groups.GroupDriver;
import io.metacloud.network.ConnectionDriver;
import io.metacloud.services.ServiceDriver;
import io.metacloud.webservice.RestDriver;
import lombok.Getter;
import lombok.Setter;

public class Driver {

    private static Driver instance;
    private StorageDriver storageDriver;
    private ConsoleDriver consoleDriver;
    private RestDriver restDriver;
    private ConnectionDriver connectionDriver;
    private GroupDriver groupDriver;
    private ServiceDriver serviceDriver;

    public Driver(){
        instance = this;
        serviceDriver = new ServiceDriver();
        groupDriver = new GroupDriver();
        restDriver = new RestDriver();
        storageDriver = new StorageDriver();
        connectionDriver = new ConnectionDriver();
    }


    public ServiceDriver getServiceDriver() {
        return serviceDriver;
    }

    public ConnectionDriver getConnectionDriver() {
        return connectionDriver;
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
