package me.iseal.powergems.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerManager {

    Map<Player, LinkedList<ItemStack>> onlinePlayers = new HashMap<>();

    public void addPlayer(Player p, LinkedList<ItemStack> gems){
        onlinePlayers.put(p, gems);
    }

    public void removePlayer(Player p){
        onlinePlayers.remove(p);
    }

    public LinkedList<ItemStack> getPlayerGems(Player plr){
        if (!onlinePlayers.containsKey(plr)) return null;
        return onlinePlayers.get(plr);
    }

}
