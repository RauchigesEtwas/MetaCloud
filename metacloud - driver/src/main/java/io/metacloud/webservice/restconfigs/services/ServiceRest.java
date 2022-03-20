package io.metacloud.webservice.restconfigs.services;

import io.metacloud.webservice.bin.IRestConfig;

import java.util.ArrayList;

public class ServiceRest implements IRestConfig {

    private ArrayList<LiveService> services;
    private ArrayList<Integer> usedIDs;

    public ServiceRest() {
        usedIDs = new ArrayList<>();
        services = new ArrayList<>();
    }

    public ArrayList<LiveService> getServices() {
        return services;
    }

    public ArrayList<Integer> getUsedIDs() {
        return usedIDs;
    }
}
