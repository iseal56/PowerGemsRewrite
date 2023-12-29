package me.iseal.powergems.managers;

import me.iseal.powergems.Main;

public enum CooldownManager {
    STRENGTH_SHIFT(Main.cd.getOrSetDefault("Strength_Shift", 45)),
    STRENGTH_RIGHT(Main.cd.getOrSetDefault("Strength_Right", 20)),
    STRENGTH_LEFT(Main.cd.getOrSetDefault("Strength_Left", 30)),
    HEALING_SHIFT(Main.cd.getOrSetDefault("Healing_Shift", 10)),
    HEALING_RIGHT(Main.cd.getOrSetDefault("Healing_Right", 5)),
    HEALING_LEFT(Main.cd.getOrSetDefault("Healing_Left", 120)),
    FIRE_SHIFT(Main.cd.getOrSetDefault("Fire_Shift", 60)),
    FIRE_RIGHT(Main.cd.getOrSetDefault("Fire_Right", 30)),
    FIRE_LEFT(Main.cd.getOrSetDefault("Fire_Left", 120)),
    AIR_SHIFT(Main.cd.getOrSetDefault("Air_Shift", 30)),
    AIR_RIGHT(Main.cd.getOrSetDefault("Air_Right", 30)),
    AIR_LEFT(Main.cd.getOrSetDefault("Air_Left", 60)),
    LIGHTNING_SHIFT(Main.cd.getOrSetDefault("Lightning_Shift", 120)),
    LIGHTNING_RIGHT(Main.cd.getOrSetDefault("Lightning_Right", 45)),
    LIGHTNING_LEFT(Main.cd.getOrSetDefault("Lightning_Left", 15)),
    IRON_SHIFT(Main.cd.getOrSetDefault("Iron_Shift", 60)),
    IRON_RIGHT(Main.cd.getOrSetDefault("Iron_Right", 20)),
    IRON_LEFT(Main.cd.getOrSetDefault("Iron_Left", 60)),
    SAND_SHIFT(Main.cd.getOrSetDefault("Sand_Shift", 45)),
    SAND_RIGHT(Main.cd.getOrSetDefault("Sand_Right", 60)),
    SAND_LEFT(Main.cd.getOrSetDefault("Sand_Left", 30)),
    ICE_SHIFT(Main.cd.getOrSetDefault("Ice_Shift", 45)),
    ICE_RIGHT(Main.cd.getOrSetDefault("Ice_Right", 60)),
    ICE_LEFT(Main.cd.getOrSetDefault("Ice_Left", 30)),
    LAVA_SHIFT(Main.cd.getOrSetDefault("Lava_Shift", 300)),
    LAVA_RIGHT(Main.cd.getOrSetDefault("Lava_Right", 180)),
    LAVA_LEFT(Main.cd.getOrSetDefault("Lava_Left", 60));

    private long cd;


    CooldownManager(int cooldown){
        cd = cooldown;
    }

    public long getCooldown(){
        return cd;
    }

}
