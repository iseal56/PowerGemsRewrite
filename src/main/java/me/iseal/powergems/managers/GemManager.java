package me.iseal.powergems.managers;

import me.iseal.powergems.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GemManager {

    private ItemStack randomGem = null;
    private Random rand = new Random();

    public boolean isGem(ItemStack is){
        if (is == null) return false;
        if (!is.hasItemMeta()) return false;
        if (is.getItemMeta().getPersistentDataContainer().has(Main.getIsGemKey(), PersistentDataType.BOOLEAN)) return true;
        return false;
    }

    public ItemStack getRandomGemItem(){
        if (randomGem == null){
            randomGem = new ItemStack(Material.EMERALD);
            ItemMeta gemMeta = randomGem.getItemMeta();
            gemMeta.setDisplayName(ChatColor.GREEN+"Random Gem");
            PersistentDataContainer pdc = gemMeta.getPersistentDataContainer();
            pdc.set(Main.getIsRandomGemKey(), PersistentDataType.BOOLEAN, true);
            randomGem.setItemMeta(gemMeta);
        }
        return randomGem;
    }

    public ItemStack createGem(int gemInt, int gemLvl){
        return generateItemStack(gemInt, gemLvl);
    }
    public ItemStack createGem(int gemInt){
        return generateItemStack(gemInt, 1);
    }

    public ItemStack createGem(){
        return generateItemStack(rand.nextInt(9) + 1, 1);
    }

    public ItemMeta createLore(ItemMeta meta){
        ArrayList<String> lore = new ArrayList<>();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(Main.getGemLevelKey(), PersistentDataType.INTEGER, 1);
        lore.add(ChatColor.DARK_BLUE + "Level: " + ChatColor.DARK_GREEN + pdc.get(Main.getGemLevelKey(), PersistentDataType.INTEGER));
        meta.setLore(lore);
        return meta;
    }

    private ItemStack generateItemStack(int gemNumber, int gemLevel){
        ItemStack holderItem = new ItemStack(Material.EMERALD);
        ItemMeta reGemMeta = holderItem.getItemMeta();
        reGemMeta = createLore(reGemMeta);
        List<String> currentLore = reGemMeta.getLore();
        ItemStack finalGem = new ItemStack(Material.EMERALD);
        switch (gemNumber){
            case 1:
                reGemMeta.setDisplayName(ChatColor.RED+"Strength Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Saturation, Strenght and Resistance (all lvl 2)");
                currentLore.add(ChatColor.WHITE + "Shift click: An arena that keeps anyone from entering, useful to heal");
                currentLore.add(ChatColor.WHITE + "Left click: A shockwave that sends everyone near flying and damages them");
                break;
            case 2:
                reGemMeta.setDisplayName(ChatColor.DARK_GREEN+"Healing Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Parry");
                currentLore.add(ChatColor.WHITE + "Shift click: Instant heal");
                currentLore.add(ChatColor.WHITE + "Left click: 1 minute of regeneration 2");
                break;
            case 3:
                reGemMeta.setDisplayName(ChatColor.GRAY+"Air Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Creates a tether of wind between the player and a target player, pulling the target closer.");
                currentLore.add(ChatColor.WHITE + "Shift click: Creates a cloud of smoke, granting temporary invisibility and propelling the player forward.");
                currentLore.add(ChatColor.WHITE + "Left click: Unleashes a burst of wind, launching nearby entities into the air and dealing damage.");
                break;
            case 4:
                reGemMeta.setDisplayName(ChatColor.DARK_RED+"Fire Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Creates a fiery aura around the player, granting fire resistance and igniting nearby air blocks.");
                currentLore.add(ChatColor.WHITE + "Shift click: Triggers a powerful explosion at the player's location, damaging nearby entities and applying fire damage.");
                currentLore.add(ChatColor.WHITE + "Left click: Launches a fireball in the direction the player is facing, causing an explosion upon impact.");
                break;
            case 5:
                reGemMeta.setDisplayName(ChatColor.GRAY+"Iron Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Temporarily grants the player increased absorption and knockback resistance.");
                currentLore.add(ChatColor.WHITE + "Shift click: Temporarily increases the player's armor and armor toughness.");
                currentLore.add(ChatColor.WHITE + "Left click: Fires a barrage of spectral arrows in a circle shape.");
                break;
            case 6:
                reGemMeta.setDisplayName(ChatColor.YELLOW+"Lightning Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Strikes lightning at the target location and nearby entities, damaging them.");
                currentLore.add(ChatColor.WHITE + "Shift click: Emits a thunder sound effect and applies a glowing potion effect to nearby entities, excluding the player.");
                currentLore.add(ChatColor.WHITE + "Left click: Launches the player forward in the direction rail.");
                break;
            case 7:
                reGemMeta.setDisplayName(ChatColor.YELLOW+"Sand Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Strikes lightning at the target location and nearby entities, damaging them.");
                currentLore.add(ChatColor.WHITE + "Shift click: Emits a thunder sound effect and applies a glowing potion effect to nearby entities, excluding the player.");
                currentLore.add(ChatColor.WHITE + "Left click: Launches the player forward in the direction rail.");
                break;
            case 8:
                reGemMeta.setDisplayName(ChatColor.DARK_AQUA+"Ice Gem");
                currentLore.add(ChatColor.GREEN+"Abilities");
                currentLore.add(ChatColor.WHITE + "Right click: Throw an ice block, dealing damage to whoever gets hit");
                currentLore.add(ChatColor.WHITE + "Shift click: Spawns snow golems to fight for you");
                currentLore.add(ChatColor.WHITE + "Left click: Freezes the player you aim giving him slowness");
                break;
            case 9:
                reGemMeta.setDisplayName(ChatColor.DARK_RED+"L"+ChatColor.YELLOW+"a"+ChatColor.DARK_RED+"v"+ChatColor.YELLOW+"a"+ChatColor.DARK_RED+" Gem");
                break;
            default:
                Bukkit.getLogger().warning("There was an error creating a gem, please inform the developer.");
                break;
        }
        PersistentDataContainer reDataContainer = reGemMeta.getPersistentDataContainer();
        reDataContainer.set(Main.getIsGemKey(), PersistentDataType.BOOLEAN, true);
        reDataContainer.set(Main.getGemPowerKey(), PersistentDataType.INTEGER, gemNumber);
        reDataContainer.set(Main.getGemLevelKey(), PersistentDataType.INTEGER, gemLevel);
        reGemMeta.setCustomModelData(gemNumber);
        reGemMeta.setLore(currentLore);
        finalGem.setItemMeta(reGemMeta);
        return finalGem;
    }

        public HashMap<Integer, ItemStack> getAllGems(){
        HashMap<Integer, ItemStack> allGems = new HashMap<>(8);
        for (int i = 1; i <= 9; i++) {
            allGems.put(i,generateItemStack(i, 1));
        }
        return allGems;
    }

}
