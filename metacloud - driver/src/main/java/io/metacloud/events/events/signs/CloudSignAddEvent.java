package io.metacloud.events.events;

import io.metacloud.events.bin.IEventStack;

public class CloudSignAddEvent extends IEventStack {
    private String signUUID;
    private double locationPosX;
    private double locationPosY;
    private double locationPosZ;
    private String locationWorld;
    private String groupName;

    public String getSignUUID() {
        return signUUID;
    }

    public void setSignUUID(String signUUID) {
        this.signUUID = signUUID;
    }

    public double getLocationPosX() {
        return locationPosX;
    }

    public void setLocationPosX(double locationPosX) {
        this.locationPosX = locationPosX;
    }

    public double getLocationPosY() {
        return locationPosY;
    }

    public void setLocationPosY(double locationPosY) {
        this.locationPosY = locationPosY;
    }

    public double getLocationPosZ() {
        return locationPosZ;
    }

    public void setLocationPosZ(double locationPosZ) {
        this.locationPosZ = locationPosZ;
    }

    public String getLocationWorld() {
        return locationWorld;
    }

    public void setLocationWorld(String locationWorld) {
        this.locationWorld = locationWorld;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
