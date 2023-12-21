package me.iseal.powergems.listeners;

import me.iseal.powergems.Main;
import me.iseal.powergems.gems.*;
import me.iseal.powergems.managers.TempDataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class useEvent implements Listener {

    private final SingletonManager sm = Main.getSingletonManager();
    private TempDataManager tdm = sm.tempDataManager;
    private final healingGem heal = new healingGem();
    private final powerGem power = new powerGem();
    private final fireGem fire = new fireGem();
    private final airGem air = new airGem();
    private final ironGem iron = new ironGem();
    private final lightningGem light = new lightningGem();
    private final sandGem sand = new sandGem();
    private final iceGem ice = new iceGem();
    private final lavaGem lava = new lavaGem();


    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        Player player = e.getPlayer();
        if (tdm.cantUseGems.containsKey(player)) {
            if (System.currentTimeMillis() < tdm.cantUseGems.get(player)) {
                player.sendMessage(ChatColor.DARK_RED + "You can't use gems for another " + (tdm.cantUseGems.get(player) - System.currentTimeMillis()) / 1000 + " seconds!");
                return;
            } else {
                tdm.cantUseGems.remove(player);
            }
        }
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack item = null;
        if (offHandItem.getType() == Material.EMERALD && offHandItem.hasItemMeta()){
            item = offHandItem;
        } else if (mainHandItem.getType() == Material.EMERALD && mainHandItem.hasItemMeta()) {
            item = mainHandItem;
        } else {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (dataContainer.has(Main.getIsGemKey(), PersistentDataType.BOOLEAN) && dataContainer.has(Main.getGemPowerKey(), PersistentDataType.INTEGER)) {
            if (!item.getItemMeta().hasCustomModelData()){
                Bukkit.getLogger().info("Found legacy gem! Migrating...");
                meta.setCustomModelData(dataContainer.get(Main.getGemPowerKey(), PersistentDataType.INTEGER));
                item.setItemMeta(meta);
                Bukkit.getLogger().info("Done!");
            }
            int power = dataContainer.get(Main.getGemPowerKey(), PersistentDataType.INTEGER);
            int lvl = dataContainer.get(Main.getGemLevelKey(), PersistentDataType.INTEGER);
            Action action = e.getAction();
            handlePower(player, action, power, lvl);
        }
    }

    private void handlePower(Player p, Action a, int powerInt, int lvl){
        boolean gemActive = true;
        switch (powerInt){
            case 1:
                gemActive = power.handlePower(p,a,lvl);
                break;
            case 2:
                gemActive = heal.handlePower(p,a,lvl);
                break;
            case 3:
                gemActive = air.handlePower(p,a,lvl);
                break;
            case 4:
                gemActive = fire.handlePower(p,a,lvl);
                break;
            case 5:
                gemActive = iron.handlePower(p,a,lvl);
                break;
            case 6:
                gemActive = light.handlePower(p,a,lvl);
                break;
            case 7:
                gemActive = sand.handlePower(p,a,lvl);
                break;
            case 8:
                gemActive = ice.handlePower(p,a,lvl);
                break;
            case 9:
                gemActive = lava.handlePower(p,a,lvl);
        }
        if (!gemActive){
            p.sendMessage(ChatColor.DARK_RED+"That gem is disabled!");
            p.sendMessage(ChatColor.DARK_RED+"Ask the server manager if you think it should be activated.");
        }
    }

}
