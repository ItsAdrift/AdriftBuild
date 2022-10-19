package me.itsadrift.adriftbuild;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class AdriftBuild extends JavaPlugin {

    public HashMap<UUID, Boolean> buildMap = new HashMap<UUID, Boolean>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        BuildCommand cmd = new BuildCommand(this);
        getCommand("build").setExecutor(cmd);
        getCommand("build").setTabCompleter(cmd);
        Bukkit.getPluginManager().registerEvents(new BuildListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
