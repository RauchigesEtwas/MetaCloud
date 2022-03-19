package io.metacloud;

import io.metacloud.command.CloudCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloudStorageDriver {

    private boolean cloudSetup;
    private Map<String, Object> setupStorage;
    private Integer setupStep;
    private String setupType;
    private String version = "HURRIKAN-1.0.1";
    private boolean Shutdown = false;

    public boolean isShutdown() {
        return Shutdown;
    }

    public void setShutdown(boolean shutdown) {
        Shutdown = shutdown;
    }

    public CloudStorageDriver() {
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

    public ArrayList<String> getGroups() {
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".json")[0];
            modules.add(group);
        }
        return modules;
    }

}
