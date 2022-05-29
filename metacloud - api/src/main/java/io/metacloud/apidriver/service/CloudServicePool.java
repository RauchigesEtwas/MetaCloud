package io.metacloud.apidriver.service;

import io.metacloud.Driver;
import io.metacloud.apidriver.bootstrap.BungeeBootstrap;
import io.metacloud.apidriver.bootstrap.SpigotBootstrap;
import io.metacloud.apidriver.utilitis.DataDriver;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.util.ArrayList;

public class CloudServicePool {


    public Integer getPlayerCount(String group){
        if (DataDriver.isBungeeSide ){
            Integer currentPlayers = 0;
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);
            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                currentPlayers = currentPlayers + serviceRest.getServices().get(i).getCurrentCloudPlayers();
            }

            return currentPlayers;
        }else {
            Integer currentPlayers = 0;
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);
            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                currentPlayers = currentPlayers + serviceRest.getServices().get(i).getCurrentCloudPlayers();
            }

            return currentPlayers;
        }

    }

    public CloudServiceState getLiveServiceState(CloudService service){
        if (DataDriver.isBungeeSide ){
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + service.getGroupName(), ServiceRest.class);
            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                LiveService liveService =  serviceRest.getServices().get(i);
                if (liveService.getServiceName().equalsIgnoreCase(service.getServiceName())){
                    i = serviceRest.getServices().size();
                    return liveService.getServiceState();
                }
            }
        }else {
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + service.getGroupName(), ServiceRest.class);

            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                LiveService liveService =  serviceRest.getServices().get(i);
                if (liveService.getServiceName().equalsIgnoreCase(service.getServiceName())){
                    i = serviceRest.getServices().size();
                    return liveService.getServiceState();
                }
            }
        }
        return null;
    }

    public ArrayList<LiveService> getAllLiveServices(String group){
        if (DataDriver.isBungeeSide ){
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);

            return serviceRest.getServices();
        }else {
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);

            return serviceRest.getServices();
        }

    }

    public ArrayList<LiveService> getAllJoinLiveServices(String group){

        if (DataDriver.isBungeeSide ){
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);
            ArrayList<LiveService> liveServices = new ArrayList<>();

            serviceRest.getServices().forEach(service -> {
                if (service.getServiceState() == CloudServiceState.LOBBY){
                    liveServices.add(service);
                }
            });

            return liveServices;
        }else {
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + group, ServiceRest.class);
            ArrayList<LiveService> liveServices = new ArrayList<>();

            serviceRest.getServices().forEach(service -> {
                if (service.getServiceState() == CloudServiceState.LOBBY){
                    liveServices.add(service);
                }
            });

            return liveServices;
        }

    }

    public LiveService getLiveService(CloudService service){
        if (DataDriver.isBungeeSide ){

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + service.getGroupName(), ServiceRest.class);

            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                LiveService liveService = serviceRest.getServices().get(i);
                if (liveService.getServiceName().equalsIgnoreCase(service.getServiceName())){
                    i = serviceRest.getServices().size();
                    return liveService;
                }
            }
        }else {

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" +  SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-" + service.getGroupName(), ServiceRest.class);
            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                LiveService liveService = serviceRest.getServices().get(i);
                if (liveService.getServiceName().equalsIgnoreCase(service.getServiceName())){
                    i = serviceRest.getServices().size();
                    return liveService;
                }
            }
        }



        return null;
    }

}
