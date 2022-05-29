package io.metacloud.events.events.signs;

import io.metacloud.events.bin.IEventStack;

public class CloudSignRemoveEvent extends IEventStack {

    private String signuuid;


    public String getSignuuid() {
        return signuuid;
    }

    public void setSignuuid(String signuuid) {
        this.signuuid = signuuid;
    }
}
