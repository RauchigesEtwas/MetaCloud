package io.metacloud.bungeecord.networking;

import io.metacloud.Driver;
import io.metacloud.bungeecord.BungeeBridge;
import io.metacloud.cloudplayer.ClickEventAction;
import io.metacloud.cloudplayer.CloudTextComponent;
import io.metacloud.cloudplayer.HoverEventAction;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.apidriver.out.ApiSendCloudPlayerDataPacket;
import io.metacloud.network.packets.services.in.*;
import io.metacloud.protocol.Packet;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ManagerListener extends PacketListener{



    @PacketProvideHandler
    public void hanndelAP(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ApiSendCloudPlayerDataPacket){
            ApiSendCloudPlayerDataPacket packet = (ApiSendCloudPlayerDataPacket)readPacket;
            if (packet.getChosen().equalsIgnoreCase("SEND_MESSAGE")){
                ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                    if (proxiedPlayer.getName().equalsIgnoreCase(packet.getCloudPlayerName())){
                        proxiedPlayer.sendMessage(packet.getMessage());
                    }
                });
            }     if (packet.getChosen().equalsIgnoreCase("SEND_SERVER")){

                ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                    if (proxiedPlayer.getName().equalsIgnoreCase(packet.getCloudPlayerName())){
                        proxiedPlayer.connect(BungeeBridge.getInstance().getServerDriver().getServerInfo(packet.getServer()));
                    }
                });

            }     if (packet.getChosen().equalsIgnoreCase("SEND_KICK")){

                ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                    if (proxiedPlayer.getName().equalsIgnoreCase(packet.getCloudPlayerName())){
                       proxiedPlayer.disconnect(packet.getMessage());
                    }
                });

            }     if (packet.getChosen().equalsIgnoreCase("SEND_TEXT_COMPONENT")){
                ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                    if (proxiedPlayer.getName().equalsIgnoreCase(packet.getCloudPlayerName())){

                        CloudTextComponent component = packet.getComponent();

                        TextComponent textComponent = new TextComponent(component.getComponent());
                        component.getExtras().forEach(s -> {
                            textComponent.addExtra(s);
                        });

                        component.getClickEventAction().forEach(clickEventActionObjectHashMap -> {
                            clickEventActionObjectHashMap.forEach((clickEventAction, o) -> {
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(clickEventAction.toString()),String.valueOf(o)) );
                            });
                        });
                        component.getHoverEventAction().forEach(hoverEventActionObjectHashMap -> {

                            hoverEventActionObjectHashMap.forEach((hoverEventAction, o) -> {
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.valueOf(hoverEventAction.toString()),new ComponentBuilder(String.valueOf(o)).create()));
                            });
                     });

                        proxiedPlayer.sendMessage(packet.getMessage());
                    }
                });
            }
        }
    }
    @PacketProvideHandler
    public void handelCommand(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ServiceSendCommandPacket){
            ServiceSendCommandPacket packet = (ServiceSendCommandPacket)readPacket;
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), packet.getCommand());
        }
    }

    @PacketProvideHandler
    public void handelServiceConnect(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ProxyServiceAddServicePacket){
            ProxyServiceAddServicePacket packet = (ProxyServiceAddServicePacket) readPacket;

            if (packet.getLiveService().getGroupConfiguration().getMode() == GroupType.PROXY){
                return;
            }else{

                if (!BungeeBridge.getInstance().getServerDriver().serverExists(packet.getLiveService().getServiceName())){

                    BungeeBridge.getInstance().getHandlerDriver().registerService(packet.getLiveService());
                    if (!BungeeBridge.getInstance().getDataDriver().getRegisteredGroups().contains(packet.getLiveService().getGroupConfiguration().getName()))
                        BungeeBridge.getInstance().getDataDriver().getRegisteredGroups().add(packet.getLiveService().getGroupConfiguration().getName());
                    ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                            "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
                    ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                        if (proxiedPlayer.hasPermission("cloud.notify.services"))       {
                            proxiedPlayer.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getServiceConnectedToProxyNotification()).replace("&", "§").replace("%SERVICE_NAME%", packet.getLiveService().getServiceName()));
                        }
                    });
                    BungeeBridge.getInstance().getServerDriver().addServer(   ProxyServer.getInstance().constructServerInfo(packet.getLiveService().getServiceName(),
                            new InetSocketAddress(packet.getLiveService().getHostAddress(),
                                    packet.getLiveService().getSelectedPort()),
                            "metacloud-cloudservice",
                            false));
                }
            }

        }
    }

    @PacketProvideHandler
    public void handelServiceDisconnect(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ProxyServiceRemoveServicePacket){
            ProxyServiceRemoveServicePacket packet = (ProxyServiceRemoveServicePacket) readPacket;
            if (BungeeBridge.getInstance().getServerDriver().serverExists(packet.getService())){

                ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                        "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
                ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                    if (proxiedPlayer.hasPermission("cloud.notify.services"))       {
                        proxiedPlayer.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getServiceStoppingNotification()).replace("&", "§").replace("%SERVICE_NAME%", packet.getService()));
                    }
                });
                BungeeBridge.getInstance().getHandlerDriver().unregisterService(packet.getService());
                BungeeBridge.getInstance().getServerDriver().removeServer(packet.getService());
            }
        }
    }

    @PacketProvideHandler
    public void handelServiceUpdate(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ProxyServiceUpdateServicesPacket){
            ProxyServiceUpdateServicesPacket packet = (ProxyServiceUpdateServicesPacket) readPacket;
            packet.getGroups().forEach(groupname -> {
                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                        "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ groupname, ServiceRest.class);
                serviceRest.getServices().forEach(service -> {
                    if (service.getServiceState() == CloudServiceState.INGAME || service.getServiceState() == CloudServiceState.LOBBY ) {

                        if (service.getGroupConfiguration().getMode() == GroupType.PROXY) {
                            return;
                        }else {
                            if (!BungeeBridge.getInstance().getServerDriver().serverExists(service.getServiceName())) {
                                BungeeBridge.getInstance().getHandlerDriver().registerService(service);
                                BungeeBridge.getInstance().getDataDriver().getRegisteredGroups().add(groupname);
                                BungeeBridge.getInstance().getServerDriver().addServer(ProxyServer.getInstance().constructServerInfo(service.getServiceName(),
                                        new InetSocketAddress(service.getHostAddress(),
                                                service.getSelectedPort()),
                                        "metacloud-cloudservice",
                                        false));

                            }
                        }
                    }
                });

            });
        }
        if (readPacket instanceof ProxyServiceStartupNoificationPacket){
            ProxyServiceStartupNoificationPacket packet = (ProxyServiceStartupNoificationPacket)readPacket;
            ServiceConfiguration cloudservice = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                    "/" + BungeeBridge.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/service", ServiceConfiguration.class);
            ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
                if (proxiedPlayer.hasPermission("cloud.notify.services"))       {
                    proxiedPlayer.sendMessage(Driver.getInstance().getStorageDriver().base64ToUTF8(cloudservice.getMessages().getServiceStartingNotification()).replace("&", "§").replace("%SERVICE_NAME%", packet.getService()));
                }
            });
        }
    }

}
