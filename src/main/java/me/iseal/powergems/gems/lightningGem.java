package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.iseal.powergems.Main.gemActive;

public class lightningGem {

    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.LIGHTNING_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.LIGHTNING_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.LIGHTNING_SHIFT.getCooldown();
    private final SingletonManager sm = Main.getSingletonManager();
    private Utils u = sm.utils;
    private final ConfigManager cm = sm.configManager;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("lightning", true)) {
            return false;
        }
        level = lvl;
        if (p.isSneaking()) {
            onShiftClick(p);
        } else if (u.isLeftClick(a)) {
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
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Cooldown: "+endtime));
                return;
            }
        }
        //power
        Block possibleTarget = plr.getTargetBlock(null, 90);
        if (possibleTarget == null){
            plr.sendMessage(ChatColor.DARK_RED+"You must be looking at a block to do that");
            return;
        }
        Location targetLocation = possibleTarget.getLocation();
        World plrWorld = plr.getWorld();
        plrWorld.strikeLightning(targetLocation);
        for(Entity e : plrWorld.getNearbyEntities(targetLocation,5,5,5)){
            if (e instanceof LivingEntity){
                plrWorld.strikeLightning(e.getLocation());
            }
        }
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
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Cooldown: "+endtime));
                return;
            }
        }
        //power
        Location playerLocation = plr.getLocation();
        World world = playerLocation.getWorld();
        plr.setVelocity(playerLocation.getDirection().multiply(5));
        world.spawnParticle(Particle.FLASH, playerLocation, 100, 0, 0, 0, 0.2);
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
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Cooldown: "+endtime));
                return;
            }
        }
        //power
        Location playerLocation = plr.getLocation();
        World world = playerLocation.getWorld();
        world.playSound(playerLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        for (Entity e : world.getNearbyEntities(playerLocation,5,5,5)){
            if (e instanceof LivingEntity) {
                if (e != plr) {
                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
                }
            }
        }
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }
}