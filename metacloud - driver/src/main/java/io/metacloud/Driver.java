package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import io.metacloud.events.EventDriver;
import io.metacloud.groups.GroupDriver;
import io.metacloud.modules.ModuleDriver;
import io.metacloud.network.ConnectionDriver;
import io.metacloud.queue.QueueDriver;
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
    private ModuleDriver moduleDriver;
    private QueueDriver queueDriver;
    private EventDriver eventDriver;

    public Driver(){
        instance = this;
        eventDriver = new EventDriver();
        moduleDriver = new ModuleDriver();
        serviceDriver = new ServiceDriver();
        groupDriver = new GroupDriver();
        restDriver = new RestDriver();
        storageDriver = new StorageDriver();
        connectionDriver = new ConnectionDriver();
        queueDriver = new QueueDriver();
    }


    public EventDriver getEventDriver() {
        return eventDriver;
    }

    public QueueDriver getQueueDriver() {
        return queueDriver;
    }

    public ModuleDriver getModuleDriver() {
        return moduleDriver;
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
