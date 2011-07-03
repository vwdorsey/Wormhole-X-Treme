/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.utils;

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author luricos
 */
public class WXTLogger {
    private static Level logLevel = Level.INFO;
    private static Logger logger = null;
    private static String logPluginName = null;
    private static String logPluginVersion = null;
    
    public static void initLogger(String pluginName, String pluginVersion, Level logLevel) {
        if (WXTLogger.logger == null) {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
            if (plugin != null) {
                WXTLogger.logger = Logger.getLogger(plugin.getServer().getLogger().getName() + "." + pluginName);
            }
            
            WXTLogger.logLevel = logLevel;
            WXTLogger.logger.setLevel(logLevel);
            WXTLogger.logPluginName = pluginName;
            WXTLogger.logPluginVersion = pluginVersion;
        }
    }
    
    public static void setLogLevel(Level logLevel) {
        WXTLogger.logLevel = logLevel;
        WXTLogger.logger.setLevel(logLevel);
    }
    
    public static void prettyLog(final Level logLevel, final boolean version, final String message) {
        final String prettyName = ("[" + getName() + "]");
        final String prettyVersion = ("[v" + getVersion() + "]");
        String prettyLogLine = prettyName;
        if (version) {
            prettyLogLine += prettyVersion;
        }
        
        logger.log(logLevel, prettyLogLine + " " + message);
    }
    
    public static Level getLogLevel() {
        return logLevel;
    }
    
    public static String getVersion() {
        return logPluginVersion;
    }
    
    public static String getName() {
        return logPluginName;
    }
}
