package io.metacloud.manager;

import io.metacloud.Driver;
import io.metacloud.command.CommandDriver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.manager.commands.ClearCommand;
import io.metacloud.manager.commands.GroupCommand;
import io.metacloud.manager.commands.HelpCommand;
import io.metacloud.webservice.bin.RestServer;

import java.util.Map;

public class MetaManager {

    public MetaManager(String[] args){


        prepareRestServer();
        prepareModules();
        prepareCommands();


        while (true){}

    }


    private void prepareCommands(){
        CommandDriver driver =  Driver.getInstance().getConsoleDriver().getCommandDriver();
        driver.registerCommand(new HelpCommand());
        driver.registerCommand(new GroupCommand());
        driver.registerCommand(new ClearCommand());
    }

    private void prepareModules(){
        int moduleCount = 0;
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the modules are being prepared");

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "all modules were loaded there were "+moduleCount+" modules in total");
    }


    private void prepareRestServer(){

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the Restserver is being prepared..");

        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);
        RestServer restServer = new RestServer();
        restServer.bind(service.getCommunication().getRestApiPort())
                        .setAuthenticator(service.getCommunication().getRestApiAuthKey());
        Driver.getInstance().getRestDriver().registerRestServer(restServer);
        //inizalisation of all Configs!
        RestServer runningServer = Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort());
        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "Upload the data to the Restserver...");

        runningServer.addContent("service", "./service.json", service);

        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, false, "the Restserver is now bound on port " + service.getCommunication().getRestApiPort());

    }

}
