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

import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePlayerEmptyStargateNameException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePlayerNullPointerException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.ArrayList;

/**
 *
 * @author lycano
 */
public class WormholePlayer extends LocalPlayer {
    
    private Map<String, WormholePlayerUsageProperties> usageProperties = new HashMap<String, WormholePlayerUsageProperties>();
    private Map<String, Stargate> stargateMap = new HashMap<String, Stargate>();
    
    private String currentGateName = "";
    
    protected WormholePlayer(Player player) {
        super(player);
    }

    public void resetPlayer() {
        WXTLogger.prettyLog(Level.FINE, false, "Resetting player '" + this.getName() + "'");
        for (Stargate s : this.getStargates()) {
            this.removeStargate(s.getGateName());
            this.removeProperty(s.getGateName());
        }
    }
    
    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName();
    }
    
    /**
     * Get the player object
     * 
     * @return Player; null if nothing found
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Returns current used gate property
     * @return WormholePlayerUsageProperties may return null if there was no gate used lately
     */
    public WormholePlayerUsageProperties getProperties() {
        if (!"".equals(this.getCurrentGateName()))
            return this.getProperties(this.getCurrentGateName());
        
        return null;
    }
    
    /**
     * Get usage per stargate instance
     * 
     * @param stargate the stargate instance
     * @return 
     */
    public WormholePlayerUsageProperties getProperties(Stargate stargate) {
        return this.getProperties(stargate.getGateName());
    }
    
    
    /**
     * Get usage properties per gateName
     * 
     * @param gateName the gateName
     * @return StargateUsageEntity
     */
    public WormholePlayerUsageProperties getProperties(String gateName) {
        if (this.hasStargate(gateName))
            return usageProperties.get(gateName);
        
        return new WormholePlayerUsageProperties();
    }
       
    /**
     * Get the kickback direction of the player by block facing
     * 
     * @param facing 
     */
    public PlayerOrientation getKickBackDirection(BlockFace facing) {
        return this.getKickBackDirection(null, facing);
    }
    
    /**
     * Get the kickback direction by relative to his own direction
     * 
     * @param direction 
     */
    public PlayerOrientation getKickBackDirection(PlayerOrientation direction) {
        return this.getKickBackDirection(direction, null);
    }
    
    /**
     * Get the kickback direction by direction or facing
     * Only one argument has to be given
     * 
     * @param direction null or PlayerOrientation
     * @param facing null or BlockFace
     */
    private PlayerOrientation getKickBackDirection(PlayerOrientation direction, BlockFace facing) {
        if ((this.isOnline()) && ((direction != null) || (facing != null))) {
            WXTLogger.prettyLog(Level.FINE, false, "PlayerDirection: " + this.getCardinalDirection() + ", BlockFacing: " + facing);

            PlayerOrientation kickBack = null;
            if (direction != null) {
                kickBack = PlayerOrientation.byCaseInsensitiveName(direction.name());
            }
            
            if (facing != null) {
                kickBack = PlayerOrientation.byCaseInsensitiveName(facing.name());
            }
            
            switch (kickBack) {
                case NORTH:
                case NORTH_EAST:
                case NORTH_WEST:
                    WXTLogger.prettyLog(Level.FINE, false, "NORTH: kickback direction SOUTH");
                    return PlayerOrientation.SOUTH;
                case SOUTH:
                case SOUTH_EAST:
                case SOUTH_WEST:
                    WXTLogger.prettyLog(Level.FINE, false, "SOUTH: kickback direction NORTH");
                    return PlayerOrientation.NORTH;
                case EAST:
                    WXTLogger.prettyLog(Level.FINE, false, "EAST: kickback direction WEST");
                    return PlayerOrientation.WEST;
                case WEST:
                    WXTLogger.prettyLog(Level.FINE, false, "WEST: kickback direction EAST");
                    return PlayerOrientation.EAST;
                default:
                    WXTLogger.prettyLog(Level.FINE, false, "No kickback direction found");
                    break;
            }
        }
        
        return null;
    }
    
    /**
     * Get current used Stargate
     * 
     * @return may return null if there was no gate used lately
     */
    public Stargate getStargate() {
        if (!"".equalsIgnoreCase(this.getCurrentGateName()))
            return this.getStargate(this.getCurrentGateName());
        
        return null;
    }
    
    public List<Stargate> getStargates() {
        List<Stargate> stargates = new ArrayList<Stargate>();
        for (Stargate s : stargateMap.values())
            stargates.add(s);
        
        return stargates;
    }
    
    /**
     * Set current used stargate name
     * @param gateName 
     */
    public void setCurrentGateName(String gateName) {
        if (gateName == null)
            gateName = "";
        
        WXTLogger.prettyLog(Level.FINE, false, "Setting current used gateName to '" + gateName + "' for player '" + getName() + "'");
        this.currentGateName = gateName;
    }
    
    public String getCurrentGateName() {
        return this.currentGateName;
    }
    
    /**
     * Get stargate instance for gatename
     * @param gateName the gatename to fetch
     * @return 
     */
    public Stargate getStargate(String gateName) {
        if (this.hasStargate(gateName)) {
            WXTLogger.prettyLog(Level.FINE, false, "Get stargate '" + gateName + "'");
            return stargateMap.get(gateName);
        }
        
        WXTLogger.prettyLog(Level.WARNING, false, "Could not get stargate '" + gateName + "' for player '" + getName() + "'");
        return new Stargate();
    }

    /**
     * Does the Stargate exist in player space?
     * 
     * @param stargate
     * @return false if nothing found
     */
    public boolean hasStargate(Stargate stargate) {
        return this.hasStargate(stargate.getGateName());
    }
    
    /**
     * Does the Stargate exist in player space?
     * 
     * @param gateName
     * @return 
     */
    public boolean hasStargate(String gateName) {
        try {
            if (gateName != null)
                return (stargateMap.containsKey(gateName)) ? true : false;
            
            throw new WormholePlayerNullPointerException("hasStargate checked for null. Can't check for null gateNames!");
        } catch (WormholePlayerNullPointerException e) {
            WXTLogger.prettyLog(Level.SEVERE, true, e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Add a stargate to a player 
     * If the gate already exists in keyring currentGate will be set to passed stargate
     * 
     * @param stargate the stargate instance
     */
    public void addStargate(Stargate stargate) {
        if (this.hasStargate(stargate)) {
            WXTLogger.prettyLog(Level.FINE, false, "Stargate '" + stargate.getGateName() + "' was already added for player '" + getName() + "'");
            this.setCurrentGateName(stargate.getGateName());
            return;
        }
        
        WXTLogger.prettyLog(Level.FINE, false, "Adding Stargate '" + stargate.getGateName() + "' to player '" + getName() + "'");
        stargateMap.put(stargate.getGateName(), stargate);
        this.addProperties(stargate.getGateName());
        this.setCurrentGateName(stargate.getGateName());
    }
    
    private void addProperties(String stargateName) {
        WXTLogger.prettyLog(Level.FINE, false, "Adding properties for gate '" + stargateName + "' to player '" + getName() + "'");
        usageProperties.put(stargateName, new WormholePlayerUsageProperties());
    }

    /**
     * Remove a gate from player object
     * @param stargate the stargate instance
     */
    public void removeStargate(Stargate stargate) {
        try {
            if (stargate != null) {
                this.removeStargate(stargate.getGateName());
                return;
            }
            
            throw new WormholePlayerNullPointerException("Remove Stargate failed. Stargate name was null.");
        } catch (WormholePlayerNullPointerException e) {
            WXTLogger.prettyLog(Level.SEVERE, true, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Remove a gate from player object
     * @param stargateName the gate name
     */
    public void removeStargate(String stargateName) {
        try {
            WXTLogger.prettyLog(Level.FINE, false, "Removing Stargate '" + stargateName + "' from player '" + getName() + "'");
            if (!"".equals(stargateName)) {
                stargateMap.remove(stargateName);
                this.removeProperty(stargateName);
                
                WXTLogger.prettyLog(Level.FINE, false, "StargateMaps count is: '" + this.getGateCount() + "'");
                return;
            }
            
            throw new WormholePlayerEmptyStargateNameException("Stargate name can't be empty. Probably a malfunction during execution.");
        } catch (WormholePlayerEmptyStargateNameException e) {
            WXTLogger.prettyLog(Level.SEVERE, true, e.getMessage());
        }
    }
    
    private void removeProperty(String gateName) {
        WXTLogger.prettyLog(Level.FINE, false, "Removing property for Stargate '" + gateName + "' from player '" + getName() + "'");
        usageProperties.remove(gateName);
    }
    
    public int getGateCount() {
        return stargateMap.keySet().size();
    }
    
    public void sendMessage(String message) {
        if (this.player.isOnline())
            this.getPlayer().sendMessage(message);
    }
}
