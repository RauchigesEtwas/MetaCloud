package io.metacloud.manager;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.channels.Channel;
import io.metacloud.command.CommandDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.commands.*;
import io.metacloud.manager.networking.NodeHandlerListener;
import io.metacloud.manager.networking.ServiceHandlerListener;
import io.metacloud.network.packets.nodes.ManagerShuttingDownPacket;
import io.metacloud.network.server.NetworkServerDriver;
import io.metacloud.webservice.bin.RestServer;
import io.metacloud.webservice.restconfigs.livenodes.NodesRestConfig;
import jline.internal.ShutdownHooks;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class MetaManager {


    private NetworkServerDriver serverDriver;

    public MetaManager(String[] args){


        prepareRestServer();
        prepareModules();
        prepareCommands();
        prepareNetworkingServer();
        shutdownHook();

        long time =  Driver.getInstance().getStorageDriver().getStartTime();
        long finalTime =  (System.currentTimeMillis() - time);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the cloud is §bnow ready§7 to use [It takes §b"+finalTime+" ms§r]");
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "a new node called §bInternalNode§7 wants to connect");
        NodesRestConfig config = new NodesRestConfig();
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, false, "The node would be §aconnected§7, all data would be §aloaded§7 to the §bRestAPI.");
        config.setName("InternalNode");
        config.setServices(new ArrayList<>());
        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("running-node-InternalNode", config);
        Driver.getInstance().getGroupDriver().getGroupsFromNode("InternalNode").forEach(group -> {
            Driver.getInstance().getGroupDriver().launchService(group.getName(), group.getMinOnlineServers());
        });
        while (true){}

    }


    private void prepareCommands(){
        Thread execuet = new Thread(() -> {
        CommandDriver driver =  Driver.getInstance().getConsoleDriver().getCommandDriver();
        driver.registerCommand(new HelpCommand());
        driver.registerCommand(new GroupCommand());
        driver.registerCommand(new ClearCommand());
        driver.registerCommand(new EndCommand());
        driver.registerCommand(new MetaCloudCommand());
        driver.registerCommand(new NodeCommand());
        driver.registerCommand(new ServiceCommand());

        });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();

    }

    private void prepareModules(){

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES, false, "the modules are being prepared");

        Driver.getInstance().getModuleDriver().enableAllModules();

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES, false, "all modules were loaded there were §b"+ Driver.getInstance().getModuleDriver().getLoadedModules().size()+" modules§7 in total");
    }


    private void prepareNetworkingServer(){
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        this.serverDriver = new NetworkServerDriver();
        this.serverDriver.bind(service.getCommunication().getNetworkingPort()).run();
        NetworkingBootStrap.packetListenerHandler.registerListener(new NodeHandlerListener());
        NetworkingBootStrap.packetListenerHandler.registerListener(new ServiceHandlerListener());
    }


    private void prepareRestServer(){
        Thread execuet = new Thread(() -> {
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the Restserver is being prepared..");

        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        RestServer restServer = new RestServer();
        restServer.bind(service.getCommunication().getRestApiPort())
                        .setAuthenticator(service.getCommunication().getRestApiAuthKey());
        Driver.getInstance().getRestDriver().registerRestServer(restServer);
        RestServer runningServer = Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "Upload the data to the Restserver...");

        runningServer.addContent("service", "./service.json", service);

        NodeConfiguration nodes = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);


        runningServer.addContent("nodes", "./local/nodes.json", nodes);

        Driver.getInstance().getGroupDriver().getGroups().forEach(configuration -> {Driver.getInstance().getGroupDriver().deployOnRest(configuration.getName(), null);});


        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the Restserver is now bound on port " + service.getCommunication().getRestApiPort());
    });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();
    }

    private void shutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Driver.getInstance().getConnectionDriver().getAllNodesChannel().forEach(new Consumer<Channel>() {
                @Override
                public void accept(Channel channel) {
                    channel.sendPacket(new ManagerShuttingDownPacket());

                }
            });
            Driver.getInstance().getServiceDriver().getRunningProcesses().forEach(cloudService -> {
                Driver.getInstance().getServiceDriver().haltService(cloudService.getStorage().getServiceName());
            });
        }));
    }

}
