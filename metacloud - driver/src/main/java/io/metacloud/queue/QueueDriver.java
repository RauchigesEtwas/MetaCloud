package io.metacloud.queue;

import io.metacloud.Driver;
import io.metacloud.queue.bin.QueueContainer;
import io.metacloud.queue.bin.QueueStatement;

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
                        Driver.getInstance().getGroupDriver().launchService(container.getContent(), 1);
                    }
                    if (container.getQueueStatement() == QueueStatement.STOPPING){
                        Driver.getInstance().getGroupDriver().shutdownService(container.getContent());
                    }


                }

                }
            }, 1000, 1000*5);

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
