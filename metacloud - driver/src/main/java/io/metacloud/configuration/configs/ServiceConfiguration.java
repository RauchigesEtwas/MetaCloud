package io.metacloud.configuration.configs;

import io.metacloud.configuration.IConfig;

public class ServiceConfiguration implements IConfig {


    private String test;

    public ServiceConfiguration() {}


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
