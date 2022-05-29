package io.metacloud.module.listener;

import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.events.bin.EventHandler;
import io.metacloud.events.bin.ICloudEvent;
import io.metacloud.events.events.service.ServiceConnectEvent;
import io.metacloud.events.events.service.ServiceRemoveEvent;
import io.metacloud.module.LoadBalancerModule;
import io.metacloud.module.ProxyData;
import io.metacloud.module.utils.subgates.SubGate;

public class CloudListener implements ICloudEvent {

    @EventHandler(priority = 60)
    public void handelServiceConnected(ServiceConnectEvent event){

        String service = event.getServiceName();
        String host = event.getHost();
        Integer port = event.getUsedPort();
        GroupConfiguration group = event.getGroup();
        if (group.getMode() == GroupType.PROXY){
            LoadBalancerModule.getInstance().getProxyStorage().put(service, new ProxyData(0, service, host, port, new SubGate(service, host, port)));
        }
    }

    @EventHandler(priority = 60)
    public void handelServiceShutdown(ServiceRemoveEvent event){
        if (LoadBalancerModule.getInstance().getProxyStorage().containsKey(event.getServiceName())){
            LoadBalancerModule.getInstance().getProxyStorage().remove(event.getServiceName());
        }
    }
}
