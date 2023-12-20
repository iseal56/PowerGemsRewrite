package me.iseal.powergems;

import de.leonhard.storage.Yaml;
import me.iseal.powergems.commands.checkUpdateCommand;
import me.iseal.powergems.commands.giveAllGemCommand;
import me.iseal.powergems.commands.giveGemCommand;
import me.iseal.powergems.listeners.*;
import me.iseal.powergems.listeners.powerListeners.*;
import me.iseal.powergems.managers.*;
import me.iseal.powergems.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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


    @Override
    public void onEnable() {
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
        sm.recipeManager.initiateRecipes();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new useEvent(), this);
        pluginManager.registerEvents(new enterExitListener(), this);
        if (config.getBoolean("canDropGems") || config.getBoolean("keepGemsOnDeath")) pluginManager.registerEvents(new dropEvent(), this);
        if (!config.getBoolean("explosionDamageAllowed")) pluginManager.registerEvents(new entityExplodeListener(), this);
        if (config.getBoolean("preventGemPowerTampering")) pluginManager.registerEvents(new noGemHittingListener(), this);
        pluginManager.registerEvents(new ironProjectileLandListener(), this);
        pluginManager.registerEvents(new multipleGemCraftDisabler(), this);
        pluginManager.registerEvents(sm.strenghtMoveListen, this);
        pluginManager.registerEvents(sm.sandMoveListen, this);
        pluginManager.registerEvents(sm.recipeManager, this);
        Bukkit.getServer().getPluginCommand("givegem").setExecutor(new giveGemCommand());
        Bukkit.getServer().getPluginCommand("giveallgem").setExecutor(new giveAllGemCommand());
        Bukkit.getServer().getPluginCommand("checkupdates").setExecutor(new checkUpdateCommand());
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

}