package me.rainstxrm.downbutnotout;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KOHandler {

    private static List<UUID> downedPlayers = new ArrayList<>();


    public static void KOPlayer(UUID player){
        downedPlayers.add(player);
    }
    public static void removePlayer(UUID player){
        downedPlayers.remove(player);
    }
    public static List<UUID> getDownedPlayers() {return downedPlayers;}

    public static void revivePlayer(UUID pID){
        Player player = Bukkit.getPlayer(pID);
        for (Entity en : player.getNearbyEntities(2,2,2)){
            if (en.hasMetadata("DownedStand") || en.hasMetadata("ReviveStand")){
                en.remove();
            }
        }
        if (player.getLocation().clone().add(0,1,0).getBlock().getType().equals(Material.BARRIER)){
            player.getLocation().clone().add(0,1,0).getBlock().setType(Material.AIR);
        }

        removePlayer(pID);
    }

    public static void spawnStand(Player player){
        ArmorStand downed = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().clone().add(0,-0.5,0), EntityType.ARMOR_STAND);
        downed.setVisible(false);
        downed.setCustomNameVisible(true);
        downed.setGravity(false);
        downed.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + player.getDisplayName() + " IS DOWN!");
        downed.setMetadata("DownedStand", new FixedMetadataValue(DownButNotOut.plugin, "downedstand"));

        ArmorStand revive = (ArmorStand) player.getWorld().spawnEntity(downed.getLocation().clone().add(0,-0.25,0), EntityType.ARMOR_STAND);
        revive.setVisible(false);
        revive.setGravity(false);
        revive.setCustomNameVisible(true);
        revive.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Right click to revive!");
        revive.setMetadata("ReviveStand", new FixedMetadataValue(DownButNotOut.plugin, "Revivestand"));
    }
    public static void playerCountDown(Player player){
        new BukkitRunnable(){
            int timer = DownButNotOut.plugin.getConfig().getInt("bleed-out-time");
            @Override
            public void run() {
                if(!getDownedPlayers().contains(player.getUniqueId())){
                    cancel();
                }

                player.sendTitle(ChatColor.RED + "" + timer, null, 0, 25, 0);
                if (DownButNotOut.plugin.getConfig().getBoolean("play-sounds")){
                    float percent = (float) timer / DownButNotOut.plugin.getConfig().getInt("bleed-out-time") * 2;
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, percent);
                }

                if (timer == 0){
                    if (getDownedPlayers().contains(player.getUniqueId())){
                        player.setHealth(0);
                    }
                    cancel();
                }
                timer--;
            }
        }.runTaskTimer(DownButNotOut.plugin, 0, 20);
    }
}
