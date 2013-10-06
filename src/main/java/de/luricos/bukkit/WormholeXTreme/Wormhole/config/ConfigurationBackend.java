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
package de.luricos.bukkit.WormholeXTreme.Wormhole.config;

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.exceptions.WormholePermissionBackendException;
import de.luricos.bukkit.WormholeXTreme.Wormhole.permissions.PermissionManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lycano
 */
public abstract class ConfigurationBackend {

    public final static String defaultBackend = "xml";
    private static final Map<String, Class<? extends ConfigurationBackend>> REGISTERED_BACKENDS = new HashMap<String, Class<? extends ConfigurationBackend>>();

    protected Configuration config;

    protected ConfigurationBackend(Configuration config) {
        this.config = config;
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
     * Return class name for alias
     *
     * @param alias Alias for backend
     * @return Class name if found or alias if there is no such class name present
     */
    public static String getBackendClassName(String alias) {

        if (REGISTERED_BACKENDS.containsKey(alias)) {
            return REGISTERED_BACKENDS.get(alias).getName();
        }

        return alias;
    }

    /**
     * Returns Class object for specified alias, if there is no alias registered
     * then try to find it using Class.forName(alias)
     *
     * @param alias
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<? extends ConfigurationBackend> getBackendClass(String alias) throws ClassNotFoundException {
        if (!REGISTERED_BACKENDS.containsKey(alias)) {
            Class<?> clazz = Class.forName(alias);
            if (!ConfigurationBackend.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Provided class " + alias + " is not a subclass of ConfigurationBackend!");
            }
            return clazz.asSubclass(ConfigurationBackend.class);
        }

        return REGISTERED_BACKENDS.get(alias);
    }

    /**
     * Register new alias for specified backend class
     *
     * @param alias String the alias to register
     * @param backendClass ConfigurationBackend the backendClass
     */
    public static void registerBackendAlias(String alias, Class<? extends ConfigurationBackend> backendClass) {
        if (!ConfigurationBackend.class.isAssignableFrom(backendClass)) {
            throw new IllegalArgumentException("Provided class should be subclass of ConfigurationBackend"); // This should be enforced at compile time
        }

        REGISTERED_BACKENDS.put(alias, backendClass);

        WXTLogger.info(String.format("ConfigurationBackend: '%s' registered!", alias));
    }

    /**
     * Return alias for specified backend class
     * If there is no such class registered the fullname of this class would
     * be returned using backendClass.getName();
     *
     * @param backendClass
     * @return alias or class fullname when not found using backendClass.getName()
     */
    public static String getBackendAlias(Class<? extends ConfigurationBackend> backendClass) {
        if (REGISTERED_BACKENDS.containsValue(backendClass)) {
            for (String alias : REGISTERED_BACKENDS.keySet()) { // Is there better way to find key by value?
                if (REGISTERED_BACKENDS.get(alias).equals(backendClass)) {
                    return alias;
                }
            }
        }

        return backendClass.getName();
    }

    /**
     * Returns new backend class instance for specified backendName
     *
     * @param backendName Class name or alias of backend
     * @param config      Configuration object to access backend settings
     * @return new instance of PermissionBackend object
     */
    public static ConfigurationBackend getBackend(String backendName, Configuration config) throws WormholePermissionBackendException {
        return getBackend(backendName, WormholeXTreme.getPermissionManager(), config, defaultBackend);
    }


    /**
     * Returns new Backend class instance for specified backendName
     *
     * @param backendName     Class name or alias of backend
     * @param manager         ConfigurationManager object
     * @param config          Configuration object to access backend settings
     * @param fallBackBackend name of backend that should be used if specified backend was not found or failed to initialize
     * @return new instance of PermissionBackend object
     */
    public static ConfigurationBackend getBackend(String backendName, PermissionManager manager, ConfigurationSection config, String fallBackBackend) throws WormholePermissionBackendException {
        if (backendName == null || backendName.isEmpty()) {
            backendName = defaultBackend;
        }

        String className = getBackendClassName(backendName);

        try {
            Class<? extends ConfigurationBackend> backendClass = getBackendClass(backendName);

            WXTLogger.info("Initializing " + backendName + " backend");

            Constructor<? extends ConfigurationBackend> constructor = backendClass.getConstructor(PermissionManager.class, ConfigurationSection.class);
            return constructor.newInstance(manager, config);
        } catch (ClassNotFoundException e) {

            WXTLogger.warn("Backend \"" + backendName + "\" is unknown.");

            if (fallBackBackend == null) {
                throw new RuntimeException(e);
            }

            if (!className.equals(getBackendClassName(fallBackBackend))) {
                return getBackend(fallBackBackend, manager, config, null);
            } else {
                throw new RuntimeException(e);
            }
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                e = e.getCause();
                if (e instanceof WormholePermissionBackendException) {
                    throw ((WormholePermissionBackendException) e);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{config=" + getConfig().getName() + "}";
    }

    protected final ConfigurationSection getConfig() {
        return this.config;
    }

    public abstract Set<String> getKeys(boolean deep);

    public abstract Map<String, Object> getValues(boolean deep);

    public abstract boolean contains(String path);

    public abstract boolean isSet(String path);

    public abstract String getCurrentPath();

    public abstract String getName();

    public abstract Configuration getRoot();

    public abstract ConfigurationSection getParent();

    public abstract Object get(String path);

    public abstract Object get(String path, Object def);

    public abstract void set(String path, Object value);

    public abstract ConfigurationSection createSection(String path);

    public abstract ConfigurationSection createSection(String path, Map<?, ?> map);

    public abstract String getString(String path);

    public abstract String getString(String path, String def);

    public abstract boolean isString(String path);

    public abstract int getInt(String path);

    public abstract int getInt(String path, int def);

    public abstract boolean isInt(String path);

    public abstract boolean getBoolean(String path);

    public abstract boolean getBoolean(String path, boolean def);

    public abstract boolean isBoolean(String path);

    public abstract double getDouble(String path);

    public abstract double getDouble(String path, double def);

    public abstract boolean isDouble(String path);

    public abstract long getLong(String path);

    public abstract long getLong(String path, long def);

    public abstract boolean isLong(String path);

    public abstract List<?> getList(String path);

    public abstract List<?> getList(String path, List<?> def);

    public abstract boolean isList(String path);

    public abstract List<String> getStringList(String path);

    public abstract List<Integer> getIntegerList(String path);

    public abstract List<Boolean> getBooleanList(String path);

    public abstract List<Double> getDoubleList(String path);

    public abstract List<Float> getFloatList(String path);

    public abstract List<Long> getLongList(String path);

    public abstract List<Byte> getByteList(String path);

    public abstract List<Character> getCharacterList(String path);

    public abstract List<Short> getShortList(String path);

    public abstract List<Map<?, ?>> getMapList(String path);

    public abstract Vector getVector(String path);

    public abstract Vector getVector(String path, Vector def);

    public abstract boolean isVector(String path);

    public abstract OfflinePlayer getOfflinePlayer(String path);

    public abstract OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def);

    public abstract boolean isOfflinePlayer(String path);

    public abstract ItemStack getItemStack(String path);

    public abstract ItemStack getItemStack(String path, ItemStack def);

    public abstract boolean isItemStack(String path);

    public abstract Color getColor(String path);

    public abstract Color getColor(String path, Color def);

    public abstract boolean isColor(String path);

    public abstract ConfigurationSection getConfigurationSection(String path);

    public abstract boolean isConfigurationSection(String path);

    public abstract ConfigurationSection getDefaultSection();

    public abstract void addDefault(String path, Object value);

    public abstract void addDefaults(Map<String, Object> defaults);

    public abstract void addDefaults(Configuration defaults);

    public abstract void setDefaults(Configuration defaults);

    public abstract Configuration getDefaults();

    public abstract ConfigurationOptions options();

}
