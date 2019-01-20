package de.luricos.bukkit.WormholeXTreme.Wormhole.config;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConfigLoader {

    private static TOMLConfig config;
    private static Path configFileLocation = Paths.get("plugins/WormholeExtreme/config.toml");

    public static TOMLConfig getConfig() {
        if(config == null) {
            config = loadConfig();
        }
        return config;
    }

    private static TOMLConfig loadConfig() {
        FileConfig config = FileConfig.of(configFileLocation);
        ObjectConverter converter = new ObjectConverter();
        TOMLConfig configObject = converter.toObject(config, TOMLConfig::new);
        config.close();
        return configObject;
    }

    public static void saveConfig(TOMLConfig config) {
        FileConfig output = FileConfig.of(configFileLocation);
        ObjectConverter converter = new ObjectConverter();
        converter.toConfig(config, output);
        output.save();
        output.close();
    }

    public static void generateNewConfig() {
        TOMLConfig config = new TOMLConfig();
        saveConfig(config);
    }
}
