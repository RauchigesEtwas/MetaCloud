package io.metacloud.manager.commands;

import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeProperties;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
                String  name = args[1];
                String  host = args[2];
                this.exists = false;
                configuration.getNodes().forEach(nodeProperties -> {
                    if (nodeProperties.getNodeName().equalsIgnoreCase(name)){
                        this.exists = true;
                    }
                });
                if (!this.exists){

                    NodeProperties properties = new NodeProperties();
                    properties.setNodeName(name);
                    properties.setNodeHost(host);

                    configuration.getNodes().add(properties);
                    new ConfigDriver("./local/nodes.json").save(configuration);
                    ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
                    logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the node has been added to the cloud ");
                    logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the Setup Link: §b http://"+ service.getCommunication().getManagerHostAddress()+":" + service.getCommunication().getRestApiPort()+"/"+service.getCommunication().getRestApiAuthKey()+ "/SETUP-NODE-" + name);
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "A node already §bexists §7under this name");
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
                    logger.log(MSGType.MESSAGETYPE_SUCCESS, true, "the node was§b successfully§7 deleted, all services are now §bstopped");
                }else{
                    logger.log(MSGType.MESSAGETYPE_COMMAND, true, "The specified node §bcould not be found§7 in the Cloud ");
                }


            }else{
                sendHelp(logger);
            }

        } else if (args[0].equalsIgnoreCase("list")){
            NodeConfiguration configuration = (NodeConfiguration) new ConfigDriver("./local/nodes.json").read(NodeConfiguration.class);
            configuration.getNodes().forEach(properties -> {
                logger.log(MSGType.MESSAGETYPE_COMMAND, true, " > §b" + properties.getNodeName() + "~" + properties.getNodeHost());
            });
        }else {
            sendHelp(logger);
        }

        return false;
    }


    private void sendHelp(Logger logger){
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bnode create §7<§bname§7> §7<§bhost§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bnode delete §7<§bname§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "> §bnode list");
    }

}
