package io.metacloud.bungeecord.utilities.serverhelper;

import io.metacloud.bungeecord.BungeeBridge;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


public class ServerConfig {

    private static File file;
    private static Configuration bungeeConfig;
    private static boolean locked; // TODO: This is dumb. Writes are lost when locked

    static {
        setupConfig();
        if (locked) {
            ProxyServer.getInstance().getScheduler().schedule(BungeeBridge.getInstance(), ServerConfig::setupConfig, 5L, TimeUnit.SECONDS);
        }
    }

    public static void addToConfig(ServerInfo serverInfo) {
        if (locked) {
            return;
        }

        bungeeConfig.set("servers." + serverInfo.getName() + ".motd", serverInfo.getMotd().replace(ChatColor.COLOR_CHAR, '&'));
        bungeeConfig.set("servers." + serverInfo.getName() + ".address", serverInfo.getSocketAddress().toString());
        bungeeConfig.set("servers." + serverInfo.getName() + ".restricted", false);
        saveConfig();
    }





    public static void removeFromConfig(ServerInfo serverInfo) {
        removeFromConfig(serverInfo.getName());
    }

    public static void removeFromConfig(String name) {
        if (locked) {
            return;
        }

        bungeeConfig.set("servers." + name, null);
        saveConfig();
    }



    private static void saveConfig() {
        if (locked) {
            return;
        }

        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(bungeeConfig, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setupConfig() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            file = new File(ProxyServer.getInstance().getPluginsFolder().getParentFile(), "config.yml");

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, StandardCharsets.ISO_8859_1);

            bungeeConfig = YamlConfiguration.getProvider(YamlConfiguration.class).load(isr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ignored) {}
        }

        locked = bungeeConfig == null;
    }
}
