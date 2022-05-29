package io.metacloud.events.events;

import io.metacloud.events.bin.IEventStack;

public class NodeConnectToManagerEvent extends IEventStack {

    private String nodeName, hostIp;

    public NodeConnectToManagerEvent(String nodeName, String hostIp) {
        this.nodeName = nodeName;
        this.hostIp = hostIp;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getHostIp() {
        return hostIp;
    }
}
