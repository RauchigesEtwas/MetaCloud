package io.metacloud.servers;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.configuration.configs.service.CloudServiceConfiguration;
import io.metacloud.global.ModuleConfig;
import io.metacloud.servers.listener.MotdListener;
import io.metacloud.servers.listener.TablistListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Timer;
import java.util.TimerTask;

public class SyncProxyService extends Plugin {

    private static SyncProxyService instance;
    public Integer pingCount;
    public CloudServiceConfiguration configuration;
    public ModuleConfig config;
    public Integer tabCount;

    @Override
    public void onEnable() {
        new Driver();
        instance = this;

        configuration = (CloudServiceConfiguration) new ConfigDriver("./cloudservice.json").read(CloudServiceConfiguration.class);
        pingCount = 0;
        tabCount = 0;
        checker();
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MotdListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new TablistListener());
        TablistListener.updates();
    }

    public static SyncProxyService getInstance() {
        return instance;
    }




    private void checker(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               config = (ModuleConfig) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig( "http://" + configuration.getNetworkProperty().getManagerAddress() + ":" +configuration.getNetworkProperty().getRestAPIPort()+
                        "/" + configuration.getNetworkProperty().getAuthRestAPIKey()+ "/module-syncproxy", ModuleConfig.class);

                GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + configuration.getNetworkProperty().getManagerAddress()+ ":" + configuration.getNetworkProperty().getRestAPIPort() + "/"
                        + configuration.getNetworkProperty().getAuthRestAPIKey() + "/group-" +configuration.getGroupname(), GroupConfiguration.class);
                if (tabCount >= config.getTablist().size()-1){
                    tabCount = 0;
                }else {
                    tabCount++;
                }
                if (group.getMaintenance()){
                    if (pingCount >= config.getMaintenancen().size()-1){
                        pingCount = 0;
                    }else {
                        pingCount++;
                    }
                }else {
                    if (pingCount >= config.getDefaults().size()-1){
                        pingCount = 0;
                    }else {
                        pingCount++;
                    }
                }

            }
        },1000,5*1000);
    }
}
