package me.rainstxrm.downbutnotout.Commands;

import me.rainstxrm.downbutnotout.DownButNotOut;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SetBleedOutTime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            DownButNotOut.plugin.getLogger().log(Level.WARNING, "Only players can execute this command!");
            return true;
        }
        String prefix = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DBNO" + ChatColor.DARK_GREEN + "]";

        Player player = (Player) sender;
        int newTime;
        // Set clicks command - Sets the amount of clicks required to revive a player
        // Permission - dnbo.admin.settime

        if (player.hasPermission("dbno.admin.settime")){
            if (args.length >= 1){
                try{
                    newTime = Integer.parseInt(args[0]);
                } catch (Exception e){
                    player.sendMessage(prefix + ChatColor.RED + " " + args[0] + " Is not a valid number!");
                    return true;
                }
                int oldTime = DownButNotOut.plugin.getConfig().getInt("bleed-out-time");
                player.sendMessage(prefix + ChatColor.GREEN + " Bleed out time has been changed from " + ChatColor.BOLD + oldTime + " to " + newTime);
                DownButNotOut.plugin.getConfig().set("bleed-out-time", newTime);
                DownButNotOut.plugin.saveConfig();
            }  else {
                player.sendMessage(prefix + ChatColor.RED + " /setbleedouttime [amount]");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + " You do not have the permission to use that command!");
        }
        return true;
    }
}
