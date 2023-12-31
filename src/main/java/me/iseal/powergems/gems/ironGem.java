package me.iseal.powergems.gems;

import me.iseal.powergems.Main;
import me.iseal.powergems.managers.ConfigManager;
import me.iseal.powergems.managers.CooldownManager;
import me.iseal.powergems.managers.TempDataManager;
import me.iseal.powergems.misc.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.Particle;
import org.bukkit.event.block.Action;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static me.iseal.powergems.Main.gemActive;

public class ironGem {
    private Map<UUID, Long> rightCooldowns = new HashMap<>();
    private Map<UUID, Long> leftCooldowns = new HashMap<>();
    private Map<UUID, Long> shiftCooldowns = new HashMap<>();

    private final long rightCooldown = CooldownManager.IRON_RIGHT.getCooldown();
    private final long leftCooldown = CooldownManager.IRON_LEFT.getCooldown();
    private final long shiftCooldown = CooldownManager.IRON_SHIFT.getCooldown();
    private Utils u = Main.getSingletonManager().utils;
    private final TempDataManager tdm = Main.getSingletonManager().tempDataManager;
    private final ConfigManager cm = Main.getSingletonManager().configManager;
    private int level;
    private final Logger l = Bukkit.getLogger();
    private final AttributeModifier armorModifier = new AttributeModifier(Main.getAttributeUUID(), "Iron Fortification", 8, AttributeModifier.Operation.ADD_NUMBER);
    private final AttributeModifier toughnessModifier = new AttributeModifier(Main.getAttributeUUID(), "Iron Fortification", 4, AttributeModifier.Operation.ADD_NUMBER);
    private final AttributeModifier knockbackAttribute = new AttributeModifier(Main.getAttributeUUID(), "Iron Fortification - Knockback", 5, AttributeModifier.Operation.ADD_NUMBER);

    public boolean handlePower(Player p, Action a, int lvl){
        if (!gemActive.getOrSetDefault("iron", true)) {
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
        plr.getWorld().spawnParticle(Particle.CRIT, plr.getLocation().add(0, 1, 0), 20);
        plr.setAbsorptionAmount(2*level);
        AttributeInstance knockbackInstance = plr.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        try {
            knockbackInstance.addModifier(knockbackAttribute);
        } catch (IllegalArgumentException ex) {
            l.warning("[PowerGems] "+plr.getDisplayName()+" used Iron Gem Shift while already having the modifiers, please report this to the developer");
        }
        plr.setVelocity(new Vector(0, 0, 0));
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            OfflinePlayer op = Bukkit.getOfflinePlayer(plrID);
            if (op.isOnline()) {;
                plr.setAbsorptionAmount(0.0);
                knockbackInstance.removeModifier(knockbackAttribute);
            } else {
                tdm.ironRightLeft.add(op.getUniqueId());
            }
        }, 150*level);
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
        Vector direction = plr.getEyeLocation().getDirection();
        for (int i = 0; i < 20+(level*2); i++) {
            Vector coneDirection = direction.clone().rotateAroundY(i * 20);
            Arrow sa = plr.launchProjectile(Arrow.class,coneDirection);
            sa.setBounce(true);
            sa.setDamage(level);
            sa.setVelocity(sa.getVelocity().multiply(level));
            sa.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            sa.getPersistentDataContainer().set(Main.getIsGemProjectileKey(), PersistentDataType.BOOLEAN, true);
        }
        for (int i = 0; i < 5+level; i++) {
            Arrow sa = plr.launchProjectile(Arrow.class);
            sa.setBounce(true);
            sa.setDamage(level);
            sa.setVelocity(sa.getVelocity().multiply(level));
            sa.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
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
                plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Cooldown: "+endtime));
                return;
            }
        }
        //power
        AttributeInstance armorAttribute = plr.getAttribute(Attribute.GENERIC_ARMOR);
        AttributeInstance toughnessAttribute = plr.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        try {
            armorAttribute.addModifier(armorModifier);
            toughnessAttribute.addModifier(toughnessModifier);
        } catch (IllegalArgumentException ex) {
            l.warning("[PowerGems] "+plr.getDisplayName()+" used Iron Gem Shift while already having the modifiers, please report this to the developer");
        }
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            OfflinePlayer op = Bukkit.getOfflinePlayer(plrID);
            if (op.isOnline()) {
                armorAttribute.removeModifier(armorModifier);
                toughnessAttribute.removeModifier(toughnessModifier);
            } else {
                tdm.ironShiftLeft.add(op.getUniqueId());
            }
        }, 200);
        //cooldown add
        shiftCooldowns.put(plrID, System.currentTimeMillis()+(shiftCooldown*1000-(level*cm.getGemCooldownBoost())));
    }

    public void removeShiftModifiers(Player plr){
        AttributeInstance armorAttribute = plr.getAttribute(Attribute.GENERIC_ARMOR);
        AttributeInstance toughnessAttribute = plr.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        armorAttribute.removeModifier(armorModifier);
        toughnessAttribute.removeModifier(toughnessModifier);
    }
    public void removeRightModifiers(Player plr){
        AttributeInstance knockbackInstance = plr.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        knockbackInstance.removeModifier(knockbackAttribute);
    }

}
