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

import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Player-direction
 * 
 * @author sk89q, lycano
 */
public enum PlayerOrientation {
    NORTH("North", (new Vector(-1, 0, 0)), (new Vector(0, 0, 1)), true),
    NORTH_EAST("Northeast", (new Vector(-1, 0, -1)).normalize(), (new Vector(-1, 0, 1)).normalize(), false),
    EAST("East", (new Vector(0, 0, -1)), (new Vector(-1, 0, 0)), true),
    SOUTH_EAST("Southeast", (new Vector(1, 0, -1)).normalize(), (new Vector(-1, 0, -1)).normalize(), false),
    SOUTH("South", (new Vector(1, 0, 0)), (new Vector(0, 0, -1)), true),
    SOUTH_WEST("Southwest", (new Vector(1, 0, 1)).normalize(), (new Vector(1, 0, -1)).normalize(), false),
    WEST("West", (new Vector(0, 0, 1)), (new Vector(1, 0, 0)), true),
    NORTH_WEST("Northwest", (new Vector(-1, 0, 1)).normalize(), (new Vector(1, 0, 1)).normalize(), false);
    
    private String name;
    private Vector direction;
    private Vector leftDirection;
    private boolean isOrthogonal;
    
    private static final Map<String, PlayerOrientation> mapping = new HashMap<String, PlayerOrientation>();

    static {
        for (PlayerOrientation pd : EnumSet.allOf(PlayerOrientation.class)) {
            mapping.put(pd.name, pd);
        }
    }    
    
    private PlayerOrientation(String name, Vector direction, Vector leftDirection, boolean isOrthogonal) {
        this.name = name;
        this.direction = direction;
        this.leftDirection = leftDirection;
        this.isOrthogonal = isOrthogonal;
    }    
    
    /**
     * Get facing name
     * 
     * @return 
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * get vector
     * @return 
     */
    public Vector getVector() {
        return direction;
    }
    
    /**
     * get left vector
     * @return 
     */
    public Vector leftVector() {
        return leftDirection;
    }
    
    /**
     * if facing orthogonal
     * 
     * @return 
     */
    public boolean isOrthogonal() {
        return isOrthogonal;
    }
    
    /**
     * Get PlayerOrientation by case-insensitive name
     * 
     * @param name North, Northeast, East, Southeast, South, Southwest, West, Northwest
     * @return PlayerOrientation
     */
    public static PlayerOrientation byCaseInsensitiveName(String name) {
        return mapping.get(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
    }

    /**
     * Get PlayerOrientation by name
     * 
     * @param name North, Northeast, East, Southeast, South, Southwest, West, Northwest
     * @return PlayerOrientation
     */
    public static PlayerOrientation byName(String name) {
        return mapping.get(name);
    }
    
    /**
     * Get PlayerOrientation by facing
     * @param PlayerOrientation NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
     * @return PlayerOrientation
     */
    public static PlayerOrientation byDirection(PlayerOrientation facing) {
        return mapping.get(facing.name());
    }
}
