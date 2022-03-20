package io.metacloud.manager.networking;

import io.metacloud.channels.Channel;
import io.metacloud.handlers.bin.IEvent;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.protocol.Packet;

public class NodeHandlerListener extends PacketListener {


    @PacketProvideHandler
    public void handleNodeConnection(PacketReceivedEvent event){
        Packet packet = event.getPacket();
        Channel channel = event.getChannel();


        //LOGIN & LOGOUT

    }

    @PacketProvideHandler
    public void handleNodeService(PacketReceivedEvent event){
        Packet packet = event.getPacket();
        Channel channel = event.getChannel();

        //ADDSERVICE & REMOVESERVICE
    }

}
