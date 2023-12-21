package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class dropEvent implements Listener {

    private Map<UUID, List<ItemStack>> keepItems = new HashMap<>();

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

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if (!Main.config.getBoolean("keepGemsOnDeath")) return;
        final List<ItemStack> toKeep = new ArrayList<>();

        for (ItemStack item : e.getDrops()){
            if (item.hasItemMeta()){
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                if (dataContainer.has(Main.getIsGemKey(), PersistentDataType.BOOLEAN)){
                    toKeep.add(item);
                }
            }
        }
        if (!toKeep.isEmpty())
        {
            e.getDrops().removeAll(toKeep);
            keepItems.put(e.getEntity().getUniqueId(), toKeep);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        final List<ItemStack> toRestore = keepItems.get(e.getPlayer().getUniqueId());
        if (toRestore != null) {
            e.getPlayer().getInventory().addItem(toRestore.toArray(new ItemStack[0]));
            keepItems.remove(e.getPlayer().getUniqueId());
        }
    }

}
