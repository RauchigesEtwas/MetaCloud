package io.metacloud.webservice.restconfigs.livenodes;

import io.metacloud.webservice.bin.IRestConfig;

import java.util.ArrayList;

public class NodesRestConfig implements IRestConfig {

    private String name;
    private ArrayList<String> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }
}
