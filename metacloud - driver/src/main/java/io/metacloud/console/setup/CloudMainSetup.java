package io.metacloud.console.setup;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.nodes.GeneralNodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeConfiguration;
import io.metacloud.configuration.configs.nodes.NodeProperties;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.webservice.restconfigs.nodesetup.NodeSetupConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CloudMainSetup {

    public CloudMainSetup( String line) {
        switch (Driver.getInstance().getStorageDriver().getSetupStep()){
            case 0:
                if (line.equalsIgnoreCase("manager")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("MODE", "MANAGER");
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "should the cloud update itself automatically? | §3y §7/ §3n");


                }else if (line.equalsIgnoreCase("node")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("MODE", "NODE");
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Please enter the link from the manager to finish the setup!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "example: §3http://your-ip/api-auth-key/node-name/");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Incorrect entry");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "You can only deselect between manager and node.");

                }
                break;
            case 1:
                if (Driver.getInstance().getStorageDriver().getSetupStorage().get("MODE").toString().equalsIgnoreCase("MANAGER")){
                    if (line.equalsIgnoreCase("y")){
                        Driver.getInstance().getStorageDriver().getSetupStorage().put("autoUpdate", "true");
                        Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "what is the address of the manager?");

                    }else {
                        Driver.getInstance().getStorageDriver().getSetupStorage().put("autoUpdate", "false");
                        Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "what is the address of the manager?");
                    }
                }else {
                    //todo: File Create


                    if (line.contains("http://")) {
                        NodeSetupConfig config = (NodeSetupConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig(line, NodeSetupConfig.class);

                        if (config == null){
                            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter an right URL-Link!");
                            break;
                        }

                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "You have successfully completed the setup!");
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "the cloud will be relaunched shortly");
                        GeneralNodeConfiguration configuration = new GeneralNodeConfiguration();
                        configuration.setNodeName(config.getNodeName());
                        configuration.setRestAPIAuthKey(config.getCommunication().getRestAPIAuthKey());
                        configuration.setManagerHostAddress(config.getCommunication().getManagerHostAddress());
                        configuration.setNetworkAuthKey(config.getCommunication().getNetworkAuthKey());
                        configuration.setNetworkCommunicationPort(config.getCommunication().getNetworkCommunicationPort());
                        configuration.setRestAPICommunicationPort(config.getCommunication().getRestAPICommunicationPort());

                        new ConfigDriver("./nodeservice.json").save(configuration);
                        new File("./local/").mkdirs();
                        new File("./local/global/plugins/").mkdirs();
                        new File("./local/storage/jars/").mkdirs();
                        new File("./local/storage/cache/").mkdirs();
                        new File("./local/templates/").mkdirs();
                        new File("./modules/").mkdirs();

                        System.exit(0);

                    }else{
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter an right URL-Link!");

                    }
                }
                break;

            case 2:
                if (line.contains(".")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("managerHostAddress", line);
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "what is the internal communication port? | default: §37862");
                }else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter a numeric address!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "what is the address of the manager?");
                }
                break;
            case 3:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("networkingPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "What is the RestAPI port to be used? | default: §38012");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "what is the internal communication port? | default: §37862");

                }
                break;
            case 4:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("restApiPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "from which port should the proxies start? | default: §325565");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "What is the RestAPI port to be used? | default: §38012");
                }
                break;
            case 5:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultProxyStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "from which port should the servers start? | default: §34000");
                }else{
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "from which port should the proxies start? | default: §325565");
                }
                break;
            case 6:
                if(line.matches("[0-9]+")){
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultServerStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep( Driver.getInstance().getStorageDriver().getSetupStep() + 1);
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "You have successfully completed the setup!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "the cloud will be relaunched shortly");


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
                    general.setWhitelist(new ArrayList<>());
                    general.setProxyOnlineMode(true);
                    general.setServerSplitter("-");
                    configuration.setGeneral(general);
                    ServiceConfiguration.Messages messages = new ServiceConfiguration.Messages();
                    messages.setPrefix("§3MetaCloud §8| §7");

                    messages.setMaintenanceKickMessage("§8» §7The §bNetwork§7 is currently in §bmaintenance");
                    messages.setMaintenanceGroupMessage("§8» §7The §bService§7 is currently in §bmaintenance");
                    messages.setNoFallbackKickMessage("§8» §7No §bfallback§7 can be §bfound");
                    messages.setFullNetworkKickMessage("§8» §7The §bNetwork§7 is full, please buy §ePremium");
                    messages.setFullServiceKickMessage("§8» §7The §bService§7 is currently §bfull");
                    messages.setOnlyProxyJoinKickMessage("§8» §7Please join over §bplay.yourserver.io");
                    messages.setHubCommandNoFallbackFound("§3MetaCloud §8| §7No §bfallback§7 can be §bfound");
                    messages.setHubCommandAlreadyOnFallBack("§3MetaCloud §8| §7you are already on a §bFallback");
                    messages.setHubCommandSendToAnFallback("§3MetaCloud §8| §7ýou have ben send to an §bfallback-server");
                    messages.setServiceStartingNotification("§3MetaCloud §8| §7%SERVICE_NAME% is §estarting...");
                    messages.setServiceConnectedToProxyNotification("§3MetaCloud §8| §7%SERVICE_NAME% is §bconnected");
                    messages.setServiceStoppingNotification("§3MetaCloud §8| §7%SERVICE_NAME% is §cstopping...");

                    configuration.setMessages(messages);
                    new ConfigDriver("./service.json").save(configuration);



                    new File("./local/").mkdirs();
                    new File("./local/storage/jars/").mkdirs();
                    new File("./local/storage/cache/").mkdirs();
                    new File("./local/groups/").mkdirs();
                    new File("./local/global/plugins/").mkdirs();
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
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "please enter a number!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "from which port should the servers start? | default: §34000");
                }
                break;
        }
    }
}
