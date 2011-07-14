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
package de.luricos.bukkit.WormholeXTreme.Wormhole;

import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremeServerListener;
import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremeRedstoneListener;
import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremeVehicleListener;
import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremeBlockListener;
import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremeEntityListener;
import de.luricos.bukkit.WormholeXTreme.Wormhole.listeners.WormholeXTremePlayerListener;
import de.luricos.bukkit.WormholeXTreme.Worlds.handler.WorldHandler;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Build;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Compass;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Complete;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Dial;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Force;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Go;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.WXIDC;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.WXList;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.WXRemove;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.Wormhole;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.WXReload;
import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.commands.WXStatus;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.Configuration;
import de.luricos.bukkit.WormholeXTreme.Wormhole.logic.StargateHelper;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.Stargate;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateDBManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionsManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.plugin.HelpSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.plugin.PermissionsSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.plugin.WormholeWorldsSupport;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.DBUpdateUtil;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import me.taylorkelly.help.Help;

import com.nijiko.permissions.PermissionHandler;
import de.luricos.bukkit.WormholeXTreme.Wormhole.player.WormholePlayerManager;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * WormholeXtreme for Bukkit.
 * 
 * @author Ben Echols (Lologarithm)
 * @author Dean Bailey (alron)
 */
public class WormholeXTreme extends JavaPlugin {

    /** listeners **/
    private static final WormholeXTremePlayerListener playerListener = new WormholeXTremePlayerListener();
    private static final WormholeXTremeBlockListener blockListener = new WormholeXTremeBlockListener();
    private static final WormholeXTremeVehicleListener vehicleListener = new WormholeXTremeVehicleListener();
    private static final WormholeXTremeEntityListener entityListener = new WormholeXTremeEntityListener();
    private static final WormholeXTremeServerListener serverListener = new WormholeXTremeServerListener();
    private static final WormholeXTremeRedstoneListener redstoneListener = new WormholeXTremeRedstoneListener();
    
    /** plugins **/
    private static PermissionHandler permissions = null;
    private static Help help = null;
    
    /** The wormhole x treme worlds. */
    private static WorldHandler worldHandler = null;
    
    /** The Scheduler. */
    private static BukkitScheduler scheduler = null;

    /* (non-Javadoc)
     * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
     */
    @Override
    public void onLoad() {
        // init the WXTLogger
        WXTLogger.initLogger(this.getDescription().getName(), this.getDescription().getVersion(), ConfigManager.getLogLevel());        

        // send welcome message
        WXTLogger.prettyLog(Level.INFO, true, "Loading WormholeXTreme ...");
        
        // set scheduler
        WormholeXTreme.setScheduler(this.getServer().getScheduler());
        
        // Load our config files and set logging level right away.
        ConfigManager.setupConfigs(this.getDescription());
        
        // set logging level after loading config
        WXTLogger.setLogLevel(ConfigManager.getLogLevel());
        
        // Make sure DB is up to date with latest SCHEMA
        DBUpdateUtil.updateDB();
        
        // Load our shapes, stargates, and internal permissions.
        StargateHelper.loadShapes();
        
        WXTLogger.prettyLog(Level.INFO, true, "Load complete");
    }
    
    public boolean reloadPlugin() {
        WXTLogger.prettyLog(Level.INFO, true, "Reload in progress...");
        
        // save all gates to sql and save config
        try {
            Configuration.writeFile(getDescription());
            final ArrayList<Stargate> gates = StargateManager.getAllGates();
            // Store all our gates
            for (final Stargate gate : gates) {
                if (gate.isGateActive() || gate.isGateLightsActive()) {
                    gate.shutdownStargate(false);
                }
                StargateDBManager.stargateToSQL(gate);
            }

            WXTLogger.prettyLog(Level.INFO, true, "Configuration written and stargates saved.");
        } catch (final Exception e) {
            WXTLogger.prettyLog(Level.SEVERE, false, "Caught exception while reloading: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // shutdown db
        StargateDBManager.shutdown();
        
        // clear wormholePlayers
        WormholePlayerManager.unregisterAllPlayers();
        
        // disconnect from Worlds
        WormholeWorldsSupport.disableWormholeWorlds();
        
        // load config
        ConfigManager.setupConfigs(this.getDescription());
        
        // set logging level after loading config
        WXTLogger.setLogLevel(ConfigManager.getLogLevel());
        
        // reload stargate shapes
        StargateHelper.reloadShapes();
        
        // Try and attach to Permissions and iConomy and Help
        if (!ConfigManager.isWormholeWorldsSupportEnabled()) {
            WXTLogger.prettyLog(Level.INFO, true, "Wormhole Worlds support disabled in settings.txt, loading stargates and worlds ourself.");
            StargateDBManager.loadStargates(this.getServer());
        }

        // enable support if configured
        WormholeWorldsSupport.enableWormholeWorlds(true);
        
        // register all players
        WormholePlayerManager.registerAllOnlinePlayers();
        
        WXTLogger.prettyLog(Level.INFO, true, "Reloading complete.");
        return true;
    }    
    
    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onEnable()
     */
    @Override
    public void onEnable() {
       WXTLogger.prettyLog(Level.INFO, true, "Boot sequence initiated...");
        
        // Try and attach to Permissions and iConomy and Help
        if (!ConfigManager.isWormholeWorldsSupportEnabled()) {
            WXTLogger.prettyLog(Level.INFO, true, "Wormhole Worlds support disabled in settings.txt, loading stargates and worlds ourself.");
            StargateDBManager.loadStargates(this.getServer());
        }
        
        PermissionsManager.loadPermissions();

        try {
            PermissionsSupport.enablePermissions();
            HelpSupport.enableHelp();
            if (ConfigManager.isWormholeWorldsSupportEnabled()) {
                WormholeWorldsSupport.enableWormholeWorlds();
            }
        } catch (final Exception e) {
            WXTLogger.prettyLog(Level.WARNING, false, "Caught Exception while trying to load support plugins." + e.getMessage());
            e.printStackTrace();
        }
        
        registerEvents(true);
        HelpSupport.registerHelpCommands();
        if (!ConfigManager.isWormholeWorldsSupportEnabled()) {
            registerEvents(false);
            registerCommands();
        }
        
        // register all online players onenable/onreload
        WormholePlayerManager.registerAllOnlinePlayers();
        
        WXTLogger.prettyLog(Level.INFO, true, "Boot sequence completed");
    }   
    
    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onDisable()
     */
    @Override
    public void onDisable() {
        WXTLogger.prettyLog(Level.INFO, true, "Shutdown sequence initiated...");
        
        try {
            Configuration.writeFile(getDescription());
            final ArrayList<Stargate> gates = StargateManager.getAllGates();
            // Store all our gates
            for (final Stargate gate : gates) {
                if (gate.isGateActive() || gate.isGateLightsActive()) {
                    gate.shutdownStargate(false);
                }
                StargateDBManager.stargateToSQL(gate);
            }

            StargateDBManager.shutdown();
            
            // clear wormholePlayers
            WormholePlayerManager.unregisterAllPlayers();            
            
            WXTLogger.prettyLog(Level.INFO, true, "Successfully shutdown WXT.");
        } catch (final Exception e) {
            WXTLogger.prettyLog(Level.SEVERE, false, "Caught exception while shutting down: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the help.
     * 
     * @return the help
     */
    public static Help getHelp() {
        return help;
    }

    /**
     * Gets the permissions.
     * 
     * @return the permissions
     */
    public static PermissionHandler getPermissions() {
        return permissions;
    }

    /**
     * Gets the scheduler.
     * 
     * @return the scheduler
     */
    public static BukkitScheduler getScheduler() {
        return scheduler;
    }

    /**
     * Gets the plugin.
     * 
     * @return the plugin instance
     */
    public static WormholeXTreme getThisPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WormholeXTreme");
        if (plugin == null || !(plugin instanceof WormholeXTreme)) {
            throw new RuntimeException("'WormholeXTreme' not found. 'WormholeXTreme' plugin disabled?");
        }

        return ((WormholeXTreme) plugin);
    }

    /**
     * Gets the wormhole x treme worlds.
     * 
     * @return the wormhole x treme worlds
     */
    public static WorldHandler getWorldHandler() {
        return worldHandler;
    }

    /**
     * Register commands.
     */
    public static void registerCommands() {
        final WormholeXTreme tp = getThisPlugin();
        tp.getCommand("wxforce").setExecutor(new Force());
        tp.getCommand("wxidc").setExecutor(new WXIDC());
        tp.getCommand("wxcompass").setExecutor(new Compass());
        tp.getCommand("wxcomplete").setExecutor(new Complete());
        tp.getCommand("wxremove").setExecutor(new WXRemove());
        tp.getCommand("wxlist").setExecutor(new WXList());
        tp.getCommand("wxgo").setExecutor(new Go());
        tp.getCommand("dial").setExecutor(new Dial());
        tp.getCommand("wxbuild").setExecutor(new Build());
        tp.getCommand("wormhole").setExecutor(new Wormhole());
        tp.getCommand("wxreload").setExecutor(new WXReload());
        tp.getCommand("wxstatus").setExecutor(new WXStatus());
    }

    /**
     * Register events.
     */
    public static void registerEvents(final boolean critical) {
        final WormholeXTreme tp = getThisPlugin();
        final PluginManager pm = tp.getServer().getPluginManager();

        if (critical) {
            // Listen for enable events.
            pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Priority.Monitor, tp);
            
            // Listen for disable events.
            pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Priority.Monitor, tp);
        } else {
            // Listen on Block events
            pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.BLOCK_FROMTO, blockListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, tp);

            // Listen on Player events
            pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Priority.Normal, tp);
            
            // redstone listener
            pm.registerEvent(Event.Type.REDSTONE_CHANGE, redstoneListener, Priority.Normal, tp);
            
            // Handle minecarts going through portal
            pm.registerEvent(Event.Type.VEHICLE_MOVE, vehicleListener, Priority.Normal, tp);
            pm.registerEvent(Event.Type.VEHICLE_DAMAGE, vehicleListener, Priority.Normal, tp);
            
            // Handle player walking through the lava.
            pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, tp);
            
            // Handle Creeper explosions damaging Gate components.
            pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Priority.Normal, tp);
        }
    }

    /**
     * Sets the help.
     * 
     * @param help the new help
     */
    public static void setHelp(final Help help) {
        WormholeXTreme.help = help;
    }

    /**
     * Sets the permissions.
     * 
     * @param permissions the new permissions
     */
    public static void setPermissions(final PermissionHandler permissions) {
        WormholeXTreme.permissions = permissions;
    }

    /**
     * Sets the scheduler.
     * 
     * @param scheduler
     *            the new scheduler
     */
    protected static void setScheduler(final BukkitScheduler scheduler) {
        WormholeXTreme.scheduler = scheduler;
    }

    /**
     * Sets the wormhole x treme worlds.
     * 
     * @param worldHandler
     *            the new wormhole x treme worlds
     */
    public static void setWorldHandler(final WorldHandler worldHandler) {
        WormholeXTreme.worldHandler = worldHandler;
    }
}
