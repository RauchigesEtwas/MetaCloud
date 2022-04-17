package io.metacloud.node.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.nodes.*;
import io.metacloud.node.MetaNode;
import io.metacloud.protocol.Packet;
import io.metacloud.services.processes.utils.ServiceStorage;

public class ManagerHandlerListener extends PacketListener {


    @PacketProvideHandler
    public void handelAuthCallBack(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();

        if (readPacket instanceof NodeRegisterCallBackPacket){
            NodeRegisterCallBackPacket packet = (NodeRegisterCallBackPacket) readPacket;
            if (packet.isConnectionAccept()){

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "the node is now §asuccessfully §7connected to the manager!");
            }else {
                System.exit(0);
            }
        }
    }

    @PacketProvideHandler
    public void handelShutdown(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof ManagerShuttingDownPacket){
            Driver.getInstance().getStorageDriver().setShutdownFromManager(true);
            System.exit(0);
        }
    }

    @PacketProvideHandler
    public void handelServiceShutdown(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof NodeHaltServicePacket){
            NodeHaltServicePacket packet = (NodeHaltServicePacket) readPacket;
            Driver.getInstance().getServiceDriver().haltService(packet.getService());
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "new §btask§7, stop the service §b" + packet.getService());

        }
    }

    @PacketProvideHandler
    public void handelServiceLaunch(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel channel = event.getChannel();
        if (readPacket instanceof NodeLaunchServicePacket){
            NodeLaunchServicePacket packet = (NodeLaunchServicePacket) readPacket;
            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);
            GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + configuration.getManagerHostAddress()+ ":" + configuration.getRestAPICommunicationPort() + "/"
                    + configuration.getRestAPIAuthKey() + "/group-" + packet.getGroup(), GroupConfiguration.class);

            ServiceConfiguration serviceConfiguration = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + configuration.getManagerHostAddress()+ ":" + configuration.getRestAPICommunicationPort() + "/"
                    + configuration.getRestAPIAuthKey() + "/service", ServiceConfiguration.class);
            ServiceStorage storage = new ServiceStorage();
            storage.setNetworkingPort(configuration.getNetworkCommunicationPort());
            storage.setRestAPIPort(configuration.getRestAPICommunicationPort());
            storage.setAuthNetworkingKey(configuration.getNetworkAuthKey());
            storage.setAuthRestAPIKey(configuration.getRestAPIAuthKey());
            storage.setManagerAddress(configuration.getManagerHostAddress());

            if (group.getMode() == GroupType.PROXY){
                Integer port = Driver.getInstance().getServiceDriver().getFreePort(true);
                storage.setSelectedPort(port);
                NodeLaunchServiceCallBackPacket NodeLaunchServiceCallBackPacket = new NodeLaunchServiceCallBackPacket();
                NodeLaunchServiceCallBackPacket.setSelecedPort(port);
                NodeLaunchServiceCallBackPacket.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter()+ packet.getServiceCount());
                channel.sendPacket(NodeLaunchServiceCallBackPacket);
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "new §btask§7, Start a new service of the group §b" + group.getName() +"§7 [selectedPort: §b"+port+"§7]");
            }else {
                Integer port = Driver.getInstance().getServiceDriver().getFreePort(false);
                NodeLaunchServiceCallBackPacket NodeLaunchServiceCallBackPacket = new NodeLaunchServiceCallBackPacket();
                NodeLaunchServiceCallBackPacket.setSelecedPort(port);
                NodeLaunchServiceCallBackPacket.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter()+ packet.getServiceCount());
                channel.sendPacket(NodeLaunchServiceCallBackPacket);
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "new §btask§7, Start a new service of the group §b" + group.getName() +"§7 [selectedPort: §b"+port+"§7]");
                storage.setSelectedPort(port);
            }
            storage.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter()+ packet.getServiceCount());
            storage.setGroupConfiguration(group);


            Driver.getInstance().getServiceDriver().launchService(storage);

        }
    }
}
