package me.rainstxrm.downbutnotout.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBleedOutEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;

    public PlayerBleedOutEvent(Player player){
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }


}
