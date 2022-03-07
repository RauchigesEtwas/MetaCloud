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
        return "              §b__  ___   §7  __        §b______§7__                __§7\n" +
                "            §b§b /  |/  /§7__  / /_____ _§b/ ____§7/ /___  __  ______/ /§7\n" +
                "            §b/ /|_/ /§7 _ \\/ __/ __ `§b/ /   §7/ / __ \\/ / / / __  / §7\n" +
                "          §b / /  / /§7  __/ /_/ /_/ §b/ /___/ §7/ /_/ / /_/ / /_/ /  §7\n" +
                "          §b/_/  /_/§7\\___/\\__/\\__,_/§b\\____/§7_/§7\\____/\\__,_/\\__,_/ §7\n" +
                "                    The §bCloudSystem§r for §bEveryone§r\n\n" +
                "     <§b!§7> thank you for §bchoosing §7the §bMetacloud-Service§7 \n" +
                "     <§b!§7> visit our Support-discord: §bhttps://discord.gg/4kKEcaP9WC\n";
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
