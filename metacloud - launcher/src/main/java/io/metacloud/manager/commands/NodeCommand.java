package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeProperties;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.webservice.restconfigs.nodesetup.NodeSetupCommunication;
import io.metacloud.webservice.restconfigs.nodesetup.NodeSetupConfig;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(command = "node", description = "manage all Nodes", aliases = {"nodes", "wrappers"})
public class NodeCommand extends CloudCommand {
    boolean exists;
    boolean isDeletet;
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        if (args.length == 0){
            sendHelp(logger);
        }else if (args[0].equalsIgnoreCase("create")){
            if (args.length == 3){
                NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
                ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                String  name = args[1];
                String  host = args[2];
                this.exists = false;
                configuration.getNodes().forEach(nodeProperties -> {
                    if (nodeProperties.getNodeName().equalsIgnoreCase(name)){
                        this.exists = true;
                    }
                });
                if (!this.exists){

                    service.getCommunication().getWhitelistAddresses().add(host);

                    new ConfigDriver("./service.json").save(service);
                    Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("service", service);


                    NodeProperties properties = new NodeProperties();
                    properties.setNodeName(name);
                    properties.setNodeHost(host);

                    configuration.getNodes().add(properties);
                    new ConfigDriver("./local/nodes.json").save(configuration);

                    logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the node has been added to the cloud ");

                    NodeSetupConfig setupConfig = new NodeSetupConfig();
                    setupConfig.setNodeName(name);
                    NodeSetupCommunication communication = new NodeSetupCommunication();
                    communication.setNetworkCommunicationPort(service.getCommunication().getNetworkingPort());
                    communication.setRestAPICommunicationPort(service.getCommunication().getRestApiPort());
                    communication.setManagerHostAddress(service.getCommunication().getManagerHostAddress());
                    communication.setNetworkAuthKey(service.getCommunication().getNodeAuthKey());
                    communication.setRestAPIAuthKey(service.getCommunication().getRestApiAuthKey());
                    setupConfig.setCommunication(communication);
                    Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("setup-node-" + name , setupConfig);

                    logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the Setup Link: §b http://"+ service.getCommunication().getManagerHostAddress()+":" + service.getCommunication().getRestApiPort()+"/"+service.getCommunication().getRestApiAuthKey()+ "/setup-node-" + name);
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "A node already §bexists §7under this name");
                }

            }else {
             sendHelp(logger);
            }
        }else if (args[0].equalsIgnoreCase("delete")){
            if (args.length == 2){
                String  node = args[1];
                isDeletet = false;
                NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
                List<NodeProperties> nodeProperties = new ArrayList<>();

                configuration.getNodes().forEach(properties -> {
                    if (        properties.getNodeName().equalsIgnoreCase(node)){

                        isDeletet = true;
                    }else {
                        nodeProperties.add(properties);
                    }

                });

                configuration.setNodes(nodeProperties);

                new ConfigDriver("./local/nodes.json").save(configuration);

                if (isDeletet){
                    logger.log(MSGType.MESSAGETYPE_SUCCESS,  "the node was§b successfully§7 deleted, all services are now §bstopped");
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified node §bcould not be found§7 in the Cloud ");
                }


            }else{
                sendHelp(logger);
            }

        } else if (args[0].equalsIgnoreCase("list")){
            NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
            configuration.getNodes().forEach(properties -> {
                if (properties.getNodeName().equals("InternalNode")){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  " > §b" + properties.getNodeName() + "~" + properties.getNodeHost() + "  §7(§aConnected§7)");
                }else if (Driver.getInstance().getConnectionDriver().isNodeRegistered(properties.getNodeName())){
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  " > §b" + properties.getNodeName() + "~" + properties.getNodeHost() + "  §7(§aConnected§7)");
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  " > §b" + properties.getNodeName() + "~" + properties.getNodeHost() + "  §7(§cOffline§7)");
                }

            });
        }else {
            sendHelp(logger);
        }

        return false;
    }


    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        ArrayList<String> results = new ArrayList<>();
        if (args.length == 0){
            results.add("create");
            results.add("delete");
            results.add("list");
        }else if (args[0].equalsIgnoreCase("delete")){
            if (args.length == 1){
                NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
                configuration.getNodes().forEach(properties -> {
                    results.add(properties.getNodeName());
                });
            }
        }

        return results;
    }

    @Override
    public void sendHelp(Logger logger) {
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bnode create §7<§bname§7> §7<§bhost§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bnode delete §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bnode list");
    }
}
