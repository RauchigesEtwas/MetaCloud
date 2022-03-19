package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.DecimalFormat;

@CommandInfo(command = "cloud", description = "here you can find the full Information about the CloudSystem", aliases = {"me", "metacloud"})
public class MetaCloudCommand extends CloudCommand {

    @Override
    public boolean performCommand(io.metacloud.command.CloudCommand command, Logger logger, String[] args) {

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "current version: §bMetaCloud-" + Driver.getInstance().getCloudStorage().getVersion());
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "authors: §bRauchigesEtwas  §r| §7Discord: §bhttps://discord.gg/4kKEcaP9WC");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "github: §bhttps://github.com/RauchigesEtwas/MetaCloud");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        double prozent = (((double) Runtime.getRuntime().maxMemory() - (double)Runtime.getRuntime().freeMemory())*100)/(double)Runtime.getRuntime().maxMemory();
        DecimalFormat f = new DecimalFormat("#0.00");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Using memory: §b" + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024)  + "MB §7(§b"+f.format(prozent)+ "%§7)");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Total memory: §b" + Runtime.getRuntime().maxMemory()/ (1024*1024) +"MB");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Cores: §b" + Runtime.getRuntime().availableProcessors());
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Threads: §b" + Thread.getAllStackTraces().keySet().size());
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Loaded Classes: §b" + ManagementFactory.getClassLoadingMXBean().getLoadedClassCount() + " classes");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");

        return false;
    }
}
