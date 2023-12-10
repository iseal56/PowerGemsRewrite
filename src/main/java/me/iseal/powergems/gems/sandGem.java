package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.iseal.powergems.Main.gemActive;

public class sandGem {
    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.SAND_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.SAND_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.SAND_SHIFT.getCooldown();
    private final SingletonManager sm = Main.getSingletonManager();
    private Utils u = sm.utils;
    private final ConfigManager cm = sm.configManager;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("sand", true)) {
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
        RayTraceResult result = plr.getWorld().rayTrace(plr.getEyeLocation(), plr.getEyeLocation().getDirection(), 200D, FluidCollisionMode.ALWAYS, true, 1, entity -> !entity.equals(plr) && entity instanceof Player);
        Player target = (Player) result.getHitEntity();
        if (target == null){
            plr.sendMessage(ChatColor.DARK_RED+"You must be looking at a player to do that");
            return;
        }
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80+(level*40), 1+level));
        target.setFoodLevel(Math.max(0, target.getFoodLevel() - 5+(level*2)));
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
        //power
        RayTraceResult result = plr.getWorld().rayTrace(plr.getEyeLocation(), plr.getEyeLocation().getDirection(), 10+level, FluidCollisionMode.ALWAYS, true, 1, entity -> !entity.equals(plr) && entity instanceof Player);
        if (result == null || result.getHitEntity() == null){
            plr.sendMessage(ChatColor.DARK_RED+"You need to aim at a player to do that");
            return;
        }
        Player target = (Player) result.getHitEntity();
        target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60+(level*40), 1+level));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60+(level*40), 1+level));
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
        //power
        Block possibleTarget = plr.getTargetBlock(null, 90);
        if (possibleTarget == null){
            plr.sendMessage(ChatColor.DARK_RED+"You must be looking at a block to do that");
            return;
        }
        Material oldMaterial = possibleTarget.getType();
        Location targetLocation = possibleTarget.getLocation();
        targetLocation.getBlock().setType(Material.SAND);
        sm.sandMoveListen.addToList(possibleTarget);

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            targetLocation.getBlock().setType(oldMaterial);
            sm.sandMoveListen.removeFromList(possibleTarget);
        }, 200L);
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }
}