package me.rainstxrm.downbutnotout.Commands;

import me.rainstxrm.downbutnotout.DownButNotOut;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ReloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            DownButNotOut.plugin.getLogger().log(Level.WARNING, "Only players can execute this command!");
            return true;
        }
        String prefix = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DBNO" + ChatColor.DARK_GREEN + "]";

        Player player = (Player) sender;

        // Reload Config command - /dnboreloadconfig - Reloads the config
        // Permission - dnbo.admin.reload

        if (player.hasPermission("dbno.admin.reload")){
            player.sendMessage(prefix + ChatColor.GREEN + " The DBNO config was reloaded!");
            DownButNotOut.plugin.reloadConfig();
        } else {
            player.sendMessage(prefix + ChatColor.RED + " You do not have permission to use this command!");
        }

        return true;
    }
}
