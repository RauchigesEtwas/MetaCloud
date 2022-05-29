package io.metacloud.network.server;

import io.metacloud.NetworkServer;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.Options;
import io.metacloud.channels.ChannelPipeline;
import io.metacloud.handlers.PacketListenerHandler;
import lombok.SneakyThrows;

import java.io.IOException;

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
            try {
                NetworkingBootStrap.server = new NetworkServer();
                NetworkingBootStrap.server.init(channel -> {})
                        .option(Options.BUFFER_SIZE, 2024)
                        .option(Options.TIMEOUT, -1)
                        .option(Options.PERFORMANCE_BOOST, true)
                        .bind(this.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void end(){
        NetworkingBootStrap.server.close();
    }

}
