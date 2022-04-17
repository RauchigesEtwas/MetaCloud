package io.metacloud.webservice;

import io.metacloud.webservice.bin.RestAPI;
import io.metacloud.webservice.bin.RestServer;

import java.util.HashMap;
import java.util.Map;

public class RestDriver {

    private Map<Integer, RestServer> runningRestServer;
    private RestAPI restAPI;

    public RestDriver() {
        this.runningRestServer = new HashMap<>();
        this.restAPI = new RestAPI();
    }

    public RestAPI getRestAPI() {
        return restAPI;
    }

    public boolean existsRestServer(Integer port){
        if (this.runningRestServer.containsKey(port)){
            return true;
        }
        return false;
    }

    public RestServer getRestServer(Integer port){
        if (existsRestServer(port)){
            return this.runningRestServer.get(port);
        }
        return null;
    }

    public void registerRestServer(RestServer restServer){
        if (!existsRestServer(restServer.getRunningPort())){
         this.runningRestServer.put(restServer.getRunningPort(), restServer);
            this.getRestServer(restServer.getRunningPort()).runRestServer();
        }
    }

    public void unregisterRestServer(Integer port){
        if (existsRestServer(port)){
            this.runningRestServer.get(port).shutdownRestServer();
            this.runningRestServer.remove(port);
        }
    }


}
