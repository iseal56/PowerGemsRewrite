package me.iseal.powergems.managers;

import me.iseal.powergems.gems.powerClasses.tasks.fireballPowerDecay;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TempDataManager {

    //Fire players in fireball
    public HashMap<Player, fireballPowerDecay> chargingFireball = new HashMap<>(1);

    //Players not able to use gems
    public HashMap<Player, Long> cantUseGems = new HashMap<>(1);

}
