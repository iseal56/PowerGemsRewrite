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

    public ItemMeta createLore(ItemMeta meta, int gemNumber){
        ArrayList<String> lore = new ArrayList<>();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(Main.getGemLevelKey(), PersistentDataType.INTEGER)) {
            pdc.set(Main.getGemLevelKey(), PersistentDataType.INTEGER, 1);
        }
        lore.add(ChatColor.DARK_BLUE + "Level: " + ChatColor.DARK_GREEN + pdc.get(Main.getGemLevelKey(), PersistentDataType.INTEGER));
        switch (gemNumber){
            case 1:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Saturation, Strenght and Resistance (all lvl 2)");
                lore.add(ChatColor.WHITE + "Shift click: An arena that keeps anyone from entering, useful to heal");
                lore.add(ChatColor.WHITE + "Left click: A shockwave that sends everyone near flying and damages them");
                break;
            case 2:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Parry");
                lore.add(ChatColor.WHITE + "Shift click: Instant heal");
                lore.add(ChatColor.WHITE + "Left click: 1 minute of regeneration 2");
                break;
            case 3:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Creates a tether of wind between the player and a target player, pulling the target closer.");
                lore.add(ChatColor.WHITE + "Shift click: Creates a cloud of smoke, granting temporary invisibility and propelling the player forward.");
                lore.add(ChatColor.WHITE + "Left click: Unleashes a burst of wind, launching nearby entities into the air and dealing damage.");
                break;
            case 4:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Creates a fiery aura around the player, granting fire resistance and igniting nearby air blocks.");
                lore.add(ChatColor.WHITE + "Shift click: Triggers a powerful explosion at the player's location, damaging nearby entities and applying fire damage.");
                lore.add(ChatColor.WHITE + "Left click: Launches a fireball in the direction the player is facing, causing an explosion upon impact.");
                break;
            case 5:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Temporarily grants the player increased absorption and knockback resistance.");
                lore.add(ChatColor.WHITE + "Shift click: Temporarily increases the player's armor and armor toughness.");
                lore.add(ChatColor.WHITE + "Left click: Fires a barrage of spectral arrows in a circle shape.");
                break;
            case 6:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Strikes lightning at the target location and nearby entities, damaging them.");
                lore.add(ChatColor.WHITE + "Shift click: Emits a thunder sound effect and applies a glowing potion effect to nearby entities, excluding the player.");
                lore.add(ChatColor.WHITE + "Left click: Launches the player forward in the direction rail.");
                break;
            case 7:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Strikes lightning at the target location and nearby entities, damaging them.");
                lore.add(ChatColor.WHITE + "Shift click: Emits a thunder sound effect and applies a glowing potion effect to nearby entities, excluding the player.");
                lore.add(ChatColor.WHITE + "Left click: Launches the player forward in the direction rail.");
                break;
            case 8:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Throw an ice block, dealing damage to whoever gets hit");
                lore.add(ChatColor.WHITE + "Shift click: Spawns snow golems to fight for you");
                lore.add(ChatColor.WHITE + "Left click: Freezes the player you aim giving him slowness");
                break;
            case 9:
                lore.add(ChatColor.GREEN+"Abilities");
                lore.add(ChatColor.WHITE + "Right click: Make a wall of lava");
                lore.add(ChatColor.WHITE + "Shift click: Spawn a blaze to fight for you");
                lore.add(ChatColor.WHITE + "Left click: 1 minute of Fire resistance");
            default:
                Bukkit.getLogger().warning("There was an error creating a gem, please inform the developer.");
                break;
        }
        meta.setLore(lore);
        return meta;
    }

    public ItemMeta createLore(ItemMeta meta){
        if (meta.getPersistentDataContainer().has(Main.getGemPowerKey(), PersistentDataType.INTEGER)){
            return createLore(meta, meta.getPersistentDataContainer().get(Main.getGemPowerKey(), PersistentDataType.INTEGER));
        } else {
            return createLore(meta, 1);
        }
    }

    private ItemStack generateItemStack(int gemNumber, int gemLevel){
        ItemStack holderItem = new ItemStack(Material.EMERALD);
        ItemMeta reGemMeta = holderItem.getItemMeta();
        reGemMeta = createLore(reGemMeta, gemNumber);
        List<String> currentLore = reGemMeta.getLore();
        ItemStack finalGem = new ItemStack(Material.EMERALD);
        switch (gemNumber){
            case 1:
                reGemMeta.setDisplayName(ChatColor.RED+"Strength Gem");
                break;
            case 2:
                reGemMeta.setDisplayName(ChatColor.DARK_GREEN+"Healing Gem");
                break;
            case 3:
                reGemMeta.setDisplayName(ChatColor.GRAY+"Air Gem");
                break;
            case 4:
                reGemMeta.setDisplayName(ChatColor.DARK_RED+"Fire Gem");
                break;
            case 5:
                reGemMeta.setDisplayName(ChatColor.GRAY+"Iron Gem");
                break;
            case 6:
                reGemMeta.setDisplayName(ChatColor.YELLOW+"Lightning Gem");
                break;
            case 7:
                reGemMeta.setDisplayName(ChatColor.YELLOW+"Sand Gem");
                break;
            case 8:
                reGemMeta.setDisplayName(ChatColor.DARK_AQUA+"Ice Gem");
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
        reGemMeta = createLore(reGemMeta);
        reGemMeta.setCustomModelData(gemNumber);
        reGemMeta.setLore(currentLore);
        finalGem.setItemMeta(reGemMeta);
        return finalGem;
    }

        public HashMap<Integer, ItemStack> getAllGems(){
        HashMap<Integer, ItemStack> allGems = new HashMap<>(9);
        for (int i = 1; i <= 9; i++) {
            allGems.put(i,generateItemStack(i, 1));
        }
        return allGems;
    }

}
