package me.iseal.powergems.managers;

import me.iseal.powergems.gems.powerClasses.tasks.fireballPowerDecay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TempDataManager {

    //Fire players in fireball
    public HashMap<Player, fireballPowerDecay> chargingFireball = new HashMap<>(1);

}
