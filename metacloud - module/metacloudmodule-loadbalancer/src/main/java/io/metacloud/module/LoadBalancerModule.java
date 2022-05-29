package io.metacloud.module;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.module.config.Configuration;
import io.metacloud.module.config.ConnectionType;
import io.metacloud.module.listener.CloudListener;
import io.metacloud.module.utils.LoadBalancer;
import io.metacloud.module.utils.subgates.SubGate;
import io.metacloud.modules.interfaces.IModule;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BiConsumer;

public class LoadBalancerModule implements IModule {


    private static LoadBalancerModule instance;
    private HashMap<String, ProxyData> proxyStorage;
    private LoadBalancer loadBalancer;

    @Override
    public void onEnable() {
        instance = this;
        proxyStorage = new HashMap<>();


        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        if (service.getGeneral().getDefaultProxyStartupPort() == 25565){
            service.getGeneral().setDefaultProxyStartupPort(3000);
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the standert port for proxies was set to §b3000");
            new ConfigDriver("./service.json").save(service);
        }

        if (!new File("./modules/metacloud-loadbalancer/config.json").exists()){

            new File("./modules/metacloud-loadbalancer/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setConnectionType(ConnectionType.RANDOM);
            configuration.setConnectionPort(25565);
            new ConfigDriver("./modules/metacloud-loadbalancer/config.json").save(configuration);
        }

        Driver.getInstance().getEventDriver().registerListener(new CloudListener());
        Configuration config = (Configuration) new ConfigDriver("./modules/metacloud-loadbalancer/config.json").read(Configuration.class);

        this.loadBalancer = new LoadBalancer(service.getCommunication().getManagerHostAddress(), config.getConnectionPort());

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {

    }

    public HashMap<String, ProxyData> getProxyStorage() {
        return proxyStorage;
    }

    public static LoadBalancerModule getInstance() {
        return instance;
    }


    public SubGate getRandomSub(){
        Random generator = new Random();
        ArrayList<SubGate> subGates = new ArrayList<>();

        proxyStorage.forEach((s, proxyData) -> {
            subGates.add(proxyData.getSubGate());
        });

        if (subGates.isEmpty()){
            return new SubGate("test", "127.0.0.1", 2012);
        }

        return subGates.get(generator.nextInt(subGates.size()));
    }


}
