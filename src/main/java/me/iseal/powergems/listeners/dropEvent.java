package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class dropEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!Main.config.getBoolean("canDropGems")) return;
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.hasItemMeta()){
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            if (dataContainer.has(Main.getIsGemKey(), PersistentDataType.BOOLEAN)){
                e.setCancelled(true);
            }
        }
    }

}
