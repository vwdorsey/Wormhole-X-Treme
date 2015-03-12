package de.luricos.bukkit.WormholeXTreme.Wormhole.config.backends;

import de.luricos.bukkit.WormholeXTreme.Wormhole.bukkit.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationBackend;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigurationManager;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lycano
 */
public class YmlConfigurationSupport extends ConfigurationBackend {

    private FileConfiguration fileConfiguration;

    public YmlConfigurationSupport(ConfigurationManager manager) {
        super(manager);

        this.fileConfiguration = (FileConfiguration) this.manager.getConfig();
    }

    @Override
    public void initialize() {
        this.fileConfiguration.options().copyDefaults(true);
        WormholeXTreme.getPlugin().saveConfig();
    }

    @Override
    public void reload() {
        this.end();
        this.initialize();
    }

    @Override
    public void end() {
        WormholeXTreme.getPlugin().saveConfig();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getKeys
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        return this.fileConfiguration.getKeys(deep);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getValues
     */
    @Override
    public Map<String, Object> getValues(boolean deep) {
        return this.fileConfiguration.getValues(deep);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#contains
     */
    @Override
    public boolean contains(String path) {
        return this.fileConfiguration.contains(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isSet
     */
    @Override
    public boolean isSet(String path) {
        return this.fileConfiguration.isSet(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getCurrentPath
     */
    @Override
    public String getCurrentPath() {
        return this.fileConfiguration.getCurrentPath();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getName
     */
    @Override
    public String getName() {
        return this.fileConfiguration.getName();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getRoot
     */
    @Override
    public Configuration getRoot() {
        return this.fileConfiguration.getRoot();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getParent
     */
    @Override
    public ConfigurationSection getParent() {
        return this.fileConfiguration.getParent();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#get
     */
    @Override
    public Object get(String path) {
        return this.fileConfiguration.get(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#get
     */
    @Override
    public Object get(String path, Object def) {
        return this.fileConfiguration.get(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#set
     */
    @Override
    public void set(String path, Object value) {
        this.fileConfiguration.set(path, value);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#createSection
     */
    @Override
    public ConfigurationSection createSection(String path) {
        return this.fileConfiguration.createSection(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#createSection
     */
    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return this.fileConfiguration.createSection(path, map);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getString
     */
    @Override
    public String getString(String path) {
        return this.fileConfiguration.getString(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getString
     */
    @Override
    public String getString(String path, String def) {
        return this.fileConfiguration.getString(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isString
     */
    @Override
    public boolean isString(String path) {
        return this.fileConfiguration.isString(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getInt
     */
    @Override
    public int getInt(String path) {
        return this.fileConfiguration.getInt(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getInt
     */
    @Override
    public int getInt(String path, int def) {
        return this.fileConfiguration.getInt(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isInt
     */
    @Override
    public boolean isInt(String path) {
        return this.fileConfiguration.isInt(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getBoolean
     */
    @Override
    public boolean getBoolean(String path) {
        return this.fileConfiguration.getBoolean(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getBoolean
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        return this.fileConfiguration.getBoolean(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isBoolean
     */
    @Override
    public boolean isBoolean(String path) {
        return this.fileConfiguration.isBoolean(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getDouble
     */
    @Override
    public double getDouble(String path) {
        return this.fileConfiguration.getDouble(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getDouble
     */
    @Override
    public double getDouble(String path, double def) {
        return this.fileConfiguration.getDouble(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isDouble
     */
    @Override
    public boolean isDouble(String path) {
        return this.fileConfiguration.isDouble(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getLong
     */
    @Override
    public long getLong(String path) {
        return this.fileConfiguration.getLong(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getLong
     */
    @Override
    public long getLong(String path, long def) {
        return this.fileConfiguration.getLong(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isLong
     */
    @Override
    public boolean isLong(String path) {
        return this.fileConfiguration.isLong(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getList
     */
    @Override
    public List<?> getList(String path) {
        return this.fileConfiguration.getList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getList
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        return this.fileConfiguration.getList(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isList
     */
    @Override
    public boolean isList(String path) {
        return this.fileConfiguration.isList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getStringList(path)
     */
    @Override
    public List<String> getStringList(String path) {
        return this.fileConfiguration.getStringList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getIntegerList
     */
    @Override
    public List<Integer> getIntegerList(String path) {
        return this.fileConfiguration.getIntegerList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getBooleanList
     */
    @Override
    public List<Boolean> getBooleanList(String path) {
        return this.fileConfiguration.getBooleanList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getDoubleList
     */
    @Override
    public List<Double> getDoubleList(String path) {
        return this.fileConfiguration.getDoubleList(path);
    }

    /* (non-Javadoc)
    * @see org.bukkit.configuration#getFloatList
    */
    @Override
    public List<Float> getFloatList(String path) {
        return this.fileConfiguration.getFloatList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getLongList
     */
    @Override
    public List<Long> getLongList(String path) {
        return this.fileConfiguration.getLongList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getByteList
     */
    @Override
    public List<Byte> getByteList(String path) {
        return this.fileConfiguration.getByteList(path);
    }

    /* (non-Javadoc)
    * @see org.bukkit.configuration#getCharacterList
    */
    @Override
    public List<Character> getCharacterList(String path) {
        return this.fileConfiguration.getCharacterList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getShortList
     */
    @Override
    public List<Short> getShortList(String path) {
        return this.fileConfiguration.getShortList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getMapList
     */
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return this.fileConfiguration.getMapList(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getVector
     */
    @Override
    public Vector getVector(String path) {
        return this.fileConfiguration.getVector(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getVector
     */
    @Override
    public Vector getVector(String path, Vector def) {
        return this.fileConfiguration.getVector(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isVector
     */
    @Override
    public boolean isVector(String path) {
        return this.fileConfiguration.isVector(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getOfflinePlayer
     */
    @Override
    public OfflinePlayer getOfflinePlayer(String path) {
        return this.fileConfiguration.getOfflinePlayer(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getOfflinePlayer
     */
    @Override
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return this.fileConfiguration.getOfflinePlayer(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isOfflinePlayer
     */
    @Override
    public boolean isOfflinePlayer(String path) {
        return this.fileConfiguration.isOfflinePlayer(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getItemStack
     */
    @Override
    public ItemStack getItemStack(String path) {
        return this.fileConfiguration.getItemStack(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getItemStack
     */
    @Override
    public ItemStack getItemStack(String path, ItemStack def) {
        return this.fileConfiguration.getItemStack(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isItemStack
     */
    @Override
    public boolean isItemStack(String path) {
        return this.fileConfiguration.isItemStack(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getColor
     */
    @Override
    public Color getColor(String path) {
        return this.fileConfiguration.getColor(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getColor
     */
    @Override
    public Color getColor(String path, Color def) {
        return this.fileConfiguration.getColor(path, def);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isColor
     */
    @Override
    public boolean isColor(String path) {
        return this.fileConfiguration.isColor(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getConfigurationSection
     */
    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return this.fileConfiguration.getConfigurationSection(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#isConfigurationSection
     */
    @Override
    public boolean isConfigurationSection(String path) {
        return this.fileConfiguration.isConfigurationSection(path);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getDefaultSection
     */
    @Override
    public ConfigurationSection getDefaultSection() {
        return this.fileConfiguration.getDefaultSection();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#addDefault
     */
    @Override
    public void addDefault(String path, Object value) {
        this.fileConfiguration.addDefault(path, value);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#addDefaults
     */
    @Override
    public void addDefaults(Map<String, Object> defaults) {
        this.fileConfiguration.addDefaults(defaults);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#addDefaults
     */
    @Override
    public void addDefaults(Configuration defaults) {
        this.fileConfiguration.addDefaults(defaults);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#setDefaults
     */
    @Override
    public void setDefaults(Configuration defaults) {
        this.fileConfiguration.setDefaults(defaults);
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#getDefaults
     */
    @Override
    public Configuration getDefaults() {
        return this.fileConfiguration.getDefaults();
    }

    /* (non-Javadoc)
     * @see org.bukkit.configuration#options
     */
    @Override
    public ConfigurationOptions options() {
        return this.fileConfiguration.options();
    }
}
