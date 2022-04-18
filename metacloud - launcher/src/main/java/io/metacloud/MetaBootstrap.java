package io.metacloud;

import io.metacloud.configuration.ConfigDriver;
import io.metacloud.console.ConsoleDriver;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.MetaManager;
import io.metacloud.node.MetaNode;
import io.metacloud.webservice.DownloadDriver;
import lombok.SneakyThrows;

import java.io.File;

public class MetaBootstrap {


    @SneakyThrows
    public static void main(String[] args) {
        new Driver();
        if (Driver.getInstance().getConsoleDriver() == null){
            Driver.getInstance().setConsoleDriver(new ConsoleDriver());
            Driver.getInstance().getConsoleDriver().start();
        }
        Driver.getInstance().getStorageDriver().setStartTime(System.currentTimeMillis());

        Driver.getInstance().getConsoleDriver().clearConsole();
        Thread.sleep(250);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY,  Driver.getInstance().getStorageDriver().getCloudLogo());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO,  "everything is being prepared...");

        if(!new File("./service.json").exists()){
            if(!new File("./nodeservice.json").exists()){
                Driver.getInstance().getConsoleDriver().clearConsole();

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY,  Driver.getInstance().getStorageDriver().getCloudLogo());
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "it seems that the cloud is starting for the first time");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "please specify what you would like to setup?");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "types: §3Manager §7/ §3Node");
                Driver.getInstance().getStorageDriver().setCloudSetup(true);
                Driver.getInstance().getStorageDriver().setSetupStep(0);
                Driver.getInstance().getStorageDriver().setSetupType("MAIN_SETUP");
                while (true){}
            }

        }
        if(!new File("./nodeservice.json").exists()){

            if(!new File("./service.json").exists()){
                Driver.getInstance().getConsoleDriver().clearConsole();

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY,  Driver.getInstance().getStorageDriver().getCloudLogo());
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "it seems that the cloud is starting for the first time");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "please specify what you would like to setup?");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP,  "types: §3Manager §7/ §3Node");
                Driver.getInstance().getStorageDriver().setCloudSetup(true);
                Driver.getInstance().getStorageDriver().setSetupStep(0);
                Driver.getInstance().getStorageDriver().setSetupType("MAIN_SETUP");
                while (true){}
            }

        }


        if(!new File("./local/server-icon.png").exists()){
            new DownloadDriver("server-icon.png", "./local/", "https://i.ibb.co/LSfzr1p/icon.png");
        }


        if (new File("./service.json").exists()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO,  "an instance of the §bManager§7 is executed");

            new MetaManager(args);
        }else {
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO,  "an instance of the §bNode§7 is executed");
            new MetaNode(args);
        }

    }

}
