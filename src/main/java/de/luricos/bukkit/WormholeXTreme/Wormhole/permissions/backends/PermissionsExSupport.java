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
package de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.backends;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Level;

/**
 * The Class PermissionsExSupport.
 *
 * Add features for PermissionsEx implementation
 * 
 * @author lycano
 */

public class PermissionsExSupport extends PermissionBackend {

    protected PermissionManager pexManager;

    public PermissionsExSupport(de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionManager manager, ConfigManager configManager) {
        super(manager, configManager);
    }

    @Override
    public void initialize() {
        if (!ConfigManager.getPermissionsSupportDisable()) {
            if (WormholeXTreme.getPermissionManager() == null) {
                final Plugin test = WormholeXTreme.getThisPlugin().getServer().getPluginManager().getPlugin("PermissionsEx");
                if ((test != null) && (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx"))) {
                    final String v = test.getDescription().getVersion();
                    checkPermissionsVersion(v);
                    try {
                        pexManager = PermissionsEx.getPermissionManager();
                        WXTLogger.prettyLog(Level.INFO, false, "Attached to PermissionsEx version " + v);
                        if (ConfigManager.getSimplePermissions()) {
                            WXTLogger.prettyLog(Level.INFO, false, "Simple Permissions Enabled");
                        } else {
                            WXTLogger.prettyLog(Level.INFO, false, "Complex Permissions Enabled");
                        }
                    } catch (final ClassCastException e) {
                        WXTLogger.prettyLog(Level.WARNING, false, "Failed to get Permissions Handler. Defaulting to built-in permissions.");
                    }
                } else {
                    WXTLogger.prettyLog(Level.INFO, false, "Permission Plugin not yet available. Defaulting to built-in permissions until Permissions is loaded.");
                }
            }
        } else {
            WXTLogger.prettyLog(Level.INFO, false, "Permission Plugin support disabled via settings.txt.");
        }
    }

    @Override
    public void reload() {
        WXTLogger.prettyLog(Level.INFO, false, "Detached from Permissions plugin.");
    }

    /**
     * Check permissions version.
     *
     * @param version
     *            the version
     */
    private static void checkPermissionsVersion(String version) {
        if (!isSupportedVersion(version)) {
            WXTLogger.prettyLog(Level.WARNING, false, "Not supported version of PermissionsEx. Recommended is at least 1.18");
        }
    }

    public static boolean isSupportedVersion(String verIn) {
        return isSupportedVersion(verIn, 1.18);
    }

    public static boolean isSupportedVersion(String verIn, Double checkVer) {
        String comp1 = verIn.replaceAll("\\.", "");
        int subVCount = verIn.length() - comp1.length();

        if ((subVCount < 2) && (Double.parseDouble(verIn) >= checkVer))
            return true;

        if ((subVCount < 2) && (Double.parseDouble(verIn) < checkVer))
            return false;

        int firstMatch = verIn.indexOf(".");
        String verOut = verIn.substring(0, firstMatch) + "." + comp1.substring(firstMatch);

        return Double.parseDouble(verOut) >= checkVer;
    }

    @Override
    public boolean hasPermission(Player player, String permissionString) {
        return pexManager.has(player, permissionString);
    }
}
