package io.metacloud.events.events;

import io.metacloud.events.bin.IEventStack;

public class NodeDisconnectFromManagerEvent extends IEventStack {

    private String nodeName;

    public NodeDisconnectFromManagerEvent(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }
}
