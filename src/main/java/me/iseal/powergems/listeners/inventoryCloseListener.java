package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.GemManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Random;

public class inventoryCloseListener implements Listener {

    private final ItemStack randomGem = Main.getSingletonManager().gemManager.getRandomGemItem();
    private final GemManager gm = Main.getSingletonManager().gemManager;
    private final Random rand = new Random();

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (!(e.getInventory().getHolder() instanceof Player)) {
            return;
        }
        if (!(e.getView().getBottomInventory() instanceof PlayerInventory)) {
            return;
        }
        Player plr = (Player) e.getInventory().getHolder();
        checkIfMultipleGems(plr);
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

    private void checkIfMultipleGems(Player plr){
        if (!Main.config.getBoolean("allowOnlyOneGem")){
            return;
        }
        ArrayList<ItemStack> gems = new ArrayList<>(3);
        final Inventory plrInv = plr.getInventory();
        for (ItemStack i : plrInv.getContents()){
            if (gm.isGem(i)){
                i.setAmount(1);
                gems.add(i);
                plrInv.remove(i);
            }
        }
        if (gems.isEmpty()){
            return;
        }
        if (gems.size() == 1){
            plrInv.addItem(gems.get(0));
            return;
        }
        ItemStack randomGem = gems.get(rand.nextInt(gems.size()));
        plrInv.addItem(randomGem);
    }

}
