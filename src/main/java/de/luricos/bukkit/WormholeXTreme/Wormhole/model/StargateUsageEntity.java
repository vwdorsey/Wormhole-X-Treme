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

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.player.PlayerOrientation;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 *
 * @author lycano
 */
public class StargateUsageEntity {
    private Player player = null;
    private StargateUsageEntityPermission permission = new StargateUsageEntityPermission();
    
    public StargateUsageEntity() {
    }
    
    public StargateUsageEntity(Player player) {
        this.player = player;
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
     * Get states of the current player
     * 
     * @return StargateUsageEntity
     */
    public StargateUsageEntityPermission getState() {
        return this.permission;
    }
    
    /**
     * Entity is human?
     * 
     * @return
     */
    public boolean isHuman() {
        if (player != null)
            return true;
        
        return false;
    }
    
    /**
     * Get the player's cardinal direction (N, W, NW, etc.).
     *
     * @return null if nothing found
     */
    public PlayerOrientation getCardinalDirection() {
        if (!this.isHuman())
            return null;
        
        double rotation = (this.getPlayer().getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        
        return this.getDirection(rotation);
    }

    /**
     * Returns direction according to rotation.
     * 
     * @param rotation
     * @return null if nothing found
     */
    private PlayerOrientation getDirection(double rotation) {
        if (0 <= rotation && rotation < 22.5) {
            return PlayerOrientation.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return PlayerOrientation.NORTH_EAST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return PlayerOrientation.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return PlayerOrientation.SOUTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return PlayerOrientation.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return PlayerOrientation.SOUTH_WEST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return PlayerOrientation.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return PlayerOrientation.NORTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return PlayerOrientation.NORTH;
        } else {
            return null;
        }
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
        if ((this.isHuman()) && ((direction != null) || (facing != null))) {
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
     * Get the name of the player
     * 
     * @return String playerName null if no human entity was found
     */
    public String getName() {
        if (this.isHuman())
            return this.player.getName();

        return null;
    }
}
