package io.metacloud.bungeecord.commands;

import io.metacloud.Driver;
import io.metacloud.bungeecord.BungeeBridge;
import io.metacloud.configuration.configs.ServiceConfiguration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Random;

public class HubCommand extends Command {

    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            ArrayList<String > premiumLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingPermissionLobby(player);
            ArrayList<String > freeLobby = BungeeBridge.getInstance().getHandlerDriver().getMatchingLobby(player);
            ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
            boolean isOnLobby = false;
            for (int i = 0; i != premiumLobby.size() ; i++) {
                if (premiumLobby.get(i).equalsIgnoreCase(player.getServer().getInfo().getName())){
                    isOnLobby = true;
                }
            }
            for (int i = 0; i != freeLobby.size() ; i++) {
                if (freeLobby.get(i).equalsIgnoreCase(player.getServer().getInfo().getName())){
                    isOnLobby = true;
                }
            }

            if (isOnLobby){
                player.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getHubCommandAlreadyOnFallBack()).replace("&", "§"));
            }else {
                if (premiumLobby.isEmpty() && freeLobby.isEmpty()){
                    player.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getHubCommandNoFallbackFound()).replace("&", "§"));
                }else if (!premiumLobby.isEmpty()){
                    String info = premiumLobby.get(new Random().nextInt(premiumLobby.size()));
                    player.connect(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    player.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getHubCommandSendToAnFallback()).replace("&", "§").replace("%NEW_SERVER%", info));
                }else{
                    String info = freeLobby.get(new Random().nextInt(freeLobby.size()));
                    player.connect(BungeeBridge.getInstance().getServerDriver().getServerInfo(info));
                    player.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getHubCommandSendToAnFallback()).replace("&", "§").replace("%NEW_SERVER%", info));
                }
            }
        }
    }
}
