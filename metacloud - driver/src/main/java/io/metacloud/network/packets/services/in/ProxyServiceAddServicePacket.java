package io.metacloud.network.packets.services.in;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;
import io.metacloud.webservice.restconfigs.services.LiveService;


public class ProxyServiceAddServicePacket extends Packet {


    private LiveService liveService;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("service", liveService);
    }

    @Override
    public void read(IBuffer buffer) {
        liveService = buffer.read("service", LiveService.class);
    }

    public LiveService getLiveService() {
        return liveService;
    }

    public void setLiveService(LiveService liveService) {
        this.liveService = liveService;
    }
}
