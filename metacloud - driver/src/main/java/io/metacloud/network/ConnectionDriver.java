package io.metacloud.network;

import io.metacloud.channels.Channel;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionDriver {

    private HashMap<String, Channel> nodesChannel;
    private HashMap<String, Channel> serviceChannel;


    public ConnectionDriver() {
        this.nodesChannel = new HashMap<>();
        this.serviceChannel = new HashMap<>();
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
        if (this.serviceChannel.containsKey(service)){
            return true;
        }else {
            return false;
        }
    }

    public void addServiceChannel(String service, Channel channel){
        if (!isServiceRegistered(service)){
            this.serviceChannel.put(service, channel);
        }
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
