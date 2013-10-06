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

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * The Class PermissionsExSupport.
 *
 * Add features for PermissionsEx implementation
 * 
 * @author lycano
 */

public class PermissionsExSupport extends PermissionBackend {

    protected ru.tehkode.permissions.PermissionManager provider = null;

    public PermissionsExSupport(PermissionManager manager, ConfigurationManager configManager, String providerName) {
        super(manager, configManager, providerName);
    }

    @Override
    public void initialize() {
        if (!(WormholeXTreme.getPermissionManager() == null)) {
            return;
        }

        Plugin testPlugin = Bukkit.getServer().getPluginManager().getPlugin(getProviderName());
        if ((testPlugin != null) && (Bukkit.getServer().getPluginManager().isPluginEnabled(getProviderName()))) {
            final String version = testPlugin.getDescription().getVersion();
            checkPermissionsVersion(version);

            try {
                provider = PermissionsEx.getPermissionManager();
                WXTLogger.info(String.format("Attached to %s version %s", providerName, version));
            } catch (final ClassCastException e) {
                WXTLogger.info("Failed to get Permissions Handler. Defaulting to built-in permissions.");
            }
        } else {
            WXTLogger.info("Permission Plugin not yet available. Defaulting to built-in permissions until Permissions is loaded.");
        }
    }

    @Override
    public void reload() {
        this.end();
        this.initialize();
    }

    public void end() {
        provider = null;
        WXTLogger.info(String.format("Detached from Permissions plugin '%s'.", getProviderName()));

    }

    /**
     * Check permissions version.
     *
     * @param version
     *            the version
     */
    private static void checkPermissionsVersion(String version) {
        if (!isSupportedVersion(version)) {
            WXTLogger.warn("Not supported version. Recommended is at least 1.18");
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
        return provider.has(player, permissionString);
    }
}
