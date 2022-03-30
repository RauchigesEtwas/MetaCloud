package io.metacloud;

import io.metacloud.command.CloudCommand;
import io.metacloud.configuration.configs.group.GroupConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageDriver {

    private boolean cloudSetup;
    private Map<String, Object> setupStorage;
    private GroupConfiguration groupSetupStorage;
    private Integer setupStep;
    private String setupType;
    private Boolean shutdownFromManager;
    private Long startTime;
    private String version = "HURRICAN-1.0.0";
    private boolean Shutdown = false;

    public GroupConfiguration getGroupSetupStorage() {
        return groupSetupStorage;
    }

    public void setGroupSetupStorage(GroupConfiguration groupSetupStorage) {
        this.groupSetupStorage = groupSetupStorage;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Boolean getShutdownFromManager() {
        return shutdownFromManager;
    }

    public void setShutdownFromManager(Boolean shutdownFromManager) {
        this.shutdownFromManager = shutdownFromManager;
    }

    public boolean isShutdown() {
        return Shutdown;
    }

    public void setShutdown(boolean shutdown) {
        Shutdown = shutdown;
    }

    public StorageDriver() {
        this.setupStorage = new HashMap<>();
    }

    public String getSetupType() {
        return setupType;
    }

    public void setSetupType(String setupType) {
        this.setupType = setupType;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getSetupStorage() {
        return setupStorage;
    }


    public Integer getSetupStep() {
        return setupStep;
    }

    public void setSetupStep(Integer setupStep) {
        this.setupStep = setupStep;
    }

    public boolean isCloudSetup() {
        return cloudSetup;
    }

    public void setCloudSetup(boolean cloudSetup) {
        this.cloudSetup = cloudSetup;
    }

    public String getCloudLogo(){
        return "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b"+version+"§7]\n" +
                "       ________________________________________________________\n" +
                "           The §bnext generation §7of Minecraft §bcloud systems§7\n" +
                "     \n" +
                "     <§b!§7> Thank you for using §bMetaCloudService§7 for your §bNetwork§r\n" +
                "     <!> Our §bSupport§7 can you find -§b https://discord.gg/4kKEcaP9WC\n";
    }
    public CloudCommand fromFirstArgument(String commandLine) {
        String[] split = commandLine.split(" ");
        return Driver.getInstance().getConsoleDriver().getCommandDriver().getCommand(split[0]);
    }

    public String[] dropFirstString(String[] input){
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public boolean versionCheck(String version, String checkType){
        if (checkType.equalsIgnoreCase("PROXY")){
            if ( version.equalsIgnoreCase("bungeecord_latest") || version.equalsIgnoreCase("waterfall_latest") ) {
                return true;
            }else {
                return false;
            }
        }else   if (checkType.equalsIgnoreCase("SERVER")){
            if(
                    //PAPERSPIGOT
                    version.equalsIgnoreCase("PAPERSPIGOT_1_8_8") || version.equalsIgnoreCase("PAPERSPIGOT_1_9_4") || version.equalsIgnoreCase("PAPERSPIGOT_1_10_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_11_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_12_2")
                            || version.equalsIgnoreCase("PAPERSPIGOT_1_13_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_14_4") || version.equalsIgnoreCase("PAPERSPIGOT_1_15_2") || version.equalsIgnoreCase("PAPERSPIGOT_1_16_5") || version.equalsIgnoreCase("PAPERSPIGOT_1_17_1") || version.equalsIgnoreCase("PAPERSPIGOT_1_18_1")


            ){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public String getSpigotConfiguration(){
        return "settings:\n" +
                "  allow-end: true\n" +
                "  warn-on-overload: true\n" +
                "  permissions-file: permissions.yml\n" +
                "  update-folder: update\n" +
                "  plugin-profiling: false\n" +
                "  connection-throttle: 0\n" +
                "  query-plugins: true\n" +
                "  deprecated-verbose: default\n" +
                "  shutdown-message: Server closed\n" +
                "spawn-limits:\n" +
                "  monsters: 70\n" +
                "  animals: 15\n" +
                "  water-animals: 5\n" +
                "  ambient: 15\n" +
                "chunk-gc:\n" +
                "  period-in-ticks: 600\n" +
                "  load-threshold: 0\n" +
                "ticks-per:\n" +
                "  animal-spawns: 400\n" +
                "  monster-spawns: 1\n" +
                "  autosave: 6000\n" +
                "aliases: now-in-commands.yml\n" +
                "database:\n" +
                "  username: bukkit\n" +
                "  isolation: SERIALIZABLE\n" +
                "  driver: org.sqlite.JDBC\n" +
                "  password: walrus\n" +
                "  url: jdbc:sqlite:{DIR}{NAME}.db\n";
    }

    public String getSpigotProperty(){
        return "#Minecraft server properties\n" +
                "#Mon Jan 25 10:33:48 CET 2021\n" +
                "spawn-protection=0\n" +
                "generator-settings=\n" +
                "force-gamemode=false\n" +
                "allow-nether=true\n" +
                "gamemode=0\n" +
                "broadcast-console-to-ops=true\n" +
                "enable-query=false\n" +
                "player-idle-timeout=0\n" +
                "difficulty=1\n" +
                "spawn-monsters=true\n" +
                "op-permission-level=0\n" +
                "resource-pack-hash=\n" +
                "announce-player-achievements=true\n" +
                "pvp=true\n" +
                "snooper-enabled=true\n" +
                "level-type=DEFAULT\n" +
                "hardcore=false\n" +
                "enable-command-block=false\n" +
                "max-players=\n" +
                "network-compression-threshold=256\n" +
                "max-world-size=29999984\n" +
                "server-port=\n" +
                "debug=false\n" +
                "server-ip=\n" +
                "spawn-npcs=true\n" +
                "allow-flight=false\n" +
                "level-name=world\n" +
                "view-distance=10\n" +
                "resource-pack=\n" +
                "spawn-animals=true\n" +
                "white-list=false\n" +
                "generate-structures=true\n" +
                "online-mode=false\n" +
                "max-build-height=256\n" +
                "level-seed=\n" +
                "enable-rcon=false\n" +
                "motd=\"§8| §bMetaCloud §8- §7Server Service\"\n";
    }

    public String getBungeeCordConfiguration(int port){
        return "server_connect_timeout: 5000\n" +
                "remote_ping_cache: -1\n" +
                "forge_support: true\n" +
                "player_limit: -1\n" +
                "permissions:\n" +
                "  default:\n" +
                "  - bungeecord.command.server\n" +
                "  - bungeecord.command.list\n" +
                "  admin:\n" +
                "  - bungeecord.command.alert\n" +
                "  - bungeecord.command.end\n" +
                "  - bungeecord.command.ip\n" +
                "  - bungeecord.command.reload\n" +
                "timeout: 30000\n" +
                "log_commands: false\n" +
                "network_compression_threshold: 256\n" +
                "online_mode: true\n" +
                "disabled_commands:\n" +
                "- disabledcommandhere\n" +
                "servers:\n" +
                "  lobby:\n" +
                "    motd: '&1Just another Waterfall - Forced Host'\n" +
                "    address: localhost:25566\n" +
                "    restricted: false\n" +
                "listeners:\n" +
                "- query_port: "+port+"\n" +
                "  motd: '&bMetaCloud &7- ProxyServer'\n" +
                "  tab_list: GLOBAL_PING\n" +
                "  query_enabled: false\n" +
                "  proxy_protocol: false\n" +
                "  forced_hosts:\n" +
                "    pvp.md-5.net: pvp\n" +
                "  ping_passthrough: false\n" +
                "  priorities:\n" +
                "  - lobby\n" +
                "  bind_local_address: true\n" +
                "  host: 0.0.0.0:"+port+"\n" +
                "  max_players: 1\n" +
                "  tab_size: 60\n" +
                "  force_default_server: false\n" +
                "ip_forward: false\n" +
                "remote_ping_timeout: 5000\n" +
                "prevent_proxy_connections: false\n" +
                "groups:\n" +
                "  RauchigesEtwas:\n" +
                "  - admin\n" +
                "connection_throttle: 4000\n" +
                "stats: ddace828-7313-4d44-a1e2-6736196a8ab5\n" +
                "connection_throttle_limit: 3\n" +
                "log_pings: true\n";
    }

}
