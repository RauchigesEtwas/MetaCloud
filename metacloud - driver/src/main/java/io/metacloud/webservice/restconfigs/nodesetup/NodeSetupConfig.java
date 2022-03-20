package io.metacloud.webservice.restconfigs.nodesetup;

import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.webservice.bin.IRestConfig;

public class NodeSetupConfig implements IRestConfig {

    private String nodeName;
    private NodeSetupCommunication communication;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public NodeSetupCommunication getCommunication() {
        return communication;
    }

    public void setCommunication(NodeSetupCommunication communication) {
        this.communication = communication;
    }


}
