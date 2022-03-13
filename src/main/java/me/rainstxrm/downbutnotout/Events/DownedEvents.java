package me.rainstxrm.downbutnotout.Events;

import me.rainstxrm.downbutnotout.DownButNotOut;
import me.rainstxrm.downbutnotout.KOHandler;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DownedEvents implements Listener {

    @EventHandler
    public void KOPlayer(EntityDamageByEntityEvent e){
        if (DownButNotOut.plugin.getConfig().getBoolean("enable-downs")){
            if (!(e.getEntity() instanceof Player)){
                return;
            }
            Player player = (Player) e.getEntity();

            if (KOHandler.getDownedPlayers().contains(player.getUniqueId())){
                return;
            }

            double healthOnAttack = player.getHealth();
            double attackDamage = e.getDamage();

            if (healthOnAttack - attackDamage <= 0){
                e.setDamage(0);
                player.setHealth(20);
                KOHandler.KOPlayer(player.getUniqueId());
                KOHandler.spawnStand(player);
                player.sendTitle(ChatColor.RED + "You have been downed.", ChatColor.RED + "Hopefully someone is able to revive you!" , 0, 40, 0);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.5f, 0.5f);

                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(player.getDisplayName() + " was downed by " + e.getDamager().getName());
                }

                if (player.getLocation().clone().add(0,1,0).getBlock().getType().equals(Material.AIR)){
                    player.getLocation().clone().add(0,1,0).getBlock().setType(Material.BARRIER);
                }
                player.setSwimming(true);
                KOHandler.playerCountDown(player);
            }
        }
    }

    @EventHandler
    public void onDamageWithNoCause(EntityDamageEvent e){
        if (DownButNotOut.plugin.getConfig().getBoolean("enable-downs")){
            if (!(e.getEntity() instanceof Player)){
                return;
            }
            Player player = (Player) e.getEntity();

            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                return;
            }

            if (KOHandler.getDownedPlayers().contains(player.getUniqueId())){
                return;
            }

            String cause = null;

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                cause = "a big fall";
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) ||
                    e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                cause = "a lack of oxygen";
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
            || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)
            || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                cause = "being too hot";
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
                cause = "not looking where they were going";
            }


            double healthOnAttack = player.getHealth();
            double attackDamage = e.getDamage();

            if (healthOnAttack - attackDamage <= 0){
                e.setDamage(0);
                player.setHealth(20);
                KOHandler.KOPlayer(player.getUniqueId());
                KOHandler.spawnStand(player);
                player.sendTitle(ChatColor.RED + "You have been downed.", ChatColor.RED + "Hopefully someone is able to revive you!" , 0, 40, 0);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.5f, 0.5f);

                for (Player p : Bukkit.getOnlinePlayers()){
                    if (cause == null){
                        p.sendMessage(player.getDisplayName() + " was downed.");
                    } else {
                        p.sendMessage(player.getDisplayName() + " was downed by " + cause);
                    }
                }

                if (player.getLocation().clone().add(0,1,0).getBlock().getType().equals(Material.AIR)){
                    player.getLocation().clone().add(0,1,0).getBlock().setType(Material.BARRIER);
                }
                player.setSwimming(true);
                KOHandler.playerCountDown(player);
            }
        }
    }

    @EventHandler
    public void unKOPlayer(PlayerDeathEvent e){
        Player player = e.getEntity();
        if (KOHandler.getDownedPlayers().contains(e.getEntity().getUniqueId())){
            //Remove downed related entities
            e.setDeathMessage(ChatColor.WHITE + player.getDisplayName() + " Bled out!");
            for (Entity en : player.getNearbyEntities(2,2,2)){
                if (en.hasMetadata("DownedStand") || en.hasMetadata("ReviveStand")){
                    en.remove();
                }
            }
            if (player.getLocation().clone().add(0,1,0).getBlock().getType().equals(Material.BARRIER)){
                player.getLocation().clone().add(0,1,0).getBlock().setType(Material.AIR);
            }

            KOHandler.removePlayer(player.getUniqueId());
        }
    }

    @EventHandler
    public void noMoveWhenDowned(PlayerMoveEvent e){
        if (KOHandler.getDownedPlayers().contains(e.getPlayer().getUniqueId())){
            Location from = new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ());
            Location to = new Location(e.getFrom().getWorld(), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
            if (!from.equals(to)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noDamageWhenDowned(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) e.getEntity();
        if (!DownButNotOut.plugin.getConfig().getBoolean("damage-while-down")){
            if (KOHandler.getDownedPlayers().contains(player.getUniqueId())){
                e.setCancelled(true);
                e.getDamager().setVelocity(player.getLocation().toVector().normalize().multiply(-4));
            }
        }
    }

    @EventHandler
    public void noSuffocateWhenDowned(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                if (KOHandler.getDownedPlayers().contains(player.getUniqueId())){
                    e.setCancelled(true);
                }
            }
        }
    }
}
