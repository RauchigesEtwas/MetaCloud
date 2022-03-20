package io.metacloud.channels;

/*
 * Projectname: VapeCloud
 * Created AT: 28.12.2021/17:39
 * Created by Robin B. (RauchigesEtwas)
 */


import io.metacloud.NetworkingBootStrap;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.protocol.codec.IDecoder;
import io.metacloud.protocol.codec.IEncoder;

public class ChannelPipeline {

    private IEncoder encoder = new IEncoder.NetPacketEncoder();
    private IDecoder decoder = new IDecoder.NetPacketDecoder();



    public ChannelPipeline addLast(IEncoder encoder) {
        if (encoder == null) return this;
        this.encoder = encoder;
        return this;
    }

    public ChannelPipeline addLast(IDecoder decoder) {
        if (decoder == null) return this;
        this.decoder = decoder;
        return this;
    }


    public void addLast(PacketListener packetListener){
        NetworkingBootStrap.packetListenerHandler.registerListener(packetListener);
    }

    public IEncoder getEncoder() {
        return encoder;
    }

    public IDecoder getDecoder() {
        return decoder;
    }

}
