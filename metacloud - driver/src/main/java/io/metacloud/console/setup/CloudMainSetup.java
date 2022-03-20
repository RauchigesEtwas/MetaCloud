package io.metacloud.console.setup;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeProperties;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.webservice.restconfigs.nodesetup.NodeSetupConfig;
import jline.console.ConsoleReader;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CloudMainSetup {

    public CloudMainSetup(ConsoleReader reader, String line) {
        switch (Driver.getInstance().getStorageDriver().getSetupStep()){
            case 0:
                if (line.equalsIgnoreCase("manager")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("MODE", "MANAGER");
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "should the cloud update itself automatically? | §3y §7/ §3n");


                }else if (line.equalsIgnoreCase("node")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("MODE", "NODE");
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "Please enter the link from the manager to finish the setup!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "example: §3http://your-ip/api-auth-key/node-name/");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "Incorrect entry");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "You can only deselect between manager and node.");

                }
                break;
            case 1:
                if (Driver.getInstance().getStorageDriver().getSetupStorage().get("MODE").toString().equalsIgnoreCase("MANAGER")){
                    if (line.equalsIgnoreCase("y")){
                        Driver.getInstance().getStorageDriver().getSetupStorage().put("autoUpdate", "true");
                        Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "what is the address of the manager?");

                    }else {
                        Driver.getInstance().getStorageDriver().getSetupStorage().put("autoUpdate", "false");
                        Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "what is the address of the manager?");
                    }
                }else {
                    //todo: File Create


                    if (line.contains("http://")) {
                        NodeSetupConfig config = (NodeSetupConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig(line, NodeSetupConfig.class);

                        if (config == null){
                            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter an right URL-Link!");
                            break;
                        }

                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "You have successfully completed the setup!");
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "the cloud will be relaunched shortly");
                        GeneralNodeConfiguration configuration = new GeneralNodeConfiguration();
                        configuration.setNodeName(config.getNodeName());
                        configuration.setRestAPIAuthKey(config.getCommunication().getRestAPIAuthKey());
                        configuration.setManagerHostAddress(config.getCommunication().getManagerHostAddress());
                        configuration.setNetworkAuthKey(config.getCommunication().getNetworkAuthKey());
                        configuration.setNetworkCommunicationPort(config.getCommunication().getNetworkCommunicationPort());
                        configuration.setRestAPICommunicationPort(config.getCommunication().getRestAPICommunicationPort());

                        new ConfigDriver("./nodeservice.json").save(configuration);

                        new File("./live/").mkdirs();
                        new File("./local/").mkdirs();
                        new File("./local/storage/jars/").mkdirs();
                        new File("./local/storage/cache/").mkdirs();
                        new File("./local/templates/").mkdirs();
                        new File("./modules/").mkdirs();

                        System.exit(0);

                    }else{
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter an right URL-Link!");

                    }
                }
                break;

            case 2:
                if (line.contains(".")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("managerHostAddress", line);
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "what is the internal communication port? | default: §37862");
                }else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter a numeric address!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "what is the address of the manager?");
                }
                break;
            case 3:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("networkingPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "What is the RestAPI port to be used? | default: §38012");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "what is the internal communication port? | default: §37862");

                }
                break;
            case 4:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("restApiPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "from which port should the proxies start? | default: §325565");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "What is the RestAPI port to be used? | default: §38012");
                }
                break;
            case 5:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultProxyStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "from which port should the servers start? | default: §34000");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "from which port should the proxies start? | default: §325565");
                }
                break;
            case 6:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultServerStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "You have successfully completed the setup!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "the cloud will be relaunched shortly");


                    ServiceConfiguration configuration = new ServiceConfiguration();
                    ServiceConfiguration.Communication communication = new ServiceConfiguration.Communication();
                    communication.setManagerHostAddress(Driver.getInstance().getStorageDriver().getSetupStorage().get("managerHostAddress").toString());
                    communication.setNetworkingPort(Integer.valueOf(Driver.getInstance().getStorageDriver().getSetupStorage().get("networkingPort").toString()));
                    communication.setRestApiPort(Integer.valueOf(Driver.getInstance().getStorageDriver().getSetupStorage().get("restApiPort").toString()));

                    ArrayList<String> addresses = new ArrayList<>();

                    String address = Driver.getInstance().getStorageDriver().getSetupStorage().get("managerHostAddress").toString();

                    if (!address.equalsIgnoreCase("127.0.0.1")){
                        addresses.add(address);
                    }

                    addresses.add("127.0.0.1");

                    communication.setWhitelistAddresses(addresses);
                    communication.setNodeAuthKey(UUID.randomUUID() + UUID.randomUUID().toString());
                    communication.setRestApiAuthKey(UUID.randomUUID() + UUID.randomUUID().toString());
                    configuration.setCommunication(communication);
                    ServiceConfiguration.General general = new ServiceConfiguration.General();
                    general.setAutoUpdate(Boolean.getBoolean(Driver.getInstance().getStorageDriver().getSetupStorage().get("autoUpdate").toString()));
                    general.setDefaultProxyStartupPort(Integer.valueOf(Driver.getInstance().getStorageDriver().getSetupStorage().get("defaultProxyStartupPort").toString()));
                    general.setDefaultServerStartupPort(Integer.valueOf(Driver.getInstance().getStorageDriver().getSetupStorage().get("defaultServerStartupPort").toString()));
                    general.setShowPlayerConnections(true);
                    general.setProxyOnlineMode(true);
                    general.setServerSplitter("-");
                    configuration.setGeneral(general);
                    ServiceConfiguration.Messages messages = new ServiceConfiguration.Messages();
                    messages.setPrefix("§3MetaCloud §8| §7");
                    messages.setMaintenanceGroupMessage("§cthis group is in §4maintenance");
                    messages.setMaintenanceKickMessage("§cThe network is currently in §4maintenance");
                    messages.setNoFallbackKickMessage("§cno fallback can be §4found");
                    messages.setFullNetworkKickMessage("§cthe network is full, please buy §4Premium");
                    messages.setFullServiceKickMessage("§cthe service is §4full");
                    messages.setOnlyProxyJoinKickMessage("§cpleas join over the main address");

                    configuration.setMessages(messages);
                    new ConfigDriver("./service.json").save(configuration);




                    new File("./live/").mkdirs();
                    new File("./local/").mkdirs();
                    new File("./local/storage/jars/").mkdirs();
                    new File("./local/storage/cache/").mkdirs();
                    new File("./local/groups/").mkdirs();
                    new File("./local/templates/").mkdirs();
                    new File("./modules/").mkdirs();

                    NodeConfiguration nodes = new NodeConfiguration();

                    NodeProperties properties = new NodeProperties();
                    properties.setNodeName("InternalNode");
                    properties.setNodeHost("127.0.0.1");

                    nodes.getNodes().add(properties);


                    new ConfigDriver("./local/nodes.json").save(nodes);

                    System.exit(0);

                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, true, "from which port should the servers start? | default: §34000");
                }
                break;
        }
    }
}
