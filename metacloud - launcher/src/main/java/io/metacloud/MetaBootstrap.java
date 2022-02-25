package io.metacloud;

import io.metacloud.console.ConsoleDriver;
import io.metacloud.console.logger.enums.MSGType;

public class MetaBootstrap {


    public static void main(String[] args) {
        new Driver();
        if (Driver.getInstance().getConsoleDriver() == null){
            Driver.getInstance().setConsoleDriver(new ConsoleDriver());
        }
        Driver.getInstance().getConsoleDriver().clearScreen();
        Driver.getInstance().getConsoleDriver().getLogger().sendMessage(MSGType.MESSAGETYPE_EMPTY, false, Driver.getInstance().getCloudStorage().getCloudLogo());


        while (true){}
    }

}
