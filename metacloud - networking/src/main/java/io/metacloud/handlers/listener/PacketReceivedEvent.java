package io.metacloud.handlers.listener;

import io.metacloud.channels.Channel;
import io.metacloud.handlers.bin.IEvent;
import io.metacloud.protocol.Packet;

public class PacketReceivedEvent implements IEvent {

    private Channel channel;
    private Packet packet;

    public PacketReceivedEvent(Channel channel, Packet packet) {
        this.channel = channel;
        this.packet = packet;
    }

    public Channel getChannel() {
        return channel;
    }

    public Packet getPacket() {
        return packet;
    }
}
