package me.iseal.powergems;

import de.leonhard.storage.Yaml;
import me.iseal.powergems.commands.checkUpdateCommand;
import me.iseal.powergems.commands.giveAllGemCommand;
import me.iseal.powergems.commands.giveGemCommand;
import me.iseal.powergems.commands.reloadConfigCommand;
import me.iseal.powergems.listeners.*;
import me.iseal.powergems.listeners.passivePowerListeners.damageListener;
import me.iseal.powergems.listeners.powerListeners.*;
import me.iseal.powergems.managers.*;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.SimpleBarChart;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static JavaPlugin plugin = null;
    public static Yaml cd = null;
    public static Yaml gemActive = null;
    public static Yaml config = null;
    private static SingletonManager sm = null;
    private static NamespacedKey isGemKey = null;
    private static NamespacedKey gemPowerKey = null;
    private static NamespacedKey gemLevelKey = null;
    private static NamespacedKey isGemProjectileKey = null;
    private static NamespacedKey isRandomGemKey = null;
    private static NamespacedKey isGemExplosionKey = null;
    private static final UUID attributeUUID = UUID.fromString("d21d674e-e7ec-4cd0-8258-4667843f26fd");
    private final Logger l = Bukkit.getLogger();


    @Override
    public void onEnable() {
        l.info("Initializing plugin");
        plugin = this;
        sm = new SingletonManager();
        sm.updaterManager.start();
        sm.configManager.setUpConfig();
        cd = new Yaml("cooldowns", this.getDataFolder()+"\\config\\");
        gemActive = new Yaml("gem_active", this.getDataFolder()+"\\config\\");
        isGemKey = new NamespacedKey(this, "is_power_gem");
        gemPowerKey = new NamespacedKey(this, "gem_power");
        gemLevelKey = new NamespacedKey(this, "gem_level");
        isGemProjectileKey = new NamespacedKey(this, "is_gem_projectile");
        isRandomGemKey = new NamespacedKey(this, "is_random_gem");
        isGemExplosionKey = new NamespacedKey(this, "is_gem_explosion");
        l.info("Registering recipes");
        sm.recipeManager.initiateRecipes();
        l.info("Registering listeners");
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new useEvent(), this);
        pluginManager.registerEvents(new enterExitListener(), this);
        if (config.getBoolean("keepGemsOnDeath")) pluginManager.registerEvents(new deathEvent(), this);
        if (!config.getBoolean("canDropGems")) pluginManager.registerEvents(new dropEvent(), this);
        if (!config.getBoolean("explosionDamageAllowed")) pluginManager.registerEvents(new entityExplodeListener(), this);
        if (config.getBoolean("preventGemPowerTampering")) pluginManager.registerEvents(new noGemHittingListener(), this);
        pluginManager.registerEvents(new ironProjectileLandListener(), this);
        pluginManager.registerEvents(new inventoryCloseListener(), this);
        pluginManager.registerEvents(new damageListener(), this);
        pluginManager.registerEvents(sm.strenghtMoveListen, this);
        pluginManager.registerEvents(sm.sandMoveListen, this);
        pluginManager.registerEvents(sm.recipeManager, this);
        l.info("Registering commands");
        Bukkit.getServer().getPluginCommand("givegem").setExecutor(new giveGemCommand());
        Bukkit.getServer().getPluginCommand("giveallgem").setExecutor(new giveAllGemCommand());
        Bukkit.getServer().getPluginCommand("checkupdates").setExecutor(new checkUpdateCommand());
        Bukkit.getServer().getPluginCommand("reloadconfig").setExecutor(new reloadConfigCommand());
        l.info("Registering bstats metrics");
        Metrics metrics = new Metrics(plugin, 20723);
        metrics.addCustomChart(new AdvancedPie("gems_enabled", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                Map<String, Integer> map = new HashMap<>();
                for (String gem : gemActive.singleLayerKeySet()){
                    map.put(gem, gemActive.getBoolean(gem) ? 1 : 0);
                }
                return map;
            }
        }));
        //TODO: addon api
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    //getters beyond this point

    public static SingletonManager getSingletonManager() { return sm;}
    public static JavaPlugin getPlugin(){
        return plugin;
    }
    public static NamespacedKey getIsGemKey() {
        return isGemKey;
    }
    public static NamespacedKey getGemPowerKey(){
        return gemPowerKey;
    }
    public static NamespacedKey getGemLevelKey(){
        return gemLevelKey;
    }
    public static NamespacedKey getIsGemProjectileKey() {
        return isGemProjectileKey;
    }
    public static NamespacedKey getIsRandomGemKey(){
        return isRandomGemKey;
    }
    public static NamespacedKey getIsGemExplosionKey() {
        return isGemExplosionKey;
    }
    public static UUID getAttributeUUID() {
        return attributeUUID;
    }

}