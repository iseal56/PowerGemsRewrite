package me.iseal.powergems.gems.powerClasses.tasks;

import me.iseal.powergems.Main;
import me.iseal.powergems.gems.fireGem;
import me.iseal.powergems.managers.SingletonManager;
import me.iseal.powergems.managers.TempDataManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class fireballPowerDecay extends BukkitRunnable {

    private final TempDataManager tdm = Main.getSingletonManager().tempDataManager;
    private final fireGem fg = new fireGem();
    public Player plr = null;
    public int currentPower = 0;
    public int level = 1;

    @Override
    public void run() {
        currentPower-=1;
        if (currentPower <= 0){
            tdm.chargingFireball.remove(plr);
            fg.addLeftCooldown(plr, level);
            plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED+"You failed to launch the fireball."));
            this.cancel();
            return;
        }
        if (currentPower >= 100){
            spawnFireball();
            tdm.chargingFireball.remove(plr);
            fg.addLeftCooldown(plr, level);
            plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED+"You successfully launched the fireball."));
            this.cancel();
            return;
        }
        StringBuilder toSay = new StringBuilder(ChatColor.GREEN + "");
        int nOfBars = currentPower / 10;
        for (int i = 0; i < nOfBars; i++) {
            toSay.append("| ");
        }
        toSay.append(ChatColor.GRAY);
        for (int i = 0; i < 10-nOfBars; i++) {
            toSay.append("| ");
        }
        plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(toSay.toString()));
    }

    private void spawnFireball(){
        Vector direction = plr.getEyeLocation().getDirection();
        Fireball fireball = plr.launchProjectile(Fireball.class);
        fireball.setVelocity(direction.multiply(2));
        fireball.setYield(5+level);
        fireball.setVisualFire(false);
        fireball.setIsIncendiary(Main.config.getBoolean("explosionDamageAllowed"));
        fireball.getPersistentDataContainer().set(Main.getIsGemExplosionKey(), PersistentDataType.BOOLEAN, true);
        fireball.getPersistentDataContainer().set(Main.getIsGemProjectileKey(), PersistentDataType.BOOLEAN, true);
    }

}
