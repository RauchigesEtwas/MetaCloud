package io.metacloud.apidriver.cloudplayer;


import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.apidriver.bootstrap.BungeeBootstrap;
import io.metacloud.apidriver.bootstrap.SpigotBootstrap;
import io.metacloud.apidriver.utilitis.DataDriver;
import io.metacloud.cloudplayer.CloudTextComponent;
import io.metacloud.network.packets.apidriver.out.ApiSendCloudPlayerDataPacket;
import io.metacloud.webservice.restconfigs.cloudpalyer.CloudPlayerConfig;

public class CloudPlayer {


    private String playerName;

    public CloudPlayer(String playerName) {
        this.playerName = playerName;
    }


    public String getName() {
        return playerName;
    }

    public String getCurrentProxy(){
        if (DataDriver.isBungeeSide){
            CloudPlayerConfig cloudPlayerConfig = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/cloudplayer-"+ this.playerName, CloudPlayerConfig.class);
            return cloudPlayerConfig.getCurrentProxy();
        }else {
            CloudPlayerConfig cloudPlayerConfig = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                    "/" + SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/cloudplayer-"+ this.playerName, CloudPlayerConfig.class);
            return cloudPlayerConfig.getCurrentProxy();
        }
    }

    public String getCurrentServer(){
      if (DataDriver.isBungeeSide){
          CloudPlayerConfig cloudPlayerConfig = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  BungeeBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                  "/" + BungeeBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/cloudplayer-"+ this.playerName, CloudPlayerConfig.class);
          return cloudPlayerConfig.getCurrentServer();
      }else {
          CloudPlayerConfig cloudPlayerConfig = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotBootstrap.configuration.getNetworkProperty().getManagerAddress() + ":" +  SpigotBootstrap.configuration.getNetworkProperty().getRestAPIPort()+
                  "/" + SpigotBootstrap.configuration.getNetworkProperty().getAuthRestAPIKey()+ "/cloudplayer-"+ this.playerName, CloudPlayerConfig.class);
          return cloudPlayerConfig.getCurrentServer();
      }
    }

    public void sendMessage(String message){
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_MESSAGE", message));
    }

    public void sendMessage(CloudTextComponent cloudText){
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_TEXT_COMPONENT", cloudText));
    }


    public void sendActionBar(String message){
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_ACTIONBAR", message));
    }


    public void kickPlayer(String reason){
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_KICK", reason));
    }

    public void sendPlayer(String server){
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_SERVER", server));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        NetworkingBootStrap.client.sendPacket(new ApiSendCloudPlayerDataPacket(this.playerName, "SEND_TITLE", title, subTitle, fadeIn, stay, fadeOut));
    }


}
