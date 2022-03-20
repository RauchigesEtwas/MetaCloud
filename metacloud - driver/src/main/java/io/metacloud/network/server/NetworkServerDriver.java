package io.metacloud.network.server;

import io.metacloud.NetworkServer;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.Options;
import io.metacloud.channels.ChannelPipeline;
import io.metacloud.handlers.PacketListenerHandler;
import lombok.SneakyThrows;

public class NetworkServerDriver {

    private int port;

    public NetworkServerDriver(){}

    public NetworkServerDriver bind(int port){
        this.port = port;
        return this;
    }

    @SneakyThrows
    public void run(){
        NetworkingBootStrap.packetListenerHandler = new PacketListenerHandler();
        NetworkingBootStrap.server = new NetworkServer();
        NetworkingBootStrap.server.init(channel -> {
            ChannelPipeline pipeline = channel.getPipeline();
        }).option(Options.BUFFER_SIZE, 2024).option(Options.PERFORMANCE_BOOST, true).bind(this.port);
    }

    public void end(){
        NetworkingBootStrap.server.close();
    }

}
