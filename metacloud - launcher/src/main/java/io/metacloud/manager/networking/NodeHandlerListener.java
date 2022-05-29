package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.events.events.NodeConnectToManagerEvent;
import io.metacloud.events.events.NodeDisconnectFromManagerEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.nodes.in.NodeRegisterPacket;
import io.metacloud.network.packets.nodes.in.NodeUnregisterPacket;
import io.metacloud.network.packets.nodes.out.NodeLaunchServiceCallBackPacket;
import io.metacloud.network.packets.nodes.out.NodeRegisterCallBackPacket;
import io.metacloud.protocol.Packet;
import io.metacloud.webservice.restconfigs.livenodes.NodesRestConfig;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.util.ArrayList;

public class NodeHandlerListener extends PacketListener {

    public boolean exists;
    private         LiveService liveServices;

    @PacketProvideHandler
     public void handleNodeConnection(PacketReceivedEvent event){
        Packet readpacket = event.getPacket();
        Channel readchannel = event.getChannel();
        if (readpacket instanceof NodeRegisterPacket){
            NodeRegisterPacket packet = (NodeRegisterPacket) readpacket;
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

            if (!service.getCommunication().getWhitelistAddresses().contains(readchannel.getLocalAddress().getHostAddress())){
                readchannel.close();
                return;
            }

            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, "a new node called §b"+packet.getNodeName()+"§7 wants to connect");





            if (packet.getAuthKey().equals(service.getCommunication().getNodeAuthKey())){
             if (!Driver.getInstance().getConnectionDriver().isNodeRegistered(packet.getNodeName())){
                 NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);


                 configuration.getNodes().forEach(nodeProperties -> {
                     if (nodeProperties.getNodeName().equals(packet.getNodeName())){
                         this.exists = true;
                     }
                 });
                 if (this.exists){

                     NodesRestConfig config = new NodesRestConfig();
                     Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, "The node would be §aconnected§7, all data would be §aloaded§7 to the §bRestAPI.");
                     config.setName(packet.getNodeName());
                     Driver.getInstance().getEventDriver().executeEvent(new NodeConnectToManagerEvent(packet.getNodeName(), readchannel.getLocalAddress().getHostAddress()));
                     config.setServices(new ArrayList<>());
                     Driver.getInstance().getConnectionDriver().addNodeChannel(packet.getNodeName(), readchannel);
                     Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("running-node-"+ packet.getNodeName(), config);

                     NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                     callBackPacket.setConnectionAccept(true);

                     Driver.getInstance().getConnectionDriver().getNodeChannel(packet.getNodeName()).sendPacket(callBackPacket);


                     Driver.getInstance().getGroupDriver().getGroupsFromNode(packet.getNodeName()).forEach(group -> {
                         for (int i = 0; i != group.getMinOnlineServers(); i++){
                             Driver.getInstance().getGroupDriver().launchService(group.getName(), group.getMinOnlineServers());
                         }
                     });


                     }else {
                     Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, "the node does not exist and the connection was rejected.");
                     NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                     callBackPacket.setConnectionAccept(false);

                     readchannel.sendPacket(callBackPacket);
                     readchannel.close();

                 }
                  }else {
                 Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, "the node is already connected to the cloud.");
                 NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                 callBackPacket.setConnectionAccept(false);
                 readchannel.sendPacket(callBackPacket);
                 readchannel.close();


             }
            }else{
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, "the authkey is not correct, the connection was rejected");
                NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                callBackPacket.setConnectionAccept(false);
                readchannel.sendPacket(callBackPacket);
                readchannel.close();

            }

        }else if (readpacket instanceof NodeUnregisterPacket){
            NodeUnregisterPacket packet = (NodeUnregisterPacket) readpacket;

            NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

            if (!service.getCommunication().getWhitelistAddresses().contains(readchannel.getLocalAddress().getHostAddress())){
                readchannel.close();
            }

            this.exists = false;
            configuration.getNodes().forEach(nodeProperties -> {
                if (nodeProperties.getNodeName().equalsIgnoreCase(packet.getNodeName())){
                    this.exists = true;
                }
            });
            if (exists){

                Driver.getInstance().getEventDriver().executeEvent(new NodeDisconnectFromManagerEvent(packet.getNodeName()));
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, "the node named §b"+packet.getNodeName()+"§7 was shutdown");
                Driver.getInstance().getConnectionDriver().removeNodeChannel(packet.getNodeName());
                Driver.getInstance().getGroupDriver().shutdownNode(packet.getNodeName());
                Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).removeContent("running-node-"+ packet.getNodeName());
            }else {
                readchannel.close();
            }

        }

    }

    @PacketProvideHandler
    public void handleNodeService(PacketReceivedEvent event){
        Packet packet = event.getPacket();
        Channel channel = event.getChannel();
        if (packet instanceof NodeLaunchServiceCallBackPacket){
            NodeLaunchServiceCallBackPacket NodeLaunchServiceCallBackPacket = (NodeLaunchServiceCallBackPacket) packet;
            GroupConfiguration group = Driver.getInstance().getGroupDriver().getGroupByService(NodeLaunchServiceCallBackPacket.getServiceName());
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+NodeLaunchServiceCallBackPacket.getServiceName()+"§7 is now started (§b"+group.getProperties().getNode()+"§7~§b"+group.getProperties().getVersion()+"§7~§b"+NodeLaunchServiceCallBackPacket.getSelecedPort()+"§7)");

            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            ServiceRest rest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + NodeLaunchServiceCallBackPacket.getServiceName().split(service.getGeneral().getServerSplitter())[0], ServiceRest.class);

            for (int i = 0; i != rest.getServices().size() ; i++) {
                if (  rest.getServices().get(i).getServiceName().equalsIgnoreCase(NodeLaunchServiceCallBackPacket.getServiceName())){
                    rest.getServices().get(i).setSelectedPort(NodeLaunchServiceCallBackPacket.getSelecedPort());
                }
            }
            Driver.getInstance().getGroupDriver().deployOnRest(NodeLaunchServiceCallBackPacket.getServiceName().split(service.getGeneral().getServerSplitter())[0], rest);
        }
    }


}
