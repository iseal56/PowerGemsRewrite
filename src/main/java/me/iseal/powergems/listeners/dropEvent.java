package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.GemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class dropEvent implements Listener {

    private final GemManager gm = Main.getSingletonManager().gemManager;

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (gm.isGem(item)){
            e.setCancelled(true);
        }
    }

}
