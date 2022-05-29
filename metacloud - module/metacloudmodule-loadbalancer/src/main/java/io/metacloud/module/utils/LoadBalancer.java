package io.metacloud.module.utils;

import io.metacloud.module.utils.networking.NetworkManager;
import io.metacloud.module.utils.networking.bridges.ClientConnection;
import io.netty.util.ResourceLeakDetector;
import java.io.Closeable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Load balancer.
 */
public class LoadBalancer implements AutoCloseable, Closeable {

    /**
     * The constant ONLINE_PLAYERS.
     */
    public static final List<ClientConnection> ONLINE_PLAYERS = new CopyOnWriteArrayList<>();

    public NetworkManager networkManager;

    /**
     * Instantiates a new Load balancer.
     *
     * @param host  the host
     * @param port  the port
     */
    public LoadBalancer(final String host, final int port) {

    new Thread(() -> {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        try {
            this.networkManager = new NetworkManager(this, host, port);
            this.networkManager.connect();

        } catch (Exception exception) {
        } finally {
            this.close();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }).start();
    }

    @Override
    public void close() {
        this.networkManager.getBossGroup().shutdownGracefully();
        this.networkManager.getWorkerGroup().shutdownGracefully();
    }

}
