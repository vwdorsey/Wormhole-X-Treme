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

/**
 * The Enum ComplexPermissionType.
 * 
 * @author alron
 */
enum ComplexPermissionType {
//
//    /** Sign Dialer Use */
//    USE_SIGN("wormhole.use.sign"),
//    /** Normal Dial Use */
//    USE_DIALER("wormhole.use.dialer"),
//    /** Compass Use */
//    USE_COMPASS("wormhole.use.compass"),
//    /** Use Cooldown Group One */
//    USE_COOLDOWN_GROUP_ONE("wormhole.cooldown.groupone"),
//    /** Use Cooldown Group two. */
//    USE_COOLDOWN_GROUP_TWO("wormhole.cooldown.grouptwo"),
//    /** Use Cooldown Group three. */
//    USE_COOLDOWN_GROUP_THREE("wormhole.cooldown.groupthree"),
//    /** Remove Own */
//    REMOVE_OWN("wormhole.remove.own"),
//    /** Remove All */
//    REMOVE_ALL("wormhole.remove.all"),
//    /** Build */
//    BUILD("wormhole.build"),
//    /** Build Restriction Group one. */
//    BUILD_RESTRICTION_GROUP_ONE("wormhole.build.groupone"),
//    /** Build Restriction Group two. */
//    BUILD_RESTRICTION_GROUP_TWO("wormhole.build.grouptwo"),
//    /** Build Restriction Group three. */
//    BUILD_RESTRICTION_GROUP_THREE("wormhole.build.groupthree"),
//    /** Config */
//    CONFIG("wormhole.config"),
//    /** List */
//    LIST("wormhole.list"),
//    /** Use Network */
//    NETWORK_USE("wormhole.network.use."),
//    /** Build Network. */
//    NETWORK_BUILD("wormhole.network.build."),
//    /** Go. */
//    GO("wormhole.go");
//    /** The complex permission node. */
//    private final String complexPermissionNode;
//    /** The Constant complexPermissionMap. */
//    private static final Map<String, ComplexPermissionType> complexPermissionMap = new HashMap<>();
//
//    static {
//        for (final ComplexPermissionType type : EnumSet.allOf(ComplexPermissionType.class)) {
//            complexPermissionMap.put(type.complexPermissionNode, type);
//        }
//    }
//
//    /**
//     * From complex permission node.
//     *
//     * @param complexPermissionNode
//     *            the complex permission node
//     * @return the complex permission
//     */
//    public static ComplexPermissionType fromComplexPermissionNode(final String complexPermissionNode) // NO_UCD
//    {
//        return complexPermissionMap.get(complexPermissionNode);
//    }
//
//    /**
//     * Instantiates a new complex permission.
//     *
//     * @param complexPermissionNode
//     *            the complex permission node
//     */
//    private ComplexPermissionType(final String complexPermissionNode) {
//        this.complexPermissionNode = complexPermissionNode;
//    }
//
//    /**
//     * Check permission.
//     *
//     * @param player
//     *            the player
//     * @return true, if successful
//     */
//    protected boolean checkPermission(final Player player) {
//        return checkPermission(player, null, null);
//    }
//
//    /**
//     * Check permission.
//     *
//     * @param player
//     *            the player
//     * @param stargate
//     *            the stargate
//     * @return true, if successful
//     */
//    protected boolean checkPermission(final Player player, final Stargate stargate) {
//        return checkPermission(player, stargate, null);
//    }
//
//    /**
//     * Check permission.
//     *
//     * @param player the player
//     * @param stargate the gate
//     * @param networkName the network name
//     * @return true, if successful
//     */
//    public boolean checkPermission(final Player player, final Stargate stargate, final String networkName) {
//        if ((player != null) && (WormholeXTreme.getPermissionManager() != null) && !ConfigManager.getSimplePermissions()) {
//            boolean allowed = false;
//
//            switch (this) {
//                case NETWORK_USE:
//                case NETWORK_BUILD:
//                    allowed = networkName != null && WormholeXTreme.getPermissionManager().has(player, getString() + networkName);
//                    break;
//                case REMOVE_OWN:
//                    allowed = ((stargate != null) && (stargate.getGateOwner() != null) && stargate.getGateOwner().equals(player.getName()) && WormholeXTreme.getPermissionManager().has(player, complexPermissionNode));
//                    break;
//                default:
//                    allowed = WormholeXTreme.getPermissionManager().has(player, getString());
//                    break;
//            }
//
//            if (allowed) {
//                WXTLogger.prettyLog(Level.FINE, false, "Player: '" + player.getName() + "' granted complex \"" + toString() + "\" permission" + (networkName != null
//                        ? " on network \"" + networkName + "\""
//                        : "") + ".");
//                return true;
//            }
//
//            WXTLogger.prettyLog(Level.FINE, false, "Player: '" + player.getName() + "' denied complex \"" + toString() + "\" permission" + (networkName != null
//                    ? " on network \"" + networkName + "\""
//                    : "") + ".");
//        }
//        return false;
//    }
//
//    /**
//     * Check permission.
//     *
//     * @param player
//     *            the player
//     * @param networkName
//     *            the network name
//     * @return true, if successful
//     */
//    protected boolean checkPermission(final Player player, final String networkName) {
//        return checkPermission(player, null, networkName);
//    }
//
//    /**
//     * Gets the complex permission.
//     *
//     * @return the complex permission string
//     */
//    public String getString() {
//        return complexPermissionNode;
//    }
}
