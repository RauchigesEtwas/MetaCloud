package io.metacloud.node.networking;

import io.metacloud.Driver;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.nodes.ManagerShuttingDownPacket;
import io.metacloud.network.packets.nodes.NodeLaunchServicePacket;
import io.metacloud.network.packets.nodes.NodeRegisterCallBackPacket;
import io.metacloud.node.MetaNode;
import io.metacloud.protocol.Packet;

public class ManagerHandlerListener extends PacketListener {


    @PacketProvideHandler
    public void handelAuthCallBack(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();

        if (readPacket instanceof NodeRegisterCallBackPacket){
            NodeRegisterCallBackPacket packet = (NodeRegisterCallBackPacket) readPacket;
            if (packet.isConnectionAccept()){

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "the node is now §asuccessfully §7connected to the manager!");
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
    public void handelServiceStartUps(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        if (readPacket instanceof NodeLaunchServicePacket){
            NodeLaunchServicePacket packet = (NodeLaunchServicePacket) readPacket;
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "the Node got an new Task!");
        }
    }
}
