package io.metacloud;

import io.metacloud.command.CloudCommand;
import io.metacloud.configuration.configs.group.GroupConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageDriver {

    private boolean cloudSetup;
    private Map<String, Object> setupStorage;
    private GroupConfiguration groupSetupStorage;
    private Integer setupStep;
    private String setupType;
    private Boolean shutdownFromManager;
    private String version = "HURRICAN-1.0.0";
    private boolean Shutdown = false;

    public GroupConfiguration getGroupSetupStorage() {
        return groupSetupStorage;
    }

    public void setGroupSetupStorage(GroupConfiguration groupSetupStorage) {
        this.groupSetupStorage = groupSetupStorage;
    }


    public Boolean getShutdownFromManager() {
        return shutdownFromManager;
    }

    public void setShutdownFromManager(Boolean shutdownFromManager) {
        this.shutdownFromManager = shutdownFromManager;
    }

    public boolean isShutdown() {
        return Shutdown;
    }

    public void setShutdown(boolean shutdown) {
        Shutdown = shutdown;
    }

    public StorageDriver() {
        this.setupStorage = new HashMap<>();
    }

    public String getSetupType() {
        return setupType;
    }

    public void setSetupType(String setupType) {
        this.setupType = setupType;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getSetupStorage() {
        return setupStorage;
    }


    public Integer getSetupStep() {
        return setupStep;
    }

    public void setSetupStep(Integer setupStep) {
        this.setupStep = setupStep;
    }

    public boolean isCloudSetup() {
        return cloudSetup;
    }

    public void setCloudSetup(boolean cloudSetup) {
        this.cloudSetup = cloudSetup;
    }

    public String getCloudLogo(){
        return "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b"+version+"§7]\n" +
                "       ________________________________________________________\n" +
                "           The §bnext generation §7of Minecraft §bcloud systems§7\n" +
                "     \n" +
                "     <§b!§7> Thank you for using §bMetaCloudService§7 for your §bNetwork§r\n" +
                "     <!> Our §bSupport§7 can you find -§b https://discord.gg/4kKEcaP9WC\n";
    }
    public CloudCommand fromFirstArgument(String commandLine) {
        String[] split = commandLine.split(" ");
        return Driver.getInstance().getConsoleDriver().getCommandDriver().getCommand(split[0]);
    }

    public String[] dropFirstString(String[] input){
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public boolean versionCheck(String version, String checkType){
        if (checkType.equalsIgnoreCase("PROXY")){
            if ( version.equalsIgnoreCase("bungeecord_latest") || version.equalsIgnoreCase("waterfall_latest") ) {
                return true;
            }else {
                return false;
            }
        }else   if (checkType.equalsIgnoreCase("SERVER")){
            if(
                    //PAPERSPIGOT
                    version.equalsIgnoreCase("PAPERSPIGOT_1_8_8") || version.equalsIgnoreCase("PAPERSPIGOT_1_9_4") || version.equalsIgnoreCase("PAPERSPIGOT_1_10_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_11_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_12_2")
                            || version.equalsIgnoreCase("PAPERSPIGOT_1_13_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_14_4") || version.equalsIgnoreCase("PAPERSPIGOT_1_15_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_16_5") || version.equalsIgnoreCase("PAPERSPIGOT_1_17_1") || version.equalsIgnoreCase("PAPERSPIGOT_1_18_1")


            ){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

}
