package me.iseal.powergems.managers;

import me.iseal.powergems.listeners.fallingBlockHitListener;
import me.iseal.powergems.listeners.powerListeners.iceTargetListener;
import me.iseal.powergems.listeners.powerListeners.sandMoveListener;
import me.iseal.powergems.listeners.powerListeners.strenghtMoveListener;
import me.iseal.powergems.misc.Utils;

public class SingletonManager {

    //Classes

    public final GemManager gemManager = new GemManager();
    public final strenghtMoveListener strenghtMoveListen = new strenghtMoveListener();
    public final Utils utils = new Utils();
    public final RecipeManager recipeManager = new RecipeManager();
    public final UpdaterManager updaterManager = new UpdaterManager();
    public final ConfigManager configManager = new ConfigManager();
    public final PlayerManager playerManager = new PlayerManager();
    public final sandMoveListener sandMoveListen = new sandMoveListener();
    public final iceTargetListener iceTargetListen = new iceTargetListener();
    public final me.iseal.powergems.listeners.fallingBlockHitListener fallingBlockHitListen = new fallingBlockHitListener();

}
