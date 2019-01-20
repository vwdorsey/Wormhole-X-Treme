package de.luricos.bukkit.WormholeXTreme.Wormhole.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for the newer style TOML configuration file. Defines all fields available in the file.
 * @author sir-dizzle
 */
public class TOMLConfig {
    private Plugin plugin;
    private Permissions permissions;
    private Timeouts timeouts;
    private Welcome welcome;

    public Plugin plugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Permissions permissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Timeouts timeouts() {
        return timeouts;
    }

    public void setTimeouts(Timeouts timeouts) {
        this.timeouts = timeouts;
    }

    public Welcome welcome() {
        return welcome;
    }

    public void setWelcome(Welcome welcome) {
        this.welcome = welcome;
    }

    public class Plugin {
        private String help;
        private String transportType;
        private String logLevel;

        public String help() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String transportType() {
            return transportType;
        }

        public void setTransportType(String transportType) {
            this.transportType = transportType;
        }

        public String logLevel() {
            return logLevel;
        }

        public void setLogLevel(String logLevel) {
            this.logLevel = logLevel;
        }
    }

    public class Permissions {
        private String provider;
        private String level;

        public String provider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String level() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public class Timeouts {
        private int predial;
        private int postdial;
        private HashMap<Integer, Integer> CooldownGroups;

        public int predial() {
            return predial;
        }

        public void setPredial(int predial) {
            this.predial = predial;
        }

        public int postdial() {
            return postdial;
        }

        public void setPostdial(int postdial) {
            this.postdial = postdial;
        }
    }

    public class Welcome {
        private boolean enabled;
        private String message;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
