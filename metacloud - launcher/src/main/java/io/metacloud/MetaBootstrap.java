package io.metacloud;

import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.console.ConsoleDriver;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.webservice.bin.RestServer;
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
        Thread.sleep(50);
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getCloudStorage().getCloudLogo());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "everything is being prepared...");
        Thread.sleep(150);
        if(!new File("./service.json").exists()){

           /*
            Driver.getInstance().getConsoleDriver().clearScreen();
            Thread.sleep(50);
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getCloudStorage().getCloudLogo());
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "it seems that the cloud is starting for the first time");
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "please specify what you would like to setup?");
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "types: §bManager §7/ §bNode");
            Driver.getInstance().getCloudStorage().setCloudSetup(true);
            Driver.getInstance().getCloudStorage().setSetupStep(0);
            Driver.getInstance().getCloudStorage().setSetupType("MAIN_SETUP");
            */
        }



        while (true){}
    }

}
