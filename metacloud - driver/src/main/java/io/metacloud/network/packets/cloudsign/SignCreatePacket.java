package io.metacloud.networking;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class SignCreatePacket extends Packet {

    private String signUUID;
    private double locationPosX;
    private double locationPosY;
    private double locationPosZ;
    private String locationWorld;
    private String groupName;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("signUUID", signUUID);
        buffer.write("locationPosX", locationPosX);
        buffer.write("locationPosY", locationPosY);
        buffer.write("locationPosZ", locationPosZ);
        buffer.write("locationWorld", locationWorld);
        buffer.write("groupName", groupName);
    }

    @Override
    public void read(IBuffer buffer) {
        signUUID = buffer.read("signUUID", String.class);
        locationPosX = buffer.read("locationPosX", Double.class);
        locationPosY = buffer.read("locationPosY", Double.class);
        locationPosZ = buffer.read("locationPosZ", Double.class);
        locationWorld = buffer.read("locationWorld", String.class);
        groupName = buffer.read("groupName", String.class);
    }

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
