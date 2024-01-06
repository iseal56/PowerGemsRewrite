package me.iseal.powergems.listeners;

import de.leonhard.storage.Json;
import me.iseal.powergems.Main;
import me.iseal.powergems.gems.ironGem;
import me.iseal.powergems.managers.GemManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.managers.TempDataManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.UUID;

public class enterExitListener implements Listener {

    Json playerJoined = new Json("playerData", Main.getPlugin().getDataFolder().getPath());
    private final SingletonManager sm = Main.getSingletonManager();
    private final ironGem ironGem = new ironGem();
    private final GemManager gm = sm.gemManager;
    private TempDataManager tdm = sm.tempDataManager;
    private final long delay = Main.config.getInt("delayToUseGemsOnJoin")*1000;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player plr = e.getPlayer();
        checkIfRemovePowers(plr);
        addDelay(plr);
        giveGemOnFirstLogin(plr);
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

    private void addDelay(Player plr){
        if (tdm.cantUseGems.containsKey(plr)){
            tdm.cantUseGems.remove(plr);
        }
        //add delay
        tdm.cantUseGems.put(plr, (System.currentTimeMillis()+delay));
        LinkedList<ItemStack> gems = new LinkedList<>();
        for (ItemStack i : plr.getInventory().getContents()) {
            if (gm.isGem(i)){
                gems.add(i);
            }
        }
    }

    private void checkIfRemovePowers(Player plr){
        if (tdm.ironShiftLeft.contains(plr)){
            ironGem.removeShiftModifiers(plr);
            tdm.ironShiftLeft.remove(plr);
        }
        if (tdm.ironRightLeft.contains(plr)){
            ironGem.removeRightModifiers(plr);
            tdm.ironRightLeft.remove(plr);
        }
    }

}
