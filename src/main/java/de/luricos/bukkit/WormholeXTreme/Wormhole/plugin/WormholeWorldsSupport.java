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
package de.luricos.bukkit.WormholeXTreme.Wormhole.plugin;

import de.luricos.bukkit.WormholeXTreme.Worlds.WormholeXTremeWorlds;
import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateDBManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * The Class WormholeWorldsSupport.
 * 
 * @author alron
 */
public class WormholeWorldsSupport {

    /**
     * Disable wormhole worlds.
     */
    public static void disableWormholeWorlds() {
        if (WormholeXTreme.getWorldHandler() != null) {
            WormholeXTreme.setWorldHandler(null);
            WXTLogger.prettyLog(Level.INFO, false, "Detached from Wormhole Worlds plugin.");
        }
    }

    /**
     * Enable wormhole worlds.
     */
    public static void enableWormholeWorlds() {
        enableWormholeWorlds(false);
    }
    
    public static void enableWormholeWorlds(boolean reload) {
        if (ConfigManager.isWormholeWorldsSupportEnabled()) {
            if (!WormholeWorldsSupport.isEnabled()) {
                final Plugin worldsTest = Bukkit.getServer().getPluginManager().getPlugin("WormholeXTremeWorlds");
                if (worldsTest != null) {
                    final String version = worldsTest.getDescription().getVersion();
                    if (checkWorldsVersion(version)) {
                        try {
                            WormholeXTreme.setWorldHandler(WormholeXTremeWorlds.getWorldHandler());
                            WXTLogger.prettyLog(Level.INFO, false, "Attached to Wormhole Worlds version " + version);

                            // Worlds support means we can continue our load.
                            StargateDBManager.loadStargates(Bukkit.getServer());
                            
                            if (!reload) {
                                WormholeXTreme.registerEvents(false);
                                WormholeXTreme.registerCommands();
                            }
                            
                            WXTLogger.prettyLog(Level.INFO, true, "Enable Completed.");
                        } catch (final ClassCastException e) {
                            WXTLogger.prettyLog(Level.WARNING, false, "Failed to get cast to Wormhole Worlds: " + e.getMessage());
                        }
                    }
                } else {
                    WXTLogger.prettyLog(Level.INFO, false, "Wormhole Worlds Plugin not yet available Stargates will not load until it enables.");
                }
            } else {
                WXTLogger.prettyLog(Level.INFO, false, "Wormhole Worlds Plugin not yet available Stargates will not load until it enables.");
            }
        } else {
            WXTLogger.prettyLog(Level.INFO, false, "Wormhole X-Treme Worlds Plugin support disabled via settings.txt.");
        }
    }

    /**
     * Check worlds version.
     *
     * @param version the version
     * @return true, if successful
     */
    private static boolean checkWorldsVersion(String version) {
        if (!isSupportedVersion(version)) {
            WXTLogger.prettyLog(Level.SEVERE, false, "Not a supported version of WormholeXTreme-Worlds. Recommended is > 0.507");
            return false;
        }

        return true;
    }

    public static boolean isSupportedVersion(String verIn) {
        return isSupportedVersion(verIn, 0.507);
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
    
    public static boolean isEnabled() {
        return WormholeXTreme.getWorldHandler() != null;
    }
}