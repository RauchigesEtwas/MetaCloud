package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.modules.LoadedModule;

import java.util.ArrayList;
import java.util.function.Consumer;


@CommandInfo(command = "modules", description = "manage all modules", aliases = {"module"})
public class ModuleCommand extends CloudCommand {


    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        if (args.length == 0){
            sendHelp(logger);
        }else if (args[0].equalsIgnoreCase("list")){
            Driver.getInstance().getModuleDriver().getLoadedModules().forEach(loadedModule -> {
                logger.log(MSGType.MESSAGETYPE_COMMAND,  "§7> Name: §b" + loadedModule.getProperties().getProperty("name") +"§7~Author(s):§b " + loadedModule.getProperties().getProperty("author") +"§7-Version:§b " +  loadedModule.getProperties().getProperty("version") );
            });
        }else if (args[0].equalsIgnoreCase("reload")){
            if (args.length == 2){
                String module = args[1];

                boolean exists = false;
                LoadedModule selecedModule = null;

                for (int i = 0; i != Driver.getInstance().getModuleDriver().getLoadedModules().size() ; i++) {
                    LoadedModule module1 = Driver.getInstance().getModuleDriver().getLoadedModules().get(i);
                    if ( module1.getProperties().getProperty("name").equalsIgnoreCase(module)){
                        selecedModule = module1;
                        exists = true;
                    }
                }
                if (exists){
                    selecedModule.reload();
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The module was reloaded with §asuccess");

                }else {
                    logger.log(MSGType.MESSAGETYPE_COMMAND,  "The specified module §bcould not be found§7 in the Cloud ");
                }

            }else {
                sendHelp(logger);
            }
        }else {
            sendHelp(logger);
        }


        return false;
    }


    @Override
    public void sendHelp(Logger logger) {
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bmodules reload §7<§bmodule§7>");
        logger.log(MSGType.MESSAGETYPE_COMMAND,  "> §bmodules list");
    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {

        ArrayList<String> results = new ArrayList<>();
        if (args.length == 0){
            results.add("list");
            results.add("reload");
        }else if (args[0].equalsIgnoreCase("reload")){
            if (args.length==1){
                Driver.getInstance().getModuleDriver().getLoadedModules().forEach(loadedModule -> {
                    results.add( loadedModule.getProperties().getProperty("name"));
                });
            }
        }else{
            results.add("list");
            results.add("reload");
        }

        return results;
    }
}
