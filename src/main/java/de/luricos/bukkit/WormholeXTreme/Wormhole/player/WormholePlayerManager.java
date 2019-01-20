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
package de.luricos.bukkit.WormholeXTreme.Wormhole.player;

import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePlayerEmptyPlayerNameException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePlayerNotFoundException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePlayerNotOnlineException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manage wormhole players
 * 
 * @author lycano
 * @TODO create offlineKeyring to fetch offline usage of players
 *       create registerAsOfflinePlayer, unregisterOfflinePlayer, unregisterAllOfflinePlayers, getOfflinePlayer, getAllOfflinePlayers methods
 *       put unregsiterAllOfflinePlayers to scheduledTask queue after shutdown timeout
 */
public class WormholePlayerManager {
    
    private static Map<String, WormholePlayer> wormholePlayers = new HashMap<>();
    
    public static void registerPlayer(String playerName) {
        try {
            Player player = Bukkit.getServer().getPlayer(playerName);
            if (player == null)
                throw new WormholePlayerNotFoundException("Player '" + playerName + "' not found");
            
            if ((player != null) && (!player.isOnline()))
                throw new WormholePlayerNotOnlineException("Player '" + playerName + "' is not online");
            
            WormholePlayerManager.registerPlayer(player);
        } catch (WormholePlayerNotFoundException e) {
            WXTLogger.prettyLog(Level.SEVERE, false, e.getMessage());
        } catch (WormholePlayerNotOnlineException e) {
            WXTLogger.prettyLog(Level.WARNING, false, e.getMessage());
        }
    }
    
    /**
     * Register a Player to keyring
     * 
     * @param player 
     */
    public static void registerPlayer(Player player) {
        if (!isRegistered(player.getName())) {
            WXTLogger.prettyLog(Level.FINE, false, "Registering player '" + player.getName() +"' as WormholePlayer");
            wormholePlayers.put(player.getName(), new WormholePlayer(player));
        }
    }
    
    /**
     * Is player registered? (by player object)
     * 
     * @param player the player object
     * @return boolean false if no such player was found
     */
    public static boolean isRegistered(Player player) {
        return isRegistered(player.getName());
    }
    
    /**
     * Is player registered? (by string)
     * @param playerName the playerName
     * @return 
     */
    public static boolean isRegistered(String playerName) {
        try {
            if ("".equals(playerName))
                throw new WormholePlayerEmptyPlayerNameException("playerName can't be empty.");
            
            if (!wormholePlayers.containsKey(playerName)) {
                WXTLogger.prettyLog(Level.FINE, false, "'" + playerName +"' was not registered");
                return false;
            }
        } catch (WormholePlayerEmptyPlayerNameException e) {
            WXTLogger.prettyLog(Level.SEVERE, true, e.getMessage());
        }
        
        return true;
    }
    
    /**
     * Unregister a player from keyring
     * 
     * @param player
     */
    public static void unregisterPlayer(Player player) {
        unregisterPlayer(player.getName());
    }

    /**
     * Unregister a player from keyring
     * 
     * @param playerName 
     */
    public static void unregisterPlayer(String playerName) {
        if (!isRegistered(playerName))
            return;
        
        WXTLogger.prettyLog(Level.FINE, false, "Unregistering WormholePlayer '" + playerName +"'");
        
        // reset player first before remove
        wormholePlayers.get(playerName).resetPlayer();
        wormholePlayers.remove(playerName);
    }
    
    /**
     * Unregister all registered Players
     */
    public static void unregisterAllPlayers() {
        WXTLogger.prettyLog(Level.FINE, false, "Unregistering all WormholePlayers.");
        wormholePlayers.clear();
    }
    
    public static void registerAllOnlinePlayers() {
        WXTLogger.prettyLog(Level.FINE, false, "Registering all online players as WormholePlayers.");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            registerPlayer(player);
        }
    }
    
    /**
     * Get all registered WormholePlayers
     * 
     * @return HashMap<String, WormholePlayer>
     */
    public static HashMap<String, WormholePlayer> getAllRegisteredPlayers() {
        return (HashMap<String, WormholePlayer>) wormholePlayers;
    }
    
    /**
     * Get WormholePlayer
     * 
     * @param player 
     * @return WormholePlayer null if no player was found
     */
    public static WormholePlayer getRegisteredWormholePlayer(Player player) {
        return getRegisteredWormholePlayer(player.getName());
    }
    
    /**
     * Get WormholePlayer
     * @param playerName the player's name
     * @return WomrholePlayer null if no player was found
     */
    public static WormholePlayer getRegisteredWormholePlayer(String playerName) {
        if (isRegistered(playerName))
            return wormholePlayers.get(playerName);
        
        return null;        
    }
    
    /**
     * Lookup WormholePlayer by gateName
     * 
     * @param gateName stargate's name
     * @return WormholePlayer or null if no gate was found
     */
    public static WormholePlayer findPlayerByGateName(String gateName) {
        for (String pl : wormholePlayers.keySet()) {
            for (Stargate s : wormholePlayers.get(pl).getStargates()) {
                if (s.getGateName().equalsIgnoreCase(gateName))
                    return wormholePlayers.get(pl);
            }
        }
        
        return null;
    }
}
