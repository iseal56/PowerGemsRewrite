package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.GemManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.MissingFormatArgumentException;

public class multipleGemCraftDisabler implements Listener {

    private final ItemStack randomGem = Main.getSingletonManager().gemManager.getRandomGemItem();
    private final GemManager gm = Main.getSingletonManager().gemManager;

    @EventHandler
    public void onClose(InventoryCloseEvent e){
         if (!(e.getView().getBottomInventory() instanceof PlayerInventory)) {
            return;
        }
        Player plr = (Player) e.getInventory().getHolder();
        if (!e.getView().getBottomInventory().containsAtLeast(randomGem, 1)){
            return;
        }
        PlayerInventory pi = (PlayerInventory) e.getView().getBottomInventory();
        int nOfGems = 0;
        int intAt = -1;
        for (ItemStack item : pi.getContents()){
            intAt++;
            if (item == null || !item.isSimilar(randomGem)){
                continue;
            }
            nOfGems+= item.getAmount();
            pi.setItem(intAt, null);
        }
        if (Main.config.getBoolean("allowOnlyOneGem")){
            pi.addItem(gm.createGem());
        } else {
            World plrWorld = plr.getWorld();
            Location plrPos = plr.getLocation();
            for (int i = 0; i < nOfGems; i++) {
                if (pi.firstEmpty() == -1){
                    plrWorld.dropItem(plrPos, gm.createGem());
                    continue;
                }
                pi.addItem(gm.createGem());
            }
        }
    }
}
