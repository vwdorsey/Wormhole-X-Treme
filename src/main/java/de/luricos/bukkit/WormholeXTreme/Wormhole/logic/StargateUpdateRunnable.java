/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.logic;

import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.StargateRestrictions;
import de.luricos.bukkit.WormholeXTreme.Wormhole.player.WormholePlayerManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.logging.Level;

/**
 * WormholeXtreme Runnable thread for updating stargates.
 * 
 * @author Ben Echols (Lologarithm)
 */
public class StargateUpdateRunnable implements Runnable {

    /**
     * The Enum ActionToTake.
     */
    public enum ActionToTake {

        /** The SHUTDOWN task. */
        SHUTDOWN,
        /** The ANIMATE OPENING task. */
        ANIMATE_WOOSH,
        /** The DEACTIVATE task. */
        DEACTIVATE,
        /** The AFTERSHUTDOWN task. */
        AFTERSHUTDOWN,
        /** The SIGNCLICK. */
        DIAL_SIGN_CLICK,
        /** Action to iterate over lighting up blocks during activation. */
        LIGHTUP,
        COOLDOWN_REMOVE,
        DIAL_SIGN_RESET,
        ESTABLISH_WORMHOLE
    }
    
    /** The stargate. */
    private final Stargate stargate;
    /** The action. */
    private final ActionToTake action;

    private Action eventBlockAction;
    
    /**
     * Instantiates a new stargate update runnable.
     * 
     * @param stargate the s
     * @param action the act
     */
    public StargateUpdateRunnable(final Stargate stargate, final ActionToTake action) {
        this(stargate, action, null);
    }
    
    public StargateUpdateRunnable(Stargate stargate, ActionToTake action, Action eventBlockAction) {
        this.stargate = stargate;
        this.action = action;
        this.eventBlockAction = eventBlockAction;
    }

    private void runLogger(ActionToTake action) {
        // set some messages to FINER
        switch (action) {
            case ESTABLISH_WORMHOLE:
            case ANIMATE_WOOSH:
            case LIGHTUP:
                WXTLogger.prettyLog(Level.FINER, false, "Run Action \"" + action.toString() + (stargate != null
                        ? "\" Stargate \"" + stargate.getGateName()
                        : "") + "\"");
                return;
        }
        
        WXTLogger.prettyLog(Level.FINE, false, "Run Action \"" + action.toString() + ", ActionType: " + ((this.eventBlockAction != null) ? this.eventBlockAction.toString() : "NULL") + (stargate != null
                ? "\" Stargate \"" + stargate.getGateName()
                : "") + "\"");        
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        runLogger(action);
        
        Player player = null;
        if (WormholePlayerManager.getRegisteredWormholePlayer(stargate.getLastUsedBy()) != null) {
            player = WormholePlayerManager.getRegisteredWormholePlayer(stargate.getLastUsedBy()).getPlayer();
        }

        switch (action) {
            case ESTABLISH_WORMHOLE:
                stargate.establishWormhole();
                break;
            case SHUTDOWN:
                stargate.shutdownStargate(true);
                break;
            case ANIMATE_WOOSH:
                stargate.animateOpening();
                break;
            case DEACTIVATE:
                stargate.timeoutStargate();
                break;
            case AFTERSHUTDOWN:
                stargate.stopAfterShutdownTimer();
                break;
            case DIAL_SIGN_CLICK:
                stargate.dialSignClicked(this.eventBlockAction);
                if ((player != null) && (stargate.getGateDialSignTarget() == null)) {
                    player.sendMessage("No available target to set dialer to.");
                }
                break;
            case DIAL_SIGN_RESET:
                stargate.resetSign(true);
                break;
            case LIGHTUP:
                stargate.lightStargate(true);
                break;
                /*
            case COOLDOWN_REMOVE:
                StargateRestrictions.removePlayerUseCooldown(player);
                break;*/
            default:
                break;
        }
    }
}
