package io.metacloud.network;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionDriver {

    private HashMap<String, Channel> nodesChannel;
    private HashMap<String, Channel> serviceChannel;


    public ConnectionDriver() {
        this.nodesChannel = new HashMap<>();
        this.serviceChannel = new HashMap<>();
    }


    public ArrayList<Channel> getAllNonProxiedChannel() {
        ArrayList<Channel> channels = new ArrayList<>();
        for(String key : serviceChannel.keySet()){
            String group = Driver.getInstance().getGroupDriver().getGroupByService(key).getName();
            GroupConfiguration groupConfiguration = Driver.getInstance().getGroupDriver().getGroup(group);
            if (groupConfiguration.getMode() == GroupType.GAME || groupConfiguration.getMode() == GroupType.LOBBY){
                channels.add(this.serviceChannel.get(key));
            }
        }
        return channels;
    }

    public ArrayList<Channel> getAllGameChannel() {
        ArrayList<Channel> channels = new ArrayList<>();
        for(String key : serviceChannel.keySet()){
            String group =Driver.getInstance().getGroupDriver().getGroupByService(key).getName();
            GroupConfiguration groupConfiguration = Driver.getInstance().getGroupDriver().getGroup(group);
            if (groupConfiguration.getMode() == GroupType.GAME){
                channels.add(this.serviceChannel.get(key));
            }
        }
        return channels;
    }

    public ArrayList<Channel> getAllLobbyChannel() {
        ArrayList<Channel> channels = new ArrayList<>();
        for(String key : serviceChannel.keySet()){
            String group = Driver.getInstance().getGroupDriver().getGroupByService(key).getName();
            GroupConfiguration groupConfiguration = Driver.getInstance().getGroupDriver().getGroup(group);
            if (groupConfiguration.getMode() == GroupType.LOBBY){
                channels.add(this.serviceChannel.get(key));
            }
        }
        return channels;
    }

    public ArrayList<Channel> getAllProxyChannel() {
        ArrayList<Channel> channels = new ArrayList<>();
        for(String key : serviceChannel.keySet()){
            String group = Driver.getInstance().getGroupDriver().getGroupByService(key).getName();
            GroupConfiguration groupConfiguration = Driver.getInstance().getGroupDriver().getGroup(group);
            if (groupConfiguration.getMode() == GroupType.PROXY){
                channels.add(this.serviceChannel.get(key));
            }
        }
        return channels;
    }


    public ArrayList<Channel> getAllNodesChannel() {
        ArrayList<Channel> channels = new ArrayList<>();
        for(String key : nodesChannel.keySet()){
            channels.add(this.nodesChannel.get(key));
        }
        return channels;
    }

    public boolean isNodeRegistered(String node){
        if (this.nodesChannel.containsKey(node)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isServiceRegistered(String service){
        if (this.serviceChannel.get(service) != null){
            return true;
        }else {
            return false;
        }
    }

    public void addServiceChannel(String service, Channel channel){
        this.serviceChannel.put(service, channel);
    }
    public void addNodeChannel(String node, Channel channel){
        if (!isNodeRegistered(node)){
            this.nodesChannel.put(node, channel);
        }
    }
    public void removeServiceChannel(String service){
        if (isServiceRegistered(service)){
            this.serviceChannel.remove(service);
        }
    }

    public void removeNodeChannel(String node){
        if (isNodeRegistered(node)){
            this.nodesChannel.remove(node);
        }
    }
    public Channel getServiceChannel(String service){
        return this.serviceChannel.get(service);
    }
    public Channel getNodeChannel(String node){
        return this.nodesChannel.get(node);
    }






}
