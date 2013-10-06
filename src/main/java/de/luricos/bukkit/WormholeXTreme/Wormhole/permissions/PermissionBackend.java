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

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePermissionBackendException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lycano
 */
public abstract class PermissionBackend {

    protected final static String defaultBackend = "bukkit";
    protected static Map<String, Class<? extends PermissionBackend>> registeredBackendAliases = new HashMap<String, Class<? extends PermissionBackend>>();
    protected PermissionManager manager;
    protected ConfigurationManager configManager;
    protected String providerName;

    protected PermissionBackend(PermissionManager manager, ConfigurationManager configManager, String providerName) {
        this.manager = manager;
        this.configManager = configManager;
        this.providerName = providerName;
    }

    /**
     * Backend initialization
     */
    public abstract void initialize();

    /**
     * Reload Backend
     */
    public abstract void reload();

    /**
     * End Backend
     */
    public abstract void end();

    /**
     * Get ProviderName
     *
     * @return String ProviderName
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Return class name for backendAlias
     *
     * @param alias
     * @return Class name when found or the alias if there was no registered alias found
     */
    public static String getBackendClassName(String alias) {
        if (registeredBackendAliases.containsKey(alias)) {
            return registeredBackendAliases.get(alias).getName();
        }

        return alias;
    }

    /**
     * Return PluginName from ClassName
     *
     * @param alias
     * @return pluginName
     */
    public static String getBackendPluginName(String alias) {
        String pluginName = getBackendClassName(alias);
        if (pluginName.lastIndexOf('.') > 0) {
            pluginName = pluginName.substring(pluginName.lastIndexOf('.'));
        }
        return pluginName.substring(1, pluginName.length() - "Support".length());
    }

    /**
     * Returns Class object for the specified alias, if there is no alias registered
     * then try to find it using Class.forName(alias)
     *
     * @param alias
     * @return Class extends PermissionBackend
     * @throws ClassNotFoundException
     */
    public static Class<? extends PermissionBackend> getBackendClass(String alias) throws ClassNotFoundException {
        if (!registeredBackendAliases.containsKey(alias)) {
            return (Class<? extends PermissionBackend>) Class.forName(alias);
        }

        return registeredBackendAliases.get(alias);
    }

    /**
     * Register new alias for specified backend class
     *
     * @param alias
     * @param backendClass
     */
    public static void registerBackendAlias(String alias, Class<? extends PermissionBackend> backendClass) {
        if (!PermissionBackend.class.isAssignableFrom(backendClass)) {
            throw new WormholePermissionBackendException("Provided class should be subclass of PermissionBackend.class");
        }

        registeredBackendAliases.put(alias, backendClass);

        WXTLogger.info(String.format("PermissionAlias backend: '%s' registered!", alias));
    }

    /**
     * Return alias for specified backend class
     *
     * If there is no such class registred the FullName of this class
     * will be returned using backendClass.getName()
     *
     * @param backendClass
     * @return alias or class FullName when registeredBackendAlias not found using backendClass.getName()
     */
    public static String getBackendAlias(Class<? extends PermissionBackend> backendClass) {
        if (registeredBackendAliases.containsValue(backendClass)) {
            for (String alias : registeredBackendAliases.keySet()) {
                if (registeredBackendAliases.get(alias).equals(backendClass)) {
                    return alias;
                }
            }
        }

        return backendClass.getName();
    }

    /**
     * Resolve permission backend
     *
     * first enabled will be used.
     * Config node permissions.backend will be set to resolved backend
     */
    public static void resolvePermissionBackends() {
        for (String providerAlias : getRegisteredAliases()) {
            String pluginName = getBackendPluginName(providerAlias);
            WXTLogger.info(String.format("Attempting to use supported permissions plugin '%s'", pluginName));

            Plugin permToLoad = Bukkit.getPluginManager().getPlugin(pluginName);
            if ((pluginName.equals(PermissionBackend.getDefaultBackend().getProviderName())) || ((permToLoad != null) && (permToLoad.isEnabled()))) {

                //@TODO set permission provider in config
                //ConfigManager.setPermissionBackend(providerAlias);
                //WXTLogger.prettyLog(Level.INFO, false, "Config node PERMISSIONS_BACKEND changed to '" + providerAlias + "'");

                return;
            } else {
                WXTLogger.info(String.format("Permission backend '%s' was not found as plugin or not enabled!", providerAlias));
            }
        }
    }

    /**
     * Returns the default Backend class instance
     *
     * @return new instance of PermissionBackend default object
     */
    public static PermissionBackend getDefaultBackend() {
        return getBackend(null, WormholeXTreme.getPermissionManager(), null, defaultBackend);
    }


    /**
     * Returns new backend class instance for specified backendName
     *
     * @param backendName Class name or alias of backend
     * @param configManager ConfigurationManager object to access backend settings
     * @return new instance of PermissionBackend object
     */
    public static PermissionBackend getBackend(String backendName, ConfigurationManager configManager) {
        return getBackend(backendName, WormholeXTreme.getPermissionManager(), configManager, defaultBackend);
    }

    /**
     * Returns new Backend class instance for specified backendName
     *
     * @param backendName Class name or alias of backend
     * @param manager PermissionManager object
     * @param configManager ConfigurationManager object to access backend settings
     * @return new instance of PermissionBackend object
     */
    public static PermissionBackend getBackend(String backendName, PermissionManager manager, ConfigurationManager configManager) {
        return getBackend(backendName, manager, configManager, defaultBackend);
    }


    /**
     * Returns new Backend class instance for specified backendName
     *
     * @param backendName Class name or alias of backend
     * @param manager PermissionManager object
     * @param configManager ConfigurationManager object to access backend settings
     * @param fallBackBackend name of backend that should be used if specified backend was not found or failed to initialize
     * @return new instance of PermissionBackend object
     */
    public static PermissionBackend getBackend(String backendName, PermissionManager manager, ConfigurationManager configManager, String fallBackBackend) {
        if (backendName == null || backendName.isEmpty()) {
            backendName = defaultBackend;
        }

        String className = getBackendClassName(backendName);

        try {
            Class<? extends PermissionBackend> backendClass = getBackendClass(backendName);

            WXTLogger.info(String.format("Initializing PermissionBackend \"%s\"", backendName));

            Constructor<? extends PermissionBackend> constructor = backendClass.getConstructor(PermissionManager.class, ConfigurationManager.class, String.class);
            return (PermissionBackend) constructor.newInstance(manager, configManager, getBackendPluginName(backendName));
        } catch (ClassNotFoundException e) {

            WXTLogger.warn(String.format("PermissionBackend \"%s\" not found", backendName));

            if (fallBackBackend == null) {
                throw new WormholePermissionBackendException("PermissionBackend \"" + backendName + "\" not found: " + e.getMessage());
            }

            if (!className.equals(getBackendClassName(fallBackBackend))) {
                return getBackend(fallBackBackend, manager, configManager, null);
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getRegisteredAliases() {
        return new ArrayList<String>(registeredBackendAliases.keySet());
    }

    public static List<Class<? extends PermissionBackend>> getRegisteredClasses() {
        return new ArrayList<Class<? extends PermissionBackend>>(registeredBackendAliases.values());
    }

    /**
     * Has a player that specific permission
     * @param player
     * @param permissionString
     * @return checkPermission(player, permissionString)
     */
    public boolean has(Player player, String permissionString) {
        return hasPermission(player, permissionString);
    }

    public abstract boolean hasPermission(Player player, String permissionString);

}
