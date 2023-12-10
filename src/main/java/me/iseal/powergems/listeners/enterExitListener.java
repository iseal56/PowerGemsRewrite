package me.iseal.powergems.listeners;

import de.leonhard.storage.Json;
import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.GemManager;
import me.iseal.powergems.managers.PlayerManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.UUID;

public class enterExitListener implements Listener {

    Json playerJoined = new Json("playerData", Main.getPlugin().getDataFolder().getPath());
    private final SingletonManager sm = Main.getSingletonManager();
    PlayerManager pm = sm.playerManager;
    GemManager gm = sm.gemManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        LinkedList<ItemStack> gems = new LinkedList<>();
        for (ItemStack i : e.getPlayer().getInventory().getContents()) {
            if (gm.isGem(i)){
                gems.add(i);
            }
        }
        pm.addPlayer(e.getPlayer(), gems);
        if (!Main.config.getBoolean("giveGemOnFirstLogin")){
            return;
        }
        Player plr = e.getPlayer();
        UUID playerUUID = plr.getUniqueId();
        if (!playerJoined.contains(String.valueOf(playerUUID))){
            playerJoined.set(String.valueOf(playerUUID), System.currentTimeMillis());
            plr.getInventory().addItem(gm.createGem());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        pm.removePlayer(e.getPlayer());
    }

}
