package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.handlers.bin.IEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.NetworkExceptionEvent;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.nodes.*;
import io.metacloud.protocol.Packet;
import io.metacloud.webservice.restconfigs.livenodes.NodesRestConfig;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class NodeHandlerListener extends PacketListener {

    public boolean exists;
    private         LiveService liveServices;

    @PacketProvideHandler
     public void handleNodeConnection(PacketReceivedEvent event){
        Packet readpacket = event.getPacket();
        Channel readchannel = event.getChannel();
        if (readpacket instanceof NodeRegisterPacket){
            NodeRegisterPacket packet = (NodeRegisterPacket) readpacket;

            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "a new node called §b"+packet.getNodeName()+"§7 wants to connect");


            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (packet.getAuthKey().equals(service.getCommunication().getNodeAuthKey())){
             if (!Driver.getInstance().getConnectionDriver().isNodeRegistered(packet.getNodeName())){
                 NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);

                 configuration.getNodes().forEach(nodeProperties -> {
                     if (nodeProperties.getNodeName().equalsIgnoreCase(packet.getNodeName())){
                         this.exists = true;
                     }
                 });
                 if (this.exists){
                     NodesRestConfig config = new NodesRestConfig();
                     Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "The node would be §aconnected§7, all data would be §aloaded§7 to the §bRestAPI.");
                     config.setName(packet.getNodeName());
                     config.setServices(new ArrayList<>());
                     Driver.getInstance().getConnectionDriver().addNodeChannel(packet.getNodeName(), readchannel);
                     Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("running-node-"+ packet.getNodeName(), config);

                     NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                     callBackPacket.setConnectionAccept(true);

                     Driver.getInstance().getConnectionDriver().getNodeChannel(packet.getNodeName()).sendPacket(callBackPacket);


                     Driver.getInstance().getGroupDriver().getGroupsFromNode(packet.getNodeName()).forEach(group -> {
                         Driver.getInstance().getGroupDriver().launchService(group.getName(), group.getMinOnlineServers());
                     });


                     }else {
                     Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, false, "the node does not exist and the connection was rejected.");
                     NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                     callBackPacket.setConnectionAccept(false);

                     Driver.getInstance().getConnectionDriver().getNodeChannel(packet.getNodeName()).sendPacket(callBackPacket);

                     }
                  }else {
                 Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, false, "the node is already connected to the cloud.");
                 NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                 callBackPacket.setConnectionAccept(false);

                 Driver.getInstance().getConnectionDriver().getNodeChannel(packet.getNodeName()).sendPacket(callBackPacket);

             }
            }else{
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK_FAIL, false, "the authkey is not correct, the connection was rejected");
                NodeRegisterCallBackPacket callBackPacket = new NodeRegisterCallBackPacket();
                callBackPacket.setConnectionAccept(false);
                Driver.getInstance().getConnectionDriver().getNodeChannel(packet.getNodeName()).sendPacket(callBackPacket);

            }

        }else if (readpacket instanceof NodeUnregisterPacket){
            NodeUnregisterPacket packet = (NodeUnregisterPacket) readpacket;
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "the node named §b"+packet.getNodeName()+"§7 was shut down");
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            Driver.getInstance().getConnectionDriver().removeNodeChannel(packet.getNodeName());
            Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).removeContent("running-node-"+ packet.getNodeName());

        }

    }

    @PacketProvideHandler
    public void handleNodeService(PacketReceivedEvent event){
        Packet packet = event.getPacket();
        Channel channel = event.getChannel();
        if (packet instanceof NodeLaunchServiceCallBackPacket){
            NodeLaunchServiceCallBackPacket NodeLaunchServiceCallBackPacket = (NodeLaunchServiceCallBackPacket) packet;
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "the service §b"+NodeLaunchServiceCallBackPacket.getServiceName()+"§7 was successfully §astarted§7 [selectedPort: §b"+NodeLaunchServiceCallBackPacket.getSelecedPort()+"§7]");
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
