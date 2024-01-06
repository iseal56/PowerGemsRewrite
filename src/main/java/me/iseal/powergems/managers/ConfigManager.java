package me.iseal.powergems.managers;

import de.leonhard.storage.Config;
import me.iseal.powergems.Main;

public class ConfigManager {
    Config config = null;

    public void setUpConfig(){
        config = new Config("config", Main.getPlugin().getDataFolder().getPath());
        Main.config = config;
        config.setDefault("allowOnlyOneGem", false);
        config.setDefault("canDropGems", false);
        config.setDefault("giveGemOnFirstLogin", true);
        config.setDefault("canUpgradeGems", true);
        config.setDefault("canCraftGems", true);
        config.setDefault("keepGemsOnDeath", true);
        config.setDefault("gemsHaveDescriptions", false);
        config.setDefault("explosionDamageAllowed", true);
        config.setDefault("preventGemPowerTampering", true);
        config.setDefault("doGemDecay", true);
        config.setDefault("doDecayOnLevel1", false);
        config.setDefault("cooldownBoostPerLevelInSeconds", 2L);
        config.setDefault("delayToUseGemsOnJoin", 30);
    }

    public long getGemCooldownBoost(){
        return config.getLong("cooldownBoostPerLevelInSeconds")*1000;
    }

}
