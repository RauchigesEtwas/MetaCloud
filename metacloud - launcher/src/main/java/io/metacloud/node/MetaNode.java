package io.metacloud.node;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.command.CommandDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.manager.networking.NodeHandlerListener;
import io.metacloud.network.client.NetworkClientDriver;
import io.metacloud.network.packets.nodes.NodeRegisterPacket;
import io.metacloud.network.packets.nodes.NodeUnregisterPacket;
import io.metacloud.network.server.NetworkServerDriver;
import io.metacloud.node.commands.ClearCommand;
import io.metacloud.node.commands.EndCommand;
import io.metacloud.node.commands.HelpCommand;
import io.metacloud.node.networking.ManagerHandlerListener;

public class MetaNode {


    private NetworkClientDriver networkClientDriver;


    public MetaNode(String[] args){
        Driver.getInstance().getStorageDriver().setShutdownFromManager(false);
        prepareNetworkingClient();
        shutdownHook();
        prepareCommands();

        while (true){}
    }


    public void prepareCommands(){
        CommandDriver driver =  Driver.getInstance().getConsoleDriver().getCommandDriver();
        driver.registerCommand(new HelpCommand());
        driver.registerCommand(new EndCommand());
        driver.registerCommand(new ClearCommand());
    }

    public void prepareNetworkingClient(){

        GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);

        this.networkClientDriver = new NetworkClientDriver();
        this.networkClientDriver.bind(configuration.getManagerHostAddress(), configuration.getNetworkCommunicationPort()).run();
        NodeRegisterPacket packet = new NodeRegisterPacket();
        packet.setNodeName(configuration.getNodeName());
        packet.setAuthKey(configuration.getNetworkAuthKey());
        NetworkingBootStrap.client.sendPacket(packet);
        NetworkingBootStrap.packetListenerHandler.registerListener(new ManagerHandlerListener());
    }

    private void shutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!Driver.getInstance().getStorageDriver().getShutdownFromManager()){
                NodeUnregisterPacket packet = new NodeUnregisterPacket();
                GeneralNodeConfiguration configuration = (GeneralNodeConfiguration) new ConfigDriver("./nodeservice.json").read(GeneralNodeConfiguration.class);
                packet.setNodeName(configuration.getNodeName());
                NetworkingBootStrap.client.sendPacket(packet);
            }

        }));
    }


}
