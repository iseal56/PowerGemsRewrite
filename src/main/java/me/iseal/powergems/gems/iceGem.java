package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.listeners.fallingBlockHitListener;
import me.iseal.powergems.listeners.powerListeners.iceTargetListener;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.misc.Utils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
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

public class iceGem {
    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.ICE_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.ICE_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.ICE_SHIFT.getCooldown();
    private final SingletonManager sm = Main.getSingletonManager();
    private Utils u = sm.utils;
    private final ConfigManager cm = sm.configManager;
    private final iceTargetListener itl = sm.iceTargetListen;
    private final fallingBlockHitListener fbhl = sm.fallingBlockHitListen;
    private int level;

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("ice", true)) {
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
        Location l = plr.getEyeLocation();
        FallingBlock fb = l.getWorld().spawnFallingBlock(l, Material.ICE.createBlockData());
        fb.setHurtEntities(true);
        fb.setDamagePerBlock(level);
        fb.setVelocity(l.getDirection());
        fb.getVelocity().multiply((level*5)+1);
        fbhl.addEntityUUID(fb.getUniqueId());
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
        int distance = 5*(level/2); // Maximum distance between the players
        RayTraceResult result = plr.getWorld().rayTrace(plr.getEyeLocation(), plr.getEyeLocation().getDirection(), distance, FluidCollisionMode.ALWAYS, true, 1, entity -> !entity.equals(plr) && entity instanceof Player);
        if (result == null){
            plr.sendMessage(ChatColor.DARK_RED+"You need to aim at a player to do that");
            return;
        }
        Player targetplr = (Player) result.getHitEntity();
        targetplr.setFreezeTicks(100+(level*2)*20);
        targetplr.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100+(level*2)*20, level-1));
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
        Location l = plr.getLocation();
        World w = plr.getWorld();
        for (int i = 0; i < level*2; i++) {
            w.spawnEntity(l, EntityType.SNOWMAN);
        }
        itl.addToList(plr);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                itl.removeFromList(plr);
            }
        }, 1200);
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }
}
