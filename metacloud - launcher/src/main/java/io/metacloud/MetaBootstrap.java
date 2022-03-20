package io.metacloud;

import io.metacloud.configuration.ConfigDriver;
import io.metacloud.console.ConsoleDriver;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.MetaManager;
import io.metacloud.node.MetaNode;
import lombok.SneakyThrows;

import java.io.File;

public class MetaBootstrap {


    @SneakyThrows
    public static void main(String[] args) {
        new Driver();
        if (Driver.getInstance().getConsoleDriver() == null){
            Driver.getInstance().setConsoleDriver(new ConsoleDriver());
        }
        Driver.getInstance().getConsoleDriver().clearScreen();
        Thread.sleep(250);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getStorageDriver().getCloudLogo());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "everything is being prepared...");

        if(!new File("./service.json").exists()){
            if(!new File("./nodeservice.json").exists()){
                Driver.getInstance().getConsoleDriver().clearScreen();

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getStorageDriver().getCloudLogo());
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "it seems that the cloud is starting for the first time");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "please specify what you would like to setup?");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "types: §3Manager §7/ §3Node");
                Driver.getInstance().getStorageDriver().setCloudSetup(true);
                Driver.getInstance().getStorageDriver().setSetupStep(0);
                Driver.getInstance().getStorageDriver().setSetupType("MAIN_SETUP");
                while (true){}
            }

        }
        if(!new File("./nodeservice.json").exists()){

            if(!new File("./service.json").exists()){
                Driver.getInstance().getConsoleDriver().clearScreen();

                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getStorageDriver().getCloudLogo());
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "it seems that the cloud is starting for the first time");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "please specify what you would like to setup?");
                Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, false, "types: §3Manager §7/ §3Node");
                Driver.getInstance().getStorageDriver().setCloudSetup(true);
                Driver.getInstance().getStorageDriver().setSetupStep(0);
                Driver.getInstance().getStorageDriver().setSetupType("MAIN_SETUP");
                while (true){}
            }

        }

        new File("./live/").mkdirs();

        if (new File("./service.json").exists()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the manager was prepared§7 and will be start");

            new MetaManager(args);
        }else {
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the node was prepared§7 and will be start");
            new MetaNode(args);
        }

    }

}
