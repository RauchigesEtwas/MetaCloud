package io.metacloud.services.processes;

import io.metacloud.services.processes.utils.ProcessStorage;

public class CloudProcess {


    private Thread processThread;
    private Process process;
    private ProcessStorage storage;

    public CloudProcess() {}


    public CloudProcess bindStorage(ProcessStorage storage){
        this.storage = storage;

        return this;
    }

    public void run(){

        this.processThread = new Thread(() -> {

        });
        this.processThread.setPriority(Thread.MIN_PRIORITY);
        this.processThread.setDaemon(false);
        this.processThread.start();

    }


    public void stop(){
        this.process.destroy();
        this.processThread.stop();
    }

}
