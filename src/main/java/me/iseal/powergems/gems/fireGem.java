package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.gems.powerClasses.tasks.fireballPowerDecay;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

public class fireGem {
    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.FIRE_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.FIRE_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.FIRE_SHIFT.getCooldown();
    private final SingletonManager sm = Main.getSingletonManager();
    private final Utils u = sm.utils;
    private final ConfigManager cm = sm.configManager;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("fire", true)) {
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
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Cooldown: "+endtime));
                return;
            }
        }
        //power
        Location playerLocation = plr.getLocation();
        World world = plr.getWorld();
        plr.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 1));
        int radius = 8*(level/2);
        for (int x = playerLocation.getBlockX() - radius; x <= playerLocation.getBlockX() + radius; x++) {
            for (int z = playerLocation.getBlockZ() - radius; z <= playerLocation.getBlockZ() + radius; z++) {
                Block block = world.getBlockAt(x, playerLocation.getBlockY(), z);
                if (block.getType() == Material.AIR || block.getType() == Material.SNOW && !block.isLiquid()) {
                    block.setType(Material.FIRE);
                }
            }
        }
        //cooldown add
        rightCooldowns.put(plrID, System.currentTimeMillis()+(rightCooldown*1000-(level*cm.getGemCooldownBoost())));
    }


    private void onLeftClick(Player plr) {
        if (sm.tempDataManager.chargingFireball.containsKey(plr)){
            sm.tempDataManager.chargingFireball.get(plr).currentPower+=10;
            return;
        }
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
        fireballPowerDecay fpd = new fireballPowerDecay();
        fpd.plr = plr;
        fpd.currentPower = 10;
        fpd.level = level;
        fpd.runTaskTimer(Main.getPlugin(), 2, 2);
        sm.tempDataManager.chargingFireball.put(plr, fpd);
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
        World world = plr.getWorld();
        plr.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 5));
        plr.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 1));
        world.createExplosion(playerLocation, 4f, true, false);
        for (Entity entity : plr.getNearbyEntities(6, 6, 6)) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(5*(level/2), plr);
                entity.setFireTicks(100);
            }
        }
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }

    public void addLeftCooldown(Player p, int level){
        leftCooldowns.put(p.getUniqueId(), System.currentTimeMillis()+(leftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }

}