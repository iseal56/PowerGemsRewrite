package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.misc.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.iseal.powergems.Main.gemActive;

public class airGem {
    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.AIR_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.AIR_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.AIR_SHIFT.getCooldown();
    private Utils u = Main.getSingletonManager().utils;
    private final ConfigManager cm = Main.getSingletonManager().configManager;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("air", true)) {
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
        int distance = 5*(level/2); // Maximum distance between the players
        double force = 1.5+level; // Strength of the pull

        RayTraceResult result = plr.getWorld().rayTrace(plr.getEyeLocation(), plr.getEyeLocation().getDirection(), distance, FluidCollisionMode.ALWAYS, true, 1, entity -> !entity.equals(plr) && entity instanceof Player);
        if (result == null){
            plr.sendMessage(ChatColor.DARK_RED+"You need to aim at a player to do that");
            return;
        }
        Player targetplr = (Player) result.getHitEntity();
        Location playerLocation = plr.getLocation();
        //
        Location targetLocation = targetplr.getLocation();
        Vector direction = playerLocation.subtract(targetLocation).toVector().normalize();
        targetplr.setVelocity(targetplr.getVelocity().add(direction.multiply(force)));

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
        Location playerLocation = plr.getLocation();
        double radius = 5.0*(level/2); // Radius of effect
        double power = 2.5+(level/2); // Strength of the burst

        plr.getWorld().getEntities().stream()
                .filter(entity -> entity.getLocation().distance(playerLocation) <= radius)
                .forEach(entity -> {
                    if (entity != plr) {
                        entity.setVelocity(entity.getVelocity().add(new Vector(0, power, 0)));
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity) entity).damage(power);
                        }
                    }
                });
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
        double distance = 6*(level/2.0);
        Location location = plr.getLocation();
        Vector direction = location.getDirection().normalize();
        AreaEffectCloud effect = (AreaEffectCloud) plr.getWorld().spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
        effect.setDuration(60);
        effect.setRadius(1.0f);
        effect.setParticle(Particle.SMOKE_LARGE);
        effect.setColor(Color.BLACK);
        plr.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
        plr.setVelocity(direction.multiply(distance));
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }
}
