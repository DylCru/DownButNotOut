package me.rainstxrm.downbutnotout.Commands;

import me.rainstxrm.downbutnotout.DownButNotOut;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.logging.Level;

public class SetReviveClicks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            DownButNotOut.plugin.getLogger().log(Level.WARNING, "Only players can execute this command!");
            return true;
        }
        String prefix = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DBNO" + ChatColor.DARK_GREEN + "]";

        Player player = (Player) sender;
        int newClicks;
        // Set clicks command - Sets the amount of clicks required to revive a player
        // Permission - dnbo.admin.setclicks

        if (player.hasPermission("dbno.admin.setclicks")){
            if (args.length >= 1){
                try{
                    newClicks = Integer.parseInt(args[0]);
                } catch (Exception e){
                    player.sendMessage(prefix + ChatColor.RED + " " + args[0] + " Is not a valid number!");
                    return true;
                }
                int oldClicks = DownButNotOut.plugin.getConfig().getInt("clicks-to-revive");
                player.sendMessage(prefix + ChatColor.GREEN + " Clicks needed to revive has been changed from " + ChatColor.BOLD + oldClicks + " to " + newClicks);
                DownButNotOut.plugin.getConfig().set("clicks-to-revive", newClicks);
                DownButNotOut.plugin.saveConfig();
            } else {
                player.sendMessage(prefix + ChatColor.RED + " /setreviveclicks [amount]");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + " You do not have the permission to use that command!");
        }
        return true;
    }
}
