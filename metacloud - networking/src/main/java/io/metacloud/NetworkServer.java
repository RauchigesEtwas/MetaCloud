package io.metacloud;

/*
 * Projectname: VapeCloud
 * Created AT: 28.12.2021/17:35
 * Created by Robin B. (RauchigesEtwas)
 */



import io.metacloud.channels.IChannel;
import io.metacloud.channels.IChannelInitializer;
import io.metacloud.protocol.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class NetworkServer implements Structure {


    private final Worker worker;


    public NetworkServer() {
        this.worker = new Worker();
    }


    public Worker getWorker() {
        return worker;
    }

    public void bind(int port) throws IOException {
        worker.bind(new InetSocketAddress(port));
    }

    @Override
    public void close() {
        worker.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        worker.sendPacket(packet);
    }

    @Override
    public NetworkServer init(IChannelInitializer initializer) {
        worker.init(initializer);
        return this;
    }

    @Override
    public NetworkServer option(Options<?> option, Object value) {
        worker.option(option, value);
        return this;
    }

    @Override
    public <T> T getOption(Options<T> option) {
        return worker.getOption(option);
    }

    @Override
    public boolean isConnected() {
        return worker.isConnected();
    }

    public InetAddress getInetAddress() {
        return worker.getInetAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return worker.getLocalSocketAddress();
    }

    public int getPort() {
        return worker.getPort();
    }

    public List<IChannel> getChannels() {
        return worker.getChannels();
    }
}
