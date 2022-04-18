package io.metacloud.queue.bin;

public class QueueContainer {
    
    private QueueStatement queueStatement;
    private String content;

    public QueueContainer(QueueStatement queueStatement, String content) {
        this.queueStatement = queueStatement;
        this.content = content;
    }

    public QueueStatement getQueueStatement() {
        return queueStatement;
    }

    public String getContent() {
        return content;
    }
}
