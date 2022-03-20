package io.metacloud.services.processes;


import io.metacloud.services.processes.utils.ServiceStorage;

public class CloudService {


    private Thread serviceThread;
    private Process service;
    private ServiceStorage storage;

    public CloudService() {}


    public CloudService bindStorage(ServiceStorage storage){
        this.storage = storage;
        return this;
    }

    public void run(){

        this.serviceThread = new Thread(() -> {

        });
        this.serviceThread.setPriority(Thread.MIN_PRIORITY);
        this.serviceThread.setDaemon(false);
        this.serviceThread.start();

    }

    public void stop(){
        this.service.destroy();
        this.serviceThread.stop();
    }


    public ServiceStorage getStorage() {
        return storage;
    }
}
