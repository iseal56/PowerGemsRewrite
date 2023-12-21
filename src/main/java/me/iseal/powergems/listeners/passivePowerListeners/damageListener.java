package me.iseal.powergems.listeners.passivePowerListeners;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class damageListener implements Listener {

    private final PlayerManager pm = Main.getSingletonManager().playerManager;
    private final ArrayList<Integer> compatibleIDs = new ArrayList<>(Arrays.asList(3,6));

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (!(e instanceof Player)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        for (ItemStack i : pm.getPlayerGems((Player) e.getEntity())){
            if (compatibleIDs.contains(i.getItemMeta().getPersistentDataContainer().get(Main.getGemPowerKey(), PersistentDataType.INTEGER))){
                e.setCancelled(true);
            }
        }
    }
}
