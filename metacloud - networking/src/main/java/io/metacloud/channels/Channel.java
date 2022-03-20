package io.metacloud.channels;


import io.metacloud.NetworkServer;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.Options;
import io.metacloud.Structure;
import io.metacloud.handlers.listener.ClientConnectEvent;
import io.metacloud.handlers.listener.ClientDisconectEvent;
import io.metacloud.handlers.listener.NetworkExceptionEvent;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.protocol.Buffer;
import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
public class Channel implements IChannel, Runnable {

    private final Thread thread = new Thread(this);

    private final ChannelPipeline pipeline = new ChannelPipeline();

    private IChannelInitializer initializer;

    private final Structure structure;

    private final Socket socket;

    private boolean connected;


    public Socket getSocket() {
        return socket;
    }

    public Channel(Structure structure, Socket socket) {
        this.structure = structure;
        this.socket = socket;
        this.connected = true;
    }

    public Channel(Structure structure) {
        this.structure = structure;
        this.socket = new Socket();
    }

    public void connect(SocketAddress address) {
        try {
            socket.connect(address);
            connected = true;
            initializer.initChannel(this);
            NetworkingBootStrap.packetListenerHandler.executeEvent(new ClientConnectEvent(this));
        } catch (IOException e) {

        }

    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (connected) {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                ByteArrayOutputStream array = new ByteArrayOutputStream();
                int read, count = 0;
                while (connected && (read = inputStream.readByte()) > -1) {
                    count++;
                    if (structure.getOption(Options.BUFFER_SIZE) < count) {
                        throw new Exception("The buffer is too big, allowed size " + structure.getOption(Options.BUFFER_SIZE));
                    }
                    array.write(read);
                }
                Packet decode = pipeline.getDecoder().decode(new Packet() {
                    @Override
                    public void write(IBuffer buffer) {}
                    @Override
                    public void read(IBuffer buffer) {}
                }, new Buffer(array.toByteArray()));
                NetworkingBootStrap.packetListenerHandler.executeEvent(new PacketReceivedEvent(this, decode));
            } catch (IOException e) {
                NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
                close();
            } catch (Exception exception) {
                NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(exception));
                close();
            }
        }
    }

    @Override
    public void close() {
        try {
            if (socket != null && connected) {
                connected = false;
                socket.close();
                if (structure instanceof NetworkServer) ((NetworkServer) structure).getChannels().remove(this);
                NetworkingBootStrap.packetListenerHandler.executeEvent(new ClientDisconectEvent(this));

            }
        } catch (IOException e) {
            NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
            close();
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        try {
            DataOutputStream data = new DataOutputStream(socket.getOutputStream());
            pipeline.getEncoder().encode(data, packet, new Buffer());
        } catch (IOException e) {
            NetworkingBootStrap.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
        }
    }


    public void init(IChannelInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public ChannelPipeline getPipeline() {
        return pipeline;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }
}
