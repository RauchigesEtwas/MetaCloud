package io.metacloud.queue;

import io.metacloud.Driver;
import io.metacloud.events.events.service.ServiceRemoveEvent;
import io.metacloud.network.packets.services.in.ProxyServiceStartupNoificationPacket;
import io.metacloud.queue.bin.QueueContainer;
import io.metacloud.queue.bin.QueueStatement;
import io.metacloud.services.processes.utils.ServiceStorage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class QueueDriver {

    private Queue<QueueContainer> queue;
    private Thread thread;

    public QueueDriver() {
        this.queue = new LinkedList<>();
        launch();
    }


    public void addTaskToQueue(QueueContainer queueContainer){
        this.queue.add(queueContainer);
    }

    private void launch(){
        this.thread = new Thread(() -> {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                if (!queue.isEmpty()){
                    QueueContainer container = queue.poll();

                    if (container.getQueueStatement() == QueueStatement.LAUNCHING){

                        ServiceStorage storage = new ServiceStorage();
                        storage.setServiceName(container.getServiceName());
                        storage.setGroupConfiguration(container.getGroupConfiguration());
                        storage.setNetworkingPort(container.getNetworkingPort());
                        storage.setRestAPIPort(container.getRestPort());
                        storage.setManagerAddress(container.getManagerAddress());
                        storage.setAuthNetworkingKey(container.getNetworkAuthKey());
                        storage.setAuthRestAPIKey(container.getRestAuthKey());
                        storage.setSelectedPort(container.getPort());

                        ProxyServiceStartupNoificationPacket packet = new ProxyServiceStartupNoificationPacket();
                        packet.setService(container.getServiceName());
                        Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel -> {
                            channel.sendPacket(packet);
                        });


                        Driver.getInstance().getServiceDriver().launchService(storage);
                    }     if (container.getQueueStatement() == QueueStatement.STOPPING){
                        Driver.getInstance().getServiceDriver().haltService(container.getServiceName());
                        Driver.getInstance().getEventDriver().executeEvent(new ServiceRemoveEvent(container.getServiceName(), container.getGroupConfiguration()));
                    }


                }

                }
            }, 1000, 500);

        });
        this.thread.start();
        this.thread.setName("METACLOUD_QUEUE");
        this.thread.setPriority(Thread.MIN_PRIORITY);
    }


    public void destroy(){
        if (this.thread.isAlive())
            this.thread.destroy();
    }
}
