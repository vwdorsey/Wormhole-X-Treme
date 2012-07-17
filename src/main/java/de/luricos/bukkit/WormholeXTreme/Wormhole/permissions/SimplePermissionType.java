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
package de.luricos.bukkit.WormholeXTreme.Wormhole.permissions;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The Enum SimplePermissionType.
 * 
 * @author alron
 */
enum SimplePermissionType {

    /** The USE. */
    USE("wormhole.simple.use"),
    /** The BUILD. */
    BUILD("wormhole.simple.build"),
    /** The REMOVE. */
    REMOVE("wormhole.simple.remove"),
    /** The CONFIG. */
    CONFIG("wormhole.simple.config");
    /** The simple permission node. */
    private final String simplePermissionNode;
    /** The Constant simplePermissionMap. */
    private static final Map<String, SimplePermissionType> simplePermissionMap = new HashMap<String, SimplePermissionType>();

    static {
        for (final SimplePermissionType simplePermissionType : EnumSet.allOf(SimplePermissionType.class)) {
            simplePermissionMap.put(simplePermissionType.simplePermissionNode, simplePermissionType);
        }
    }

    /**
     * From simple permission node.
     * 
     * @param simplePermissionNode
     *            the simple permission node
     * @return the simple permission
     */
    public static SimplePermissionType fromSimplePermissionNode(final String simplePermissionNode) // NO_UCD
    {
        return simplePermissionMap.get(simplePermissionNode);
    }

    /**
     * Instantiates a new simple permission.
     * 
     * @param simplePermissionNode
     *            the simple permission node
     */
    private SimplePermissionType(final String simplePermissionNode) {
        this.simplePermissionNode = simplePermissionNode;
    }

    /**
     * Gets the simple permission.
     * 
     * @return the simple permission string
     */
    public String getString() {
        return simplePermissionNode;
    }

    public boolean checkPermission(Player player) {
        return WormholeXTreme.getPermissionManager().has(player, getString());
    }
}
