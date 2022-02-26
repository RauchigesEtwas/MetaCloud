package io.metacloud;

import io.metacloud.command.CloudCommand;

import java.util.HashMap;
import java.util.Map;

public class CloudStorageDriver {

    private boolean cloudSetup;
    private Map<String, Object> setupStorage;
    private Integer setupStep;
    private String setupType;
    private String version;

    public CloudStorageDriver() {
        this.version = "DEV-1.0.0";
        this.setupStorage = new HashMap<>();
    }

    public String getSetupType() {
        return setupType;
    }

    public void setSetupType(String setupType) {
        this.setupType = setupType;
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

    public String getVersion() {
        return version;
    }

    public String getCloudLogo(){
        return "        __  ___     __        ________                __\n" +
                "       /  |/  /__  / /_____ _/ ____/ /___  __  ______/ /\n" +
                "      / /|_/ / _ \\/ __/ __ `/ /   / / __ \\/ / / / __  / \n" +
                "     / /  / /  __/ /_/ /_/ / /___/ / /_/ / /_/ / /_/ /  \n" +
                "    /_/  /_/\\___/\\__/\\__,_/\\____/_/\\____/\\__,_/\\__,_/ \n" +
                "              The §bCloudSystem§r for §bEveryone§r\n";
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

}
