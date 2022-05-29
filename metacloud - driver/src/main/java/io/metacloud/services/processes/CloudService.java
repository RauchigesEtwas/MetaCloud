package io.metacloud.services.processes;


import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupType;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.configuration.configs.service.ServiceNetworkProperty;
import io.metacloud.console.logger.enums.MSGType;
import io.metacloud.events.events.service.ServiceAddEvent;
import io.metacloud.services.processes.utils.ServiceStorage;
import io.metacloud.webservice.DownloadDriver;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.Line;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class CloudService {


    private Process service;
    private ServiceStorage storage;
    private boolean isToggle;
    private Thread outputThread;

    public CloudService() {}




    public CloudService bindStorage(ServiceStorage storage){
        this.storage = storage;
        return this;
    }
    @SneakyThrows
    public void run(){
        if (!new File("." + this.getStorage().getGroupConfiguration().getProperties().getTemplate()).exists()){
            if (this.getStorage().getGroupConfiguration().getStaticServices()){
                new File("." + this.getStorage().getGroupConfiguration().getProperties().getTemplate()+ "/defaults/plugins").mkdirs();
                new DownloadDriver("server.jar", "./local/templates/"+this.getStorage().getGroupConfiguration().getName()+ "/defaults/", this.getStorage().getGroupConfiguration().getProperties().getVersion());
                Thread.sleep(500);
            }else {
                new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+ "/plugins").mkdirs();
                new DownloadDriver("server.jar", "."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+ "/", this.getStorage().getGroupConfiguration().getProperties().getVersion());
                Thread.sleep(500);
            }
        }
        //create Live data
        if (!new File("./live/").exists()){
            new File("./live/").mkdirs();
        }

        Driver.getInstance().getEventDriver().executeEvent(new ServiceAddEvent(storage.getServiceName(), storage.getSelectedPort(), storage.getGroupConfiguration()));

        new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName()+"/plugins/").mkdirs();

        if (!this.getStorage().getGroupConfiguration().getStaticServices()){
                FileUtils.copyDirectory(new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/"),new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName()+"/"));
        }else {
            if (new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/" + storage.getServiceName() + "/").exists()){
                FileUtils.copyDirectory(new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/" + storage.getServiceName() + "/"),new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName()+"/"));
            }else {
                FileUtils.copyDirectory(new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/defaults/"),new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName()+"/"));
            }
        }

        FileUtils.copyDirectory(new File("./local/global/"),new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/"));
        FileUtils.copyFile(new File("./local/server-icon.png"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/server-icon.png"));
        FileUtils.copyFile(new File("./local/storage/jars/metacloud-plugin.jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/metacloud-plugin.jar"));
        FileUtils.copyFile(new File("./local/storage/jars/metacloud-api.jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/metacloud-api.jar"));

        CloudServiceConfiguration configuration = new CloudServiceConfiguration();
        configuration.setServicename(storage.getServiceName());
        configuration.setGroupname(storage.getGroupConfiguration().getName());
        ServiceNetworkProperty property = new ServiceNetworkProperty();
        property.setAuthRestAPIKey(storage.getAuthRestAPIKey());
        property.setAuthNetworkingKey(storage.getAuthNetworkingKey());
        property.setManagerAddress(storage.getManagerAddress());
        property.setNetworkingPort(storage.getNetworkingPort());
        property.setRestAPIPort(storage.getRestAPIPort());
        configuration.setNetworkProperty(property);


        new ConfigDriver("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/cloudservice.json").save(configuration);

        Driver.getInstance().getModuleDriver().getAllProperties().forEach((s, properties) -> {

            if (properties.getProperty("usetype").equalsIgnoreCase("BOTH")){
                try {
                    FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/"+ s +".jar"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (properties.getProperty("usetype").equalsIgnoreCase("SERVER")){
                if (storage.getGroupConfiguration().getMode() != GroupType.PROXY){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (properties.getProperty("usetype").equalsIgnoreCase("PROXY")){
                if (storage.getGroupConfiguration().getMode() == GroupType.PROXY){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (properties.getProperty("usetype").equalsIgnoreCase("LOBBY")){
                if (storage.getGroupConfiguration().getMode() == GroupType.LOBBY){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (new File("./service.json").exists()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+storage.getServiceName()+"§7 is now started (§b"+storage.getGroupConfiguration().getProperties().getNode()+"§7~§b"+storage.getGroupConfiguration().getProperties().getVersion()+"§7~§b"+storage.getSelectedPort()+"§7)");
        }else {
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the task was §bsuccessfully§7 performed");
        }

        if (this.getStorage().getGroupConfiguration().getMode() == GroupType.PROXY) {

                File configFile = new File(System.getProperty("user.dir") + "/live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/", "config.yml");
                final FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write(Driver.getInstance().getStorageDriver().getBungeeCordConfiguration(this.storage.getSelectedPort(), storage.getServiceName(), storage.getGroupConfiguration().getMaxOnlinePlayers()));
                fileWriter.flush();
                fileWriter.close();
            try {
                service = Runtime.getRuntime().exec("java -Xmx" + storage.getGroupConfiguration().getDynamicMemory() + "M -jar server.jar", null, new File(System.getProperty("user.dir") + "/live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() ));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
            File configFile = new File(System.getProperty("user.dir") + "/live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/", "server.properties");
            final FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(Driver.getInstance().getStorageDriver().getSpigotProperty(storage.getServiceName()));
            fileWriter.flush();
            fileWriter.close();

            File configFile2 = new File(System.getProperty("user.dir") + "/live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/", "bukkit.yml");
            final FileWriter fileWriter2 = new FileWriter(configFile2);
            fileWriter2.write(Driver.getInstance().getStorageDriver().getSpigotConfiguration());
            fileWriter2.flush();
            fileWriter2.close();
            try {
                service = Runtime.getRuntime().exec("java -Xmx" + storage.getGroupConfiguration().getDynamicMemory() + "M -Dcom.mojang.eula.agree=true -jar server.jar -org.spigotmc.netty.disabled=true --port " + this.storage.getSelectedPort() + " --max-players " + this.storage.getGroupConfiguration().getMaxOnlinePlayers(), null, new File(System.getProperty("user.dir") + "/live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){


        if (new File("./service.json").exists()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the service §b"+storage.getServiceName()+"§7 is now stopping");
        }else {
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "the task was §bsuccessfully§7 performed");
        }
        this.service.destroy();


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (!storage.getGroupConfiguration().getStaticServices()){
                FileUtils.deleteDirectory(new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/"));
                File file = new File("./live/"+ getStorage().getGroupConfiguration().getName() + "/");
                if (file.list().length == 0) {
                    file.delete();
                }
            }else {
                new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/" + storage.getServiceName() + "/").delete();
                new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/" + storage.getServiceName() + "/").mkdirs();
                FileUtils.copyDirectory(new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/"), new File("."+this.getStorage().getGroupConfiguration().getProperties().getTemplate()+"/" + storage.getServiceName() + "/"));
                Thread.sleep(200);
                FileUtils.deleteDirectory(new File("./live/" + getStorage().getGroupConfiguration().getName() + "/" + getStorage().getServiceName() + "/"));

                File file = new File("./live/"+ getStorage().getGroupConfiguration().getName() + "/");
                if (file.list().length == 0) {
                    file.delete();
                }
            }
            File file = new File("./live/");
            if (file.list().length == 0) {
                file.delete();
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

    }


    @SneakyThrows
    public void toggleScreen(){
        if (isToggle){
            isToggle = false;
            outputThread.stop();
        }else {
            isToggle = true;
            outputThread = new Thread(() -> {
                String line = null;
                BufferedReader input = new BufferedReader(new InputStreamReader(service.getInputStream()));
                while (isToggle) {
                    try {
                        if (!((line = input.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_PRCESS, line);
                }
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        outputThread.start();

        }
    }




    public ServiceStorage getStorage() {
        return storage;
    }
}
