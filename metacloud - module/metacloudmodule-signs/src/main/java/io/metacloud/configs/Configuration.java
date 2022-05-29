package io.metacloud.configs;

import io.metacloud.configuration.IConfig;

import java.util.ArrayList;

public class Configuration implements IConfig {

    private ArrayList<SignLayout> empty;
    private ArrayList<SignLayout> online;
    private ArrayList<SignLayout> full;
    private ArrayList<SignLayout> maintenance;
    private ArrayList<SignLayout> loading;

    public ArrayList<SignLayout> getEmpty() {
        return empty;
    }

    public void setEmpty(ArrayList<SignLayout> empty) {
        this.empty = empty;
    }

    public ArrayList<SignLayout> getOnline() {
        return online;
    }

    public void setOnline(ArrayList<SignLayout> online) {
        this.online = online;
    }

    public ArrayList<SignLayout> getFull() {
        return full;
    }

    public void setFull(ArrayList<SignLayout> full) {
        this.full = full;
    }

    public ArrayList<SignLayout> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(ArrayList<SignLayout> maintenance) {
        this.maintenance = maintenance;
    }

    public ArrayList<SignLayout> getLoading() {
        return loading;
    }

    public void setLoading(ArrayList<SignLayout> loading) {
        this.loading = loading;
    }
}
