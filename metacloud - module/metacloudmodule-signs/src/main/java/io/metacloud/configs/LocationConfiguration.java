package io.metacloud.configs;

import io.metacloud.configuration.IConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LocationConfiguration implements IConfig {

    private ArrayList<Location> signs;

    public ArrayList<Location> getSigns() {
        return signs;
    }

    public void setSigns(ArrayList<Location> signs) {
        this.signs = signs;
    }
}
