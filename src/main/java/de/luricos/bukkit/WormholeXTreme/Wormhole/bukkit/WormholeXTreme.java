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
package de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit;

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.GateCommands;
import de.luricos.bukkit.WormholeXTreme.Wormhole.commands.CommandManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.backends.YmlConfigurationSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholeNotAvailable;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.backends.BukkitPermissionsSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.backends.PermissionsExSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * WormholeXtreme for Bukkit.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTreme extends JavaPlugin {

    private ConfigurationManager configManager;
    private CommandManager commandManager;
    private PermissionManager permissionManager;

    /* (non-Javadoc)
     * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
     */
    @Override
    public void onLoad() {
        // init the WXTLogger
        // @TODO Replace Level with config setting
        WXTLogger.initLogger(this.getDescription().getName(), this.getDescription().getVersion(), Level.INFO);

        //this.initConfiguration();

        // send welcome message
        WXTLogger.info("Loading WormholeXTreme ...", true);

        // register Configuration backends
        ConfigurationBackend.registerBackendAlias("bukkit", YmlConfigurationSupport.class);

        WXTLogger.info("Load complete", true);
    }
    
    public boolean reloadPlugin() {
        WXTLogger.info("Reload in progress...", true);
        
        return true;
    }    
    
    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onEnable()
     */
    @Override
    public void onEnable() {

        try {
            // init configManager; backend is set to bukkit by default
            if (this.configManager == null) {
                this.configManager = new ConfigurationManager(this.getConfig());
            }

            WXTLogger.setLogLevel(this.configManager.getLogLevel());

            // register Permission backends
            PermissionBackend.registerBackendAlias("pex", PermissionsExSupport.class);
            PermissionBackend.registerBackendAlias("bukkit", BukkitPermissionsSupport.class);

            // resolve currently used PermissionPlugin
            PermissionBackend.resolvePermissionBackends();

            // init permissionManager; backend is set via config static call
            if (this.permissionManager == null) {
                this.permissionManager = new PermissionManager(this.configManager);
            }

            if (this.commandManager == null) {
                this.commandManager = new CommandManager(this);
            }

            // register commands
            this.commandManager.register(new GateCommands());

        } catch (final Exception e) {
            // @TODO change this behavior to be more error friendly (skip gate instead)
            // Catched when a world is not loaded but a gate is in that world.
            // The plugin would stop working to prevent data corruption (safe-mode)
            WXTLogger.severe(String.format("Caught Exception while trying to load support plugins. {%s}", e.getMessage()));
            e.printStackTrace();
        }


        WXTLogger.info("Boot sequence completed", true);
    }   
    
    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onDisable()
     */
    @Override
    public void onDisable() {

        // end permission backend
        if (this.permissionManager != null) {
            this.permissionManager.end();
        }

        // end config backend
        if (this.configManager != null) {
            this.configManager.end();
        }

    }

    private void initConfiguration() {
        if (this.configManager == null) {
            this.configManager = new ConfigurationManager(this.getConfig());
        }

        //@TODO init backend
        //@TODO save config

        //this.config.options().copyDefaults(true);
        //saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        try {
            PluginDescriptionFile pdf = this.getDescription();
            if (args.length > 0) {
                return this.commandManager.execute(sender, command, args);
            } else {
                if (sender instanceof Player) {
                    sender.sendMessage("[" + ChatColor.RED + "WormholeXTreme" + ChatColor.WHITE + "] version [" + ChatColor.BLUE + pdf.getVersion() + ChatColor.WHITE + "]");

                    return !this.permissionManager.has((Player) sender, "wormhole.manage");
                } else {
                    sender.sendMessage("[WormholeXTreme] version [" + pdf.getVersion() + "]");

                    return false;
                }
            }
        } catch (Throwable t) {
            //ErrorReport.handleError("While " + sender.getName() + " was executing /" + command.getName() + " " + StringUtils.implode(args, " "), t, sender);
            return true;
        }
    }

    /**
     * Get the permissionManager.
     * 
     * @return the PermissionManager
     */
    public static PermissionManager getPermissionManager() {
        try {
            if (!isPluginAvailable()) {
                if (WXTLogger.getLogLevel().intValue() < Level.WARNING.intValue())
                    throw new WormholeNotAvailable("This plugin is not ready yet." + ((!getPlugin().isEnabled()) ? " Loading sequence is still in progress." : ""));
            }
        } catch (WormholeNotAvailable e) {
            WXTLogger.warn(e.getMessage());
        }

        return ((WormholeXTreme) getPlugin()).permissionManager;
    }

    /**
     * Get the configurationManager.
     *
     * @return the ConfiguratioManager
     */
    public static ConfigurationManager getConfigManager() {
        try {
            if (!isPluginAvailable()) {
                if (WXTLogger.getLogLevel().intValue() < Level.WARNING.intValue())
                    throw new WormholeNotAvailable("This plugin is not ready yet." + ((!getPlugin().isEnabled()) ? " Loading sequence is still in progress." : ""));
            }
        } catch (WormholeNotAvailable e) {
            WXTLogger.warn(e.getMessage());
        }

        return ((WormholeXTreme) getPlugin()).configManager;
    }



    /**
     * Gets the plugin.
     * 
     * @return the plugin instance
     */
    public static WormholeXTreme getPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WormholeXTreme");
        if (plugin == null || !(plugin instanceof WormholeXTreme)) {
            throw new RuntimeException("'WormholeXTreme' not found. 'WormholeXTreme' plugin disabled?");
        }

        return ((WormholeXTreme) plugin);
    }

    /**
     * Register commands.
     */
    public static void registerCommands() {

    }

    /**
     * Register events.
     */
    public void registerEvents(boolean critical) {

    }

    public static boolean isPluginAvailable() {
        Plugin plugin = getPlugin();
        if (plugin == null) {
            WXTLogger.severe("Cound not fetch plugin instance!");
            return false;
        }

        return ((WormholeXTreme) plugin).permissionManager != null;
    }
}
