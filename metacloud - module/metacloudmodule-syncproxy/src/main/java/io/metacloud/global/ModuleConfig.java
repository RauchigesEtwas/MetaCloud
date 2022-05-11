package io.metacloud.global;

import io.metacloud.configuration.IConfig;

import java.util.ArrayList;

public class ModuleConfig implements IConfig {



    private ArrayList<Motd> maintenancen;
    private ArrayList<Motd> defaults;
    private ArrayList<Tablist> tablist;

    public ArrayList<Motd> getMaintenancen() {
        return maintenancen;
    }

    public void setMaintenancen(ArrayList<Motd> maintenancen) {
        this.maintenancen = maintenancen;
    }

    public ArrayList<Motd> getDefaults() {
        return defaults;
    }

    public void setDefaults(ArrayList<Motd> defaults) {
        this.defaults = defaults;
    }

    public ArrayList<Tablist> getTablist() {
        return tablist;
    }

    public void setTablist(ArrayList<Tablist> tablist) {
        this.tablist = tablist;
    }
}
