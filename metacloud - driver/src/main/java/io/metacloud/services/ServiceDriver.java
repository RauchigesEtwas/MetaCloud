package io.metacloud.services;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.services.processes.CloudService;
import io.metacloud.services.processes.utils.ServiceStorage;

import java.io.File;
import java.util.ArrayList;

public class ServiceDriver {


    private ArrayList<CloudService> runningProcesses;
    private ArrayList<Integer> usedPorts;

    public ServiceDriver() {
        this.usedPorts = new ArrayList<>();
        this.runningProcesses = new ArrayList<>();
    }




    public void launchService(ServiceStorage storage){
        CloudService service = new CloudService().bindStorage(storage);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+service.getStorage().getServiceName()+"§7 is now being §bprepared...");
        service.run();
        runningProcesses.add(service);
    }

    public CloudService getService(String service){
        for (int i = 0; i != this.runningProcesses.size() ; i++) {
            CloudService cloudService = this.runningProcesses.get(i);
            if (cloudService.getStorage().getServiceName().equals(service)){
                return cloudService;
            }
        }
        return null;
    }

    public Integer getFreePort(boolean proxiedGroup){
        int port = 0;

        if (new File("./service.json").exists()){
            ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
            if (proxiedGroup){
                int startPort = service.getGeneral().getDefaultProxyStartupPort();

                for (int i = startPort; i != startPort + 900000; i++ ){
                    if (!this.usedPorts.contains(i)){
                        port = i;
                        this.usedPorts.add(port);
                        i = startPort + 900000;
                        return port;
                    }
                }
            }else {
                int startPort = service.getGeneral().getDefaultServerStartupPort();

                for (int i = startPort; i != startPort + 900000; i++ ){
                    if (!this.usedPorts.contains(i)){
                        port = i;
                        i = startPort + 900000;
                        this.usedPorts.add(port);
                        return port;
                    }
                }
            }
        }else{

            GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);
            ServiceConfiguration service = (ServiceConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig(
                    "http://" + configuration.getManagerHostAddress() + ":" + configuration.getRestAPICommunicationPort() +"/" + configuration.getRestAPIAuthKey() +
                            "/service", ServiceConfiguration.class);
            if (proxiedGroup){
                int startPort = service.getGeneral().getDefaultProxyStartupPort();
                for (int i = startPort; i != startPort + 900000; i++ ){
                    if (!this.usedPorts.contains(i)){
                        port = i;
                        this.usedPorts.add(port);
                        i = startPort + 900000;
                        return port;
                    }
                }
            }else {
                int startPort = service.getGeneral().getDefaultServerStartupPort();

                for (int i = startPort; i != startPort + 900000; i++ ){
                    if (!this.usedPorts.contains(i)){
                        port = i;
                        this.usedPorts.add(port);
                        i = startPort + 900000;
                        return port;
                    }
                }
            }
        }


        return 0;
    }

    public ArrayList<CloudService> getRunningProcesses() {
        return runningProcesses;
    }

    public void haltService(String serviceName){
        CloudService service = getService(serviceName);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service name is now executed...");
        service.stop();
        this.usedPorts.remove(service.getStorage().getSelectedPort());
        this.runningProcesses.remove(service);
    }

}
