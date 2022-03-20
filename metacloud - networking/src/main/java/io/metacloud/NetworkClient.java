package io.metacloud;



import io.metacloud.channels.Channel;
import io.metacloud.channels.IChannel;
import io.metacloud.channels.IChannelInitializer;
import io.metacloud.protocol.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NetworkClient implements  Structure{

    private final Channel channel = new Channel(this);

    private String host;
    private Integer port;

    public NetworkClient() {
    }



    public NetworkClient bind(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public void connect() throws IOException {
        channel.connect(new InetSocketAddress(host,port));


        channel.start();

    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.sendPacket(packet);
    }


    @Override
    public NetworkClient option(Options<?> option, Object value) {
        option.setValue(value);
        return this;
    }

    @Override
    public <T> T getOption(Options<T> option) {
        return option.value;
    }

    @Override
    public NetworkClient init(IChannelInitializer initializer) {
        channel.init(initializer);
        return this;
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    public IChannel getChannel() {
        return channel;
    }
}
