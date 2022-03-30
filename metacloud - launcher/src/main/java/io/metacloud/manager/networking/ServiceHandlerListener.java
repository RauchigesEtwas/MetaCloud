package io.metacloud.manager.networking;

import io.metacloud.Driver;
import io.metacloud.channels.Channel;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.handlers.bin.PacketListener;
import io.metacloud.handlers.bin.PacketProvideHandler;
import io.metacloud.handlers.listener.PacketReceivedEvent;
import io.metacloud.network.packets.services.ServiceRegisterPacket;
import io.metacloud.protocol.Packet;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.webservice.restconfigs.services.ServiceRest;


public class ServiceHandlerListener extends PacketListener {

    @PacketProvideHandler
    public void handelServiceRegister(PacketReceivedEvent event){
        Packet readPacket = event.getPacket();
        Channel channel = event.getChannel();
        if (readPacket instanceof ServiceRegisterPacket){
            ServiceRegisterPacket packet = (ServiceRegisterPacket) readPacket;
            Driver.getInstance().getConnectionDriver().addServiceChannel(packet.getServiceName(), channel);
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + service.getCommunication().getManagerHostAddress() + ":" + service.getCommunication().getRestApiPort()+
                    "/" + service.getCommunication().getRestApiAuthKey()+ "/livegroup-" + packet.getServiceName().split(service.getGeneral().getServerSplitter())[0], ServiceRest.class);

            for (int i = 0; i != serviceRest.getServices().size() ; i++) {
                if (serviceRest.getServices().get(i).getServiceName().equalsIgnoreCase(packet.getServiceName())){
                    serviceRest.getServices().get(i).setServiceState(CloudServiceState.LOBBY);
                    long time =   serviceRest.getServices().get(i).getStartTime();
                    long finalTime =  (System.currentTimeMillis() - time);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "the service §b"+packet.getServiceName()+"§7 is successfully §aconnected §7[§b"+finalTime+" ms§7]");

                }
            }
            Driver.getInstance().getGroupDriver().deployOnRest(packet.getServiceName().split(service.getGeneral().getServerSplitter())[0], serviceRest);
        }
    }
}
