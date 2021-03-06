package io.metacloud.network.client;

import io.metacloud.NetworkClient;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.Options;
import io.metacloud.channels.ChannelPipeline;
import io.metacloud.handlers.PacketListenerHandler;

public class NetworkClientDriver {

    private int port;
    private String host;

    public NetworkClientDriver() {

    }


    public NetworkClientDriver bind(String host, int port){
        this.host = host;
        this.port = port;
        return this;
    }

    public void run(){

        NetworkingBootStrap.packetListenerHandler = new PacketListenerHandler();

        try {
            NetworkingBootStrap.client = new NetworkClient();
            NetworkingBootStrap.client.init(channel -> {})
                    .option(Options.BUFFER_SIZE, 2024)
                    .option(Options.PERFORMANCE_BOOST, true)
                    .option(Options.TIMEOUT, -1)
                    .bind(this.host, this.port).connect();
        }catch (Exception e){}
    }

    public void end(){
        NetworkingBootStrap.client.close();
    }
}
