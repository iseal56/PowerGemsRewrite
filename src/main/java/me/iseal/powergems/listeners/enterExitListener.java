package me.iseal.powergems.listeners;

import de.leonhard.storage.Json;
import me.iseal.powergems.Main;
import me.iseal.powergems.managers.GemManager;
import me.iseal.powergems.managers.PlayerManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.managers.TempDataManager;

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
    private final PlayerManager pm = sm.playerManager;
    private final GemManager gm = sm.gemManager;
    private TempDataManager tdm = sm.tempDataManager;
    private final long delay = 30*1000;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player plr = e.getPlayer();
        if (tdm.cantUseGems.contains(plr)){
            tdm.cantUseGems.remove(plr);
        }
        //add 10 second delay
        tdm.cantUseGems.put(plr, (System.currentTimeMillis()+delay));
        LinkedList<ItemStack> gems = new LinkedList<>();
        for (ItemStack i : plr.getInventory().getView().getBottom()) {
            if (gm.isGem(i)){
                gems.add(i);
            }
        }
        pm.addPlayer(plr, gems);
        giveGemOnFirstLogin(plr);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        pm.removePlayer(e.getPlayer());
    }

    private void giveGemOnFirstLogin(Player plr){
        if (!Main.config.getBoolean("giveGemOnFirstLogin")){
            return;
        }
        UUID playerUUID = plr.getUniqueId();
        if (!playerJoined.contains(String.valueOf(playerUUID))){
            playerJoined.set(String.valueOf(playerUUID), System.currentTimeMillis());
            plr.getInventory().addItem(gm.createGem());
        }
    }


}
