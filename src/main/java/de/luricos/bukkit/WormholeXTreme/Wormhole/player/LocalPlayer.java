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

import org.bukkit.entity.Player;

/**
 *
 * @author lycano
 */
public abstract class LocalPlayer {
    protected Player player = null;
    
    protected LocalPlayer(Player player) {
        this.player = player;
    }
    
    public abstract String getName();
    
    public abstract String getDisplayName();
    
    public boolean isOnline() {
        return this.player.isOnline();

    }
    
    /**
     * Get the player's cardinal direction (N, W, NW, etc.).
     *
     * @return null if nothing found
     */
    public PlayerOrientation getCardinalDirection() {
        double rotation = (this.player.getLocation().getYaw() - 90) % 360;
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
}
