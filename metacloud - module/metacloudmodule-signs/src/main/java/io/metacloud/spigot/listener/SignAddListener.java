package io.metacloud.spigot.listener;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import io.metacloud.network.packets.cloudsign.SignCreatePacket;
import io.metacloud.network.packets.cloudsign.SignDeletePacket;
import io.metacloud.spigot.SpigotModule;
import io.metacloud.spigot.utils.CloudSign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class SignAddListener implements Listener {

    private ArrayList<Player> used = new ArrayList<Player>();

    @EventHandler
    public void handelAdd(SignChangeEvent event){
        if (event.getLine(0).equalsIgnoreCase("[cloudsign]")){
            String group = event.getLine(1);
            SpigotModule.getInstance().signDriver.registerSign(new CloudSign(UUID.randomUUID().toString(), event.getBlock().getLocation(), group, null, null, 0));
            String signuuid = UUID.randomUUID().toString();
            SignCreatePacket packet = new SignCreatePacket();
            packet.setLocationWorld(event.getBlock().getLocation().getWorld().getName());
            packet.setSignUUID(signuuid);
            packet.setGroupName(group);
            event.getPlayer().sendMessage("Sign was created");
            packet.setLocationPosX(event.getBlock().getLocation().getX());
            packet.setLocationPosY(event.getBlock().getLocation().getY());
            packet.setLocationPosZ(event.getBlock().getLocation().getZ());
            NetworkingBootStrap.client.sendPacket(packet);

        }
    }

    @EventHandler
    public void handel(BlockBreakEvent event){
        if (event.getPlayer().hasPermission("")){
            SpigotModule.getInstance().signDriver.getCloudSigns().forEach(cloudSign -> {
                if (!cloudSign.getLocation().equals(event.getBlock().getLocation())) return;
                event.getPlayer().sendMessage("Sign was removed");
                SpigotModule.getInstance().signDriver.unRegisterSign(cloudSign.getSignUUID());
                SignDeletePacket deletePacket = new SignDeletePacket();
                deletePacket.setSignuuid(cloudSign.getSignUUID());
                NetworkingBootStrap.client.sendPacket(deletePacket);
            });
        }
    }


    @EventHandler
    public void handleInterec(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            if((b.getType() != Material.SIGN) && (b.getType() != Material.SIGN_POST) && (b.getType() != Material.WALL_SIGN)) return;


            Player p = event.getPlayer();
            SpigotModule.getInstance().signDriver.getCloudSigns().forEach(cloudSign -> {
                if (!cloudSign.getLocation().equals(b.getLocation())) return;
                if (cloudSign.getService() == null) return;
                GroupConfiguration group = (GroupConfiguration) Driver.getInstance().getRestDriver().getRestAPI().convertToConfig("http://" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getManagerAddress()+ ":" + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getRestAPIPort() + "/"
                        + SpigotModule.getInstance().getConfiguration().getNetworkProperty().getAuthRestAPIKey() + "/group-" +cloudSign.getGroup(), GroupConfiguration.class);
                if (group.getMaintenance())return;
                SpigotModule.getInstance().signDriver.connect(p, cloudSign.getService());
            });
        }
    }
}
