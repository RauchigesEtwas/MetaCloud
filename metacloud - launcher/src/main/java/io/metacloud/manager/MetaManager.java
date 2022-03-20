package io.metacloud.manager;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.command.CommandDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.commands.*;
import io.metacloud.manager.networking.NodeHandlerListener;
import io.metacloud.network.server.NetworkServerDriver;
import io.metacloud.webservice.bin.RestServer;
import jline.internal.ShutdownHooks;

import java.util.Map;
import java.util.function.Consumer;

public class MetaManager {


    private NetworkServerDriver serverDriver;

    public MetaManager(String[] args){


        prepareRestServer();
        prepareModules();
        prepareCommands();
        prepareNetworkingServer();


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
    }

    private void prepareModules(){
        int moduleCount = 0;
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the modules are being prepared");

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "all modules were loaded there were "+moduleCount+" modules in total");
    }


    private void prepareNetworkingServer(){
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        this.serverDriver = new NetworkServerDriver();
        this.serverDriver.bind(service.getCommunication().getNetworkingPort()).run();
        NetworkingBootStrap.packetListenerHandler.registerListener(new NodeHandlerListener());
    }


    private void prepareRestServer(){

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

        Driver.getInstance().getGroupDriver().getGroups().forEach(configuration -> {Driver.getInstance().getGroupDriver().deployOnRest(configuration.getName());});


        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the Restserver is now bound on port " + service.getCommunication().getRestApiPort());

    }

}
