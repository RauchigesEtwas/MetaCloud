package io.metacloud;



import io.metacloud.channels.Channel;
import io.metacloud.channels.ChannelPipeline;
import io.metacloud.channels.IChannel;
import io.metacloud.channels.IChannelInitializer;
import io.metacloud.handlers.listener.ClientConnectEvent;
import io.metacloud.handlers.listener.NetworkExceptionEvent;
import io.metacloud.protocol.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;


public class Worker implements Runnable, Structure{
    private final Thread thread = new Thread(this);
    private ServerSocket socket;

    private boolean connected;

    private final List<IChannel> channels = new ArrayList<>();

    private final ChannelPipeline pipeline = new ChannelPipeline();

    private IChannelInitializer initializer;

    public Worker() {
        try {
            this.socket = new ServerSocket();
            if ( getOption(Options.PERFORMANCE_BOOST)){
                this.socket.setPerformancePreferences(0, 2, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void bind(InetSocketAddress address) throws IOException {
        socket.bind(address);
        if (getOption(Options.TIMEOUT) > 0) socket.setSoTimeout(getOption(Options.TIMEOUT));
        connected = true;
        this.thread.start();
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Channel channel = new Channel(this, socket.accept());

                if (channel == null){
                    return;
                }

                channels.add(channel);
                if (initializer != null) initializer.initChannel(channel);

                NetworkingBootStrap.packetListenerHandler.executeEvent(new ClientConnectEvent(channel));
                channel.start();
            } catch (IOException e) {
                NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
                close();
            }
        }
    }

    @Override
    public void close() {
        try {
            connected = false;
            for (int i = channels.size() - 1; i >= 0; i--) channels.get(i).close();
            socket.close();
        } catch (IOException e) {
            NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        if(packet != null)
            for (int i = channels.size() - 1; i >= 0; i--) channels.get(i).sendPacket(packet);
    }

    @Override
    public Worker init(IChannelInitializer initializer) {
        this.initializer = initializer;
        return this;
    }

    @Override
    public Worker option(Options<?> option, Object value) {
        option.setValue(value);
        return this;
    }

    @Override
    public <T> T getOption(Options<T> option) {
        return option.getValue();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    public List<IChannel> getChannels() {
        return channels;
    }
}
