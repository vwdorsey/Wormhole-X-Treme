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
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionsManager.PermissionLevel;

import org.bukkit.entity.Player;

/**
 * The Class WXPermissions.
 * 
 * @author alron
 */
public class WXPermissions {

    /**
     * The Enum PermissionType.
     */
    public static enum PermissionType {

        /** The DAMAGE permission. */
        DAMAGE,
        /** The SIGN permission. */
        SIGN,
        /** The DIALER permission. */
        DIALER,
        /** The BUILD permission. */
        BUILD,
        /** The REMOVE permission. */
        REMOVE,
        /** The USE permission. */
        USE,
        /** The LIST permission. */
        LIST,
        /** The CONFIG permission. */
        CONFIG,
        /** The GO permission. */
        GO,
        /** The COMPASS permission. */
        COMPASS,
        USE_COOLDOWN_GROUP_ONE,
        USE_COOLDOWN_GROUP_TWO,
        USE_COOLDOWN_GROUP_THREE,
        BUILD_RESTRICTION_GROUP_ONE,
        BUILD_RESTRICTION_GROUP_TWO,
        BUILD_RESTRICTION_GROUP_THREE
    }

    /**
     * Check wx permissions.
     * 
     * @param player
     *            the player
     * @param permissiontype
     *            the permissiontype
     * @return true, if successful
     */
    public static boolean checkPermission(final Player player, final PermissionType permissiontype) {
        return checkPermission(player, null, null, permissiontype);
    }

    /**
     * Check wx permisssions.
     * 
     * @param player
     *            the player
     * @param stargate
     *            the stargate
     * @param permissionType
     *            the permissionType
     * @return true, if successful
     */
    public static boolean checkPermission(final Player player, final Stargate stargate, final PermissionType permissionType) {
        return checkPermission(player, stargate, null, permissionType);
    }

    /**
     * Check wx permissions.
     * 
     * @param player
     *            the player
     * @param stargate
     *            the stargate
     * @param network
     *            the network
     * @param permissionType
     *            the permissionType
     * @return true, if successful
     */
    private static boolean checkPermission(final Player player, final Stargate stargate, final String network, final PermissionType permissionType) {
        if (player == null) {
            return false;
        }
        if (player.isOp()) {
            switch (permissionType) {
                case DAMAGE:
                case REMOVE:
                case CONFIG:
                case GO:
                case SIGN:
                case DIALER:
                case USE:
                case LIST:
                case COMPASS:
                case BUILD:
                    return true;
                default:
                    return false;
            }
        } else if (!ConfigManager.getPermissionsSupportDisable() && (WormholeXTreme.getPermissionManager() != null)) {

            if (ConfigManager.getSimplePermissions()) {
                switch (permissionType) {
                    case LIST:
                        return (SimplePermissionType.CONFIG.checkPermission(player) || SimplePermissionType.USE.checkPermission(player));
                    case GO:
                    case CONFIG:
                        return SimplePermissionType.CONFIG.checkPermission(player);
                    case DAMAGE:
                    case REMOVE:
                        return (SimplePermissionType.REMOVE.checkPermission(player) || SimplePermissionType.CONFIG.checkPermission(player));
                    case COMPASS:
                    case SIGN:
                    case DIALER:
                    case USE:
                        return SimplePermissionType.USE.checkPermission(player);
                    case BUILD:
                        return SimplePermissionType.BUILD.checkPermission(player);
                    default:
                        return false;
                }
            } else {
                String networkName = "Public";
                switch (permissionType) {
                    case LIST:
                        return (ComplexPermissionType.LIST.checkPermission(player) || ComplexPermissionType.CONFIG.checkPermission(player));
                    case CONFIG:
                        return ComplexPermissionType.CONFIG.checkPermission(player);
                    case GO:
                        return ComplexPermissionType.GO.checkPermission(player);
                    case COMPASS:
                        return ComplexPermissionType.USE_COMPASS.checkPermission(player);
                    case DAMAGE:
                    case REMOVE:
                        return (ComplexPermissionType.CONFIG.checkPermission(player) || ComplexPermissionType.REMOVE_ALL.checkPermission(player) || ComplexPermissionType.REMOVE_OWN.checkPermission(player, stargate));
                    case SIGN:
                        if ((stargate != null) && (stargate.getGateNetwork() != null)) {
                            networkName = stargate.getGateNetwork().getNetworkName();
                        }
                        return ((ComplexPermissionType.USE_SIGN.checkPermission(player) && (networkName.equals("Public") || (!networkName.equals("Public") && ComplexPermissionType.NETWORK_USE.checkPermission(player, networkName)))));
                    case DIALER:
                        if ((stargate != null) && (stargate.getGateNetwork() != null)) {
                            networkName = stargate.getGateNetwork().getNetworkName();
                        }
                        return ((ComplexPermissionType.USE_DIALER.checkPermission(player) && (networkName.equals("Public") || (!networkName.equals("Public") && ComplexPermissionType.NETWORK_USE.checkPermission(player, networkName)))));
                    case USE:
                        if ((stargate != null) && (stargate.getGateNetwork() != null)) {
                            networkName = stargate.getGateNetwork().getNetworkName();
                        }
                        return (((ComplexPermissionType.USE_SIGN.checkPermission(player) && (networkName.equals("Public") || (!networkName.equals("Public") && ComplexPermissionType.NETWORK_USE.checkPermission(player, networkName)))) || (ComplexPermissionType.USE_DIALER.checkPermission(player) && (networkName.equals("Public") || (!networkName.equals("Public") && ComplexPermissionType.NETWORK_USE.checkPermission(player, networkName))))));
                    case BUILD:
                        if (stargate != null) {
                            if (stargate.getGateNetwork() != null) {
                                networkName = stargate.getGateNetwork().getNetworkName();
                            }
                        } else {
                            if (network != null) {
                                networkName = network;
                            }
                        }
                        return ((ComplexPermissionType.BUILD.checkPermission(player) && (networkName.equals("Public") || (!networkName.equals("Public") && ComplexPermissionType.NETWORK_BUILD.checkPermission(player, networkName)))));
                    case USE_COOLDOWN_GROUP_ONE:
                        return ComplexPermissionType.USE_COOLDOWN_GROUP_ONE.checkPermission(player);
                    case USE_COOLDOWN_GROUP_TWO:
                        return ComplexPermissionType.USE_COOLDOWN_GROUP_TWO.checkPermission(player);
                    case USE_COOLDOWN_GROUP_THREE:
                        return ComplexPermissionType.USE_COOLDOWN_GROUP_THREE.checkPermission(player);
                    case BUILD_RESTRICTION_GROUP_ONE:
                        return ComplexPermissionType.BUILD_RESTRICTION_GROUP_ONE.checkPermission(player);
                    case BUILD_RESTRICTION_GROUP_TWO:
                        return ComplexPermissionType.BUILD_RESTRICTION_GROUP_TWO.checkPermission(player);
                    case BUILD_RESTRICTION_GROUP_THREE:
                        return ComplexPermissionType.BUILD_RESTRICTION_GROUP_THREE.checkPermission(player);
                    default:
                        return false;
                }
            }
        } else {
            if (stargate != null) {
                PermissionLevel lvl = null;
                switch (permissionType) {
                    case DAMAGE:
                    case REMOVE:
                    case CONFIG:
                    case GO:
                        lvl = PermissionsManager.getPermissionLevel(player, stargate);
                        return (lvl == PermissionLevel.WORMHOLE_FULL_PERMISSION);
                    case SIGN:
                    case DIALER:
                    case USE:
                    case LIST:
                    case COMPASS:
                        lvl = PermissionsManager.getPermissionLevel(player, stargate);
                        return (lvl == PermissionLevel.WORMHOLE_CREATE_PERMISSION) || (lvl == PermissionLevel.WORMHOLE_USE_PERMISSION) || (lvl == PermissionLevel.WORMHOLE_FULL_PERMISSION);
                    case BUILD:
                        lvl = PermissionsManager.getPermissionLevel(player, stargate);
                        return (lvl == PermissionLevel.WORMHOLE_CREATE_PERMISSION) || (lvl == PermissionLevel.WORMHOLE_FULL_PERMISSION);
                    default:
                        return false;

                }
            }
        }
        return false;
    }

    /**
     * Check wx permissions.
     * 
     * @param player
     *            the player
     * @param network
     *            the network
     * @param permissiontype
     *            the permissiontype
     * @return true, if successful
     */
    public static boolean checkPermission(final Player player, final String network, final PermissionType permissiontype) {
        return checkPermission(player, null, network, permissiontype);
    }
}
