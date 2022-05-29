package io.metacloud.apidriver.cloudplayer;

import io.metacloud.Driver;
import io.metacloud.apidriver.bootstrap.BungeeBootstrap;
import io.metacloud.apidriver.bootstrap.SpigotBootstrap;
import io.metacloud.apidriver.utilitis.DataDriver;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;

import java.util.ArrayList;

public class CloudPlayerPool {


    public CloudPlayer getCloudPlayer(String name){
        return new CloudPlayer(name);
    }

    public ArrayList<CloudPlayer> getAllCloudPlayers(){
        ArrayList<CloudPlayer> cloudPlayers = new ArrayList<>();
        if (DataDriver.isBungeeSide){
            CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);

            stats.getPlayers().forEach(s -> {
                cloudPlayers.add(getCloudPlayer(s));
            });
        }else {
            CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" + SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/statistics", CloudStatisticsConfig.class);
            stats.getPlayers().forEach(s -> {
                cloudPlayers.add(getCloudPlayer(s));
            });
        }

        return cloudPlayers;
    }

    public void kickCloudPlayers(ArrayList<CloudPlayer> cloudPlayers, String reason){
        cloudPlayers.forEach(cloudPlayer -> {
            cloudPlayer.kickPlayer(reason);
        });
    }
    public void  kickCloudPeWithName(ArrayList<String> players, String reason){
        players.forEach(cloudPlayer -> {
            getCloudPlayer(cloudPlayer).kickPlayer(reason);
        });
    }


    public void sendMessageWithName(ArrayList<String> players, String message){
        players.forEach(cloudPlayer -> {
           getCloudPlayer(cloudPlayer).sendMessage(message);
        });
    }

    public void sendMessage(ArrayList<CloudPlayer> cloudPlayers, String message){
        cloudPlayers.forEach(cloudPlayer -> {
            cloudPlayer.sendMessage(message);
        });
    }

}
