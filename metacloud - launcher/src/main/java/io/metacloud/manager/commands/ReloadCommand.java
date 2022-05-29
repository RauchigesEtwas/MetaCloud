package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.console.data.ConsoleStorageLine;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.awt.dnd.DragGestureEvent;
import java.util.ArrayList;

@CommandInfo(command = "reload", description = "reload all configs instantly", aliases = "rl")
public class ReloadCommand extends CloudCommand {
    @Override
    public boolean performCommand(CloudCommand command, Logger logger, String[] args) {

        logger.log(MSGType.MESSAGETYPE_COMMAND,  "the action is now executed...");
        Driver.getInstance().getModuleDriver().reloadAllModules();
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);

        logger.log(MSGType.MESSAGETYPE_COMMAND,  "reload '§bService.json§7'...");

        service.getMessages().setPrefix(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getPrefix()));
        service.getMessages().setMaintenanceGroupMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceGroupMessage()));
        service.getMessages().setMaintenanceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getMaintenanceKickMessage()));
        service.getMessages().setNoFallbackKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getNoFallbackKickMessage()));
        service.getMessages().setFullNetworkKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullNetworkKickMessage()));
        service.getMessages().setFullServiceKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getFullServiceKickMessage()));
        service.getMessages().setOnlyProxyJoinKickMessage(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getOnlyProxyJoinKickMessage()));
        service.getMessages().setHubCommandNoFallbackFound(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandNoFallbackFound()));
        service.getMessages().setHubCommandAlreadyOnFallBack(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandAlreadyOnFallBack()));
        service.getMessages().setHubCommandSendToAnFallback(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getHubCommandSendToAnFallback()));
        service.getMessages().setServiceStartingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStartingNotification()));
        service.getMessages().setServiceConnectedToProxyNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceConnectedToProxyNotification()));
        service.getMessages().setServiceStoppingNotification(Driver.getInstance().getStorageDriver().utf8ToUBase64(service.getMessages().getServiceStoppingNotification()));

        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("service", service);

        return false;
    }

    @Override
    public ArrayList<String> tabComplete(ConsoleStorageLine consoleInput, String[] args) {
        return null;
    }
}
