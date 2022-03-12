package me.rainstxrm.downbutnotout.Commands;

import me.rainstxrm.downbutnotout.DownButNotOut;
import me.rainstxrm.downbutnotout.KOHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class RevivePlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            DownButNotOut.plugin.getLogger().log(Level.WARNING, "Only players can execute this command!");
            return true;
        }
        String prefix = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DBNO" + ChatColor.DARK_GREEN + "]";

        Player player = (Player) sender;

        // Revive player - Revives any downed player
        // Permission - dnbo.admin.reviveplayer

        if (player.hasPermission("dnbo.admim.reviveplayer")){
            if (args.length >= 1){
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null){
                    player.sendMessage(prefix + ChatColor.RED + "This player does not exist!");
                    return true;
                }
                if (KOHandler.getDownedPlayers().contains(p.getUniqueId())){
                    KOHandler.revivePlayer(p.getUniqueId());
                    p.sendMessage(ChatColor.GREEN + "You were revived!");
                    player.sendMessage(prefix + ChatColor.GREEN + " Revived " + p.getDisplayName() + "!");
                } else {
                    player.sendMessage(prefix + ChatColor.RED + " This player has not been downed!");
                }
            } else {
                player.sendMessage(prefix + ChatColor.RED + " /reviveplayer [player]");
            }
        } else {
            player.sendMessage(prefix + ChatColor.RED + " You do not have permission to use this command!");
        }

        return true;
    }
}
