package io.metacloud;

import io.metacloud.channels.IChannelInitializer;
import io.metacloud.protocol.ISender;
import io.metacloud.protocol.Packet;
import java.io.Closeable;
import java.io.IOException;

public interface Structure extends Closeable, ISender {

    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    Structure init(IChannelInitializer initializer);

    Structure option(Options<?> option, Object value);

    <T> T getOption(Options<T> option);

    boolean isConnected();
}
