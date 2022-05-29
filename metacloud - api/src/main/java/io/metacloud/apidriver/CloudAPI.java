package io.metacloud.apidriver;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.apidriver.cloudplayer.CloudPlayerPool;
import io.metacloud.apidriver.service.CloudServicePool;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.network.packets.apidriver.out.ApiSendCommand;
import io.metacloud.network.packets.apidriver.out.ApiSendMessage;
import io.metacloud.network.packets.apidriver.out.ApiStartServicePacket;
import io.metacloud.network.packets.apidriver.out.ApiStopServicePacket;

public class CloudAPI {

    private static CloudAPI instance;
    private CloudServicePool cloudServicePool;
    private CloudPlayerPool cloudPlayerPool;

    public CloudAPI(){
        instance = this;
        new Driver();
        cloudServicePool = new CloudServicePool();
        cloudPlayerPool = new CloudPlayerPool();
    }

    public static CloudAPI getInstance() {
        return instance;
    }

    public CloudServicePool getCloudServicePool() {
        return cloudServicePool;
    }

    public CloudPlayerPool getCloudPlayerPool() {
        return cloudPlayerPool;
    }

    public void startCloudService(String group, Integer count) {
        ApiStartServicePacket packet = new ApiStartServicePacket();
        packet.setGroup(group);
        packet.setCount(count);
        NetworkingBootStrap.client.sendPacket(packet);
    }


    public void sendCloudMessage(MSGType type, String message){
        NetworkingBootStrap.client.sendPacket(new ApiSendMessage(type.toString(), message));
    }

    public void sendCloudCommand(String commandLine){
        NetworkingBootStrap.client.sendPacket(new ApiSendCommand(commandLine));
    }

    public void stopCloudService(String service){

        ApiStopServicePacket packet = new ApiStopServicePacket();
        packet.setService(service);
        NetworkingBootStrap.client.sendPacket(packet);

    }
}
