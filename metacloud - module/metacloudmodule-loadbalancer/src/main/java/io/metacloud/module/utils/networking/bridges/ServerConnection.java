package io.metacloud.module.utils.networking.bridges;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.nio.channels.ClosedChannelException;

/**
 * The type Server connection.
 */
public class ServerConnection extends SimpleChannelInboundHandler<ByteBuf> {

    private final ClientConnection clientConnection;

    private boolean disconnected = false;
    private Channel channel;

    /**
     * Instantiates a new Server connection.
     *
     * @param clientConnection the client connection
     */
    public ServerConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Is channel open boolean.
     *
     * @return the boolean
     */
    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen() && !this.disconnected;
    }

    /**
     * Close channel.
     */
    public void closeChannel() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.config().setAutoRead(false);
            this.channel.close();
        }
        this.disconnected = true;
        if (this.clientConnection.isChannelOpen()) {
            this.clientConnection.closeChannel();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();
        this.clientConnection.onServerConnected();
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        msg.retain();
        this.clientConnection.fastWrite(msg);
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
     * The type Server connection initializer.
     */
    public static class ServerConnectionInitializer extends ChannelInitializer<Channel> {

        private final ServerConnection serverConnection;

        /**
         * Instantiates a new Server connection initializer.
         *
         * @param serverConnection the server connection
         */
        public ServerConnectionInitializer(ServerConnection serverConnection) {
            this.serverConnection = serverConnection;
        }

        protected void initChannel(Channel channel) {
            channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
            channel.pipeline().addLast("handler", this.serverConnection);
        }
    }
}