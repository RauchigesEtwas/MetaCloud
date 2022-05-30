package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.events.events.CloudPlayerEnterNetworkEvent;
import io.metacloud.events.events.CloudPlayerQuitNetworkEvent;
import io.metacloud.events.events.CloudPlayerSwitchServiceEvent;
import io.metacloud.events.events.service.ServiceConnectEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.cloudplayer.CloudPlayerEnterNetworkPacket;
import io.metacloud.network.packets.cloudplayer.CloudPlayerEnterServerPacket;
import io.metacloud.network.packets.cloudplayer.CloudPlayerQuitNetworkPacket;
import io.metacloud.network.packets.cloudplayer.CloudPlayerQuitServerPacket;
import io.metacloud.network.packets.services.ServiceStartNewInstancePacket;
import io.metacloud.network.packets.services.in.ProxyServiceAddServicePacket;
import io.metacloud.network.packets.services.in.ProxyServiceRemoveServicePacket;
import io.metacloud.network.packets.services.in.ProxyServiceUpdateServicesPacket;
import io.metacloud.network.packets.services.out.ServiceRegisterPacket;
import io.metacloud.network.packets.services.out.ServiceUnregisterPacket;
import io.metacloud.network.packets.services.out.ServiceUpdatePacket;
import io.metacloud.protocol.Packet;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.cloudpalyer.CloudPlayerConfig;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServiceHandlerListener extends PacketListener {

    @PacketProvideHandler
    public void handelServiceRegister(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel readchannel = event.getChannel();
        if (readPacket instanceof ServiceRegisterPacket){
            ServiceRegisterPacket packet = (ServiceRegisterPacket) readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (!service.getCommunication().getWhitelistAddresses().contains(readchannel.getLocalAddress().getHostAddress())){
                readchannel.close();
            }

            if (Driver.getInstance().getGroupDriver().getGroupByService(packet.getServiceName()) == null){
                readchannel.close();
                return;
            }

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + packet.getServiceName().split(service.getGeneral().getServerSplitter())[0], ServiceRest.class);

            Driver.getInstance().getConnectionDriver().addServiceChannel(packet.getServiceName(), event.getChannel());
            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                LiveService liveService = serviceRest.getServices().get(i);
                if (serviceRest.getServices().get(i).getServiceName().equalsIgnoreCase(packet.getServiceName()) && serviceRest.getServices().get(i).getServiceState() == CloudServiceState.STARTING){
                    serviceRest.getServices().get(i).setServiceState(CloudServiceState.LOBBY);
                    long time =   serviceRest.getServices().get(i).getStartTime();
                    long finalTime =  (System.currentTimeMillis() - time);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+packet.getServiceName()+"§7 is successfully §aconnected §7(§b"+finalTime+" ms§7)");
                    Driver.getInstance().getEventDriver().executeEvent(new ServiceConnectEvent(liveService.getServiceName(), liveService.getSelectedPort(), liveService.getGroupConfiguration(), service.getCommunication().getManagerHostAddress()));
                    if (serviceRest.getServices().get(i).getGroupConfiguration().getMode() == GroupType.PROXY){
                        ProxyServiceUpdateServicesPacket updateServicesPacket = new ProxyServiceUpdateServicesPacket();
                        List<String > groups = new ArrayList<>();
                        Driver.getInstance().getGroupDriver().getGroups().forEach(groupConfiguration -> {
                            groups.add(groupConfiguration.getName());
                        });
                        updateServicesPacket.setGroups(groups);
                        readchannel.sendPacket(updateServicesPacket);
                    }else {
                        Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel1 -> {

                            ProxyServiceAddServicePacket servicePacket = new ProxyServiceAddServicePacket();
                            servicePacket.setLiveService(liveService);
                            channel1.sendPacket(servicePacket);
                        });
                    }

                }
            }

            Driver.getInstance().getGroupDriver().deployOnRest(packet.getServiceName().split(service.getGeneral().getServerSplitter())[0], serviceRest);
        }if (readPacket instanceof ServiceUnregisterPacket){
            ServiceUnregisterPacket packet = (ServiceUnregisterPacket) readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);


            if (!service.getCommunication().getWhitelistAddresses().contains(readchannel.getLocalAddress().getHostAddress())){
                readchannel.close();
            }

            if (Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()) == null){
                readchannel.close();
                return;
            }

            if(Driver.getInstance().getServiceDriver().getService(packet.getService()) == null){
                readchannel.close();
                return;
            }


            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the service §b"+packet.getService()+"§7 is successfully §aShutdown");

            Driver.getInstance().getGroupDriver().shutdownService(packet.getService());
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), ServiceRest.class);
            GroupConfiguration group = Driver.getInstance().getGroupDriver().getGroupByService(packet.getService());

            if (group.getMode() != GroupType.PROXY){
                Driver.getInstance().getConnectionDriver().getAllProxyChannel().forEach(channel1 -> {
                    ProxyServiceRemoveServicePacket removeServicePacket = new ProxyServiceRemoveServicePacket();
                    removeServicePacket.setService(packet.getService());
                    channel1.sendPacket(removeServicePacket);
                });
            }

            if (serviceRest.getServices().size() < group.getMinOnlineServers()){
                Driver.getInstance().getGroupDriver().launchService(Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), 1);
            }
        }

        if (readPacket instanceof ServiceStartNewInstancePacket){
            ServiceStartNewInstancePacket packet = (ServiceStartNewInstancePacket) readPacket;
            Driver.getInstance().getGroupDriver().launchService(packet.getGroup(), 1);
        }

    }

    @PacketProvideHandler
    public void handleNetworkEnter(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof CloudPlayerEnterServerPacket){
            CloudPlayerEnterServerPacket packet = (CloudPlayerEnterServerPacket)readPacket;


            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), ServiceRest.class);



            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                if (serviceRest.getServices().get(i).getServiceName().contains(packet.getService())){
                    serviceRest.getServices().get(i).setCurrentCloudPlayers(serviceRest.getServices().get(i).getCurrentCloudPlayers() +1);
                }
            }

            if (service.getGeneral().isShowPlayerConnections()){
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Player §b"+packet.getPlayername()+" §7has changed the server to §b" + packet.getService());
            }
            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerSwitchServiceEvent(packet.getPlayername(), packet.getService()));




            CloudPlayerConfig config = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/cloudplayer-" + packet.getPlayername(), CloudPlayerConfig.class);
            config.setCurrentServer(packet.getService());
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("cloudplayer-"+ packet.getPlayername(),  config);
            Driver.getInstance().getGroupDriver().deployOnRest(Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), serviceRest);


        }
        if (readPacket instanceof CloudPlayerEnterNetworkPacket){
            CloudPlayerEnterNetworkPacket packet = (CloudPlayerEnterNetworkPacket) readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);


            if (Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).exitsContent("cloudplayer-" + packet.getPlayerName())){
                CloudPlayerConfig config = (CloudPlayerConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/cloudplayer-" + packet.getPlayerName(), CloudPlayerConfig.class);

                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(config.getCurrentProxy()).getName(), ServiceRest.class);

                if (service.getGeneral().isShowPlayerConnections()){
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Player §b"+packet.getPlayerName()+"§7 is now §cdisconnected§7!");
                }

                for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                    if (serviceRest.getServices().get(i).getServiceName().contains(config.getCurrentProxy())){
                        serviceRest.getServices().get(i).setCurrentCloudPlayers(serviceRest.getServices().get(i).getCurrentCloudPlayers() -1);
                    }
                }

                Driver.getInstance().getGroupDriver().deployOnRest(Driver.getInstance().getGroupDriver().getGroupByService(packet.getCurrentProxy()).getName(), serviceRest);
            }

            CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/statistics", CloudStatisticsConfig.class);

            if (!stats.getPlayers().contains(packet.getPlayerName())){
                stats.getPlayers().add(packet.getPlayerName());
            }
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("statistics", stats);

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getCurrentProxy()).getName(), ServiceRest.class);

            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                if (serviceRest.getServices().get(i).getServiceName().contains(packet.getCurrentProxy())){
                    serviceRest.getServices().get(i).setCurrentCloudPlayers(serviceRest.getServices().get(i).getCurrentCloudPlayers() + 1);
                }
            }
            if (service.getGeneral().isShowPlayerConnections()){
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Player §b"+packet.getPlayerName()+"§7 is now §aconnecting§7! (Proxy: §b"+packet.getCurrentProxy()+"§7) ");
            }


            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerEnterNetworkEvent(packet.getPlayerName(), packet.getUuid(), packet.getCurrentProxy()));
            CloudPlayerConfig config = new CloudPlayerConfig();
            config.setCurrentProxy(packet.getCurrentProxy());
            config.setPlayerName(packet.getPlayerName());
            config.setModules(new HashMap<>());
            config.setCurrentServer(null);
            config.setUuid(packet.getUuid());
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("cloudplayer-"+ packet.getPlayerName(), config);
            Driver.getInstance().getGroupDriver().deployOnRest(Driver.getInstance().getGroupDriver().getGroupByService(packet.getCurrentProxy()).getName(), serviceRest);

        }
    }


    @PacketProvideHandler
    public void handelUpdate(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();


        if (readPacket instanceof ServiceUpdatePacket){
            ServiceUpdatePacket packet = (ServiceUpdatePacket)readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

            GroupConfiguration groupConfiguration = Driver.getInstance().getGroupDriver().getGroupByService(packet.getService());

            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), ServiceRest.class);

            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                if (serviceRest.getServices().get(i).getServiceName().contains(packet.getService())){
                    serviceRest.getServices().get(i).setCurrentCloudPlayers(packet.getPlayers());
                }
            }

            Driver.getInstance().getGroupDriver().deployOnRest(groupConfiguration.getName(), serviceRest);
        }
    }

    @PacketProvideHandler
    public void handleNetworkQuit(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof CloudPlayerQuitNetworkPacket){
            CloudPlayerQuitNetworkPacket packet = (CloudPlayerQuitNetworkPacket)readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).removeContent("cloudplayer-"+ packet.getPlayerName());

                ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                        "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), ServiceRest.class);


            Driver.getInstance().getEventDriver().executeEvent(new CloudPlayerQuitNetworkEvent(packet.getPlayerName(), packet.getUuid(), packet.getService()));

                for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                    if (serviceRest.getServices().get(i).getServiceName().contains(packet.getService())){
                        serviceRest.getServices().get(i).setCurrentCloudPlayers(serviceRest.getServices().get(i).getCurrentCloudPlayers() + -1);
                    }
                }

                if (service.getGeneral().isShowPlayerConnections()){
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Player §b"+packet.getPlayerName()+"§7 is now §cdisconnected§7!");
                }

            CloudStatisticsConfig stats = (CloudStatisticsConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/statistics", CloudStatisticsConfig.class);

                if (stats.getPlayers().contains(packet.getPlayerName())){
                    stats.getPlayers().remove(packet.getPlayerName());

                }
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("statistics", stats);

                Driver.getInstance().getGroupDriver().deployOnRest(Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), serviceRest);




        }
        if (readPacket instanceof CloudPlayerQuitServerPacket){
            CloudPlayerQuitServerPacket packet = (CloudPlayerQuitServerPacket) readPacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);



            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), ServiceRest.class);



            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                if (serviceRest.getServices().get(i).getServiceName().contains(packet.getService())){
                    serviceRest.getServices().get(i).setCurrentCloudPlayers(serviceRest.getServices().get(i).getCurrentCloudPlayers() -1);
                }
            }

            Driver.getInstance().getGroupDriver().deployOnRest(Driver.getInstance().getGroupDriver().getGroupByService(packet.getService()).getName(), serviceRest);
        }
    }
}
