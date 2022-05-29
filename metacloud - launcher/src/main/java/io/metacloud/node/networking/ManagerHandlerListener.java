package io.metacloud.node.networking;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
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
import io.metacloud.network.packets.nodes.in.ManagerShuttingDownPacket;
import io.metacloud.network.packets.nodes.in.NodeHaltServicePacket;
import io.metacloud.network.packets.nodes.in.NodeLaunchServicePacket;
import io.metacloud.network.packets.nodes.in.NodeSyncTemplatePacket;
import io.metacloud.network.packets.nodes.out.NodeLaunchServiceCallBackPacket;
import io.metacloud.network.packets.nodes.out.NodeRegisterCallBackPacket;
import io.metacloud.node.MetaNode;
import io.metacloud.protocol.Packet;
import io.metacloud.queue.bin.QueueContainer;
import io.metacloud.queue.bin.QueueStatement;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ManagerHandlerListener extends PacketListener {


    @PacketProvideHandler
    public void handelAuthCallBack(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel readChannel = event.getChannel();

        if (readPacket instanceof NodeRegisterCallBackPacket){
            NodeRegisterCallBackPacket packet = (NodeRegisterCallBackPacket) readPacket;
            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);

            if (!readChannel.getLocalAddress().getHostAddress().equals(configuration.getManagerHostAddress())){
                readChannel.close();
                return;
            }

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
        Channel readChannel = event.getChannel();
        if (readPacket instanceof ManagerShuttingDownPacket){
            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);


            Driver.getInstance().getStorageDriver().setShutdownFromManager(true);
            MetaNode.shutdown();
        }
    }

    @PacketProvideHandler
    public void handelServiceShutdown(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel readChannel = event.getChannel();
        if (readPacket instanceof NodeHaltServicePacket){
            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);

            NodeHaltServicePacket packet = (NodeHaltServicePacket) readPacket;
            Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.STOPPING, packet.getService()));
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Shutdown Service §b" + packet.getService() + "§7 is an new Task and was §badded§7 to the Queue");
        }
    }

    @PacketProvideHandler
    public void handelServiceLaunch(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel readChannel = event.getChannel();
        if (readPacket instanceof NodeLaunchServicePacket){
            NodeLaunchServicePacket packet = (NodeLaunchServicePacket) readPacket;
            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);


            GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + configuration.getManagerHostAddress()+ ":" + configuration.getRestAPICommunicationPort() + "/"
                    + configuration.getRestAPIAuthKey() + "/group-" + packet.getGroup(), GroupConfiguration.class);

            ServiceConfiguration serviceConfiguration = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + configuration.getManagerHostAddress()+ ":" + configuration.getRestAPICommunicationPort() + "/"
                    + configuration.getRestAPIAuthKey() + "/service", ServiceConfiguration.class);
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Launch Service §b" + packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount()+ "§7 is an new Task and was §badded§7 to the Queue");

          if (packet.isSameAddress()){
              NodeLaunchServiceCallBackPacket packet1 = new NodeLaunchServiceCallBackPacket();
              Integer port = packet.getPort();
              packet1.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount());
              packet1.setSelecedPort(port);
              NetworkingBootStrap.client.sendPacket(packet1);

              Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING,
                      packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount(),
                      packet.getPort(),
                      group,
                      serviceConfiguration.getCommunication().getNetworkingPort(),
                      serviceConfiguration.getCommunication().getRestApiPort(),
                      configuration.getManagerHostAddress(),
                      serviceConfiguration.getCommunication().getNodeAuthKey(),
                      serviceConfiguration.getCommunication().getRestApiAuthKey()));
          }else{
              if (group.getMode() == GroupType.PROXY){

                  NodeLaunchServiceCallBackPacket packet1 = new NodeLaunchServiceCallBackPacket();
                  Integer port =  Driver.getInstance().getServiceDriver().getFreePort(true);

                  packet1.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount());
                  packet1.setSelecedPort(port);
                  NetworkingBootStrap.client.sendPacket(packet1);

                  Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING,
                          packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount(),
                          port,
                          group,
                          serviceConfiguration.getCommunication().getNetworkingPort(),
                          serviceConfiguration.getCommunication().getRestApiPort(),
                          configuration.getManagerHostAddress(),
                          serviceConfiguration.getCommunication().getNodeAuthKey(),
                          serviceConfiguration.getCommunication().getRestApiAuthKey()));
              }else {
                  NodeLaunchServiceCallBackPacket packet1 = new NodeLaunchServiceCallBackPacket();
                  Integer port =  Driver.getInstance().getServiceDriver().getFreePort(false);

                  packet1.setServiceName(packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount());
                  packet1.setSelecedPort(port);
                  NetworkingBootStrap.client.sendPacket(packet1);


                  Driver.getInstance().getQueueDriver().addTaskToQueue(new QueueContainer(QueueStatement.LAUNCHING,
                          packet.getGroup() + serviceConfiguration.getGeneral().getServerSplitter() + packet.getServiceCount(),
                          port,
                          group,
                          serviceConfiguration.getCommunication().getNetworkingPort(),
                          serviceConfiguration.getCommunication().getRestApiPort(),
                          configuration.getManagerHostAddress(),
                          serviceConfiguration.getCommunication().getNodeAuthKey(),
                          serviceConfiguration.getCommunication().getRestApiAuthKey()));
              }
          }





        }
    }

    @PacketProvideHandler
    public void handelSyncTemplate(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof NodeSyncTemplatePacket){
            NodeSyncTemplatePacket packet = (NodeSyncTemplatePacket)readPacket;
          if (!new File("./live/" + packet.getGroupName()+"/" + packet.getServiceName()+"/").exists()){
              return;
          }else{
              Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK,  "Sync an Template from the Group "+packet.getGroupName()+ " is a new Task, but it run instantly");

              try {
                  FileUtils.deleteDirectory(new File(packet.getTemplatePath()));
              } catch (IOException e) {
                  e.printStackTrace();
              }
              new File(packet.getTemplatePath()).mkdirs();
              try {
                  FileUtils.copyDirectory(new File("./live/" + packet.getGroupName()+"/" + packet.getServiceName()+"/"), new File("."+packet.getTemplatePath()));
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        }
    }


}
