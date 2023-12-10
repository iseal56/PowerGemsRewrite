package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.gems.powerClasses.StrenghArena;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static me.iseal.powergems.Main.gemActive;

public class powerGem {

    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.STRENGTH_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.STRENGTH_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.STRENGTH_SHIFT.getCooldown();
    private final SingletonManager sm = Main.getSingletonManager();
    private Utils u = sm.utils;
    private final ConfigManager cm = sm.configManager;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("power", true)) {
            return false;
        }
        level = lvl;
        if (p.isSneaking()){
            onShiftClick(p);
        } else if (u.isLeftClick(a)){
            onLeftClick(p);
        } else {
            onRightClick(p);
        }
        return true;
    }

    private void onRightClick(Player plr) {
        UUID plrID = plr.getUniqueId();
        //cooldown check
        if (rightCooldowns.containsKey(plrID)){
            long cooldownEndTime = rightCooldowns.get(plrID);
            if (cooldownEndTime > System.currentTimeMillis()){
                int millis = (int) (cooldownEndTime-System.currentTimeMillis());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                String endtime;
                if (seconds < 10) {
                    endtime = String.format("0%d:0%d", minutes, seconds);
                } else {
                    endtime = String.format("0%d:%d", minutes, seconds);
                }
                plr.sendMessage(ChatColor.DARK_RED + "You are still on cooldown, time left: " + endtime);
                return;
            }
        }
        //power
        plr.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
        //cooldown add
        rightCooldowns.put(plrID, System.currentTimeMillis()+(rightCooldown*1000-(level*cm.getGemCooldownBoost())));
    }


    private void onLeftClick(Player plr) {
        UUID plrID = plr.getUniqueId();
        //cooldown check
        if (leftCooldowns.containsKey(plrID)){
            long cooldownEndTime = leftCooldowns.get(plrID);
            if (cooldownEndTime > System.currentTimeMillis()){
                int millis = (int) (cooldownEndTime-System.currentTimeMillis());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                String endtime;
                if (seconds < 10) {
                    endtime = String.format("0%d:0%d", minutes, seconds);
                } else {
                    endtime = String.format("0%d:%d", minutes, seconds);
                }
                plr.sendMessage(ChatColor.DARK_RED + "You are still on cooldown, time left: " + endtime);
                return;
            }
        }
        double distance = 10;
        double power = 3;
        Location playerLocation = plr.getLocation();
        List<Entity> nearbyEntities = plr.getNearbyEntities(distance, distance, distance);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player && !((Player)entity).equals(plr)) {
                Player nearbyPlayer = (Player) entity;
                Vector knockbackVector = nearbyPlayer.getLocation().subtract(playerLocation).toVector();
                nearbyPlayer.setVelocity(knockbackVector.multiply(power));
                nearbyPlayer.damage(5);
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2));
            }
        }
        //cooldown add
        leftCooldowns.put(plrID, System.currentTimeMillis()+(leftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }


    private void onShiftClick(Player plr) {
        UUID plrID = plr.getUniqueId();
        //cooldown check
        if (shiftCooldowns.containsKey(plrID)){
            long cooldownEndTime = shiftCooldowns.get(plrID);
            if (cooldownEndTime > System.currentTimeMillis()){
                int millis = (int) (cooldownEndTime-System.currentTimeMillis());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                String endtime;
                if (seconds < 10) {
                    endtime = String.format("0%d:0%d", minutes, seconds);
                } else {
                    endtime = String.format("0%d:%d", minutes, seconds);
                }
                plr.sendMessage(ChatColor.DARK_RED + "You are still on cooldown, time left: " + endtime);
                return;
            }
        }
        plr.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2));
        new StrenghArena(plr).start();
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }
}
