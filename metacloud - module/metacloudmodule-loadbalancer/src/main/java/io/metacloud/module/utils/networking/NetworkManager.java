package de.rauchigesetwas.loadbalancer.utils.networking;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.rauchigesetwas.loadbalancer.utils.LoadBalancer;
import de.rauchigesetwas.loadbalancer.utils.networking.bridges.ClientConnection;
import de.rauchigesetwas.loadbalancer.utils.networking.handler.LengthDeserializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * The type Network manager.
 */
public final class NetworkManager {

    @Getter private final EventLoopGroup workerGroup, bossGroup;
    private final ServerBootstrap serverBootstrap;

    private final String host;
    private final int port;

    /**
     * Instantiates a new Network manager.
     *
     * @param loadBalancer the load balancer
     * @param host         the host
     * @param port         the port
     */
    public NetworkManager(final LoadBalancer loadBalancer, final String host, final int port) {
        this.host = host;
        this.port = port;

        int cores = Runtime.getRuntime().availableProcessors();

        if (Epoll.isAvailable()) {
            this.workerGroup = new EpollEventLoopGroup(cores * 4, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
            this.bossGroup = new EpollEventLoopGroup(cores, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());

        } else {
            this.workerGroup = new NioEventLoopGroup(cores * 4, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
            this.bossGroup = new NioEventLoopGroup(cores, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());

        }


        Class<? extends ServerSocketChannel> channelClass = Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
        this.serverBootstrap = new ServerBootstrap()
                .group(this.bossGroup, this.workerGroup)
                .channel(channelClass)
                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel channel) {
                        channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
                        channel.pipeline().addLast("splitter", new LengthDeserializer());
                        channel.pipeline().addLast("packet_handler", new ClientConnection(loadBalancer));
                    }

                });
    }

    /**
     * Connect.
     */
    @SneakyThrows
    public void connect() {
        ChannelFuture channelFuture = this.serverBootstrap.bind(host, port).sync();
        channelFuture.channel().closeFuture().sync();
    }

}