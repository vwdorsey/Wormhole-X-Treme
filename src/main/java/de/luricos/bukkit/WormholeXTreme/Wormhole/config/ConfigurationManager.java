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

import de.luricos.bukkit.WormholeXTreme.Wormhole.events.WormholeSystemEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.util.logging.Level;

/**
 * @author lycano
 */
public class ConfigurationManager {

    protected ConfigurationBackend backend = null;
    private Configuration config;

    public ConfigurationManager(Configuration config) {
        this.config = config;
        this.initBackend();
    }

    private void initBackend() {
        // @TODO use config instead of static call
        String backendName = this.config.getString("configuration.backend");

        if (backendName == null || backendName.isEmpty()) {
            backendName = ConfigurationBackend.defaultBackend;
            this.config.set("configuration.backend", backendName);
        }

        this.setBackend(backendName);
    }

    /**
     * Return current backend
     *
     * @return current backend object
     */
    public ConfigurationBackend getBackend() {
        return this.backend;
    }

    public void setBackend(String backendName) {
        synchronized (this) {
            this.backend = ConfigurationBackend.getBackend(backendName, this.config);
            this.backend.initialize();
        }

        this.callEvent(WormholeSystemEvent.Action.PERMISSION_BACKEND_CHANGED);
    }

    protected void callEvent(WormholeSystemEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    protected void callEvent(WormholeSystemEvent.Action action) {
        this.callEvent(new WormholeSystemEvent(action));
    }

    public void reset() {
        if (this.backend != null) {
            this.backend.reload();
        }

        this.callEvent(WormholeSystemEvent.Action.RELOADED);
    }

    public void end() {
        if (this.backend != null)
            this.backend.end();
    }

    public Level getLogLevel() {
        return Level.parse(this.config.getString("logger.level", "INFO"));
    }

    public String getBackendName() {
        return this.backend.getName();
    }

}
