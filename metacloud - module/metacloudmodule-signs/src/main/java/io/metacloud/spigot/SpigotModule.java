package io.metacloud.spigot;

import io.metacloud.Driver;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotModule extends JavaPlugin {



    @Override
    public void onEnable() {
        new Driver();
    }
}
