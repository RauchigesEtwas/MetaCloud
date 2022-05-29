package io.metacloud.manager;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.command.CommandDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.commands.*;
import io.metacloud.manager.networking.ApiHandleListener;
import io.metacloud.manager.networking.NodeHandlerListener;
import io.metacloud.manager.networking.ServiceHandlerListener;
import io.metacloud.manager.networking.SignHandlerListener;
import io.metacloud.network.packets.nodes.in.ManagerShuttingDownPacket;
import io.metacloud.network.server.NetworkServerDriver;
import io.metacloud.webservice.bin.RestServer;
import io.metacloud.webservice.restconfigs.livenodes.NodesRestConfig;
import io.metacloud.webservice.restconfigs.statistics.CloudStatisticsConfig;


import java.util.ArrayList;

public class MetaManager {


    private NetworkServerDriver serverDriver;

    public MetaManager(String[] args){


        prepareRestServer();
        prepareModules();
        prepareCommands();
        prepareNetworkingServer();






        long time =  Driver.getInstance().getStorageDriver().getStartTime();
        long finalTime =  (System.currentTimeMillis() - time);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the cloud is now up and ready to use (§b"+finalTime+"ms§7)");
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, "a new node called §bInternalNode§7 wants to connect");
        NodesRestConfig config = new NodesRestConfig();
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_NETWORK, "The node would be §aconnected§7, all data would be §aloaded§7 to the §bRestAPI.");
        config.setName("InternalNode");
        config.setServices(new ArrayList<>());
        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("running-node-InternalNode", config);
        Driver.getInstance().getGroupDriver().getGroupsFromNode("InternalNode").forEach(group -> {
          Driver.getInstance().getGroupDriver().launchService(group.getName(), group.getMinOnlineServers());
        });
        while (true){}

    }


    private void prepareCommands(){
        CommandDriver driver =  Driver.getInstance().getConsoleDriver().getCommandDriver();
        driver.registerCommand(new HelpCommand());
        driver.registerCommand(new GroupCommand());
        driver.registerCommand(new ClearCommand());
        driver.registerCommand(new EndCommand());
        driver.registerCommand(new MetaCloudCommand());
        driver.registerCommand(new NodeCommand());
        driver.registerCommand(new ModuleCommand());
        driver.registerCommand(new ScreenCommand());
        driver.registerCommand(new ServiceCommand());
        driver.registerCommand(new ReloadCommand());
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "all commands were prepared and loaded (§b"+driver.getCommands().size()+" commands§7)");


    }

    private void prepareModules(){

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES, "all §bmodules§7 are prepared...");

        Driver.getInstance().getModuleDriver().enableAllModules();

        }


    private void prepareNetworkingServer(){
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        this.serverDriver = new NetworkServerDriver();
        this.serverDriver.bind(service.getCommunication().getNetworkingPort()).run();
        NetworkingBootStrap.packetListenerHandler.registerListener(new NodeHandlerListener());
        NetworkingBootStrap.packetListenerHandler.registerListener(new ServiceHandlerListener());
        NetworkingBootStrap.packetListenerHandler.registerListener(new ApiHandleListener());
        NetworkingBootStrap.packetListenerHandler.registerListener(new SignHandlerListener());
    }


    private void prepareRestServer(){
        Thread execuet = new Thread(() -> {
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the §bREST-Server§7 is being prepared..");

        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        RestServer restServer = new RestServer();
        restServer.bind(service.getCommunication().getRestApiPort())
                        .setAuthenticator(service.getCommunication().getRestApiAuthKey());
        Driver.getInstance().getRestDriver().registerRestServer(restServer);



        RestServer runningServer = Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "Upload the data to the §bREST-Server§7...");

            service.getMessages().setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getPrefix()));
            service.getMessages().setMaintenanceGroupMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceGroupMessage()));
            service.getMessages().setMaintenanceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceKickMessage()));
            service.getMessages().setNoFallbackKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getNoFallbackKickMessage()));
            service.getMessages().setFullNetworkKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullNetworkKickMessage()));
            service.getMessages().setFullServiceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullServiceKickMessage()));
            service.getMessages().setOnlyProxyJoinKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getOnlyProxyJoinKickMessage()));
            service.getMessages().setHubCommandNoFallbackFound(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandNoFallbackFound()));
            service.getMessages().setHubCommandAlreadyOnFallBack(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandAlreadyOnFallBack()));
            service.getMessages().setHubCommandSendToAnFallback(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandSendToAnFallback()));
            service.getMessages().setServiceStartingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStartingNotification()));
            service.getMessages().setServiceConnectedToProxyNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceConnectedToProxyNotification()));
            service.getMessages().setServiceStoppingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStoppingNotification()));


        runningServer.addContent("service", "./service.json", service);

        NodeConfiguration nodes = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);

            CloudStatisticsConfig config = new CloudStatisticsConfig();
            config.setPlayers(new ArrayList<>());
            config.setRunningServices(0);
            runningServer.addContent("statistics",config);
        runningServer.addContent("nodes", "./local/nodes.json", nodes);

        Driver.getInstance().getGroupDriver().getGroups().forEach(configuration -> {Driver.getInstance().getGroupDriver().deployOnRest(configuration.getName(), null);});


        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the §bREST-Server§7 is now bound on port §b" + service.getCommunication().getRestApiPort());
    });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();
    }

    public static void shutdown(){
                Driver.getInstance().getConnectionDriver().getAllNodesChannel().forEach(channel -> channel.sendPacket(new ManagerShuttingDownPacket()));

                Driver.getInstance().getServiceDriver().getRunningProcesses().forEach(cloudService -> {
                    cloudService.stop();
                });
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "Thanks for using MetaCloud ;->");
                System.exit(0);
    }

}
