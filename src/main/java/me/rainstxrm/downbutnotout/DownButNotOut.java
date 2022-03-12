package me.rainstxrm.downbutnotout;

import me.rainstxrm.downbutnotout.Commands.*;
import me.rainstxrm.downbutnotout.Events.DownedEvents;
import me.rainstxrm.downbutnotout.Events.ReviveEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class DownButNotOut extends JavaPlugin {

    public static DownButNotOut plugin;


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new DownedEvents(), this);
        getServer().getPluginManager().registerEvents(new ReviveEvents(), this);

        if (getConfig().getInt("bleed-out-time") <= 0){
            getLogger().log(Level.WARNING, "The bleed out time has been set to 0 in the config! PLayers will not be downed and instead instantly die.");
        }

        saveDefaultConfig();
        Metrics metrics = new Metrics(this, 14605);

        getCommand("setreviveclicks").setExecutor(new SetReviveClicks());
        getCommand("dbnoreloadconfig").setExecutor(new ReloadConfig());
        getCommand("setbleedouttime").setExecutor(new SetBleedOutTime());
        getCommand("reviveplayer").setExecutor(new RevivePlayer());
        getCommand("removedbnostands").setExecutor(new RemoveStands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
