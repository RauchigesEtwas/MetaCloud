package io.metacloud.handlers.listener;

import io.metacloud.channels.Channel;
import io.metacloud.handlers.bin.IEvent;

public class ClientDisconectEvent implements IEvent {
    private Channel channel;

    public ClientDisconectEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
