package io.metacloud.spigot;

import io.metacloud.Driver;
import io.metacloud.configs.Configuration;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.services.processes.bin.CloudServiceState;
import io.metacloud.spigot.utils.CloudSign;
import io.metacloud.webservice.restconfigs.services.LiveService;
import io.metacloud.webservice.restconfigs.services.ServiceRest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class SignDriver {

    private ArrayList<CloudSign> cloudSigns;
    private Integer emptyLayout = 0;
    private Integer onlineLayout = 0;
    private Integer fullLayout = 0;
    private Integer maintenanceLayout = 0;
    private Integer loadingLayout = 0;


    public SignDriver() {
        cloudSigns = new ArrayList<>();


        Bukkit.getScheduler().scheduleAsyncRepeatingTask(SpigotModule.getInstance(), () -> {
            Configuration config = (Configuration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" +SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                    "/" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/module-signs-layout", Configuration.class);

            if (emptyLayout >= config.getEmpty().size()-1){
                emptyLayout = 0;
            }else {
                emptyLayout++;
            }
            if (onlineLayout >= config.getOnline().size()-1){
                onlineLayout = 0;
            }else {
                onlineLayout++;
            }
            if (fullLayout >= config.getFull().size()-1){
                fullLayout = 0;
            }else {
                fullLayout++;
            }
            if (maintenanceLayout >= config.getMaintenance().size()-1){
                maintenanceLayout = 0;
            }else {
                maintenanceLayout++;
            }
            if (loadingLayout >= config.getLoading().size()-1){
                loadingLayout = 0;
            }else {
                loadingLayout++;
            }


            cloudSigns.forEach(cloudSign -> {


                GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress()+ ":" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort() + "/"
                        + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/group-" +cloudSign.getGroup(), GroupConfiguration.class);

               if (group.getMaintenance()){

                   String[] lines = {config.getMaintenance().get(maintenanceLayout).getLine0(), config.getMaintenance().get(maintenanceLayout).getLine1(), config.getMaintenance().get(maintenanceLayout).getLine2(), config.getMaintenance().get(maintenanceLayout).getLine3()};

                   set(cloudSign.getSignUUID(), lines);
                   cloudSign.setService(null);
                   cloudSign.setPort(0);
                   cloudSign.setHost(null);
              }else if(cloudSign.getService() == null){
                   String[] lines = {config.getLoading().get(loadingLayout).getLine0(), config.getLoading().get(loadingLayout).getLine1(), config.getLoading().get(loadingLayout).getLine2(), config.getLoading().get(loadingLayout).getLine3()};
                   set(cloudSign.getSignUUID(), lines);
                   getNextService(cloudSign.getSignUUID(), cloudSign.getGroup());
               }else{
                   ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                           "/" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ cloudSign.getGroup(), ServiceRest.class);
                   for (int i = 0; i != serviceRest.getServices().size(); i++) {
                       LiveService liveService = serviceRest.getServices().get(i);
                       if (liveService.getServiceName().equalsIgnoreCase(cloudSign.getService())){
                           if (liveService.getServiceState() == CloudServiceState.LOBBY){
                               if (liveService.getCurrentCloudPlayers() >= group.getMaxOnlinePlayers()){
                                   String[] lines = {config.getFull().get(fullLayout).getLine0(), config.getFull().get(fullLayout).getLine1(), config.getFull().get(fullLayout).getLine2(), config.getFull().get(fullLayout).getLine3()};
                                   set(cloudSign.getSignUUID(), lines);
                               }  else   if (liveService.getCurrentCloudPlayers() == 0){
                                   String[] lines = {config.getEmpty().get(emptyLayout).getLine0(), config.getEmpty().get(emptyLayout).getLine1(), config.getEmpty().get(emptyLayout).getLine2(), config.getEmpty().get(emptyLayout).getLine3()};
                                    set(cloudSign.getSignUUID(), lines);
                               }else{
                                   String[] lines = {config.getOnline().get(onlineLayout).getLine0(), config.getOnline().get(onlineLayout).getLine1(), config.getOnline().get(onlineLayout).getLine2(), config.getOnline().get(onlineLayout).getLine3()};
                                   set(cloudSign.getSignUUID(), lines);
                               }


                           }else {
                               String[] lines = {config.getLoading().get(loadingLayout).getLine0(), config.getLoading().get(loadingLayout).getLine1(), config.getLoading().get(loadingLayout).getLine2(), config.getLoading().get(loadingLayout).getLine3()};
                             set(cloudSign.getSignUUID(), lines);
                               cloudSign.setService(null);
                               cloudSign.setPort(0);
                               cloudSign.setHost(null);
                               getNextService(cloudSign.getSignUUID(), cloudSign.getGroup());
                           }
                       }
                   }
               }
            });


        }, 20, 20);

    }


    public ArrayList<CloudSign> getCloudSigns() {
        return cloudSigns;
    }


    public CloudSign getCloudSignByLocatin(Location location){
        for (int i = 0; i != cloudSigns.size(); i++) {
            if (cloudSigns.get(i).getService() != null){
                if (cloudSigns.get(i).getLocation().equals(location)){
                    return cloudSigns.get(i);
                }
            }
        }
        return null;
    }

    private LiveService getService(String service, String  group){
        ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ group, ServiceRest.class);
        for (int i = 0; i != serviceRest.getServices().size() ; i++) {
            if (serviceRest.getServices().get(i).getServiceName().equalsIgnoreCase(service)){
                return serviceRest.getServices().get(i);
            }
        }
        return null;
    }

    private void getNextService(String signUUID, String  group){
        ServiceRest serviceRest = (ServiceRest) Driver.getInstance().getRestDriver().getRestAPI().convertToRestConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress() + ":" +SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort()+
                "/" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey()+ "/livegroup-"+ group, ServiceRest.class);

        for (int i = 0; i != serviceRest.getServices().size(); i++) {
            LiveService service =  serviceRest.getServices().get(i);
                if (service.getServiceState() == CloudServiceState.LOBBY && !isServiceUsed(service.getServiceName())){
                    updateService(signUUID, service.getServiceName(), service.getHostAddress(), service.getSelectedPort());
                    i = serviceRest.getServices().size();
                }
        }

    }


    public boolean isServiceUsed(String service){
        for (int i = 0; i != cloudSigns.size(); i++) {
            if (cloudSigns.get(i).getService() != null){
                if (cloudSigns.get(i).getService().equalsIgnoreCase(service)){
                    return true;
                }
            }
        }
        return false;
    }

    public void registerSign(CloudSign cloudSign){
        this.cloudSigns.add(cloudSign);
    }


    public void unRegisterSign(String signUUID){
        for (int i = 0; i !=cloudSigns.size() ; i++) {
            if (cloudSigns.get(i).getSignUUID().equalsIgnoreCase(signUUID)){
                cloudSigns.remove(i);
            }
        }
    }



    public void updateService(String signUUID, String serviceName, String serviceHost, Integer servicePort){
        for (int i = 0; i !=cloudSigns.size() ; i++) {
            if (cloudSigns.get(i).getSignUUID().equalsIgnoreCase(signUUID)){

                cloudSigns.get(i).setService(serviceName);
                cloudSigns.get(i).setHost(serviceHost);
                cloudSigns.get(i).setPort(servicePort);
            }
        }
    }


    public void set(String signUUID, String[] lines){
        for (int i = 0; i !=cloudSigns.size() ; i++) {
            if (cloudSigns.get(i).getSignUUID().equalsIgnoreCase(signUUID)){
                CloudSign cloudSign = cloudSigns.get(i);
                if (cloudSign.getService() != null){
                    LiveService service = getService(cloudSign.getService(), cloudSign.getGroup());
                    HashMap<String, String> update = new HashMap<>();
                    Integer linestap = 0;

                    for (String line : lines) {
                        String lineupd = Driver.getInstance().getStorageDriver().base64ToUTF8(line)
                                .replace("&", "§")
                                .replace("%service_name%", cloudSign.getService())
                                .replace("%max_players%", service.getGroupConfiguration().getMaxOnlinePlayers() + "")
                                .replace("%online_players%", service.getCurrentCloudPlayers() + "")
                                .replace("%motd%", getMOTD(cloudSign.getHost(), cloudSign.getPort()));
                        update.put("line" + linestap, lineupd);
                        linestap++;

                    }
                    Sign sign = (Sign) cloudSign.getLocation().getBlock().getState();
                    sign.setLine(0, update.get("line0"));
                    sign.setLine(1, update.get("line1"));
                    sign.setLine(2, update.get("line2"));
                    sign.setLine(3, update.get("line3"));
                    sign.update();
                }else {
                    HashMap<String, String> update = new HashMap<>();
                    Integer linestap = 0;

                    for (String line : lines) {

                        String lineupd = Driver.getInstance().getStorageDriver().base64ToUTF8(line)
                                .replace("&", "§");
                        update.put("line" + linestap, lineupd);
                        linestap++;

                    }
                    Sign sign = (Sign) cloudSign.getLocation().getBlock().getState();

                    sign.setLine(0, update.get("line0"));
                    sign.setLine(1, update.get("line1"));
                    sign.setLine(2, update.get("line2"));
                    sign.setLine(3, update.get("line3"));
                    sign.update();
                }
            }
        }
    }



    public static String getMOTD(String host,int port) {
        try {
            Socket sock = new Socket();
            sock.setSoTimeout(100);
            sock.connect(new InetSocketAddress(host, port), 100);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuffer str = new StringBuffer();
            while ((b = in .read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }
            }

            String[] data = str.toString().split(String.valueOf(ChatColor.COLOR_CHAR));
            String serverMotd = data[0];

            sock.close();

            return String.format(serverMotd);


        } catch (ConnectException e) {
            return"ConnectException";
        } catch (UnknownHostException e) {
            return"UnknownHostException";
        } catch (IOException e) {
            return "IOException";
        }

    }


    public void connect(Player p, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            p.sendPluginMessage(SpigotModule.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException ignored) {}
    }

}
