package io.metacloud.manager.commands;

import io.metacloud.Driver;
import io.metacloud.command.CloudCommand;
import io.metacloud.command.CommandInfo;
import io.metacloud.console.logger.Logger;
import io.metacloud.console.logger.enums.MSGType;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;

@CommandInfo(command = "cloud", description = "here you can find the full Information about the CloudSystem", aliases = {"me", "metacloud", "info"})
public class MetaCloudCommand extends CloudCommand {

    @Override
    public boolean performCommand(io.metacloud.command.CloudCommand command, Logger logger, String[] args) {

        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "current version: §bMetaCloud-" + Driver.getInstance().getStorageDriver().getVersion());
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "authors: §bRauchigesEtwas  §r| §7Discord: §bhttps://discord.gg/4kKEcaP9WC");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "github: §bhttps://github.com/RauchigesEtwas/MetaCloud");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double usedMemory = (memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576);
        double maxMemory =    (memoryMXBean.getHeapMemoryUsage().getMax() / 1048576) ;
        int processors =    operatingSystemMXBean.getAvailableProcessors() ;
        int LoadAver =    threadMXBean.getThreadCount() ;
        Integer loadedClassCount = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
        long unloadedClassCount = ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount();
        long totalLoadedClassCount = ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
        double prozent = (usedMemory*100)/maxMemory;
        DecimalFormat f = new DecimalFormat("#0.00");
        DecimalFormat f2 = new DecimalFormat("#0");
        if (new File("./service.json").exists()){
            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Node: §bInternalNode");
        }else {
            logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Node: §bNode-1");
        }
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "OS Version: §b" +  System.getProperty("os.name"));
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "User name: §b" +  System.getProperty("user.name"));
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Java version: §b" +  System.getProperty("java.version"));
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Using memory: §b" + f2.format(usedMemory) + "MB§7/§b" + f2.format(maxMemory)   + "MB §7(§b"+f.format(prozent)+ "%§7)");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Cores: §b" + processors + " core");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Loaded Classes: §b" + loadedClassCount + " classes");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Unloaded Classes: §b" + unloadedClassCount + " classes");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "Total loaded Classes: §b" + totalLoadedClassCount + " classes");
        logger.log(MSGType.MESSAGETYPE_COMMAND, true, "");

        return false;
    }
}
