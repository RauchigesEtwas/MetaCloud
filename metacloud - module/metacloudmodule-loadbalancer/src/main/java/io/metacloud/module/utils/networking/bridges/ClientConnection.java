package io.metacloud.module.utils.networking.bridges;

import com.google.common.collect.Queues;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.module.LoadBalancerModule;
import io.metacloud.module.config.Configuration;
import io.metacloud.module.config.ConnectionType;
import io.metacloud.module.utils.LoadBalancer;
import io.metacloud.module.utils.storage.HandShakeData;
import io.metacloud.module.utils.subgates.SubGate;
import io.metacloud.module.utils.util.ConnectionState;
import io.metacloud.module.utils.util.PacketUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.Queue;

public class ClientConnection extends SimpleChannelInboundHandler<ByteBuf> {

    private Queue<byte[]> queuedPackets = Queues.newArrayDeque();
    private ConnectionState state = ConnectionState.DEFAULT;

    private ServerConnection serverConnection;
    private HandShakeData handshake;
    private String servername;
    private Channel channel;
    private String username;
    private boolean hasbeenblocked = false;

    private final LoadBalancer loadBalancer;

    /**
     * Instantiates a new Client connection.
     *
     * @param loadBalancer the load balancer
     */
    public ClientConnection(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    /**
     * Is channel open boolean.
     *
     * @return the boolean
     */
    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen() && this.state != ConnectionState.DISCONNECTED;
    }

    /**
     * Close channel.
     */
    public void closeChannel() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.config().setAutoRead(false);
            this.channel.close();

        }
        this.state = ConnectionState.DISCONNECTED;
        if (this.queuedPackets != null) {
            this.queuedPackets.clear();
            this.queuedPackets = null;
        }
        if (this.serverConnection != null && this.serverConnection.isChannelOpen()) {


            this.serverConnection.closeChannel();
        }

        LoadBalancerModule.getInstance().getProxyStorage().get(this.servername).setConnectedPlayers(LoadBalancerModule.getInstance().getProxyStorage().get(this.servername).getConnectedPlayers() - 1);


        LoadBalancer.ONLINE_PLAYERS.remove(this);


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.closeChannel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ClosedChannelException || !this.isChannelOpen()) {
            return;
        }
        this.closeChannel();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return username != null ? username : this.channel.remoteAddress().toString().replace("/", "");
    }

    public String getAddress() {
        return this.channel.remoteAddress().toString().replace("/", "").split(":")[0];
    }

    private void addToQueue(ByteBuf in) {
        if (this.queuedPackets == null) {
            return;
        }
        int readerIndex = in.readerIndex();
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        this.queuedPackets.add(bytes);
        in.readerIndex(readerIndex);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        if (LoadBalancerModule.getInstance().getProxyStorage().isEmpty()){
            this.closeChannel();
            return;
        }


        switch (this.state) {
            case DEFAULT: {


                this.addToQueue(in);
                int packetId = PacketUtils.readVarInt(in);
                if (packetId != 0) {
                    return;
                }
                this.handshake = new HandShakeData();

                this.handshake.read(in);
                this.state = ConnectionState.HANDSHAKE_RECEIVED;
                break;
            }
            case HANDSHAKE_RECEIVED: {

                this.addToQueue(in);
                int packetId = PacketUtils.readVarInt(in);
                if (packetId != 0) {
                    return;
                }
                if (this.handshake.getNextState() == 2) {
                    this.username = PacketUtils.readString(in);
                }
                LoadBalancer.ONLINE_PLAYERS.add(this);
                this.connectToServer();
                break;
            }
            case CONNECTING: {

                 this.addToQueue(in);
                break;
            }
            case CONNECTED: {
                in.retain();
                this.serverConnection.fastWrite(in);
            }
        }
    }




    /**
     * On server connected.
     */
    protected void onServerConnected() {
        if (!this.isChannelOpen()) {
            this.serverConnection.closeChannel();
            return;
        }
        if (this.state != ConnectionState.CONNECTING) {
            return;
        }
        this.channel.pipeline().remove("splitter");



        if (this.queuedPackets != null) {
            if(!getName().contains(".")){
            }



            for (byte[] packet : this.queuedPackets) {
                int length = packet.length;
                int size = PacketUtils.getVarIntLength(length) + length;
                ByteBuf buf = Unpooled.buffer(size, size);
                PacketUtils.writeVarInt(buf, length);
                buf.writeBytes(packet);
                this.serverConnection.fastWrite(buf);
            }
            this.queuedPackets.clear();
            this.queuedPackets = null;
        }
        this.state = ConnectionState.CONNECTED;
        this.channel.config().setAutoRead(true);
    }

    /**
     * Fast write.
     *
     * @param buf the buf
     */
    protected void fastWrite(ByteBuf buf) {
        this.channel.writeAndFlush(buf);
    }

    /**
     * Connect to server.
     */
    public void connectToServer() {

        Configuration config = (Configuration) new ConfigDriver("./modules/loadbalancer/config.json").read(Configuration.class);
        if (LoadBalancerModule.getInstance().getProxyStorage().isEmpty()){
            this.closeChannel();
            return;
        }

        if (config.getConnectionType() == ConnectionType.RANDOM){
            SubGate subGate = LoadBalancerModule.getInstance().getRandomSub();



            this.servername = subGate.proxyedtaskname;
            LoadBalancerModule.getInstance().getProxyStorage().get(subGate.proxyedtaskname).setConnectedPlayers(LoadBalancerModule.getInstance().getProxyStorage().get(subGate.proxyedtaskname).getConnectedPlayers() + 1);
            connectToServer(new InetSocketAddress(subGate.ip, subGate.port));
        }
    }

    /**
     * Connect to server.
     *
     * @param server the server
     */
    public void connectToServer(InetSocketAddress server) {

        if (LoadBalancerModule.getInstance().getProxyStorage().isEmpty()){
            this.closeChannel();
            return;
        }
        this.state = ConnectionState.CONNECTING;
        this.channel.config().setAutoRead(false);
        this.serverConnection = new ServerConnection(this);
        Class<? extends SocketChannel> channelClass = Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
        new Bootstrap()
                .channel(channelClass)

                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.IP_TOS, 24)

                .handler(new ServerConnection.ServerConnectionInitializer(this.serverConnection))

                .group(this.channel.eventLoop())
                .remoteAddress(server).connect().addListener((GenericFutureListener<? extends Future<? super Void>>) future -> {
            if (!future.isSuccess()) {
                this.closeChannel();
            }
        });
    }

    /**
     * Gets handshake.
     *
     * @return the handshake
     */
    public HandShakeData getHandshake() {
        return this.handshake;
    }
}