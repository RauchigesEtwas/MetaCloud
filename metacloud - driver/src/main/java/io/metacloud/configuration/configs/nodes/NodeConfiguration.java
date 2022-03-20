package io.metacloud.configuration.configs.nodes;

import io.metacloud.configuration.IConfig;

import java.util.ArrayList;
import java.util.List;

public class NodeConfiguration implements IConfig {

    private List<NodeProperties> nodes;


    public NodeConfiguration() {
        nodes = new ArrayList<>();
    }

    public List<NodeProperties> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeProperties> nodes) {
        this.nodes = nodes;
    }
}
