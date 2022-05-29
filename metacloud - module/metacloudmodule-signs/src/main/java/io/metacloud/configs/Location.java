package io.metacloud.configs;

public class Location {
    private String signUUID;
    private Double locationPosX;
    private Double locationPosY;
    private Double locationPosZ;
    private String locationWorld;
    private String groupName;


    public String getSignUUID() {
        return signUUID;
    }

    public void setSignUUID(String signUUID) {
        this.signUUID = signUUID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getLocationPosX() {
        return locationPosX;
    }

    public void setLocationPosX(Double locationPosX) {
        this.locationPosX = locationPosX;
    }

    public Double getLocationPosY() {
        return locationPosY;
    }

    public void setLocationPosY(Double locationPosY) {
        this.locationPosY = locationPosY;
    }

    public Double getLocationPosZ() {
        return locationPosZ;
    }

    public void setLocationPosZ(Double locationPosZ) {
        this.locationPosZ = locationPosZ;
    }

    public String getLocationWorld() {
        return locationWorld;
    }

    public void setLocationWorld(String locationWorld) {
        this.locationWorld = locationWorld;
    }
}
