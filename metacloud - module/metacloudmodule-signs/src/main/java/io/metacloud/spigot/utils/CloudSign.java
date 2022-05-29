package io.metacloud.spigot.utils;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.Locale;

public class CloudSign {

    private String SignUUID;
    private Location location;
    private String group;
    private String service;
    private String host;
    private Integer port;
    private BlockFace blockFace;

    public CloudSign(String signUUID, Location location, String group, String service, String host, Integer port) {
        SignUUID = signUUID;
        this.location = location;
        this.group = group;
        this.service = service;
        this.host = host;
        this.port = port;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getGroup() {
        return group;
    }

    public String getSignUUID() {
        return SignUUID;
    }

    public String getService() {
        return service;
    }

    public Location getLocation() {
        return location;
    }
}
