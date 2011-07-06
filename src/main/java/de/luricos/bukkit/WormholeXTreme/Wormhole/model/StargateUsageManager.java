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
package de.luricos.bukkit.WormholeXTreme.Wormhole.model;

import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 *
 * @author lycano
 */
public class StargateUsageManager {
    protected static Map<String, StargateUsage> dialStargateUsageMap = new ConcurrentHashMap<String, StargateUsage>();
    protected static Map<String, StargateUsage> signStargateUsageMap = new ConcurrentHashMap<String, StargateUsage>();
    protected static Map<String, StargateKeyring> lastKeyringUsed = new ConcurrentHashMap<String, StargateKeyring>();
    /**
     * Init Class with Player
     * 
     * @param player the Player instance
     */
    public static void init(Player player) {
        setStargateUsage(player, null);
    }    
    
    /**
     * Init with Stargate and Player objects
     * 
     * @param stargate the Stargate instance
     * @param player the Player instance
     */
    public static void init(Player player, Stargate stargate) {
        if (stargate != null) {
            setStargateUsage(player, stargate, getKeyring(stargate));
        }   
    }
    
    public static StargateKeyring getKeyring(Stargate stargate) {
        return (stargate.isGateSignPowered() ? StargateKeyring.SIGN : StargateKeyring.DIAL);
    }

    /**
     * Get the last keyring used by player
     * 
     * @param player the player object
     * @return StargateKeyring
     */
    public static StargateKeyring getLastKeyringUsed(Player player) {
        StargateKeyring lastKeyring = lastKeyringUsed.get(player.getName());
        if (lastKeyring != null)
            return lastKeyring;
        
        return StargateKeyring.NONE;
    }    
    
    /**
     * Set the last keyring used by player
     * 
     * @param player the player object
     * @param keyring DIAL, SIGN, NONE
     */
    public static void setLastKeyringUsed(Player player, StargateKeyring keyring) {
        if (!lastKeyringUsed.containsKey(player.getName())) {
            lastKeyringUsed.put(player.getName(), keyring);
        }
    }
    
    
    public static void removeLastKeyring(Player player) {
        if (player != null)
            removeLastKeyring(player.getName());
    }
    
    /**
     * Remove the last keyring entry for player
     * 
     * @param playerName
     */
    public static void removeLastKeyring(String playerName) {
        lastKeyringUsed.remove(playerName);
        WXTLogger.prettyLog(Level.FINE, false, "Removed '" + playerName + "' from last StargateUsage keyring");
    }
    
    
    public static StargateUsage getStargateUsage(Player player) {
        return getStargateUsage(player, getLastKeyringUsed(player));
    }
    
    
    /**
     * Get StargateUsage for player
     * 
     * @param player
     * @return StargateUsage
     */
    public static StargateUsage getStargateUsage(Player player, StargateKeyring keyring) {
        String playerName = "";
        if (player != null) {
            playerName = player.getName();
        }
        
        return getStargateUsage(playerName, keyring);
    }
    
    /**
     * Get StargateUsage via player's name
     * 
     * @param playerName
     * @return StargateUsage of given playerName. If playerName was not found in keyring empty StargateUsage will be returned
     */
    public static StargateUsage getStargateUsage(String playerName, StargateKeyring keyring) {
        switch (keyring) {
            case DIAL:
                if (StargateUsageManager.dialStargateUsageMap.containsKey(playerName)) {
                    WXTLogger.prettyLog(Level.FINE, false, "Get 'DIAL' StargateUsage for '" + playerName + "'");
                    return StargateUsageManager.dialStargateUsageMap.get(playerName);
                }
                break;
            case SIGN:
                if (StargateUsageManager.signStargateUsageMap.containsKey(playerName)) {
                    WXTLogger.prettyLog(Level.FINE, false, "Get 'SIGN' StargateUsage for '" + playerName + "'");
                    return StargateUsageManager.signStargateUsageMap.get(playerName);
                }
                break;
        }
        
        WXTLogger.prettyLog(Level.FINE, false, "Get 'NONE' StargateUsage for '" + playerName + "'");
        return new StargateUsage(null, null);
    }
    
    /**
     * Add Player to StargateUsage keyring
     * 
     * @param player 
     */
    public static void setStargateUsage(Player player, StargateKeyring keyring) {
        setStargateUsage(player, getStargateUsage(player, keyring).getStargate(), keyring);
    }
    
    /**
     * add Stargate and Player intsance to StargateUsage keyring
     * 
     * @param stargate
     * @param player 
     */
    public static void setStargateUsage(Player player, Stargate stargate, StargateKeyring keyring) {
        String playerName = player.getName();
        if (playerName == null)
            return;

        switch (keyring) {
            case DIAL:
                if (!StargateUsageManager.dialStargateUsageMap.containsKey(player.getName())) {
                    StargateUsage stargateUsage = new StargateUsage(player, stargate);

                    WXTLogger.prettyLog(Level.FINE, false, "Adding '" + playerName + "' to dialStargateUsage keyring");
                    StargateUsageManager.dialStargateUsageMap.put(playerName, stargateUsage);

                    removeLastKeyring(player);
                    setLastKeyringUsed(player, getKeyring(stargate));
                }
                break;
            case SIGN:
                if (!StargateUsageManager.signStargateUsageMap.containsKey(player.getName())) {
                    StargateUsage stargateUsage = new StargateUsage(player, stargate);

                    WXTLogger.prettyLog(Level.FINE, false, "Adding '" + playerName + "' to signStargateUsage keyring");
                    StargateUsageManager.signStargateUsageMap.put(playerName, stargateUsage);
                    
                    removeLastKeyring(player);
                    setLastKeyringUsed(player, getKeyring(stargate));
                }
                
                break;
        }
    }
    
    public static void removeStargateUsage(Player player) {
        removeStargateUsage(player, getLastKeyringUsed(player));
    }
    
    
    /**
     * Remove Player's StargateUsage from keyring
     * 
     * @param player 
     */
    public static void removeStargateUsage(Player player, StargateKeyring keyring) {
        String playerName = player.getName();
        if (playerName == null)
            return;

        switch (keyring) {
            case DIAL:
                WXTLogger.prettyLog(Level.FINE, false, "Removed '" + playerName + "' from DialStargateUsage keyring");
                StargateUsageManager.dialStargateUsageMap.remove(playerName);
                break;
            case SIGN:
                WXTLogger.prettyLog(Level.FINE, false, "Removed '" + playerName + "' from SignStargateUsage keyring");
                StargateUsageManager.signStargateUsageMap.remove(playerName);
                break;
        }
        
        removeLastKeyring(player);
    }
    
    /**
     * Clear the all StargateUsageMaps
     * 
     * Can be used for a global reset
     */
    public static void clearStargateUsage() {
        WXTLogger.prettyLog(Level.FINE, false, "All StargateUsage keyrings has been cleared.");
        dialStargateUsageMap.clear();
        signStargateUsageMap.clear();
        lastKeyringUsed.clear();
    }
}
